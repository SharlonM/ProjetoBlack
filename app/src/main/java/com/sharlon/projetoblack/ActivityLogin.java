package com.sharlon.projetoblack;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sharlon.projetoblack.classes.Usuarios;


public class ActivityLogin extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private EditText edtEmail;
    private int token = 0;
    static String tipoLogin = "google";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edt_email);
        final EditText edtSenha = findViewById(R.id.edt_senha);
        Button btnEntrar = findViewById(R.id.btn_entrar);
        SignInButton btnGoogle = findViewById(R.id.btn_Google);
        LinearLayout layout = findViewById(R.id.layout_main);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideTeclado(edtEmail);
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        conectarGoogleApi();

        if(firebaseAuth.getCurrentUser() != null){
            usuarioLogado();
        }else {

            btnEntrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edtEmail.getText().toString().isEmpty() || edtSenha.getText().toString().isEmpty()){
                        Usuarios.toast(getApplicationContext(),"Preencha todos os campos");
                    }else{
                        // autenticar
                        loginComEmail(edtEmail.getText().toString(),edtSenha.getText().toString());
                    }
                }
            });

            btnGoogle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signIn();
                }
            });
        }
    }

    private void loginComEmail(String email,String senha) {

        firebaseAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    tipoLogin = "email";
                    usuarioLogado();
                }else{
                    Usuarios.toast(getApplicationContext(),"Email ou Senha incorretos");
                }
            }
        });
    }

    private void signIn() {
        try {

            Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(i, 1);

        } catch (Exception e) {
            Log.w("Exception SignIn", e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseLogin(account);
            } else {
                Log.w("ERRO", result.toString()+result.getStatus());
            }

    }

    private void firebaseLogin(GoogleSignInAccount account) {
        try {

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // funcionou
                                tipoLogin = "google";
                                usuarioLogado();
                                Toast.makeText(getApplicationContext(),"logado com sucesso",Toast.LENGTH_LONG).show();

                            } else {
                                Log.w("ERRO", "TASK");
                            }
                        }
                    });

        } catch (Exception e) {
            Log.w("Exception fireBaseLogin", e);
        }
    }

    private void usuarioLogado() {
        // aqui vai fazer o funcionamento caso logue

        Intent i = new Intent(this,UserActivity.class);
        startActivity(i);

    }

    private void conectarGoogleApi() {
        try {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("706736076570-v0c07n0bsqehqvojti67mkkm702as5e6.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        } catch (Exception e) {
            Log.i("Exeption conecGoogleApi", e.toString());
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w("ERRO", "OnconnectionFailed");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void alertaInput(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Token ID");
        builder.setMessage("Digite o token");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);
        builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //sim
                 token = Integer.parseInt(input.getText().toString());
                 signIn();
            }
        });

        builder.setNegativeButton("nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //nao
                Toast.makeText(getApplicationContext(),"Para logar pf digite o token",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void hideTeclado(EditText e){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(e.getWindowToken(),0);
    }

    public static String getTipoLogin(){
        return tipoLogin;
    }

}
