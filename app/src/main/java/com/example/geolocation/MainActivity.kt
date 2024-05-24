package com.example.geolocation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity(), LocationListener {
    private val LOCATION_PERMISSION_CODE = 2
    private lateinit var locManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_CODE)
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        val prv = locManager.getBestProvider(Criteria(), true)
        if (prv != null) {
            val location = locManager.getLastKnownLocation(prv)
            val btn = findViewById<Button>(R.id.updButton).setOnClickListener {
                if (location != null) {
                    findViewById<TextView>(R.id.lat).text = String.format("%.5f", location.latitude)
                    findViewById<TextView>(R.id.lng).text = String.format("%.5f", location.longitude)
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        findViewById<TextView>(R.id.lat).text = String.format("%.5f", location.latitude)
        findViewById<TextView>(R.id.lng).text = String.format("%.5f", location.longitude)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            }
        }
    }
}