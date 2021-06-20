package com.warehousemart.wm.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.warehousemart.wm.R
import com.warehousemart.wm.adapter.AdapterBank
import com.warehousemart.wm.app.ApiConfig
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.Bank
import com.warehousemart.wm.model.Checkout
import com.warehousemart.wm.model.ResponModel
import com.warehousemart.wm.model.Transaksi
import kotlinx.android.synthetic.main.activity_pembayaran.*
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PembayaranActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        Helper().setToolbar(this, toolbar, "Metode Pembayaran")

        displayBank()
    }

    fun displayBank(){
        val arrBank = ArrayList<Bank>()
        arrBank.add(Bank("BCA","03515543","Cv. Alba Multi Tech", R.drawable.logo_bca))
        arrBank.add(Bank("BRI","09921555265","Cv. Alba Multi Tech", R.drawable.logo_bri))
        arrBank.add(Bank("Mandiri","1720011256","Cv. Alba Multi Tech", R.drawable.logo_mandiri))

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv_databank.layoutManager = layoutManager
        rv_databank.adapter = AdapterBank(arrBank, object :AdapterBank.Listeners{
            override fun onClicked(data: Bank, index: Int) {
                bayar(data)
            }
        })
    }

    fun bayar(bank: Bank){
        val json = intent.getStringExtra("extra")!!.toString()
        val checkout = Gson().fromJson(json, Checkout::class.java)
        checkout.bank = bank.nama

        ApiConfig.instanceRetrofit.checkout(checkout).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {
//                Toast.makeText(this@PengirimanActivity, "Success: "+t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val respon = response.body()!!
                if (respon.success == 1){

                    Toast.makeText(this@PembayaranActivity, "Error:"+respon.message, Toast.LENGTH_SHORT).show()

                } else{
                    val jsBank = Gson().toJson(bank, Bank::class.java)
                    val jsTransaksi = Gson().toJson(respon.transaksi, Transaksi::class.java)
                    val jsCheckout = Gson().toJson(checkout, Checkout::class.java)

                    val intent = Intent(this@PembayaranActivity, SuccessActivity::class.java)
                    intent.putExtra("bank", jsBank)
                    intent.putExtra("transaksi", jsTransaksi)
                    intent.putExtra("checkout", jsCheckout)
                    startActivity(intent)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
