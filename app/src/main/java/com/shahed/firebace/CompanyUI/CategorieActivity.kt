package com.shahed.firebace.CompanyUI


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.R
import com.shahed.firebace.network.model.Categorie
import com.shahed.firebace.network.model.User
import com.shahed.firebace.utils.AppConstants
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shahed.firebace.bind.AdapterClass
import com.shahed.firebace.views.LoginFragmentDirections
import kotlinx.android.synthetic.main.activity_main.*
import java.io.LineNumberReader
import java.util.function.Supplier

class CategorieActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var backBtn: ImageView
    val cats = ArrayList<Categorie>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categorie)
        rv = findViewById(R.id.rv)
        backBtn = findViewById(R.id.backBtn)


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
                    val adapter = AdapterClass(this, cats)
                    rv.adapter = adapter
                }
            }


    }
}