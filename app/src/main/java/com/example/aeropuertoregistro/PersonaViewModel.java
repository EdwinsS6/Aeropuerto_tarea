package com.example.aeropuertoregistro;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;

public class PersonaViewModel extends AndroidViewModel {
    private PersonaRepository repository;
    private MutableLiveData<List<Persona>> personasLiveData;

    public PersonaViewModel(Application application) {
        super(application);
        repository = PersonaRepository.getInstance(application);
        personasLiveData = new MutableLiveData<>();
        cargarPersonas();
    }

    public MutableLiveData<List<Persona>> getPersonasLiveData() {
        return personasLiveData;
    }

    public void cargarPersonas() {
        personasLiveData.setValue(repository.obtenerPersonas());
    }

    public void insertarPersona(Persona persona) {
        repository.insertarPersona(persona);
        cargarPersonas();
    }
}

