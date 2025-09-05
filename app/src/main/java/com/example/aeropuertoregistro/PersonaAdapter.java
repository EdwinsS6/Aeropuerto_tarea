package com.example.aeropuertoregistro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.PersonaViewHolder> {
    private List<Persona> personas;

    public PersonaAdapter(List<Persona> personas) {
        this.personas = personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PersonaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        return new PersonaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonaViewHolder holder, int position) {
        Persona persona = personas.get(position);
        holder.tv1.setText(persona.getNombre() + " - " + persona.getNacionalidad());
        holder.tv2.setText("Tiempo: " + persona.getTiempoRegistro() + " ms, Biometría: " + (persona.isBiometriaCorrecta() ? "Correcta" : "Fallo"));
    }

    @Override
    public int getItemCount() {
        return personas != null ? personas.size() : 0;
    }

    static class PersonaViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        PersonaViewHolder(View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(android.R.id.text1);
            tv2 = itemView.findViewById(android.R.id.text2);
        }
    }
}

