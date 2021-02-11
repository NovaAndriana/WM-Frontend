package com.warehousemart.wm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.warehousemart.wm.R
import com.warehousemart.wm.app.ApiConfig
import com.warehousemart.wm.app.ApiConfigAlamat
import com.warehousemart.wm.helper.Helper
import com.warehousemart.wm.model.Alamat
import com.warehousemart.wm.model.ModelAlamat
import com.warehousemart.wm.model.ResponModel
import com.warehousemart.wm.room.MyDatabase
import com.warehousemart.wm.util.ApiKey
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_tambah_alamat.*
import kotlinx.android.synthetic.main.activity_tambah_alamat.pb
import kotlinx.android.synthetic.main.toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class TambahAlamatActivity : AppCompatActivity() {

    var provinsi = ModelAlamat.Provinsi()
    var kota = ModelAlamat.Provinsi()
    var kecamatan = ModelAlamat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah_alamat)

        Helper().setToolbar(this,toolbar, "Tambah Alamat")

        mainButton()
        getProvinsi()

    }

    private fun mainButton(){
        btn_simpan.setOnClickListener {
            simpan()
        }
    }

    private fun simpan(){
        when {
            edt_nama.text.isEmpty() -> {
                edt_nama.error = "Kolom Nama tidak boleh kosong"
                edt_nama.requestFocus()
                return
            }
            edt_alamat.text.isEmpty() -> {
                edt_alamat.error = "Kolom Alamat tidak boleh kosong"
                edt_alamat.requestFocus()
                return
            }
            edt_phone.text.isEmpty() -> {
                edt_phone.error = "Kolom Nomor Telepon tidak boleh kosong"
                edt_phone.requestFocus()
                return
            }
            edt_type.text.isEmpty() -> {
                edt_type.error = "Kolom Tempat Tinggal tidak boleh kosong"
                edt_type.requestFocus()
                return
            }
            edt_kodePos.text.isEmpty() -> {
                edt_kodePos.error = "Kolom Kode POS tidak boleh kosong"
                edt_kodePos.requestFocus()
                return
            }
        }

        if (provinsi.province_id == "0"){
            toast("Silahkan pilih Provinsi")
            return
        }
        if (kota.city_id == "0"){
            toast("Silahkan pilih Kota")
            return
        }
//        if (kecamatan.id == 0){
//            toast("Silahkan pilih Kecamatan")
//            return
//        }

        val alamat = Alamat()
        alamat.name = edt_nama.text.toString()
        alamat.type = edt_type.text.toString()
        alamat.phone = edt_phone.text.toString()
        alamat.alamat = edt_alamat.text.toString()
        alamat.kodepos = edt_kodePos.text.toString()

        alamat.idProvinsi = Integer.valueOf(provinsi.province_id)
        alamat.provinsi = provinsi.province
        alamat.idKota = Integer.valueOf(kota.city_id)
        alamat.kota = kota.city_name
//        alamat.idKecamatan = kecamatan.id
//        alamat.kecamatan = kecamatan.nama

        insert(alamat)
    }

    fun toast(string: String){
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
    }

    private fun getProvinsi(){
        ApiConfigAlamat.instanceRetrofit.getProvinsi(ApiKey.key).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    div_provinsi.visibility = View.VISIBLE

                    val res = response.body()!!
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Provinsi")
                    val listProvinsi = res.rajaongkir.results
                    for (prov in listProvinsi){
                        arryString.add(prov.province)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_provinsi.adapter = adapter

                    spn_provinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                provinsi = listProvinsi[position - 1]
                                val idProv = provinsi.province_id
                                getKota(idProv)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }

                } else  {
                    Log.d("Error","Gagal mendapatkan data Provinsi: " + response.message())
                }
            }
        })
    }

    private fun getKota(id: String){
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKota(ApiKey.key, id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kota.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.rajaongkir.results
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kota")
                    for (kota in listArray){
                        arryString.add(kota.type + " " + kota.city_name)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kota.adapter = adapter

                    spn_kota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                kota = listArray[position - 1]
                                val kodePos = kota.postal_code
                                edt_kodePos.setText(kodePos)
                                //getKecamatan(idKota)
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }

                } else  {
                    Log.d("Error","Gagal mendapatkan data Provinsi: " + response.message())
                }
            }
        })
    }

    private fun getKecamatan(id: Int){
        pb.visibility = View.VISIBLE
        ApiConfigAlamat.instanceRetrofit.getKecamatan(id).enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {

                if (response.isSuccessful){

                    pb.visibility = View.GONE
                    div_kecamatan.visibility = View.VISIBLE

                    val res = response.body()!!
                    val listArray = res.kecamatan
                    val arryString = ArrayList<String>()
                    arryString.add("Pilih Kecamatan")
                    for (kec in listArray){
                        arryString.add(kec.nama)
                    }

                    val adapter = ArrayAdapter<Any>(this@TambahAlamatActivity, R.layout.item_spinner, arryString.toTypedArray())
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spn_kecamatan.adapter = adapter
                    spn_kecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            if (position != 0){
                                kecamatan = listArray[position - 1]
                            }
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                } else  {
                    Log.d("Error","Gagal mendapatkan data Provinsi: " + response.message())
                }
            }
        })
    }

    private fun insert(data: Alamat) {
        val myDb = MyDatabase.getInstance(this)!!
        if (myDb.daoAlamat().getByStatus(true) == null){
            data.isSelected = true
        }
        CompositeDisposable().add(Observable.fromCallable { myDb!!.daoAlamat().insert(data) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    toast("Insert Data Success")
                    onBackPressed()
                })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
