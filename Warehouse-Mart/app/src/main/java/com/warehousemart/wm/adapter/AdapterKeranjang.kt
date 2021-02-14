package com.warehousemart.wm.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.warehousemart.wm.R
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.Produk
import com.squareup.picasso.Picasso
import com.warehousemart.wm.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class AdapterKeranjang(var activity: Activity, var data: ArrayList<Produk>, var listener: Listeners) : RecyclerView.Adapter<AdapterKeranjang.Holder>() {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama = view.findViewById<TextView>(R.id.tv_nama)
        val tvHarga = view.findViewById<TextView>(R.id.tv_harga)
        //val tvbrand = view.findViewById<TextView>(R.id.tv_brand)
        val imgProduk = view.findViewById<ImageView>(R.id.img_produk)
        val layout = view.findViewById<CardView>(R.id.layout)

        val btnTambah = view.findViewById<ImageView>(R.id.btn_tambah)
        val btnKurang = view.findViewById<ImageView>(R.id.btn_kurang)
        val btnDelete = view.findViewById<ImageView>(R.id.btn_delete)
        val checkBox = view.findViewById<CheckBox>(R.id.checkBox)
        val tvJumlah = view.findViewById<TextView>(R.id.tv_jumlah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        val produk = data[position]
        val hargajual = Integer.valueOf(produk.harga_jual)
        val hargajualsdiskon = Integer.valueOf(produk.harga_sdiskon)
        holder.tvNama.text = produk.name

        if (produk.diskon == "0"){
            holder.tvHarga.text = Helper().gantiRupiah(hargajual*produk.jumlah)
        }else{
            holder.tvHarga.text = Helper().gantiRupiah(hargajualsdiskon*produk.jumlah)
        }


        var jumlah = data[position].jumlah
        holder.tvJumlah.text = jumlah.toString()

        holder.checkBox.isChecked = produk.selected
        holder.checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            produk.selected = isChecked
            update(produk)
        }

        val image = "https://warehouse-mart.com/backend/public/uploads/" + data[position].image
        Picasso.get()
                .load(image)
                .placeholder(R.drawable.product2)
                .error(R.drawable.product2)
                .into(holder.imgProduk)


        holder.btnTambah.setOnClickListener {
            //if (jumlah >= ) return@setOnClickListener
            jumlah++
            produk.jumlah = jumlah
            update(produk)

            holder.tvJumlah.text = jumlah.toString()
            if (produk.diskon == "0"){
                var hargajual = 0
                hargajual = Integer.valueOf(produk.harga_jual)
                holder.tvHarga.text = Helper().gantiRupiah(hargajual*jumlah)
            }else{
                var hargajualsdiskon = 0
                hargajualsdiskon = Integer.valueOf(produk.harga_sdiskon)
                holder.tvHarga.text = Helper().gantiRupiah(hargajualsdiskon*jumlah)
            }

        }
        holder.btnKurang.setOnClickListener {
            if (jumlah <= 1) return@setOnClickListener

            jumlah--
            produk.jumlah = jumlah
            update(produk)

            holder.tvJumlah.text = jumlah.toString()
            if (produk.diskon == "0"){
                var hargajual = 0
                hargajual = Integer.valueOf(produk.harga_jual)
                holder.tvHarga.text = Helper().gantiRupiah(hargajual-jumlah)
            }else{
                var hargajualsdiskon = 0
                hargajualsdiskon = Integer.valueOf(produk.harga_sdiskon)
                holder.tvHarga.text = Helper().gantiRupiah(hargajualsdiskon-jumlah)
            }
        }
        holder.btnDelete.setOnClickListener {
            this.delete(produk)
            listener.onDelete(position)
            this.notifyItemRemoved(position)
            this.notifyDataSetChanged()
            //notifyItemRangeChanged(position, getItemCount() - position);
        }
    }

    interface Listeners{
        fun onUpdate()
        fun onDelete(position: Int)
    }

    private fun update(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    listener.onUpdate()
                })
    }
    private fun delete(data: Produk) {
        val myDb = MyDatabase.getInstance(activity)
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoKeranjang().delete(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {

                })
    }
}