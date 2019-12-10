package com.example.floria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.floria.modelo.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class NewUserActivity extends AppCompatActivity {

    EditText etNombre;
    EditText etApe;
    EditText etCorreo;
    EditText etEdad;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public static final String SHARED_PREFS = "Preferencias";
    public static final String NAME = "keyAppUser";

    Button btnRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        etNombre = findViewById(R.id.etNombre);
        etApe = findViewById(R.id.etApe);
        etCorreo = findViewById(R.id.etCorreo);
        etEdad = findViewById(R.id.etEdad);

        btnRegistro = findViewById(R.id.btnRegistro);

        initFirebase();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });

    }

    private void registrarUsuario() {

        String nombre = etNombre.getText().toString();
        String apellido = etApe.getText().toString();
        String correo = etCorreo.getText().toString();
        String edad = etEdad.getText().toString();

        if (nombre.equals("") || apellido.equals("") || correo.equals("") || edad.equals("")) {
            Toast.makeText(this, "Completa los Campos", Toast.LENGTH_LONG).show();
        } else {

            User reg = new User();

            reg.setUid(UUID.randomUUID().toString());
            reg.setNombre(nombre);
            reg.setApellido(apellido);
            reg.setCorreo(correo);
            reg.setEdad(Integer.parseInt(edad));

            databaseReference.child("Persona").child(reg.getUid()).setValue(reg);

            newUserData(reg.getUid());

            finish();
        }

    }

    private void initFirebase() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

    }

    public void newUserData(String keyapp){

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(NAME,keyapp);
        editor.apply();


    }

}
