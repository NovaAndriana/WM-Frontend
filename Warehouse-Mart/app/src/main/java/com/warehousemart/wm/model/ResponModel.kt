package com.warehousemart.wm.model

class ResponModel {
    var success = 0
    lateinit var message: String
    var user = User()
    var produk: ArrayList<Produk> = ArrayList()
    var datamenu: ArrayList<Menu> = ArrayList()
    var databrand: ArrayList<Brand> = ArrayList()

    var rajaongkir = ModelAlamat()

    var provinsi: ArrayList<ModelAlamat> = ArrayList()
    var kota_kabupaten: ArrayList<ModelAlamat> = ArrayList()
    var kecamatan: ArrayList<ModelAlamat> = ArrayList()
}