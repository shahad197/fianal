package com.shahed.firebace.views

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.shahed.firebace.R
import com.shahed.firebace.databinding.FragmentAddEventBinding
import com.shahed.firebace.network.model.Event
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "AddPlaceFragment"

class AddPlaceFragment : Fragment() {

    private lateinit var binding: FragmentAddEventBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var lat: String
    private lateinit var lng: String
    private lateinit var address: String
    private var imageUri: Uri? = null
    private var imageUrl: String? = null
    private var uploadTask: UploadTask? = null
    private var category = "sports"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddEventBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = Firebase.firestore

        Glide.with(requireContext())
            .load(imageUri)
            .placeholder(R.drawable.ic_baseline_add_24)
            .fitCenter()
            .into(binding.ivPlace)

        binding.btnLocation.setOnClickListener {
            findNavController().navigate(
                AddPlaceFragmentDirections.actionAddPlaceFragmentToMapFragment(
                    null,
                    null
                )
            )
        }

        binding.rgCategory.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_sports -> category = "sports"
                R.id.rb_music -> category = "music"
                R.id.rb_arts -> category = "arts"
            }
        }

        binding.tvDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setTitleText("Select date")
                    .build()

            datePicker.show(childFragmentManager, datePicker.tag)
            datePicker.addOnPositiveButtonClickListener {
                val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = it
                val format = SimpleDateFormat("dd - MM - yyyy ")
                val formattedDate: String = format.format(calendar.time)
                binding.tvDate.text = formattedDate
            }
        }

        binding.ivPlace.setOnClickListener {
            pickAndUploadImage()
        }

        setFragmentResultListener("location") { requestKey, bundle ->
            lat = bundle.getString("lat")!!
            lng = bundle.getString("lng")!!
            address = bundle.getString("address")!!
        }

        binding.btnConfirm.setOnClickListener {
            val event = Event(
                title = binding.etTitle.text.toString(),
                description = binding.etDescription.text.toString(),
                date = binding.tvDate.text.toString(),
                lat = lat,
                lng = lng,
                address = address,
                imageUrl = imageUrl,
                category = category
            )

            db.collection(category).add(event)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(requireContext(), "added successfully ", Toast.LENGTH_SHORT)
                        .show()
                    findNavController().popBackStack()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(requireContext(), "some error ", Toast.LENGTH_SHORT).show()
                }
        }
    }


    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun pickAndUploadImage() = selectImageFromGalleryLauncher.launch("image/*")

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun onStorageDenied() {

    }

    private val selectImageFromGalleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            Glide.with(requireContext())
                .load(uri)
                .fitCenter()
                .into(binding.ivPlace)
            uploadImage()
        }

    private fun uploadImage() {
        val file = Uri.fromFile(File(imageUri?.path))
        val ref = Firebase.storage.reference.child("images/${UUID.randomUUID()}")


        ref.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.d(TAG, "uploadImage: success")
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    this.imageUrl = imageUrl.toString()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "uploadImage: failed")
            }
            .addOnProgressListener { task ->
                val percent = task.bytesTransferred / task.totalByteCount

            }
            .addOnCanceledListener {

            }
    }
}