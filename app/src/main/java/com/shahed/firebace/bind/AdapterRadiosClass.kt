package com.shahed.firebace.bind

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shahed.firebace.CompanyUI.AddEventActivity
import com.shahed.firebace.R
import com.shahed.firebace.network.model.Categorie
import kotlinx.android.synthetic.main.item_cat_rd.view.*
import java.util.*

class AdapterRadiosClass(val context: Context, val list1: List<Categorie>) :
    RecyclerView.Adapter<AdapterRadiosClass.MyViewHolder>() {

    inner class MyViewHolder(iteamView: View) : RecyclerView.ViewHolder(iteamView) {
        var currenttext: Categorie?? = null
        var currentposition: Int = 0

        init {
            iteamView.setOnClickListener {
                AddEventActivity.category = currenttext!!.categorieName + ""

            }
            iteamView.rb_music.setOnCheckedChangeListener { compoundButton, b ->
                AddEventActivity.category = currenttext!!.categorieName + ""
                Toast.makeText(
                    context,
                    AddEventActivity.category + " has been clicked",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        fun setData(iteam: Categorie?, pos: Int) {
            var lan: String = Locale.getDefault().getDisplayLanguage();
            Log.e("CurrL", lan)
            if (lan.equals("العربية")) {
                itemView.rb_music.text = iteam!!.categorieNameAr
            } else {
                itemView.rb_music.text = iteam!!.categorieName
            }

            this.currenttext = iteam
            this.currentposition = pos

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cat_rd, parent, false)
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