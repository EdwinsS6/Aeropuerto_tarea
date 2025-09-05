package com.example.aeropuertoregistro;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etNombre, etEdad, etPasaporte;
    private Spinner spSexo, spNacionalidad;
    private Button btnIniciarRegistro, btnSimularBiometria;
    private TextView tvCronometro;
    private RecyclerView rvPersonas;
    private PersonaAdapter adapter;
    private PersonaViewModel viewModel;

    private Handler handler = new Handler();
    private Runnable cronometroRunnable;
    private int segundosTranscurridos = 0;

    private long tiempoInicio = 0;
    private long tiempoFinal = 0;
    private boolean cronometroActivo = false;
    private boolean biometriaCorrecta = false;
    private String nacionalidadActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombre = findViewById(R.id.etNombre);
        etEdad = findViewById(R.id.etEdad);
        etPasaporte = findViewById(R.id.etPasaporte);
        spSexo = findViewById(R.id.spSexo);
        spNacionalidad = findViewById(R.id.spNacionalidad);
        btnIniciarRegistro = findViewById(R.id.btnIniciarRegistro);
        btnSimularBiometria = findViewById(R.id.btnSimularBiometria);
        tvCronometro = findViewById(R.id.tvCronometro);
        rvPersonas = findViewById(R.id.rvPersonas);

        // Configurar Spinners
        ArrayAdapter<String> sexoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("Masculino", "Femenino", "Otro"));
        sexoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSexo.setAdapter(sexoAdapter);

        ArrayAdapter<String> nacionalidadAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getPaises());
        nacionalidadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spNacionalidad.setAdapter(nacionalidadAdapter);

        // Configurar RecyclerView
        rvPersonas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PersonaAdapter(null);
        rvPersonas.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(PersonaViewModel.class);
        viewModel.getPersonasLiveData().observe(this, personas -> {
            adapter.setPersonas(personas);
        });

        btnIniciarRegistro.setOnClickListener(v -> iniciarRegistro());
        btnSimularBiometria.setOnClickListener(v -> simularBiometria());
    }

    private List<String> getPaises() {
        return Arrays.asList("Guatemalteca", "Estadounidense", "Mexicana", "Canadiense", "Colombiana", "Argentina", "Española", "Otro");
    }

    private void iniciarRegistro() {
        String nombre = etNombre.getText().toString().trim();
        String edadStr = etEdad.getText().toString().trim();
        String sexo = spSexo.getSelectedItem().toString();
        String nacionalidad = spNacionalidad.getSelectedItem().toString();
        String pasaporte = etPasaporte.getText().toString().trim();

        if (nombre.isEmpty() || edadStr.isEmpty() || pasaporte.isEmpty()) {
            Toast.makeText(this, getString(R.string.completa_campos), Toast.LENGTH_SHORT).show();
            return;
        }
        int edad;
        try {
            edad = Integer.parseInt(edadStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.edad_invalida), Toast.LENGTH_SHORT).show();
            return;
        }
        nacionalidadActual = nacionalidad;
        biometriaCorrecta = false;
        tvCronometro.setText(getString(R.string.tiempo_cero));
        segundosTranscurridos = 0;
        if (nacionalidad.equals("Guatemalteca") || nacionalidad.equals("Estadounidense")) {
            cronometroActivo = false;
            tiempoInicio = 0;
            tiempoFinal = 0;
            handler.removeCallbacksAndMessages(null);
            Toast.makeText(this, getString(R.string.cronometro_omitido), Toast.LENGTH_SHORT).show();
        } else {
            cronometroActivo = true;
            tiempoInicio = System.currentTimeMillis();
            iniciarCronometroUI();
            Toast.makeText(this, getString(R.string.cronometro_iniciado), Toast.LENGTH_SHORT).show();
        }
    }

    private void iniciarCronometroUI() {
        cronometroRunnable = new Runnable() {
            @Override
            public void run() {
                segundosTranscurridos++;
                int minutos = segundosTranscurridos / 60;
                int segundos = segundosTranscurridos % 60;
                tvCronometro.setText(String.format("Tiempo: %02d:%02d", minutos, segundos));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(cronometroRunnable);
    }

    private void simularBiometria() {
        if (nacionalidadActual.isEmpty()) {
            Toast.makeText(this, getString(R.string.primero_inicia_registro), Toast.LENGTH_SHORT).show();
            return;
        }
        handler.removeCallbacksAndMessages(null);
        if (cronometroActivo) {
            tiempoFinal = System.currentTimeMillis();
            long tiempoRegistro = tiempoFinal - tiempoInicio;
            int totalSegundos = (int) (tiempoRegistro / 1000);
            int minutos = totalSegundos / 60;
            int segundos = totalSegundos % 60;
            tvCronometro.setText(String.format("Tiempo: %02d:%02d", minutos, segundos));
            biometriaCorrecta = true;
            guardarPersona(tiempoRegistro, true);
            cronometroActivo = false;
        } else {
            // Nacionalidad exenta, tiempo = 0
            tvCronometro.setText("Tiempo: 00:00");
            biometriaCorrecta = true;
            guardarPersona(0, true);
        }
        Toast.makeText(this, getString(R.string.biometria_simulada), Toast.LENGTH_SHORT).show();
        limpiarCampos();
    }

    private void guardarPersona(long tiempoRegistro, boolean biometriaCorrecta) {
        String nombre = etNombre.getText().toString().trim();
        int edad = Integer.parseInt(etEdad.getText().toString().trim());
        String sexo = spSexo.getSelectedItem().toString();
        String nacionalidad = spNacionalidad.getSelectedItem().toString();
        String pasaporte = etPasaporte.getText().toString().trim();
        Persona persona = new Persona(0, nombre, edad, sexo, nacionalidad, pasaporte, tiempoRegistro, biometriaCorrecta);
        viewModel.insertarPersona(persona);
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etEdad.setText("");
        etPasaporte.setText("");
        spSexo.setSelection(0);
        spNacionalidad.setSelection(0);
        nacionalidadActual = "";
        tiempoInicio = 0;
        tiempoFinal = 0;
        cronometroActivo = false;
        biometriaCorrecta = false;
        handler.removeCallbacksAndMessages(null);
        segundosTranscurridos = 0;
    }
}