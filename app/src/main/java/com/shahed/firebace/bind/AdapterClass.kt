package com.shahed.firebace.bind

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shahed.firebace.R
import com.shahed.firebace.network.model.Categorie
import com.shahed.firebace.views.EventsActivity
import kotlinx.android.synthetic.main.item_cat.view.*
import java.util.*

class AdapterClass(val context: Context, val list1: List<Categorie>) :
    RecyclerView.Adapter<AdapterClass.MyViewHolder>() {

    inner class MyViewHolder(iteamView: View) : RecyclerView.ViewHolder(iteamView) {
        var currenttext: Categorie?? = null
        var currentposition: Int = 0

        init {
            iteamView.setOnClickListener {
                val intent = Intent(context, EventsActivity::class.java)
                intent.putExtra("Cat", currenttext!!.categorieName);
                context.startActivity(intent)
            }
            val type = Typeface.createFromAsset(context.getAssets(), "fonts/Exo-Regular.ttf")
            itemView.txt_item_cat.setTypeface(type)

        }

        fun setData(iteam: Categorie?, pos: Int) {
            var lan: String = Locale.getDefault().getDisplayLanguage();
            Log.e("CurrL", lan);
            if (lan.equals("العربية")) {
                itemView.txt_item_cat.text = iteam!!.categorieNameAr
            } else {
                itemView.txt_item_cat.text = iteam!!.categorieName
            }
            val type = Typeface.createFromAsset(context.getAssets(), "fonts/Exo-Regular.ttf")
            itemView.txt_item_cat.setTypeface(type)

            this.currenttext = iteam
            this.currentposition = pos

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cat, parent, false)
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