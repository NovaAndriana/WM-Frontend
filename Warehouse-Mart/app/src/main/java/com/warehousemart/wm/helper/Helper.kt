package com.warehousemart.wm.helper

import android.app.Activity
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.warehousemart.wm.activity.PengirimanActivity
import kotlinx.android.synthetic.main.toolbar.*
import java.text.NumberFormat
import java.util.*

class Helper {
    fun gantiRupiah(string: String): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(Integer.valueOf(string))
    }
    fun gantiRupiah(value: Int): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }
    fun gantiRupiah(value: Boolean): String {
        return NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(value)
    }
    fun setToolbar(activity: Activity, toolbar: Toolbar, title: String){
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        //supportActionBar!!.title = produk.name
        activity.supportActionBar!!.title = title
        activity.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}