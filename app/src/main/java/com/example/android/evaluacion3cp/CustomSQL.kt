package com.example.android.evaluacion3cp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Environment
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
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

    //Se crea una funcion que intentara escribir en la base de datos
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

    //Función para eliminar DB
    fun eliminar(nombreDb: String) {
        val database = miContexto.getDatabasePath(nombreDb)
        //chequeo si la DB existe y si existe la elimina, si no existe, no la elimina.
        //ojo que aqui el archivo DB sigue apareciendo en data/data/paquete/databases,
        //pero si intento copiarla en mi escritorio, me dice que no pudo porque "No such file or directory"
        if (database.exists()) {
            miContexto.deleteDatabase(nombreDb)
            Toast.makeText(miContexto, "DB eliminada", Toast.LENGTH_LONG).show()
        } else {
            //Luego de borrarla, al apretar de nuevo el boton BORRAR, sale esto.
            Toast.makeText(miContexto, "Error al eliminar BD", Toast.LENGTH_LONG).show()
        }
    }

    fun leerDB(latitud: String, longitud: String, nombreDb: String, cursor: Cursor) {
        val db = this.writableDatabase
        var query = "SELECT * FROM nombreDb"
        db?.execSQL(query)
    }

    fun getUbicaciones(latitudD: Double, longitudD: Double): LatLng {
        var ubicacionesList = LatLng(latitudD,longitudD)
        // Select All Query
        val selectQuery = "SELECT * FROM Ubicaciones"

        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val lat= cursor.getDouble(0)
                val lng = cursor.getDouble(1)
                ubicacionesList = LatLng(latitudD,longitudD)
            } while (cursor.moveToNext())
        }
        // return Translate list
        return ubicacionesList
    }
}