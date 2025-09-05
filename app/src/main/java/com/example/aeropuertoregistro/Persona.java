package com.example.aeropuertoregistro;

public class Persona {
    private int id;
    private String nombre;
    private int edad;
    private String sexo;
    private String nacionalidad;
    private String pasaporteCifrado;
    private long tiempoRegistro; // en milisegundos
    private boolean biometriaCorrecta;

    public Persona(int id, String nombre, int edad, String sexo, String nacionalidad, String pasaporteCifrado, long tiempoRegistro, boolean biometriaCorrecta) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.sexo = sexo;
        this.nacionalidad = nacionalidad;
        this.pasaporteCifrado = pasaporteCifrado;
        this.tiempoRegistro = tiempoRegistro;
        this.biometriaCorrecta = biometriaCorrecta;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getPasaporteCifrado() { return pasaporteCifrado; }
    public void setPasaporteCifrado(String pasaporteCifrado) { this.pasaporteCifrado = pasaporteCifrado; }
    public long getTiempoRegistro() { return tiempoRegistro; }
    public void setTiempoRegistro(long tiempoRegistro) { this.tiempoRegistro = tiempoRegistro; }
    public boolean isBiometriaCorrecta() { return biometriaCorrecta; }
    public void setBiometriaCorrecta(boolean biometriaCorrecta) { this.biometriaCorrecta = biometriaCorrecta; }
}

