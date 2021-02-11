package com.warehousemart.wm.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.warehousemart.wm.R
import com.warehousemart.wm.activity.PengirimanActivity
import com.warehousemart.wm.adapter.AdapterKeranjang
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.Produk
import com.warehousemart.wm.room.MyDatabase

/**
 * A simple [Fragment] subclass.
 */
class KeranjangFragment : Fragment() {

    lateinit var myDb : MyDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_keranjang, container, false)
        init(view)
        myDb = MyDatabase.getInstance(requireActivity())!!
        mainButton()
        return view
    }

    lateinit var adapter : AdapterKeranjang
    var listProduk = ArrayList<Produk>()
    private fun displayProduk(){

        listProduk = myDb.daoKeranjang().getAll() as ArrayList
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        adapter = AdapterKeranjang(requireActivity(), listProduk,object : AdapterKeranjang.Listeners{
            override fun onUpdate() {
                hitungTotal()
            }

            override fun onDelete(position: Int) {
                listProduk.removeAt(position)
                adapter.notifyDataSetChanged()
                hitungTotal()
            }

        })
        rvProduk.adapter = adapter
        rvProduk.layoutManager = layoutManager
    }

    private fun mainButton() {
        btnDelete.setOnClickListener {

        }

        btnBayar.setOnClickListener {
            startActivity(Intent(requireActivity(), PengirimanActivity::class.java))
        }

        cbAll.setOnClickListener {
            for (i in listProduk.indices){
                val produk = listProduk[i]
                produk.selected = cbAll.isChecked

                listProduk[i] = produk
            }
            adapter.notifyDataSetChanged()
        }
    }

    fun hitungTotal(){
        val listProduk = myDb.daoKeranjang().getAll() as ArrayList
        var hargajual = 0
        var totalHarga = 0
        var isSelectedAll = true
        for (produk in listProduk){

            if (produk.diskon == "0" && produk.selected){
                hargajual = Integer.valueOf(produk.harga_jual)
                totalHarga+= (hargajual*produk.jumlah)
            }else if (produk.diskon != "0" && produk.selected){
                hargajual = Integer.valueOf(produk.harga_sdiskon)
                totalHarga+= (hargajual*produk.jumlah)
            }else {
                isSelectedAll = false
            }

        }

        cbAll.isChecked = isSelectedAll
        tvTotal.text = Helper().gantiRupiah(totalHarga)
    }


    lateinit var btnDelete :ImageButton
    lateinit var rvProduk :RecyclerView
    lateinit var tvTotal :TextView
    lateinit var btnBayar :TextView
    lateinit var cbAll :CheckBox
    private fun init(view: View) {
        btnDelete = view.findViewById(R.id.btn_delete)
        rvProduk = view.findViewById(R.id.rv_produk)
        tvTotal = view.findViewById(R.id.tv_total)
        btnBayar = view.findViewById(R.id.btn_bayar)
        cbAll = view.findViewById(R.id.cb_all)
    }

    override fun onResume() {
        displayProduk()
        hitungTotal()
        super.onResume()
    }

}
