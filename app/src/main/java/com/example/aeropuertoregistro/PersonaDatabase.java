package com.example.aeropuertoregistro;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class PersonaDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "personas.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PERSONA = "persona";

    // Clave de cifrado AES (ejemplo didáctico)
    private static final String AES_KEY = "1234567890123456"; // 16 chars

    public PersonaDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PERSONA + " (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, edad INTEGER, sexo TEXT, nacionalidad TEXT, pasaporteCifrado TEXT, tiempoRegistro INTEGER, biometriaCorrecta INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONA);
        onCreate(db);
    }

    public void insertarPersona(Persona persona) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", persona.getNombre());
        values.put("edad", persona.getEdad());
        values.put("sexo", persona.getSexo());
        values.put("nacionalidad", persona.getNacionalidad());
        values.put("pasaporteCifrado", AESUtil.encrypt(persona.getPasaporteCifrado(), AES_KEY));
        values.put("tiempoRegistro", persona.getTiempoRegistro());
        values.put("biometriaCorrecta", persona.isBiometriaCorrecta() ? 1 : 0);
        db.insert(TABLE_PERSONA, null, values);
        db.close();
    }

    public List<Persona> obtenerPersonas() {
        List<Persona> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PERSONA, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                int edad = cursor.getInt(2);
                String sexo = cursor.getString(3);
                String nacionalidad = cursor.getString(4);
                String pasaporteCifrado = AESUtil.decrypt(cursor.getString(5), AES_KEY);
                long tiempoRegistro = cursor.getLong(6);
                boolean biometriaCorrecta = cursor.getInt(7) == 1;
                lista.add(new Persona(id, nombre, edad, sexo, nacionalidad, pasaporteCifrado, tiempoRegistro, biometriaCorrecta));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }
}

