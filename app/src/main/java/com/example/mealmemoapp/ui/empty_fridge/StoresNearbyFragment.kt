package com.example.mealmemoapp.ui.empty_fridge

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mealmemoapp.R
import com.example.mealmemoapp.data.remote_database.PlacesApiService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import dagger.hilt.android.AndroidEntryPoint
import com.example.mealmemoapp.databinding.FragmentStoresNearbyLayoutBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import javax.inject.Named

@AndroidEntryPoint
class StoresNearbyFragment: Fragment(),OnMapReadyCallback {
    private lateinit var map:GoogleMap
    @Inject
    //@Named("PlacesApi")
    lateinit var placesApi: PlacesApiService


    private var _binding: FragmentStoresNearbyLayoutBinding? = null
    private val binding get() = _binding!!

    private val permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission())
    {
            isGranted: Boolean->
        if (isGranted) getUserLocation()
        else Toast.makeText(requireContext(),"Location permission required",Toast.LENGTH_SHORT).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoresNearbyLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val mapFragment=childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)

        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance()
            childFragmentManager.beginTransaction()
                .replace(R.id.map, mapFragment)
                .commit()
        }
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map=googleMap
        getUserLocation()
    }

    private fun getUserLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        //fusedLocationClient.lastLocation.addOnSuccessListener says need member declaration maybe building it will resolve the issue
        //there is a declaration on line 79

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLandmark = LatLng(it.latitude, it.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLandmark, 14f))

                // Add a marker for the user's location
                map.addMarker(MarkerOptions().position(userLandmark).title("Your Location"))

                // Enable the location layer on the map
                map.isMyLocationEnabled = true
                searchNearbyStores(userLandmark)
            }
        }
    }

    private fun searchNearbyStores(location:LatLng){
        val apiKey=getString(R.string.google_maps_key)
        val locationString="${location.latitude},${location.longitude}"

        lifecycleScope.launch {
            try {
                val response = placesApi.getNearbyPlaces(locationString, 10000, "supermarket", apiKey)
                if (response.isSuccessful) {
                    response.body()?.let { placesResponse ->
                        // Clear existing markers
                        map.clear()

                        for (place in placesResponse.results) {
                            val markerLocation = LatLng(place.geometry.location.lat, place.geometry.location.lng)
                            map.addMarker(MarkerOptions().position(markerLocation).title(place.name))
                        }
                    }
                } else {
                    Log.e("MapFragment", "API Error: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("MapFragment", "Network request failed", e)
            }
        }
    }
}