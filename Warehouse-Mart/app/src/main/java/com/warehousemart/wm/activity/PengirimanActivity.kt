package com.warehousemart.wm.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.warehousemart.wm.MainActivity
import com.warehousemart.wm.R
import com.warehousemart.wm.adapter.AdapterKurir
import com.warehousemart.wm.app.ApiConfig
import com.warehousemart.wm.app.ApiConfigAlamat
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.helper.SharedPref
import com.warehousemart.wm.model.Alamat
import com.warehousemart.wm.model.Checkout
import com.warehousemart.wm.model.ResponModel
import com.warehousemart.wm.model.rajaongkir.Costs
import com.warehousemart.wm.model.rajaongkir.ResponOngkir
import com.warehousemart.wm.room.MyDatabase
import com.warehousemart.wm.util.ApiKey
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.activity_pengiriman.btn_tambahAlamat
import kotlinx.android.synthetic.main.activity_pengiriman.div_kosong
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengirimanActivity : AppCompatActivity() {

    lateinit var myDb : MyDatabase
    var totalHarga = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)
        Helper().setToolbar(this,toolbar, "Pengiriman")
        myDb = MyDatabase.getInstance(this)!!

        totalHarga = Integer.valueOf(intent.getStringExtra("extra")!!)

        tv_totalBelanja.text = Helper().gantiRupiah(totalHarga)
        mainButton()
        setSpinner()
    }

    fun setSpinner(){
        val arryString = ArrayList<String>()
        arryString.add("WM")
        arryString.add("JNE")
        arryString.add("POS")
        arryString.add("TIKI")

        val adapter = ArrayAdapter<Any>(this, R.layout.item_spinner, arryString.toTypedArray())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spn_kurir.adapter = adapter

        spn_kurir.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                if (position != 0){
                    getOngkir(spn_kurir.selectedItem.toString())
                } else if(position == 0){
                    //displayOngkirWM()

                    Log.d("Test","Testtttttt: ")

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    @SuppressLint("SetTextI18n")
    fun chekAlamat(){
        if (myDb.daoAlamat().getByStatus(true) != null){
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE
            div_metodePengiriman.visibility = View.VISIBLE

            val a = myDb.daoAlamat().getByStatus(true)!!
            tv_nama.text = a.name + " ( " + a.type + " )"
            tv_phone.text = a.phone
            tv_alamat.text = a.alamat + ", " + a.kecamatan + " - " + a.kota + ", " + a.kodepos
            btn_tambahAlamat.text = "Ubah Alamat"

            getOngkir("WM")

        } else{
            div_alamat.visibility = View.GONE
            div_kosong.visibility = View.VISIBLE
            btn_tambahAlamat.text = "Tambah Alamat"
        }
    }

    private fun mainButton(){
        btn_tambahAlamat.setOnClickListener{
            startActivity(Intent(this, ListAlamatActivity::class.java))
        }

        btn_bayar.setOnClickListener {
            bayar()
        }
    }

    private fun bayar(){
        val user = SharedPref(this).getUser()
        val a = myDb.daoAlamat().getByStatus(true)!!
        val listProduk = myDb.daoKeranjang().getAll() as ArrayList
        var totalItem = 0
        var totalHarga = 0
        val produks = ArrayList<Checkout.Item>()
        for (p in listProduk){
            if (p.selected && p.diskon == "0"){
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga_jual))

                val produk = Checkout.Item()
                produk.id = "" + p.id
                produk.total_item = "" + p.jumlah
                produk.total_harga = "" + (p.jumlah * Integer.valueOf(p.harga_jual))
                produk.catatan = "Catatan Pengiriman"

                produks.add(produk)
            } else {
                totalItem += p.jumlah
                totalHarga += (p.jumlah * Integer.valueOf(p.harga_sdiskon))

                val produk = Checkout.Item()
                produk.id = "" + p.id
                produk.total_item = "" + p.jumlah
                produk.total_harga = "" + (p.jumlah * Integer.valueOf(p.harga_sdiskon))
                produk.catatan = "Catatan Pengiriman"

                produks.add(produk)
            }
        }
        val checkuot = Checkout()
        checkuot.user_id = "" + user!!.id
        checkuot.total_item = "" + totalItem
        checkuot.total_harga = "" + totalHarga
        checkuot.nama = a.name
        checkuot.phone = a.phone
        checkuot.jasa_pengiriman = jasaKirim
        checkuot.ongkir = ongkir
        checkuot.kurir = kurir
        checkuot.total_transfer = "" + (totalHarga+Integer.valueOf(ongkir))
        checkuot.produks = produks

        val json = Gson().toJson(checkuot, Checkout::class.java)
        val intent = Intent(this,PembayaranActivity::class.java)
        intent.putExtra("extra",json)
        startActivity(intent)
    }

    private fun getOngkir(kurir:String){
        val alamat = myDb.daoAlamat().getByStatus(true)
        val origin = "376"
        val destination = alamat!!.idKota.toString()
        val berat = 1000
        //val kurir = "jne"

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir.toLowerCase()).enqueue(object : Callback<ResponOngkir> {
            override fun onFailure(call: Call<ResponOngkir>, t: Throwable) {
                Log.d("Error","Gagal mendapatkan data Ongkir: " + t.message)
            }

            override fun onResponse(call: Call<ResponOngkir>, response: Response<ResponOngkir>) {

                if (response.isSuccessful){

                    Log.d("Success","Berhasil memuat data")
                    val result = response.body()!!.rajaongkir.results
                    if (result.isNotEmpty()){
                        displayOngkir(result[0].code.toUpperCase(), result[0].costs)
                    }

                } else  {
                    Log.d("Error","Gagal mendapatkan data Ongkir: " + response.message())
                }
            }
        })
    }

    var ongkir = ""
    var jasaKirim = ""
    var kurir = ""
    private fun displayOngkir(_kurir:String, arrayList : ArrayList<Costs>) {

        var arrayOngkir = ArrayList<Costs>()
        for (i in arrayList.indices){
            val ongkir = arrayList[i]
            if (i == 0){
                ongkir.isActive = true
            }
            arrayOngkir.add(ongkir)
        }
        setTotal(arrayOngkir[0].cost[0].value)
        ongkir = arrayOngkir[0].cost[0].value
        kurir = _kurir
        jasaKirim = arrayOngkir[0].service

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        var adapter: AdapterKurir? = null
        adapter = AdapterKurir(arrayOngkir, _kurir, object : AdapterKurir.Listeners {
            override fun onClicked(data: Costs, index: Int) {
                val newArrayOngkir = ArrayList<Costs>()
                for (ongkir in arrayOngkir){
                    ongkir.isActive = data.description == ongkir.description
                    newArrayOngkir.add(ongkir)
                }
                arrayOngkir = newArrayOngkir
                adapter!!.notifyDataSetChanged()
                setTotal(data.cost[0].value)

                ongkir = data.cost[0].value
                kurir = _kurir
                jasaKirim = data.service
            }
        })
        rv_metode.adapter = adapter
        rv_metode.layoutManager = layoutManager
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    fun setTotal(ongkir: String){
        tv_ongkir.text = Helper().gantiRupiah(ongkir)
        tv_total.text = Helper().gantiRupiah(Integer.valueOf(ongkir) + totalHarga)
    }

    override fun onResume() {
        chekAlamat()
        super.onResume()
    }
}
