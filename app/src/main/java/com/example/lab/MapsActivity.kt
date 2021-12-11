package com.example.lab

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;
import android.R
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private val binding: ActivityMapsBinding? = null

    private val REQ_PERMISSION = 5


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (checkPermission()) {
            mMap.setMyLocationEnabled(true)
        } else {
            askPermission()
        }

        //
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL)

        // center the map on a location
        val upt = LatLng(45.74744258851616, 21.2262348494032)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(upt))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17f))

        // customize marker
        mMap.addMarker(
            MarkerOptions()
                .position(upt)
                .title("Marker in UPT")
        )
            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.uni))

        // draw line on map
        val latLngList: MutableList<LatLng> = ArrayList()
        val vest = LatLng(45.74713471641559, 21.23151170690118)
        latLngList.add(upt)
        latLngList.add(vest)
        drawPolyLineOnMap(latLngList, mMap)

        // show toast
        mMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
            if (marker.position == upt) {
                Toast.makeText(
                    applicationContext,
                    "Distance: " + SphericalUtil.computeDistanceBetween(upt, vest),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //
            }
            false
        })
    }

    fun drawPolyLineOnMap(list: kotlin.collections.List<LatLng>, googleMap: GoogleMap) {
        val polylineOptions = PolylineOptions()
        polylineOptions.color(Color.GREEN)
        polylineOptions.width(8f)
        polylineOptions.addAll(list)
        googleMap.addPolyline(polylineOptions)
        val builder = LatLngBounds.Builder()
        for (latLng in list) {
            builder.include(latLng)
        }
        builder.build()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (checkPermission()) {
                    mMap!!.isMyLocationEnabled = true
                } else {
                    // Permissions denied
                }
            }
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun askPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQ_PERMISSION
        )
    }
}