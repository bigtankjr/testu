package com.development.testu.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.development.testu.R
import com.development.testu.activities.TestingLocationListActivity.Companion.EXTRA_PLACE_DETAILS
import com.development.testu.models.TestingLocationModel
import kotlinx.android.synthetic.main.activity_map.*

/**
 * https://www.raywenderlich.com/230-introduction-to-google-maps-api-for-android-with-kotlin
 */
class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mTestingLocationDetails: TestingLocationModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (intent.hasExtra(EXTRA_PLACE_DETAILS)) {
            mTestingLocationDetails =
                intent.getParcelableExtra(EXTRA_PLACE_DETAILS) as TestingLocationModel?
        }

        if (mTestingLocationDetails != null) {

            setSupportActionBar(toolbar_map)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mTestingLocationDetails!!.title

            toolbar_map.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment: SupportMapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            supportMapFragment.getMapAsync(this)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        /**
         * Add a marker on the location using the latitude and longitude and move the camera to it.
         */
        val position = LatLng(
            mTestingLocationDetails!!.latitude,
            mTestingLocationDetails!!.longitude
        )
        googleMap.addMarker(MarkerOptions().position(position).title(mTestingLocationDetails!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        googleMap.animateCamera(newLatLngZoom)
    }
}