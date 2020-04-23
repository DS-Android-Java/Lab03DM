package com.example.lab03.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.example.lab03.R;

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

import java.util.ArrayList;
import java.util.List;

public class MantenimientoProfesorActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AdaptadorProfesor.AdaptadorProfesorListener  {

    private RecyclerView mRecyclerView;
    private AdaptadorProfesor adaptadorProfesor;
    private List<Profesor> profesorList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton floatingActionButton;
    private ModelData modelData = ModelData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_profesor);
        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);

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

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                // remove the item from recyclerView
                adaptadorProfesor.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(coordinatorLayout,name+" Removido!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // undo is selected, restore the deleted item from adapter
                        adaptadorProfesor.restoreItem(deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
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

    @Override
    public void onItemMove(int source, int target) { adaptadorProfesor.onItemMove(source, target); }

    private void checkIntentInformation() {//Aca se realiza el update y el add
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Profesor aux;
            aux = (Profesor) getIntent().getSerializableExtra("addProfesor");
            if (aux == null) {
                aux = (Profesor) getIntent().getSerializableExtra("editProfesor");
                if (aux != null) {
                    //found an item that can be updated
                    boolean founded = false;
                    for (Profesor profesor : profesorList) {
                        if (profesor.getCedula().equals(aux.getCedula())) {
                            profesorList.remove(profesor);
                            profesorList.add(aux);
                            founded = true;
                            break;
                        }
                    }
                    //check if exist
                    if (founded) {
                        Toast.makeText(getApplicationContext(), aux.getNombre() + " Editado Correctamente!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), aux.getNombre() + " No Encontrado!!!", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                //found a new Profesor Object
               profesorList.add(aux);
                Toast.makeText(getApplicationContext(), aux.getNombre() + " Agregado Correctamente!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void goToAddUpdProfesor() {
        Intent intent = new Intent(this, UpdateProfesorActivity.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }
}
