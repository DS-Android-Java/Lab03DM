package com.example.lab03.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.lab03.R;
import com.example.lab03.accesoDatos.ModelData;
import com.example.lab03.adaptador.AdaptadorCurso;
import com.example.lab03.helper.RecyclerItemTouchHelper;
import com.example.lab03.logicaDeNegocio.Curso;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MantenimientoCursoActivity extends AppCompatActivity
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener, AdaptadorCurso.AdaptadorCursoListener {

    private RecyclerView mRecyclerView;
    private AdaptadorCurso mAdapter;
    private List<Curso> cursoList;
    private CoordinatorLayout coordinatorLayout;
    private SearchView searchView;
    private FloatingActionButton fab;
    private ModelData model = ModelData.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mantenimiento_curso);
        Toolbar toolbar = findViewById(R.id.toolbarC);
        setSupportActionBar(toolbar);

        //toolbar fancy stuff
        getSupportActionBar().setTitle(getString(R.string.my_curso));

        mRecyclerView = findViewById(R.id.recycler_cursosFld);
        cursoList = new ArrayList<>();
        model= ModelData.getInstance();
        cursoList= model.getListaCurso();
        mAdapter = new AdaptadorCurso(cursoList, this);
        coordinatorLayout = findViewById(R.id.coordinator_layoutC);

        // white background notification bar
        whiteNotificationBar(mRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);

        // go to update or add career
        fab = findViewById(R.id.addBtnC);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToAddUpdCurso();
            }
        });

        //delete swiping left and right
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        //should use database info

        // Receive the Carrera sent by AddUpdCarreraActivity
        checkIntentInformation();

        //refresh view
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (direction == ItemTouchHelper.START) {
            if (viewHolder instanceof AdaptadorCurso.MyViewHolder) {
                // get the removed item name to display it in snack bar
                String name = cursoList.get(viewHolder.getAdapterPosition()).getNombre();

                // save the index deleted
                final int deletedIndex = viewHolder.getAdapterPosition();
                // remove the item from recyclerView
                mAdapter.removeItem(viewHolder.getAdapterPosition());

                // showing snack bar with Undo option
                Snackbar snackbar = Snackbar.make(coordinatorLayout, name + " Removido!", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // undo is selected, restore the deleted item from adapter
                        mAdapter.restoreItem(deletedIndex);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        } else {
            //If is editing a row object
            Curso aux = mAdapter.getSwipedItem(viewHolder.getAdapterPosition());
            //send data to Edit Activity
            Intent intent = new Intent(this, UpdateCursoActivity.class);
            intent.putExtra("editable", true);
            intent.putExtra("curso", aux);//Se pasa el objeto curso desde la lista de cursos
            mAdapter.notifyDataSetChanged(); //restart left swipe view
            startActivity(intent);
        }
    }

    @Override
    public void onItemMove(int source, int target) {
        mAdapter.onItemMove(source, target);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds cursoList to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        // Associate searchable configuration with the SearchView   !IMPORTANT
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change, every type on input
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onContactSelected(Curso curso) { //TODO get the select item of recycleView
        Toast.makeText(getApplicationContext(), "Selected: " + curso.getCodigo() + ", " + curso.getNombre(), Toast.LENGTH_LONG).show();
    }

    private void checkIntentInformation() {//Aca se realiza el update y el add
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Curso aux;
            aux = (Curso) getIntent().getSerializableExtra("addCurso");
            if (aux == null) {
                aux = (Curso) getIntent().getSerializableExtra("editCurso");
                if (aux != null) {
                    //found an item that can be updated
                    boolean founded = false;
                    for (Curso curso : cursoList) {
                        if (curso.getCodigo().equals(aux.getCodigo())) {
                            /*curso.setNombre(aux.getNombre());
                            curso.setCreditos(aux.getCreditos());
                            curso.setHora_semanales(aux.getHora_semanales());*/
                            cursoList.remove(curso);
                            cursoList.add(aux);
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
                //found a new Curso Object
                cursoList.add(aux);
                Toast.makeText(getApplicationContext(), aux.getNombre() + " Agregado Correctamente!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void goToAddUpdCurso() {
        Intent intent = new Intent(this, UpdateCursoActivity.class);
        intent.putExtra("editable", false);
        startActivity(intent);
    }
}
