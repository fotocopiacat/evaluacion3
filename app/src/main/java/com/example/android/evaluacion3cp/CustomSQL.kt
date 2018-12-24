package com.example.android.evaluacion3cp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.sql.SQLException

class CustomSQL (val miContexto: Context,
                 var nombreDb : String,
                 val factory: SQLiteDatabase.CursorFactory?,
                 var version : Int) : SQLiteOpenHelper(miContexto,
    nombreDb,
    factory,
    version) {

    override fun onCreate(db: SQLiteDatabase?) {
        var query = "CREATE TABLE Ubicaciones(id INTEGER PRIMARY KEY AUTOINCREMENT, Latitud TEXT, Longitud TEXT)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    //se crea una funcion que intentara escribir en la base de datos
    fun insertar(latitud: String, longitud : String) {
        try {
            val db = this.writableDatabase
            var cv = ContentValues()
            cv.put("latitud", latitud)
            cv.put("longitud", longitud)
            val resultado = db.insert("myDB", null, cv)
            db.close()
            if (resultado == 1L) {
                System.out.println("mensaje no agregado")
            }
            else {
                System.out.println("mensaje agregado")
            }
        } catch (e: SQLException)
        {
            System.out.println("ERROR Al insertar locacion en DB")
        }
    }

}