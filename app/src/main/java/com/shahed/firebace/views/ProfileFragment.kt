package com.shahed.firebace.views

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shahed.firebace.CompanyUI.MyEventsActivity
import com.shahed.firebace.R
import com.shahed.firebace.databinding.FragmentProfileBinding
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import java.io.File
import java.nio.file.Files.delete
import java.util.*
import kotlin.concurrent.timerTask

private const val TAG = "ProfileFragment"

private var builder: AlertDialog.Builder? = null

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var imageUri: Uri? = null
    private var imageUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = Firebase.auth.currentUser
        if (user != null) {
            binding.userName.setText(user?.displayName)
            imageUrl = user.photoUrl.toString()
            val sharedPreferences: SharedPreferences =
                activity!!.getSharedPreferences("Event App", 0)
            val img = sharedPreferences.getString("img", "User")
            if (!img?.equals("null")!!) {
                Glide.with(requireContext())
                    .load(img)
                    .fitCenter()
                    .into(binding.ivProfile)
            }


        }


        binding.ivProfile.setOnClickListener {
            pickAndUploadImage()
        }

        binding.btnConfirm.setOnClickListener {
            updateData()
        }
        builder = AlertDialog.Builder(context)

        binding.deleteA.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
            builder.setMessage("are you conform to delete your account  ")
            builder.setPositiveButton("yes") { DialogInterface, it ->
                deletUser()
            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()


        }
    }

    private fun deletUser() {
        val user = Firebase.auth.currentUser!!
        user.delete().addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("deletemsg", "delete")
                Toast.makeText(context, "You Are Delete Account Succefully ! ", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)

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
                .into(binding.ivProfile)
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
                    val sharedPreferences: SharedPreferences =
                        activity!!.getSharedPreferences("Event App", 0)
                    val id = sharedPreferences.getString("id", "User")
                    var firestore: FirebaseFirestore
                    firestore = FirebaseFirestore.getInstance()
                    firestore.collection("users").document(id.toString())
                        .update(
                            mapOf(
                                "imageUrl" to imageUrl.toString()
                            )
                        )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString("img", imageUrl.toString())
                    editor.apply()
                    editor.commit()


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

    private fun updateData() {
        val user = Firebase.auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = binding.userName.text.toString()
            if (imageUrl != null) photoUri = Uri.parse(imageUrl)
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                    findNavController().popBackStack()
                }
            }
    }

}

