package com.example.android.evaluacion3cp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.widget.Toast
import java.io.File
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
            val resultado = db.insert("ubicaciones", null, cv)
            db.close()
            if (resultado == 1L) {
                System.out.println("mensaje no agregado")
                Toast.makeText(miContexto, "Latitud y longitud NO agregados a la DB", Toast.LENGTH_SHORT).show()

            }
            else {
                System.out.println("mensaje agregado")
                Toast.makeText(miContexto, "Latitud y longitud SÍ agregados a la DB", Toast.LENGTH_SHORT).show()

            }
        } catch (e: SQLException)
        {
            System.out.println("ERROR Al insertar locacion en DB")
            Toast.makeText(miContexto, "Error al insertar locación. Revise log.", Toast.LENGTH_LONG).show()
        }
    }

    fun eliminar(nombreDb: String) {
        var db = this.writableDatabase
        val file = miContexto.getFileStreamPath(nombreDb)
        if (file.exists()) {
            miContexto.deleteDatabase(nombreDb)
            Toast.makeText(miContexto, "DB eliminada", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(miContexto, "Error al eliminar BD", Toast.LENGTH_LONG).show()
        }
    }
}