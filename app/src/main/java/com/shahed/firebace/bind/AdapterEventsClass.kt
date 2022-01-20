package com.shahed.firebace.bind

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shahed.firebace.R
import com.shahed.firebace.network.model.Categorie
import com.shahed.firebace.network.model.Event
import kotlinx.android.synthetic.main.item_cat.view.*
import kotlinx.android.synthetic.main.item_event.view.*
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import com.shahed.firebace.views.DetailsEventActivity
import com.shahed.firebace.views.EventsActivity
import kotlin.collections.ArrayList


class AdapterEventsClass(val context: Context, val list1: ArrayList<Event>) :

    RecyclerView.Adapter<AdapterEventsClass.MyViewHolder>() {

    inner class MyViewHolder(iteamView: View) : RecyclerView.ViewHolder(iteamView) {
        var currenttext: Event?? = null
        var currentposition: Int = 0

        init {
            iteamView.setOnClickListener {
                val intent = Intent(context, DetailsEventActivity::class.java)
                intent.putExtra("Event", currenttext);
                context.startActivity(intent)
            }
            iteamView.deleteBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Delete")
                builder.setMessage("are you confirm delete this Event ?  ")
                builder.setPositiveButton("yes") { DialogInterface, it ->
                    var firestore: FirebaseFirestore
                    firestore = FirebaseFirestore.getInstance()
                    firestore.collection("Events").document(currenttext?.id.toString()).delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show()
                            list1.removeAt(currentposition)
                            notifyItemRemoved(currentposition)
                            notifyItemRangeChanged(currentposition, list1.size)
                            notifyDataSetChanged()

                        }
                }

                val alertDialog: AlertDialog = builder.create()
                alertDialog.show()

            }
        }

        fun setData(iteam: Event?, pos: Int) {
            var lan: String = Locale.getDefault().getDisplayLanguage();
            Log.e("CurrL", lan);
            itemView.tv_title.text = iteam!!.title
            Glide.with(context)
                .load(iteam!!.imageUrl)
                .placeholder(R.drawable.ic_baseline_add_24)
                .fitCenter()
                .into(itemView.iv_event)
            itemView.event_id.text = iteam!!.date
            itemView.tv_description.text = iteam!!.description
            if (iteam.userID.equals(Firebase.auth.currentUser?.uid)) {
                itemView.deleteBtn.visibility = View.VISIBLE
            } else {
                itemView.deleteBtn.visibility = View.GONE
            }

            this.currenttext = iteam
            this.currentposition = pos

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_card_events, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list1.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val iteams = list1[position]
        holder.setData(iteams, position)
    }
}