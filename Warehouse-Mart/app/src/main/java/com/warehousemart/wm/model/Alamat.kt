package com.warehousemart.wm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alamat")
class Alamat {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    var idTb = 0

    var id = 0
    var name = ""
    var phone = ""
    var type = ""
    var alamat = ""

    var idProvinsi = 0
    var idKota = 0
    var idKecamatan = 0
    var provinsi = ""
    var kota = ""
    var kecamatan = ""
    var kodepos = ""
    var isSelected = false
}