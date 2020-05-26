package com.example.lab03.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import com.example.lab03.R;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab03.accesoDatos.ModelData;
import com.example.lab03.adaptador.AdaptadorProfesor;
import com.example.lab03.helper.RecyclerItemTouchHelper;
import com.example.lab03.logicaDeNegocio.Profesor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MantenimientoProfesorActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AdaptadorProfesor.AdaptadorProfesorListener  {

    //Url listar
    String apiUrl = "http://192.168.0.3:8080/Backend_JSON/modelos/profesor/list?";//Esta para mi celular ip de mi compu la ipv4 de mi LAN
    //String apiUrl = "http://10.0.2.2:8080/Backend_JSON/modelos/profesor/list";//Esta para emulador

    //Url agregar
    String apiUrlAcciones= "http://192.168.0.3:8080/Backend_JSON/Controlador/profesor?";
    //String apiUrlAcciones= "http://10.0.2.2:8080/Backend_JSON/Controlador/profesor?"; //Esta para emulador

    String apiUrlTemp;

    private RecyclerView mRecyclerView;
    private AdaptadorProfesor adaptadorProfesor;
    private List<Profesor> profesorList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    ProgressDialog progressDialog;
    private ModelData modelData;
    private String mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_profesor);
        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);
        modelData = ModelData.getInstance();
        mensaje = "";
        apiUrlTemp = apiUrl;


        getSupportActionBar().setTitle("Profesores");
        mRecyclerView = findViewById(R.id.recycler_profesoresFld);
        profesorList = new ArrayList<>();
        modelData = ModelData.getInstance();
        profesorList=modelData.getListaProfesor();
        adaptadorProfesor = new AdaptadorProfesor(profesorList, this);
        coordinatorLayout = findViewById(R.id.constraint_layoutP);

        whiteNotificationBar(mRecyclerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(adaptadorProfesor);

        //AsyncTask aca se usa el web service para cargar los datos de la base del profesor
        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        myAsyncTasks.execute();

        // go to update or add profesor
        floatingActionButton = findViewById(R.id.addBtnP);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { goToAddUpdProfesor(); }
        });


        //delete swiping left and right
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mRecyclerView);

        // Receive the Carrera sent by AddUpdProfesorActivity
        checkIntentInformation();
        //refresh view
        adaptadorProfesor.notifyDataSetChanged();

    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Profesor profesor) {
        Toast.makeText(getApplicationContext(),"Selected:" + profesor.getCedula() +","+profesor.getNombre(), Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds profesorList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
               adaptadorProfesor.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
               adaptadorProfesor.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        Intent a = new Intent(this, NavDrawerActivity.class);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(a);
        super.onBackPressed();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(direction == ItemTouchHelper.START){
            if(viewHolder instanceof  AdaptadorProfesor.MyViewHolder){
                // get the removed item name to display it in snack bar
                String name = profesorList.get(viewHolder.getAdapterPosition()).getNombre();
                String cedula = profesorList.get(viewHolder.getAdapterPosition()).getCedula();

                apiUrlTemp = apiUrlAcciones + "app=deleteP" + "&id_Profe=" + cedula;
                //MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                //myAsyncTasks.execute();
                MyAsyncTasksProfeOperaciones myAsyncTasksDeleteProfe = new MyAsyncTasksProfeOperaciones();
                myAsyncTasksDeleteProfe.execute();

                //save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                // remove the item from recyclerView
                adaptadorProfesor.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                /*Snackbar snackbar = Snackbar.make(coordinatorLayout,name+ mensaje, Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // undo is selected, restore the deleted item from adapter
                        adaptadorProfesor.restoreItem(deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();*/
            }
        } else {   //If is editing a row object
            Profesor aux = adaptadorProfesor.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, UpdateProfesorActivity.class);
            intent.putExtra("editable",true);
            intent.putExtra("profesor", aux); //Se pasa el objeto profesor desde la lista profesor
            adaptadorProfesor.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }

    public class MyAsyncTasks extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            /*progressDialog = new ProgressDialog(MantenimientoProfesorActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";


            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTemp);

                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }

                    // return the data to onPostExecute method
                    Log.w("JSON",current);
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
            // dismiss the progress dialog after receiving data from API
            //progressDialog.dismiss();

            //Json
            try{
                Gson gson = new Gson();

                profesorList = (ArrayList<Profesor>) gson.fromJson(s,
                        new TypeToken<ArrayList<Profesor>>(){}.getType());

                adaptadorProfesor = new AdaptadorProfesor(profesorList, MantenimientoProfesorActivity.this);
                coordinatorLayout = findViewById(R.id.constraint_layoutP);

                //white background notification bar
                whiteNotificationBar(mRecyclerView);
                Log.d("dataProfes", jsonObjectAsString);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(MantenimientoProfesorActivity.this, DividerItemDecoration.VERTICAL));
                mRecyclerView.setAdapter(adaptadorProfesor);

            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("JSONP",s);
        }

    }

    public class MyAsyncTasksProfeOperaciones extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // display a progress dialog for good user experiance
            /*progressDialog = new ProgressDialog(MantenimientoProfesorActivity.this);
            progressDialog.setMessage("Please Wait");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {

            // implement API in background and store the response in current variable
            String current = "";


            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrlTemp);

                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    InputStreamReader isw = new InputStreamReader(in);

                    int data = isw.read();
                    while (data != -1) {
                        current += (char) data;
                        data = isw.read();
                    }

                    // return the data to onPostExecute method
                    Log.w("JSON",current);
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
            // dismiss the progress dialog after receiving data from API
            //progressDialog.dismiss();

            //Json
            try{
                Gson gson = new Gson();

                JSONObject jsonObjectMensaje = new JSONObject(s);
                boolean estado = jsonObjectMensaje.getBoolean("error");
                String mensaje = jsonObjectMensaje.getString("mensaje");
                //Se muestra el mensaje de estado de operacion
                Toast.makeText(MantenimientoProfesorActivity.this,mensaje,Toast.LENGTH_LONG).show();

                //Y se recarga la lista de profesores
                apiUrlTemp = apiUrl;
                MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
                myAsyncTasks.execute();
            }catch (Exception e){
                e.printStackTrace();
            }
            Log.d("JSONMENSAJE",s);
        }

    }

    @Override
    public void onItemMove(int source, int target) { adaptadorProfesor.onItemMove(source, target); }

    private void checkIntentInformation() {//Aca se realiza el update y el add
        Bundle extras = getIntent().getExtras();
        Gson gson = new Gson();
        if (extras != null) {
            Profesor aux;
            aux = (Profesor) getIntent().getSerializableExtra("addProfesor");
            if (aux == null) {
                aux = (Profesor) getIntent().getSerializableExtra("editProfesor");
                if (aux != null) { //Accion de actualizar profesor
                    //found an item that can be updated
                    String profeU = "";
                    profeU = gson.toJson(aux);

                    apiUrlTemp = apiUrlAcciones+ "app=updateP" +"&profeU=" + profeU;
                    MyAsyncTasks myAsyncTasks = new MyAsyncTasks();//Aca va el de operaciones
                    myAsyncTasks.execute();
                    //Toast.makeText(getApplicationContext(), aux.getNombre() + "Editado Correctamente!", Toast.LENGTH_LONG).show();
                }
            } else { //Accion de agregar profesor
                //found a new Profesor Object
            String profeA = "";
            profeA = gson.toJson(aux);

            apiUrlTemp = apiUrlAcciones+"app=addP" + "&profeA=" + profeA;
            MyAsyncTasksProfeOperaciones myAsyncTasksP = new MyAsyncTasksProfeOperaciones();//Aca tambien
            myAsyncTasksP.execute();
            }
        }
    }

    private void goToAddUpdProfesor() {
        Intent intent = new Intent(this, UpdateProfesorActivity.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }
}
