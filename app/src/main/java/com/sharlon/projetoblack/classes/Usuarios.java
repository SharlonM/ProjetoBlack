package com.sharlon.projetoblack.classes;

import android.content.Context;
import android.widget.Toast;

public class Usuarios {
    private String email, id;

    public Usuarios() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static void toast(Context context, String mensagem){
        Toast.makeText(context,mensagem,Toast.LENGTH_LONG).show();
    }
}
