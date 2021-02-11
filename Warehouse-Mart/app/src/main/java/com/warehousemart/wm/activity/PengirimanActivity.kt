package com.warehousemart.wm.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.warehousemart.wm.R
import com.warehousemart.wm.app.ApiConfigAlamat
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.ResponModel
import com.warehousemart.wm.room.MyDatabase
import com.warehousemart.wm.util.ApiKey
import kotlinx.android.synthetic.main.activity_pengiriman.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengirimanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pengiriman)

        Helper().setToolbar(this,toolbar, "Pengiriman")

        mainButton()
    }

    @SuppressLint("SetTextI18n")
    fun chekAlamat(){
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAlamat().getByStatus(true) != null){
            div_alamat.visibility = View.VISIBLE
            div_kosong.visibility = View.GONE

            val a = myDb.daoAlamat().getByStatus(true)!!
            tv_nama.text = a.name + " ( " + a.type + " )"
            tv_phone.text = a.phone
            tv_alamat.text = a.alamat + ", " + a.kecamatan + " - " + a.kota + ", " + a.kodepos
            btn_tambahAlamat.text = "Ubah Alamat"
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
    }

    private fun getOngkir(){
        val origin = "501"
        val destination = "114"
        val berat = 1000
        val kurir = "jne"

        ApiConfigAlamat.instanceRetrofit.ongkir(ApiKey.key, origin, destination, berat, kurir).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    Log.d("Error","Gagal mendapatkan data Provinsi: " + response.message())

                } else  {
                    Log.d("Error","Gagal mendapatkan data Provinsi: " + response.message())
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onResume() {
        chekAlamat()
        super.onResume()
    }
}
