package com.example.lab03.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab03.R;
import com.example.lab03.accesoDatos.ModelData;
import com.example.lab03.logicaDeNegocio.Profesor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UpdateProfesorActivity extends AppCompatActivity {


    private FloatingActionButton floatingActionButton;
    private  boolean editable = true;
    private EditText etCedula;
    private EditText etNombreP;
    private  EditText etTelefono;
    private EditText etEmail;
    private ModelData modelData = new ModelData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profesor);

        editable = true;
        floatingActionButton = findViewById(R.id.addUpdProfesorBtn);
        etCedula=findViewById(R.id.etCedula);
        etNombreP=findViewById(R.id.etNombreP);
        etTelefono=findViewById(R.id.etTelefono);
        etEmail=findViewById(R.id.etEmail);
        etCedula.setText("");
        etNombreP.setText("");
        etTelefono.setText("");
        etEmail.setText("");

        //receiving data from admProfesorActivity
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            editable = extras.getBoolean("editable");
            if (editable) {
                Profesor aux = (Profesor) getIntent().getSerializableExtra("profesor");
                etCedula.setText(aux.getCedula());
                etCedula.setEnabled(false);
                etNombreP.setText(aux.getNombre());
                etTelefono.setText(aux.getTelefono());
                etEmail.setText(aux.getEmail());

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editProfesor();
                    }
                });
            }else{
                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addProfesor();
                    }
                });
            }
        }

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int)(ancho * 0.90), (int)(alto * 0.50));
    }

    public void editProfesor(){
        if (validateForm()) {
            Profesor profesor = new Profesor(
                    etCedula.getText().toString(),
                    etNombreP.getText().toString(),
                    etTelefono.getText().toString(),
                    etEmail.getText().toString()
            );
            Intent intent = new Intent(getBaseContext(), MantenimientoProfesorActivity.class);
            //sending profesor data
            intent.putExtra("editProfesor", profesor);
            startActivity(intent);
            finish();
        }
    }


    public void addProfesor(){
        if (validateForm()) {
            Profesor profesor = new Profesor(
                    etCedula.getText().toString(),
                    etNombreP.getText().toString(),
                    etTelefono.getText().toString(),
                    etEmail.getText().toString()
            );
            Intent intent = new Intent(getBaseContext(), MantenimientoProfesorActivity.class);
            //sending profesor data
            intent.putExtra("addProfesor", profesor);
            startActivity(intent);
            finish();
        }
    }
    public boolean validateForm(){
       int error = 0;
        if (TextUtils.isEmpty(this.etCedula.getText())) {
            etCedula.setError("Cédula requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.etNombreP.getText())) {
            etNombreP.setError("Nombre requerido");//Asi se le coloca un mensaje de error en los campos en android
            error++;
        }
        if (TextUtils.isEmpty(this.etTelefono.getText())) {
            etTelefono.setError("Teléfono requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.etEmail.getText())) {
            etEmail.setError("Email requerido");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }






}
