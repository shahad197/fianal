package com.shahed.firebace.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.databinding.FragmentEventsListBinding
import com.shahed.firebace.network.model.Event

private const val TAG = "PlaceslistFragment"

class PlacesListFragment : Fragment() {

    private lateinit var binding: FragmentEventsListBinding
    private lateinit var adapter: EventsAdapter
    private val args: PlacesListFragmentArgs by navArgs()

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
                PlacesListFragmentDirections.actionPlacesListFragmentToPlaceDetailsFragment(
                    it
                )
            )
        }

        binding.rvPlaces.adapter = adapter
        val places = db.collection(args.category).get().addOnSuccessListener {
//            Log.d(TAG, "onViewCreated: ${it.toObjects(Place::class.java)}")
            var placesList = it.toObjects(Event::class.java).toMutableList()
            it.documents.forEachIndexed { index, documentSnapshot ->
                placesList.get(index).id = documentSnapshot.id
            }

            adapter.data = placesList
        }

        db.collection("places").get().addOnSuccessListener {
            Log.d(TAG, "onViewCreated: ${it.documents}")
            it.documents.forEach {
                it.id
                it.metadata

            }
        }

    }
}