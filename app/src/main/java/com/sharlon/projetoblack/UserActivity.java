package com.sharlon.projetoblack;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sharlon.projetoblack.classes.Usuarios;

public class UserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    boolean respInput = false;
    FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;
    TextView usuario,email;
    Usuarios usuarioLogado = new Usuarios();
    ImageView img;
    FirebaseAcitivity firebaseAcitivity = new FirebaseAcitivity();
    String tipoLogin;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.txt_nome, R.string.txt_email); // abrir e fechar
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            //adciona fragmento inicial
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameLayoutContainer, new FragmentHistorico())
                    .commit();
            toolbar.setTitle("Historico");
        }

        firebaseAuth = FirebaseAuth.getInstance();


        View view = navigationView.getHeaderView(0);
        usuario = view.findViewById(R.id.txt_nav_usuario);
        email = view.findViewById(R.id.txt_nav_email);
        img = view.findViewById(R.id.img_nav_user);


        inicializarFirebase();
        conectarGoogleApi();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            firebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    finish();
                    Intent i = new Intent(UserActivity.this,MainActivity.class);
                    startActivity(i);
                    toast("deslogado com sucesso");
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_AddTreino) {
            // Handle the camera action
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, new FragmentoAddTreino())
                    .commit();

            //FragmentoAddTreino treino = new FragmentoAddTreino();
            //treino.clicou();
            toolbar.setTitle("Registrar Treino");


        } else if (id == R.id.nav_addFalta) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, new FragmentoAddFalta())
                    .commit();

            toolbar.setTitle("Registrar Falta");

        } else if (id == R.id.nav_Historico) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, new FragmentHistorico())
                    .commit();

            toolbar.setTitle("Historico");

        } else if (id == R.id.nav_apagarHistorico) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, new FragmentHistorico())
                    .commit();

            // apaga historico
            alertaInput();

        } else if(id == R.id.nav_home){
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void alertaInput() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("CONFIRMAÇAO");
        builder.setMessage("Tem certeza que deseja excluir o historico ?");
        builder.setPositiveButton("sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //sim
                firebaseAcitivity.excluirHistorico();
                toast("Historico excluido com sucesso");
                respInput = true;
            }
        });

        builder.setNegativeButton("nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toast("Historico Mantido");
                respInput = false;
            }
        });

        AlertDialog alerta = builder.create();
        alerta.show();
    }

    private void toast(String mensagem) {
        Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG).show();
    }

    private void inicializarFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    exibirDados(user);
                }
            }
        };
    }

    private void exibirDados(FirebaseUser user) {

        String nome,gmail,id;

        nome = user.getDisplayName();
        gmail = user.getEmail();
        id = user.getUid();

        usuario.setText(nome);
        email.setText(gmail);

        usuarioLogado.setEmail(gmail);
        usuarioLogado.setId(id);

        firebaseAcitivity.existeUser(usuarioLogado);

        if(tipoLogin.equals("google")) {
            Glide.with(this).load(user.getPhotoUrl()).into(img);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        tipoLogin = ActivityLogin.getTipoLogin();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void conectarGoogleApi() {
        try {

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("706736076570-v0c07n0bsqehqvojti67mkkm702as5e6.apps.googleusercontent.com")
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, null)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

        } catch (Exception e) {
            Log.i("Exeption conecGoogleApi", e.toString());
        }

    }

}