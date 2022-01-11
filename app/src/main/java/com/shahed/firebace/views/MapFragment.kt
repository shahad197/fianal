package com.shahed.firebace.views

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.shahed.firebace.R
import com.shahed.firebace.databinding.FragmentLocationBinding
import com.shahed.firebace.utils.hide
import com.shahed.firebace.utils.show
import java.io.IOException
import java.util.*

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentLocationBinding
    private val args: MapFragmentArgs by navArgs()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var googleMap: GoogleMap? = null
    private var geocoder: Geocoder? = null
    private lateinit var addresses: MutableList<Address>
    private var type: String = "name"
    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation = locationResult.lastLocation
            getAddress(mLastLocation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(view)
    }

    fun setUp(view: View?) {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (args.lat.isNullOrBlank()) lastLocation

    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
//         googleMap.setMyLocationEnabled(true);
        googleMap!!.uiSettings.isMyLocationButtonEnabled = true
        googleMap!!.uiSettings.isZoomControlsEnabled = true
        googleMap!!.setOnMapClickListener { latLng: LatLng ->
            googleMap!!.clear()
            googleMap!!.addMarker(
                MarkerOptions()
                    .position(latLng)
            )
            val location = Location("a")
            location.latitude = latLng.latitude
            location.longitude = latLng.longitude
            getAddress(location)
        }

        //move camera if edit address
        if (args.lat != null && args.lat!!.isNotBlank()) {
            val latLng = LatLng(args.lat!!.toDouble(), args.lng!!.toDouble())
            googleMap?.addMarker(MarkerOptions().position(latLng))
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
            binding.confirmAddress.hide()
            binding.llAddress.hide()
        }

    }

    @get:SuppressLint("MissingPermission")
    private val lastLocation: Unit
        private get() {
            if (checkPermissions()) {
                if (isLocationEnabled) {
                    fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location?> ->
                        val location = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            googleMap!!.animateCamera(
                                CameraUpdateFactory
                                    .newLatLngZoom(
                                        LatLng(
                                            location.latitude,
                                            location.longitude
                                        ), 15f
                                    )
                            )
                            googleMap!!.addMarker(
                                MarkerOptions()
                                    .position(
                                        LatLng(
                                            location.latitude,
                                            location.longitude
                                        )
                                    )
                            )
                            getAddress(location)
                        }
                    }
                } else {
//                    showMessage("Turn on location")
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            } else {
                requestPermissions()
            }
        }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            lastLocation
        }
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1
        )
    }

    private val isLocationEnabled: Boolean
        private get() {
            val locationManager =
                requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
            return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            ))
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getAddress(location: Location) {
        try {
            geocoder = Geocoder(requireContext(), Locale.forLanguageTag("en"))
            addresses = geocoder!!.getFromLocation(
                location.latitude,
                location.longitude, 1
            )

            val address = addresses.get(0).getAddressLine(0)
            val city = addresses.get(0).locality
            val state = addresses.get(0).subAdminArea
            val country = addresses.get(0).countryName
            val stringTokenizer = StringTokenizer(address, ",")
            var street = ""
            if (stringTokenizer.hasMoreTokens()) {
                street = stringTokenizer.nextToken().trim { it <= ' ' }
            }

            binding.llAddress.show()
            binding.tvAddress.text = address
            binding.tvCity.text = "$city , $state"

            binding.confirmAddress.setOnClickListener {
                setFragmentResult(
                    "location", bundleOf(
                        "lat" to location.latitude.toString(),
                        "lng" to location.longitude.toString(),
                        "address" to address
                    )
                )
                findNavController().popBackStack()
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}