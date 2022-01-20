package com.shahed.firebace.views

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.CompanyUI.AddEventActivity
import com.shahed.firebace.R
import com.shahed.firebace.bind.AdapterEventsClass
import com.shahed.firebace.network.model.Event
import com.shahed.firebace.network.model.Order
import kotlinx.android.synthetic.main.item_event.*
import kotlinx.android.synthetic.main.item_event.view.*
import java.lang.String
import java.text.SimpleDateFormat
import java.util.*

class DetailsEventActivity : AppCompatActivity() {
    private lateinit var backBtn: ImageView
    private lateinit var iv_place: ImageView
    private lateinit var tv_title: TextView
    private lateinit var tv_category: TextView
    private lateinit var tv_description: TextView
    private lateinit var btn_book: Button
    private lateinit var date: TextView
    private lateinit var update: TextView
    private lateinit var btn_share: Button
    private lateinit var btn_location: Button
    var currenttext: Order?? = null
    var isA: Boolean?? = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_event)

        val event = intent.getSerializableExtra("Event") as? Event
        btn_share = findViewById(R.id.btn_share)
        btn_location = findViewById(R.id.btn_location)
        update = findViewById(R.id.update)
        iv_place = findViewById(R.id.iv_place)
        backBtn = findViewById(R.id.backBtn)
        btn_book = findViewById(R.id.btn_book)
        tv_title = findViewById(R.id.tv_title)
        tv_title.text = event?.title.toString()
        tv_category = findViewById(R.id.tv_category)
        tv_category.text = event?.category.toString()
        tv_description = findViewById(R.id.tv_description)
        tv_description.text = event?.description.toString()
        val sharedPreferences: SharedPreferences = getSharedPreferences("Event App", 0)
        val sharedNameValue = sharedPreferences.getString("userType", "User")
        if (sharedNameValue.equals("Company")) {
            btn_book.visibility = View.GONE
        } else {
            btn_book.visibility = View.VISIBLE
        }
        backBtn.setOnClickListener {
            finish()
        }
        update.setOnClickListener {
            val intent = Intent(this, EditEventActivity::class.java)
            intent.putExtra("Event", event)
            startActivity(intent)
        }
        btn_share.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT, "Event Name : " + event!!.title + "\n Event Date : " + event.date
                        + "\n Event ID : " + event.id + "\n Event Details : " + event.description
            );
            startActivity(Intent.createChooser(shareIntent, "Send To"))

        }
        btn_location.setOnClickListener {

            if (event?.lat.equals("")) {
                Toast.makeText(this, "this event not have location ", Toast.LENGTH_SHORT)
                    .show()

            } else {
                val geoUri =
                    "http://maps.google.com/maps?q=loc:" + event?.lat.toString() + "," + event?.lng.toString() + " (" + event?.title.toString() + ")"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
                startActivity(intent)
            }

        }
        if (event?.userID.equals(Firebase.auth.currentUser?.uid)) {
            update.visibility = View.VISIBLE
        } else {
            update.visibility = View.GONE
        }


        btn_book.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Reserve Event")
            builder.setMessage("are you conform to reserve on this Event ?  ")
            builder.setPositiveButton("yes") { DialogInterface, it ->
                var firestore: FirebaseFirestore
                firestore = FirebaseFirestore.getInstance()
                firestore.collection("Orders")
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {

                                val eventID = document.data["eventID"].toString()
                                val userID = document.data["userID"].toString()

                                if (userID.equals(Firebase.auth.currentUser?.uid) && eventID.equals(
                                        event?.id
                                    )
                                ) {
                                    isA = false
                                    break
                                } else {
                                    isA = true
                                }
                            }
                            if (isA == true) {
                                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                                val currentDate = sdf.format(Date())
                                currenttext = Order(
                                    id = "",
                                    date = currentDate,
                                    eventID = event?.id,
                                    eventName = event?.title,
                                    eventDate = event?.date,
                                    userID = Firebase.auth.currentUser?.uid,
                                )
                                var db: FirebaseFirestore
                                db = Firebase.firestore
                                db.collection("Orders").add(currenttext!!)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d(
                                            "AddEvent",
                                            "DocumentSnapshot added with ID: ${documentReference.id}"
                                        )
                                        Toast.makeText(
                                            this,
                                            "Order successfully ",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        val intent = Intent(this, OrderDoneActivity::class.java)
                                        intent.putExtra("Order", currenttext)
                                        startActivity(intent)


                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("AddEvent", "Error adding document", e)
                                        Toast.makeText(this, "some error ", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                            } else {
                                Toast.makeText(
                                    this,
                                    "Your Already Have Reservation On this Event !  ",
                                    Toast.LENGTH_LONG
                                ).show()

                            }

                        }
                    }


            }

            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()

        }
        Glide.with(this)
            .load(event!!.imageUrl)
            .placeholder(R.drawable.ic_baseline_add_24)
            .fitCenter()
            .into(iv_place)


    }
}