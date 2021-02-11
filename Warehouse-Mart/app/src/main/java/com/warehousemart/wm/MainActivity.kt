package com.warehousemart.wm

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.warehousemart.wm.activity.MasukActivity
import com.warehousemart.wm.fragment.AkunFragment
import com.warehousemart.wm.fragment.HomeFragment
import com.warehousemart.wm.fragment.KeranjangFragment
import com.warehousemart.wm.fragment.TransaksiFragment
import com.warehousemart.wm.helper.SharedPref
import kotlinx.android.synthetic.main.alert_dialog.*

class MainActivity : AppCompatActivity() {

    private val fragmentHome: Fragment = HomeFragment()
    private val fragmentKeranjang: Fragment = KeranjangFragment()
    private val fragmentTransaksi: Fragment = TransaksiFragment()
    private var fragmentAkun: Fragment = AkunFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private var statusLogin = false


    private lateinit var s:SharedPref
    private var dariDetail: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        s = SharedPref(this)

        setUpBottomNav()
        checkKoneksi()

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:keranjang"))
    }

    val mMessage: BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail = true
        }
    }

    fun setUpBottomNav(){
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentTransaksi).hide(fragmentTransaksi).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when(item.itemId) {
                R.id.navigation_home ->{
                    callFargment(0, fragmentHome)
                }
                R.id.navigation_transaksi ->{
                    callFargment(1, fragmentTransaksi)
                }
                R.id.navigation_keranjang ->{
                    callFargment(2, fragmentKeranjang)
                }
                R.id.navigation_akun ->{
                    if (s.getStatusLogin()){
                        callFargment(3, fragmentAkun)
                    } else {
                        startActivity(Intent(this, MasukActivity::class.java))
                    }

                }
            }

            false
        }
    }

    fun callFargment(int: Int, fragment: Fragment){
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }

    private fun checkKoneksi(){

        val manager = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if (null != networkInfo){
//            if (networkInfo.type == ConnectivityManager.TYPE_WIFI){
//                Toast.makeText(this,"Akses internet anda menggunakan Wifi",Toast.LENGTH_SHORT).show()
//            }
//            else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE){
//                Toast.makeText(this,"Akses internet anda menggunakan Jaringan Seluler",Toast.LENGTH_SHORT).show()
//            }
        }
        else{
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.alert_dialog)

            dialog.setCanceledOnTouchOutside(false)

            dialog.window!!.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.btn_coba_lagi.setOnClickListener{
                dialog.show()
            }
            dialog.btn_close.setOnClickListener{
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    override fun onResume() {
        if (dariDetail) {
            dariDetail= false
            callFargment(2, fragmentKeranjang)
        }
        super.onResume()
    }
}
