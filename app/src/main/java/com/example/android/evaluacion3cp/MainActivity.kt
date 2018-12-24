package com.example.android.evaluacion3cp

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import java.sql.SQLException

class MainActivity : AppCompatActivity(),LocationListener, OnMapReadyCallback {

    //se crea variables para latitud, longitud y mapa
    var mapa: GoogleMap? = null
    var latitud = 0.0
    var longitud = 0.0
    var altitud = 0.0
    var lista_locaciones = ArrayList<String>()
    var lm : LocationManager? = null

    override fun onMapReady(p0: GoogleMap?) {
        mapa = p0

        //se revisa nuevamente si los permisos fueron otorgados
        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos)
        {
            granted = granted and (ActivityCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted)
        {
            //aqui se le pone codigo 2 en vez del anterior (el de mas abajo) que dice 1
            ActivityCompat.requestPermissions(this,permisos,2)
        }
        else {
            p0?.isMyLocationEnabled = true
        }
    }

    //se declara un location manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //se inicializa el location manager
        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //el fragmento mapa necesita un callback q haremos ahora
        val fragmentoMapaCB = supportFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
        fragmentoMapaCB.getMapAsync(this)

        //el gps es un permiso importante entonces debe salir un popup que le indique al usuario
        //sobre el uso del GPS
        val permisos = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        var granted = true
        for (permiso in permisos)
        {
            granted = granted and (ActivityCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED)
        }
        if (!granted)
        {
            ActivityCompat.requestPermissions(this,permisos,1)
        }
        else {
            lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,this)
        }

        //fragmento mapa
        val fragmentoMapa = supportFragmentManager.findFragmentById(R.id.FragmentMapa) as SupportMapFragment
        //antes decia THIS con rojo, pro al anadir el listner de mapa (ultimo metodo de main activity_) y
        // sobreescribir, deja de suceder
        fragmentoMapa.getMapAsync(this)
    }

    override fun onLocationChanged(location: Location?) {
        //se indica que las variables latitd y longitud obtienen sus valores de la locacion
        //obtenida por el servicio de ubicacion
        latitud = location?.latitude.toString().toDouble()
        longitud = location?.longitude.toString().toDouble()
        altitud = location?.altitude.toString().toDouble()

        var lat = latitud.toString()
        var long = longitud.toString()

        //var marcador = LatLng(latitud,longitud)
        //mapa?.addMarker(MarkerOptions().position(marcador))
        var customSQL = CustomSQL(this,"myDB", null, 1)
        customSQL.insertar(lat,long)

       // if(direccionactual.size>0) {
       //     mapa?.addMarker(MarkerOptions().position(marcador))

        fun comenzarAnotar() {
            /*   var ubicacion : String = marcador.toString()

               btnIniciar.setOnClickListener {
                   val db = this.writableDatabase
                   mapa?.addMarker(MarkerOptions().position(marcador))
                   fun insertar(ubicacion : String) {
                       var cv = ContentValues()
                       cv.put(marcador)
                       val resultado = db.insert(" lista", null, cv)
                       db.close()
                   }
               }*/
        }

        fun finalizarAnotar() {
            btnDetener.setOnClickListener {

            }
        }

      //  }
        fun guardarLocaciones(){

        }

   }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode)
        {
            1->
            {
                lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var granted = true
                for (permiso in permissions) {
                    granted = granted and (ActivityCompat.checkSelfPermission(
                        this, permiso)== PackageManager.PERMISSION_GRANTED)
                }
                if (grantResults.size > 0 && granted)
                {
                    lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1f,this)
                } else
                {
                    Toast.makeText(this, " permiso de gps requerido", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    //se deja vacio
    }

    override fun onProviderEnabled(provider: String?) {
        //se deja vacio
    }

    override fun onProviderDisabled(provider: String?) {
        //se deja vacio
    }
}

