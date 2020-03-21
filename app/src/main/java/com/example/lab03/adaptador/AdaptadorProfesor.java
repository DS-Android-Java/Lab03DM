package com.example.lab03.adaptador;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab03.R;
import com.example.lab03.logicaDeNegocio.Profesor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AdaptadorProfesor extends RecyclerView.Adapter<AdaptadorProfesor.MyViewHolder> implements Filterable {

    private List<Profesor> profesorList;
    private List<Profesor> profesorListFiltered;
    private AdaptadorProfesorListener listener;
    private Profesor deletedItem;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_row_profe,parent,false);

        return  new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final  Profesor profesor = profesorListFiltered.get(position);
        holder.nombre.setText(profesor.getNombre());
        holder.cedula.setText(profesor.getCedula());
        holder.telefono.setText("Teléfono" + profesor.getTelefono());
        holder.email.setText("Email:" +profesor.getEmail());
    }

    @Override
    public int getItemCount() {
        return profesorListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    profesorListFiltered =profesorList;
                }else{
                    List<Profesor> filteredList = new ArrayList<>();
                    for(Profesor row:profesorList){
                        if(row.getCedula().toLowerCase().contains(charString.toLowerCase()) || row.getNombre().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    profesorListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = profesorListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                profesorListFiltered=(ArrayList<Profesor>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, cedula, telefono, email;
        //two layers
        public RelativeLayout viewForeground, viewBackgroundDelete, viewBackgroundEdit;


        public MyViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.nombre);
            cedula= view.findViewById(R.id.cedula);
            telefono = view.findViewById(R.id.telefono);
            email = view.findViewById(R.id.email);
            viewBackgroundDelete = view.findViewById(R.id.view_background_delete);
            viewBackgroundEdit = view.findViewById(R.id.view_background_edit);
            viewForeground = view.findViewById(R.id.view_foreground);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    listener.onContactSelected(profesorListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public AdaptadorProfesor(List<Profesor> profesorList, AdaptadorProfesorListener listener) {
        this.profesorList = profesorList;
        this.listener = listener;
        //init filter
        this.profesorListFiltered = profesorList;
    }
    public void removeItem(int position){ //funcion para eliminar el profesor de la lista
        deletedItem = profesorListFiltered.remove(position);
        Iterator<Profesor> iter = profesorList.iterator();
        while (iter.hasNext()){ //recorre la lista de profesores
            Profesor aux = iter.next();
            if (deletedItem.equals(aux)) //hasta encontrar el profesor que es igual
                iter.remove(); //acá lo remueve de la lista
        }
        notifyItemRemoved(position);
    }

    public void restoreItem(int position){
        if(profesorListFiltered.size() == profesorList.size()){
            profesorListFiltered.add(position, deletedItem);
        }else{
            profesorListFiltered.add(position,deletedItem);
            profesorList.add(deletedItem);
        }
        notifyDataSetChanged();
        notifyItemInserted(position);
    }
    public  Profesor getSwipedItem(int index){
        if (this.profesorList.size()==this.profesorListFiltered.size()){
            return profesorList.get(index);
        }else{
            return profesorListFiltered.get(index);
        }
    }

    public void  onItemMove(int fromPosition, int toPosition){
        if(profesorList.size()==profesorListFiltered.size()){
            if(fromPosition < toPosition){
                for(int i = fromPosition; i<toPosition; i++){
                    Collections.swap(profesorList,i,i+1);
                }
            }else{
                for(int i=fromPosition; i>toPosition; i--){
                    Collections.swap(profesorList, i,i-1);
                }
            }
        }else{
            if(fromPosition<toPosition){
                for(int i = fromPosition; i < toPosition; i++){
                    Collections.swap(profesorListFiltered, i, i+1);
                }
            }else{
                for(int i = fromPosition; i>toPosition; i--){
                    Collections.swap(profesorListFiltered,i,i-1);
                }
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public interface AdaptadorProfesorListener {
        void onContactSelected(Profesor profesor);
    }
}
