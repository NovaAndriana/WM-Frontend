package com.warehousemart.wm.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.warehousemart.wm.R
import com.warehousemart.wm.model.Produk
import com.squareup.picasso.Picasso
import com.warehousemart.wm.activity.DetailProdukDiskonActivity
import com.warehousemart.wm.model.Brand
import com.warehousemart.wm.model.Menu
import kotlin.collections.ArrayList

class AdapterBrand(var activity: Activity, var data: ArrayList<Brand>) : RecyclerView.Adapter<AdapterBrand.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNamaBrand = view.findViewById<TextView>(R.id.tv_namabrand)
        val imgBrand = view.findViewById<ImageView>(R.id.img_brand)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_brand, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNamaBrand.text = data[position].brand_name
//        holder.imgProduk.setImageResource(data[position].image)
        val image = "https://warehouse-mart.com/backend/public/uploads/" + data[position].image
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product2)
                .error(R.drawable.product2)
                .into(holder.imgBrand)

        //holder.layout.setOnClickListener {
        //    val activiti = Intent(activity, DetailProdukDiskonActivity::class.java)
        //    val str = Gson().toJson(data[position], Produk::class.java)
        //    activiti.putExtra("extra", str)
        //    activity.startActivity(activiti)
       // }
    }

}