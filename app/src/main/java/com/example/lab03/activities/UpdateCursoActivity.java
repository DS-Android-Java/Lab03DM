package com.example.lab03.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lab03.R;
import com.example.lab03.accesoDatos.ModelData;
import com.example.lab03.logicaDeNegocio.Carrera;
import com.example.lab03.logicaDeNegocio.Curso;
import com.example.lab03.logicaDeNegocio.Profesor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UpdateCursoActivity extends AppCompatActivity {

    private FloatingActionButton fBtn;
    private boolean editable = true;
    private EditText etCodigo;
    private EditText etNombre;
    private EditText etCreditos;
    private EditText etAnio;
    private EditText etCiclo;
    private EditText etHorasSemanales;
    private Spinner spinnerProfesor;
    private Spinner spinnerCarrera;
    ArrayAdapter<Profesor> adaptadorP;
    ArrayAdapter<Carrera> adaptadorC;

    private List<Profesor> profesores;
    private List<Carrera> carreras;

    //Url cargar combo carreras
    String apiUrlCargaComboCarrera = "http://192.168.0.3:8080/Backend_JSON/modelos/curso/preparaCreate?";

    //Url cargar combo profesores
    String apiUrlCargaComboProfesores = "http://192.168.0.3:8080/Backend_JSON/modelos/curso/comboProfesor?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_curso);

        editable = true;

        // button check
        fBtn = findViewById(R.id.addUpdCursoBtn);

        //cleaning stuff
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etCreditos = findViewById(R.id.etCreditos);
        etAnio = findViewById(R.id.etAnio);
        etCiclo = findViewById(R.id.etCiclo);
        etHorasSemanales = findViewById(R.id.etHorasSemanales);
        etCodigo.setText("");
        etNombre.setText("");
        etCreditos.setText("");
        etAnio.setText("");
        etCiclo.setText("");
        etHorasSemanales.setText("");

        //Cargando spinners o combos
        MyAsyncTasksCargaComboCarreras myAsyncTasksCCCarreras = new MyAsyncTasksCargaComboCarreras();
        myAsyncTasksCCCarreras.execute();

        MyAsyncTasksCargaComboProfesores myAsyncTasksCCProfesores = new MyAsyncTasksCargaComboProfesores();
        myAsyncTasksCCProfesores.execute();

        //Cargado spinners o combos
        spinnerProfesor = findViewById(R.id.spinnerProfesor);
        profesores = new ArrayList<>();

        spinnerCarrera = findViewById(R.id.spinnerCarrera);
        carreras = new ArrayList<>();
        ////////////////////

        //receiving data from admCursoActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            editable = extras.getBoolean("editable");
            if (editable) {   // is editing some row
                Curso aux = (Curso) getIntent().getSerializableExtra("curso");
                etCodigo.setText(aux.getCodigo());
                etCodigo.setEnabled(false);
                etNombre.setText(aux.getNombre());
                etAnio.setText(aux.getAnio());
                etCiclo.setText(aux.getCiclo());
                etCreditos.setText(aux.getCreditos());
                etHorasSemanales.setText(aux.getHora_semanales());
                //spinnerProfesor.setSelection(adaptadorP.getPosition(aux.getProfesor()));
                //spinnerCarrera.setSelection(adaptadorC.getPosition(aux.getCodCarrera()));
                //edit action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editCurso();
                    }
                });
            } else {         // is adding new Carrera object
                //add new action
                fBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addCurso();
                    }
                });
            }
        }

        //Aca se prepara el popup
        DisplayMetrics medidasVentana = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(medidasVentana);

        int ancho = medidasVentana.widthPixels;
        int alto = medidasVentana.heightPixels;

        getWindow().setLayout((int)(ancho * 0.90), (int)(alto * 0.80));
    }

    public void addCurso() {
        if (validateForm()) {
            //do something
            Profesor prof = new Profesor();
            prof = ((Profesor) spinnerProfesor.getSelectedItem());
            System.out.println("Profesor data:" + prof.getNombre() + " Ced: " + prof.getCedula());

            Carrera carr = new Carrera();
            carr = ((Carrera)spinnerCarrera.getSelectedItem());
            System.out.println("Carrera data:" + carr.getNombre() + " CodCarrera: " + carr.getCodCarrera());

            Curso cur = new Curso(
                    etCodigo.getText().toString(),
                    carr.getCodCarrera(),
                    etNombre.getText().toString(),
                    etCreditos.getText().toString(),
                    etAnio.getText().toString(),
                    etCiclo.getText().toString(),
                    etHorasSemanales.getText().toString()
                    ,prof);

            Intent intent = new Intent(getBaseContext(), MantenimientoCursoActivity.class);
            //sending curso data
            intent.putExtra("addCurso", cur);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public void editCurso() {//Funcion para editar en la base y en la lista que muestra en la Recycler View
        if (validateForm()) {

            Profesor prof = new Profesor();
            prof = ((Profesor) spinnerProfesor.getSelectedItem());
            System.out.println("Profesor data:" + prof.getNombre() + " Ced: " + prof.getCedula());

            Carrera carr = new Carrera();
            carr = ((Carrera)spinnerCarrera.getSelectedItem());
            System.out.println("Carrera data:" + carr.getNombre() + " CodCarrera: " + carr.getCodCarrera());

            Curso cur = new Curso(
                    etCodigo.getText().toString(),
                    carr.getCodCarrera(),
                    etNombre.getText().toString(),
                    etCreditos.getText().toString(),
                    etAnio.getText().toString(),
                    etCiclo.getText().toString(),
                    etHorasSemanales.getText().toString()
                    ,prof);

            Intent intent = new Intent(getBaseContext(), MantenimientoCursoActivity.class);
            //sending curso data
            intent.putExtra("editCurso", cur);
            startActivity(intent);
            finish(); //prevent go back
        }
    }

    public boolean validateForm() {
        int error = 0;
        if (TextUtils.isEmpty(this.etNombre.getText())) {
            etNombre.setError("Nombre requerido");//Asi se le coloca un mensaje de error en los campos en android
            error++;
        }
        if (TextUtils.isEmpty(this.etCodigo.getText())) {
            etCodigo.setError("Codigo requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.etCreditos.getText())) {
            etCreditos.setError("Creditos requerido");
            error++;
        }
        if (TextUtils.isEmpty(this.etHorasSemanales.getText())) {
            etHorasSemanales.setError("Horas requerido");
            error++;
        }
        if (error > 0) {
            Toast.makeText(getApplicationContext(), "Algunos errores", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public class MyAsyncTasksCargaComboCarreras extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlCargaComboCarrera);

                    urlConnection = (HttpURLConnection) url.openConnection();

                    ////
                    urlConnection.setRequestMethod("GET"); // or POST
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    ////
                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        //System.out.print(current);
                    }
                    // return the data to onPostExecute method
                    Log.w("JSON", current);
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            String jsonObjectAsString = s;

            //Json
            try {
                Gson gson = new Gson();

                carreras = (ArrayList<Carrera>) gson.fromJson(s,
                        new TypeToken<ArrayList<Carrera>>() {
                        }.getType());

                adaptadorC = new ArrayAdapter<Carrera>(UpdateCursoActivity.this, R.layout.spinner_item_diego, carreras);
                spinnerCarrera.setAdapter(adaptadorC);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyAsyncTasksCargaComboProfesores extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";

            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlCargaComboProfesores);

                    urlConnection = (HttpURLConnection) url.openConnection();

                    ////
                    urlConnection.setRequestMethod("GET"); // or POST
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    ////
                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                        //System.out.print(current);
                    }
                    // return the data to onPostExecute method
                    Log.w("JSON", current);
                    return current;

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {

            String jsonObjectAsString = s;

            //Json
            try {
                Gson gson = new Gson();

                profesores = (ArrayList<Profesor>) gson.fromJson(s,
                        new TypeToken<ArrayList<Profesor>>() {
                        }.getType());

                adaptadorP = new ArrayAdapter<Profesor>(UpdateCursoActivity.this, R.layout.spinner_item_diego, profesores);
                spinnerProfesor.setAdapter(adaptadorP);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
