package com.warehousemart.wm.model

class Checkout {
    lateinit var user_id:String
    lateinit var total_item:String
    lateinit var total_harga:String
    lateinit var nama:String
    lateinit var phone:String
    lateinit var kurir:String
    lateinit var jasa_pengiriman:String
    lateinit var ongkir:String
    lateinit var total_transfer:String
    lateinit var bank:String
    var produks = ArrayList<Item>()

    class Item {
        lateinit var id:String
        lateinit var total_item:String
        lateinit var total_harga:String
        lateinit var catatan:String
    }
}