//package com.example.mealmemoapp.ui.empty_fridge
//
//import android.Manifest
//import android.content.pm.PackageManager
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.core.app.ActivityCompat
//import androidx.fragment.app.Fragment
//import com.example.mealmemoapp.databinding.FragmentHomePageBinding
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.maps.GoogleMap
//import com.google.android.gms.maps.OnMapReadyCallback
//import dagger.hilt.android.AndroidEntryPoint
//import com.example.mealmemoapp.databinding.FragmentStoresNearbyLayoutBinding
//import com.google.android.gms.location.LocationServices
//import com.google.android.gms.maps.CameraUpdateFactory
//import com.google.android.gms.maps.SupportMapFragment
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.libraries.mapsplatform.transportation.consumer.model.Location
//
//
//class StoresNearbyFragment: Fragment(),OnMapReadyCallback {
//    private lateinit var map:GoogleMap
//    private lateinit var client:FusedLocationProviderClient
//    private var _binding: FragmentStoresNearbyLayoutBinding? = null
//    private val binding get() = _binding!!
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = FragmentStoresNearbyLayoutBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val mapFragment=childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        client=LocationServices.getFusedLocationProviderClient(requireContext())
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map=googleMap
//        getUserLocation()
//    }
//
//    private fun getUserLocation(){
//        if(ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)
//            !=PackageManager.PERMISSION_GRANTED){
//            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
//            return
//        }
//        client.lastLocation.addOnSuccessListener {
//                location:Location?->location?.let{
//            val userLocaction=LatLng(it.latitude,it.longitude)
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocaction,14f))
//            searchNearbyStores(userLocaction)
//
//        }
//        }
//    }
//    private fun searchNearbyStores(location:LatLng){
//        val url="https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
//                "?location=${location.latitude},${location.longitude}" +
//                "&radius=10000" + // Search within 10km
//                "&type=supermarket" + // Type: Grocery or supermarket
//                "&key=YOUR_API_KEY"
//    }
//}