package com.shahed.firebace.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.shahed.firebace.CompanyUI.MyEventsActivity
import com.shahed.firebace.R
import com.shahed.firebace.bind.AdapterClass
import com.shahed.firebace.bind.AdapterRadiosClass
import com.shahed.firebace.bind.AutoCompleteAdapter
import com.shahed.firebace.network.model.Categorie
import com.shahed.firebace.network.model.Event
import com.shahed.firebace.views.AddPlaceFragmentDirections
import kotlinx.android.synthetic.main.activity_add_event.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditEventActivity : AppCompatActivity() {

    companion object {
        public var category = "sports"
    }

    private lateinit var rv: RecyclerView
    val cats = ArrayList<Categorie>()
    private lateinit var backBtn: ImageView
    private lateinit var iv_place: ImageView
    private lateinit var btn_location: Button
    private lateinit var tv_date: TextView
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnConfirm: Button
    private lateinit var db: FirebaseFirestore
    private var lat: String = ""
    private var lng: String = ""
    private var address: String = ""
    private var imageUri: Uri? = null
    private var imageUrl: String? = null
    private var uploadTask: UploadTask? = null
    lateinit var placesClient: PlacesClient
    lateinit var mAdapter: AutoCompleteAdapter
    var currenttext: Event?? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)
        currenttext = intent.getSerializableExtra("Event") as? Event
        category = currenttext?.category.toString()
        rv = findViewById(R.id.rv)
        backBtn = findViewById(R.id.backBtn)
        iv_place = findViewById(R.id.iv_place)
        btn_location = findViewById(R.id.btn_location)
        tv_date = findViewById(R.id.tv_date)
        btnConfirm = findViewById(R.id.btn_confirm)
        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        Glide.with(this)
            .load(currenttext!!.imageUrl)
            .placeholder(R.drawable.ic_baseline_add_24)
            .fitCenter()
            .into(iv_place)

        tv_date.text = currenttext!!.date
        etTitle.setText(currenttext!!.title.toString())
        etDescription.setText(currenttext!!.description.toString())
        lat = currenttext!!.lat.toString()
        lng = currenttext!!.lng.toString()
        address = currenttext!!.address.toString()
        imageUrl = currenttext!!.imageUrl.toString()


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager



        backBtn.setOnClickListener {
            finish()
        }

        var firestore: FirebaseFirestore
        firestore = FirebaseFirestore.getInstance()
        firestore.collection("Categories")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        val name = document.data["categorieName"].toString()
                        val namear = document.data["categorieNameAr"].toString()
                        Log.e("TtTTT", name);
                        val Categorie = Categorie(
                            categorieName = name,
                            categorieNameAr = namear
                        )
                        cats.add(Categorie)
                    }
                    val adapter = AdapterRadiosClass(this, cats)
                    rv.adapter = adapter
                }
            }



        db = Firebase.firestore

        btn_location.setOnClickListener {
            AddPlaceFragmentDirections.actionAddPlaceFragmentToMapFragment(
                null,
                null
            )

        }


        tv_date.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Select date")
                    .build()

            datePicker.show(getSupportFragmentManager(), datePicker.tag)
            datePicker.addOnPositiveButtonClickListener {
                val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = it
                val format = SimpleDateFormat("dd - MM - yyyy ")
                val formattedDate: String = format.format(calendar.time)
                tv_date.text = formattedDate
            }
        }

        iv_place.setOnClickListener {
            pickAndUploadImage()
        }


        btnConfirm.setOnClickListener {

            db.collection("Events").document(currenttext!!.id.toString())
                .update(
                    mapOf(
                        "title" to etTitle.text.toString(),
                        "description" to etDescription.text.toString(),
                        "date" to tv_date.text.toString(),
                        "lat" to lat,
                        "lng" to lng,
                        "address" to address,
                        "imageUrl" to imageUrl,
                        "userID" to Firebase.auth.currentUser?.uid,
                        "category" to category
                    )
                )
            Toast.makeText(this, "Event Update successfully ", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(this, MyEventsActivity::class.java)
            startActivity(intent)
            finish()


        }

        var apiKey = getString(R.string.google_maps_key)
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyAKlyZIHpzSuQUe9kJ2WFy-XOGGLZQTSM8")
        }
        placesClient = Places.createClient(this)

        setUpAutoCompleteTextView()

    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickAndUploadImage() = selectImageFromGalleryLauncher.launch("image/*")

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onStorageDenied() {

    }

    private val selectImageFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            Glide.with(this)
                .load(uri)
                .fitCenter()
                .into(iv_place)
            uploadImage()
        }

    private fun uploadImage() {
        val file = Uri.fromFile(File(imageUri?.path))
        val ref = Firebase.storage.reference.child("images/${UUID.randomUUID()}")


        ref.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.d("AddEvent", "uploadImage: success")
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    this.imageUrl = imageUrl.toString()
                }
            }
            .addOnFailureListener {
                Log.d("AddEvent", "uploadImage: failed")
            }
            .addOnProgressListener { task ->
                val percent = task.bytesTransferred / task.totalByteCount

            }
            .addOnCanceledListener {

            }
    }

    private fun setUpAutoCompleteTextView() {
        autocomplete.setThreshold(1)
        autocomplete.onItemClickListener = autocompleteClickListener
        mAdapter = AutoCompleteAdapter(this, placesClient)
        autocomplete.setAdapter(mAdapter)
    }


    var autocompleteClickListener: AdapterView.OnItemClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            var item: AutocompletePrediction? = mAdapter.getItem(position)
            var placeID = item?.placeId
            var placeFields: List<Place.Field> = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.LAT_LNG
            )
            var request: FetchPlaceRequest? = null
            if (placeID != null) {
                request = FetchPlaceRequest.builder(placeID, placeFields).build()
            }

            if (request != null) {
                placesClient.fetchPlace(request).addOnSuccessListener {
                    //                responseView.setText(task.getPlace().getName() + "\n" + task.getPlace().getAddress());
                    val place = it.place
                    var stringBuilder = StringBuilder()
                    stringBuilder.append("Name: ${place.name}\n")
                    var queriedLocation: LatLng? = place.latLng
                    stringBuilder.append("Latitude: ${queriedLocation?.latitude}\n")
                    stringBuilder.append("Longitude: ${queriedLocation?.longitude}\n")
                    stringBuilder.append("Address: ${place.address}\n")
                    response_view.text = stringBuilder
                    Log.i("TAG", "Called getPlaceById to get Place details for $placeID")
                    lat = place.latLng.latitude.toString()
                    lng = place.latLng.longitude.toString()
                    address = place.address
                }.addOnFailureListener {
                    it.printStackTrace()
                    response_view.text = it.message
                }
            }
            hideKeyboard()
        }


    fun Activity.hideKeyboard() {
        if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}