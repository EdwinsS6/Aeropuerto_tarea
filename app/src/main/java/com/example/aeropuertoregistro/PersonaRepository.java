package com.example.aeropuertoregistro;

import android.content.Context;
import java.util.List;

public class PersonaRepository {
    private static PersonaRepository instance;
    private PersonaDatabase database;

    private PersonaRepository(Context context) {
        database = new PersonaDatabase(context);
    }

    public static PersonaRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PersonaRepository(context.getApplicationContext());
        }
        return instance;
    }

    public void insertarPersona(Persona persona) {
        database.insertarPersona(persona);
    }

    public List<Persona> obtenerPersonas() {
        return database.obtenerPersonas();
    }
}

