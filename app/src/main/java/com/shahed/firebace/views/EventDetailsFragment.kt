package com.shahed.firebace.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.databinding.FragmentEventDetailsBinding
import com.shahed.firebace.network.model.Event
import com.shahed.firebace.network.model.User
import com.shahed.firebace.utils.AppConstants

private const val TAG = "LoginFragment"

class PlaceDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEventDetailsBinding
    private val args: PlaceDetailsFragmentArgs by navArgs()
    private lateinit var event: Event
    private var events: MutableList<Event>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        event = args.place

        binding.tvTitle.text = event.title
        binding.tvDescription.text = event.description
        binding.date.text = event.date
        binding.tvCategory.text = event.category

        Glide.with(requireContext())
            .load(event.imageUrl)
            .fitCenter()
            .into(binding.ivPlace)


        binding.btnBook.setOnClickListener {
            bookTicket()
        }

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    event.title + " , " + event.date + " , " + event.address + " , " + event.category
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.btnLocation.setOnClickListener {
            findNavController().navigate(
                PlaceDetailsFragmentDirections.actionPlaceDetailsFragmentToMapFragment(
                    event.lat, event.lng
                )
            )
        }
    }

    private fun bookTicket() {
        val db = Firebase.firestore
        val firebaseUser = Firebase.auth.currentUser

        val sharedPreferences = requireContext().getSharedPreferences(
            AppConstants.PREF_NAME,
            Context.MODE_PRIVATE
        )
        val documentId = sharedPreferences.getString("id", " ")

        db.collection("users").document(documentId!!).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                events = user?.events
                events?.add(event)

                val user1 = User(
                    userName = firebaseUser?.displayName,
                    email = firebaseUser?.email,
                    imageUrl = firebaseUser?.photoUrl.toString(),
                    events = events
                )
                db.collection("users").document(documentId).set(user1)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Booked", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }

            }


    }
}