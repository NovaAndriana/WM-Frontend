package com.warehousemart.wm.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "keranjang")
public class Produk implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTb")
    public int idTb;

    public int id;
    public String name;
    public String stok;
    public String satuan;
    public String category;
    public String brand;
    public String supplier;
    public String harga_beli;
    public String harga_jual;
    public String harga_sdiskon;
    public String diskon;
    public String deskripsi;
    public String image;
    public int is_promo = 0;
    public String created_at;
    public String updated_at;
    public int jumlah = 1;
    public boolean selected = true;
}
