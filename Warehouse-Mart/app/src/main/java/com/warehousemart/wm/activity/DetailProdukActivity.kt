package com.warehousemart.wm.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.warehousemart.wm.R
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.Produk
import com.warehousemart.wm.room.MyDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.activity_dialog_sukses.*
import kotlinx.android.synthetic.main.alert_dialog.*
import kotlinx.android.synthetic.main.toolbar.toolbar
import kotlinx.android.synthetic.main.toolbar_custom.*
import kotlinx.android.synthetic.main.fragment_keranjang.*

class DetailProdukActivity : AppCompatActivity() {

    lateinit var myDb: MyDatabase
    lateinit var produk: Produk


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        myDb = MyDatabase.getInstance(this)!! // call database

        getInfo()
        mainButton()
        checkkeranjang()
    }

    private fun mainButton() {
        btn_keranjang.setOnClickListener {
            val data = myDb.daoKeranjang().getProduk(produk.id)

            if (data == null){
                insert()
                openDialogSukses()
                //div_footer.VISIBILITY = View.VISIBLE
            } else {
                data.jumlah = data.jumlah + 1
                update(data)
                openDialogSukses()
            }


        }

        btn_favorit.setOnClickListener {
            val listData = myDb.daoKeranjang().getAll() // get All data
            for (note: Produk in listData) {
                println("-----------------------")
                println(note.name)
                println(note.harga_jual)
            }
        }
        btn_tokeranjang.setOnClickListener{
            val intent = Intent("event:keranjang")
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            onBackPressed()
        }
    }

    private fun openDialogSukses() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_dialog_sukses)

        dialog.setCanceledOnTouchOutside(false)

        dialog.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.btn_ok.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun insert() {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().insert(produk) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkkeranjang()
                    Log.d("respons", "data inserted")
                })
    }

    private fun update(data: Produk) {
        CompositeDisposable().add(Observable.fromCallable { myDb.daoKeranjang().update(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    checkkeranjang()
                    Log.d("respons", "data inserted")
                })
    }

    private fun checkkeranjang(){
        val datakeranjang = myDb.daoKeranjang().getAll()

        if(datakeranjang.isNotEmpty()){
            div_angka.visibility = View.VISIBLE
            tv_angka.text = datakeranjang.size.toString()
        } else {
            div_angka.visibility = View.GONE
        }
    }
    private fun getInfo() {
        val data = intent.getStringExtra("extra")
        produk = Gson().fromJson<Produk>(data, Produk::class.java)

        // set Value
        tv_nama.text = produk.name
        tv_harga.text = Helper().gantiRupiah(produk.harga_jual)
        tv_brand_detail.text = produk.brand
        tv_satuan.text = produk.satuan
        tv_stok.text = produk.stok
        tv_deskripsi.text = produk.deskripsi

        val img = "https://warehouse-mart.com/backend/public/uploads/" + produk.image
        Picasso.get()
                .load(img)
                .placeholder(R.drawable.product2)
                .error(R.drawable.product2)
                .resize(400, 400)
                .into(image)

        // setToolbar
        Helper().setToolbar(this,toolbar, "Produk Detail")
        //supportActionBar!!.title = produk.name

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
