package com.shahed.firebace.views

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.databinding.FragmentEventsListBinding
import com.shahed.firebace.network.model.User
import com.shahed.firebace.utils.AppConstants

private const val TAG = "PlaceslistFragment"

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentEventsListBinding
    private lateinit var adapter: EventsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        adapter = EventsAdapter {
            findNavController().navigate(
                HistoryFragmentDirections.actionHistoryFragmentToPlaceDetailsFragment(
                    it
                )
            )
        }

        binding.rvPlaces.adapter = adapter
        val sharedPreferences = requireContext().getSharedPreferences(
            AppConstants.PREF_NAME,
            Context.MODE_PRIVATE
        )
        val documentId = sharedPreferences.getString("id", " ")

        db.collection("users").document(documentId!!).get()
            .addOnSuccessListener {
//            Log.d(TAG, "onViewCreated: ${it.toObjects(Place::class.java)}")

                var eventList = it.toObject(User::class.java)?.events?.toMutableList()

                if (eventList != null) {
                    adapter.data = eventList
                }
            }

//        db.collection("places").get().addOnSuccessListener {
//            Log.d(TAG, "onViewCreated: ${it.documents}")
//            it.documents.forEach {
//                it.id
//                it.metadata
//
//            }
//        }

    }
}