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
import com.warehousemart.wm.activity.DetailProdukActivity
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.Produk
import com.squareup.picasso.Picasso
import com.warehousemart.wm.activity.DetailProdukDiskonActivity
import kotlin.collections.ArrayList

class AdapterProdukDiskon(var activity: Activity, var data: ArrayList<Produk>) : RecyclerView.Adapter<AdapterProdukDiskon.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHargaJual = view.findViewById<TextView>(R.id.tv_hargajual)
        val tvHargaDiskon = view.findViewById<TextView>(R.id.tv_hargadiskon)
        val tvbrand = view.findViewById<TextView>(R.id.tv_brand)
        val tvDiskon = view.findViewById<TextView>(R.id.tv_diskon)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_diskon, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tvNama.text = data[position].name
        holder.tvbrand.text= data[position].brand
        holder.tvDiskon.text=data[position].diskon + "%"
        holder.tvHargaJual.text = Helper().gantiRupiah(data[position].harga_jual)
        holder.tvHargaDiskon.text = Helper().gantiRupiah(data[position].harga_sdiskon)
//        holder.imgProduk.setImageResource(data[position].image)
        val image = "https://warehouse-mart.com/backend/public/uploads/" + data[position].image
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product2)
                .error(R.drawable.product2)
                .into(holder.imgProduk)

        holder.layout.setOnClickListener {
            val activiti = Intent(activity, DetailProdukDiskonActivity::class.java)
            val str = Gson().toJson(data[position], Produk::class.java)
            activiti.putExtra("extra", str)
            activity.startActivity(activiti)
        }
    }

}