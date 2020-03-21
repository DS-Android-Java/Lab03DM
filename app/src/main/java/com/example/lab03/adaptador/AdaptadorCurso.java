package com.example.lab03.adaptador;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab03.R;
import com.example.lab03.logicaDeNegocio.Curso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AdaptadorCurso extends RecyclerView.Adapter<AdaptadorCurso.MyViewHolder> implements Filterable {

    private List<Curso> cursoList;
    private List<Curso> cursoListFiltered;
    private AdaptadorCursoListener listener;
    private Curso deletedItem;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo1, titulo2, description,profe;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;

        public MyViewHolder(View view) {
            super(view);
            titulo1 = view.findViewById(R.id.titleFirstLbl);
            titulo2 = view.findViewById(R.id.titleSecLbl);
            description = view.findViewById(R.id.descriptionLbl);
            profe = view.findViewById(R.id.profAsignado);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(cursoListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }

    public AdaptadorCurso(List<Curso> cursolist, AdaptadorCursoListener listener) {
        this.cursoList = cursolist;
        this.listener = listener;
        //init filter
        this.cursoListFiltered = cursolist;
    }

    @Override
    public AdaptadorCurso.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row_profe, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdaptadorCurso.MyViewHolder holder, int position) {//Lo que se muestra en cada celda de cursos del recycler view
        // rendering view
        final Curso curso = cursoListFiltered.get(position);
        holder.titulo1.setText(curso.getCodigo());
        holder.titulo2.setText(curso.getNombre());
        holder.description.setText(curso.getCreditos() + " créditos");
        holder.profe.setText("   Profesor: "+curso.getProfesor().getNombre());
    }

    @Override
    public int getItemCount() {
        return cursoListFiltered.size();
    }

    public void removeItem(int position) {//Funcion para eliminar el curso de la lista
        deletedItem = cursoListFiltered.remove(position);
        Iterator<Curso> iter = cursoList.iterator();
        while (iter.hasNext()) {//Se recorre la lista de cursos
            Curso aux = iter.next();
            if (deletedItem.equals(aux))//Hasta encontrar el curso que es igual
                iter.remove();//Y cuando lo encuentra lo remueve de la lista
        }
        // notify item removed
        notifyItemRemoved(position);
    }

    public void restoreItem(int position) {

        if (cursoListFiltered.size() == cursoList.size()) {
            cursoListFiltered.add(position, deletedItem);
        } else {
            cursoListFiltered.add(position, deletedItem);
            cursoList.add(deletedItem);
        }
        notifyDataSetChanged();
        // notify item added by position
        notifyItemInserted(position);
    }

    public Curso getSwipedItem(int index) {
        if (this.cursoList.size() == this.cursoListFiltered.size()) { //not filtered yet
            return cursoList.get(index);
        } else {
            return cursoListFiltered.get(index);
        }
    }

    public void onItemMove(int fromPosition, int toPosition) {
        if (cursoList.size() == cursoListFiltered.size()) { // without filter
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(cursoList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(cursoList, i, i - 1);
                }
            }
        } else {
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(cursoListFiltered, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(cursoListFiltered, i, i - 1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    cursoListFiltered = cursoList;
                } else {
                    List<Curso> filteredList = new ArrayList<>();
                    for (Curso row : cursoList) {
                        // filter use two parameters
                        if (row.getCodigo().toLowerCase().contains(charString.toLowerCase()) || row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    cursoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = cursoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cursoListFiltered = (ArrayList<Curso>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdaptadorCursoListener {
        void onContactSelected(Curso curso);
    }

}
