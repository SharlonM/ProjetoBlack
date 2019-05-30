package com.sharlon.projetoblack;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import com.sharlon.projetoblack.classes.Usuarios;

public class ActivityFeedback extends AppCompatActivity {

    Spinner spinner;
    EditText edtNome,edtEmail,edtDescricao;
    LinearLayout layout;
    Button btnEnviar;
    private final String[] emailEmpresa={"sharlon12@hotmail.com","academiasharlon@gmail.com"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        spinner = findViewById(R.id.spinner2);
        btnEnviar = findViewById(R.id.btn_enviar);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrayOpcoes,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        edtDescricao = findViewById(R.id.edt_descricao);
        edtEmail = findViewById(R.id.edt_email);
        edtNome = findViewById(R.id.edt_nome);
        layout = findViewById(R.id.linearLayoutFragment);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideTeclado(edtEmail);
            }
        });

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edtNome.getText().toString().isEmpty() ||
                    edtEmail.getText().toString().isEmpty() ||
                    edtDescricao.getText().toString().isEmpty()){

                    Usuarios.toast(getApplicationContext(),"Preencha todos os campos para prosseguir");
                }else {

                    sendEmail(spinner.getSelectedItem().toString(), "De " + edtEmail.getText() + "\n\n" + edtDescricao.getText());

                    edtNome.setText("");
                    edtDescricao.setText("");
                    edtEmail.setText("");

                }

            }
        });

    }

    private void hideTeclado(EditText e){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(e.getWindowToken(),0);
    }

    public void sendEmail(String assunto,String mensagem){
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.putExtra(Intent.EXTRA_EMAIL,emailEmpresa);
        intent.putExtra(Intent.EXTRA_SUBJECT,assunto);
        intent.putExtra(Intent.EXTRA_TEXT,mensagem);
        intent.setType("message/rfc822");

        //startActivity(Intent.createChooser(intent,"Escolha seu provedor favorito"));
        startActivity(intent);
    }
}
