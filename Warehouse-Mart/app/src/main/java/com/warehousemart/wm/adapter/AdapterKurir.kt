package com.warehousemart.wm.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.warehousemart.wm.R
import com.warehousemart.wm.activity.DetailProdukActivity
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.Produk
import com.squareup.picasso.Picasso
import com.warehousemart.wm.model.Alamat
import com.warehousemart.wm.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class AdapterKurir(var data: ArrayList<Alamat>, var listener: Listeners) : RecyclerView.Adapter<AdapterKurir.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
//        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
//        val tvPhone = view.findViewById<TextView>(R.id.tv_phone)
//        val tvAlamat = view.findViewById<TextView>(R.id.tv_alamat)
//        val layout = view.findViewById<CardView>(R.id.layout)
//        val rd = view.findViewById<RadioButton>(R.id.rd_alamat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_kurir, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val a = data[position]

//        holder.rd.isChecked = a.isSelected
//        holder.tvNama.text = a.name + " ( " + a.type + " )"
//        holder.tvPhone.text= a.phone
//        holder.tvAlamat.text= a.alamat + ", " + a.kecamatan + " - " + a.kota + ", " + a.kodepos
//
//
//        holder.rd.setOnClickListener {
//            a.isSelected = true
//            listener.onClicked(a)
//        }
//
//        holder.layout.setOnClickListener {
//            a.isSelected = true
//            listener.onClicked(a)
//        }
    }

    interface Listeners{
        fun onClicked(data:Alamat)
    }

}