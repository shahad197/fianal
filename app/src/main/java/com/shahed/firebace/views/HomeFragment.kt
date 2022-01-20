package com.shahed.firebace.views

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.CompanyUI.CategorieActivity
import com.shahed.firebace.databinding.FragmentHomeBinding

private const val TAG = "LoginFragment"


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    val user = Firebase.auth.currentUser


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = Typeface.createFromAsset(activity!!.getAssets(), "fonts/Exo-Regular.ttf")
        val type2 = Typeface.createFromAsset(activity!!.getAssets(), "fonts/Capture_it.ttf")
        binding.txt.setTypeface(type)
        binding.txt1.setTypeface(type)
        binding.txt2.setTypeface(type)
        binding.txt3.setTypeface(type)
        binding.txt4.setTypeface(type)

        binding.logout.setOnClickListener {
            Firebase.auth.signOut()
            findNavController().popBackStack()
        }

        binding.sports.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToPlacesListFragment(
                    "sports"
                )
            )
        }

        binding.music.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToPlacesListFragment(
                    "music"
                )
            )
        }

        binding.arts.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToPlacesListFragment(
                    "arts"
                )
            )
        }

        binding.history.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToHistoryFragment())
        }

        binding.priofie.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
        }

        binding.btnAddPlace.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddPlaceFragment())
        }
        binding.btnAddPlace2.setOnClickListener {
            val intent = Intent(activity, EventsActivity::class.java)
            startActivity(intent)
        }
        binding.btnAddPlace3.setOnClickListener {
            val intent = Intent(activity, CategorieActivity::class.java)
            startActivity(intent)
        }

    }
}