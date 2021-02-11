package com.warehousemart.wm.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.warehousemart.wm.R
import com.warehousemart.wm.adapter.AdapterMenu
import com.warehousemart.wm.adapter.AdapterProduk
import com.warehousemart.wm.adapter.AdapterProdukDiskon
import com.warehousemart.wm.adapter.AdapterSlider
import com.warehousemart.wm.app.ApiConfig
import com.warehousemart.wm.model.Menu
import com.warehousemart.wm.model.Produk
import com.warehousemart.wm.model.ResponModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    lateinit var vpSlider: ViewPager
    lateinit var rvMenu: RecyclerView
    lateinit var rvProduk: RecyclerView
    //lateinit var rvProdukTerlasir: RecyclerView
    //lateinit var rvElektronik: RecyclerView
    lateinit var rvDiskon: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)
        init(view)
        return view
    }

    fun displayProduk() {
        val manager = GridLayoutManager(context, 2)

        val arrSlider = ArrayList<Int>()
        arrSlider.add(R.drawable.wmslider1)
        arrSlider.add(R.drawable.wmslider2)
        arrSlider.add(R.drawable.wmslider3)

        val adapterSlider = AdapterSlider(arrSlider, activity)
        vpSlider.adapter = adapterSlider

        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager1 = LinearLayoutManager(activity)
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL

        val layoutManager2 = LinearLayoutManager(activity)
        layoutManager2.orientation = LinearLayoutManager.VERTICAL

//        val layoutManager3 = LinearLayoutManager(activity)
//        layoutManager3.orientation = LinearLayoutManager.VERTICAL

        rvMenu.adapter = AdapterMenu(requireActivity(),listMenu)
        rvMenu.layoutManager = layoutManager

        rvDiskon.adapter = AdapterProdukDiskon(requireActivity(), listProdukdiskon)
        rvDiskon.layoutManager = layoutManager1

        rvProduk.adapter = AdapterProduk(requireActivity(), listProduk)
        rvProduk.layoutManager = layoutManager2
        rvProduk.setLayoutManager(manager)

//        rvProdukTerlasir.adapter = AdapterProduk(requireActivity(), listProduk)
//        rvProdukTerlasir.layoutManager = layoutManager3
    }

    private var listProduk: ArrayList<Produk> = ArrayList()
    fun getProduk() {
        ApiConfig.instanceRetrofit.getProduk().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    listProduk = res.produk
                    displayProduk()
                }
            }
        })
    }
    private var listProdukdiskon: ArrayList<Produk> = ArrayList()
    fun getProdukDiskon() {
        ApiConfig.instanceRetrofit.getProdukDiskon().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    listProdukdiskon = res.produk
                    displayProduk()
                }
            }
        })
    }

    private var listMenu: ArrayList<Menu> = ArrayList()
    fun getMenu() {
        ApiConfig.instanceRetrofit.getMenu().enqueue(object : Callback<ResponModel> {
            override fun onFailure(call: Call<ResponModel>, t: Throwable) {

            }

            override fun onResponse(call: Call<ResponModel>, response: Response<ResponModel>) {
                val res = response.body()!!
                if (res.success == 1) {
                    listMenu = res.datamenu
                    displayProduk()
                }
            }
        })
    }

    fun init(view: View) {
        vpSlider = view.findViewById(R.id.vp_slider)
        rvProduk = view.findViewById(R.id.rv_produk)
        rvMenu = view.findViewById(R.id.rv_menu)
        //rvProdukTerlasir = view.findViewById(R.id.rv_produkTerlasir)
        //rvElektronik = view.findViewById(R.id.rv_elektronik)
        rvDiskon = view.findViewById(R.id.rv_diskon)
    }

    override fun onResume() {
        getProduk()
        getProdukDiskon()
        getMenu()
        displayProduk()
        super.onResume()
    }
//    val arrProduk: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val p1 = Produk()
//        p1.nama = "HP 14_bs749tu"
//        p1.harga = "Rp.5.500.000"
//        p1.gambar = R.drawable.hp_14_bs749tu
//
//        val p2 = Produk()
//        p2.nama = "Hp Envy_13_aq0019tx"
//        p2.harga = "Rp.15.980.000"
//        p2.gambar = R.drawable.hp_envy_13_aq0019tx
//
//        val p3 = Produk()
//        p3.nama = "HP pavilion_13_an0006na"
//        p3.harga = "Rp.14.200.000"
//        p3.gambar = R.drawable.hp_pavilion_13_an0006na
//
//        val p4 = Produk()
//        p4.name = "Hp Envy_13_aq0019tx"
//        p4.harga = "Rp.15.980.000"
//        p4.gambar = R.drawable.hp_pavilion_14_ce1507sa
//
//        arr.add(p1)
//        arr.add(p2)
//        arr.add(p3)
//        arr.add(p4)
//
//        return arr
//    }
//
//    val arrElektronik: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val p1 = Produk()
//        p1.nama = "HP 14_bs749tu"
//        p1.harga = "Rp.5.500.000"
//        p1.gambar = R.drawable.hp_notebook_14_bs710tu
//
//        val p2 = Produk()
//        p2.nama = "Hp Envy_13_aq0019tx"
//        p2.harga = "Rp.15.980.000"
//        p2.gambar = R.drawable.hp_pavilion_14_ce1507sa
//
//        val p3 = Produk()
//        p3.nama = "HP pavilion_13_an0006na"
//        p3.harga = "Rp.14.200.000"
//        p3.gambar = R.drawable.hp_pavilion_13_an0006na
//
//        val p4 = Produk()
//        p4.nama = "Hp Envy_13_aq0019tx"
//        p4.harga = "Rp.15.980.000"
//        p4.gambar = R.drawable.hp_pavilion_14_ce1507sa
//
//        arr.add(p2)
//        arr.add(p3)
//        arr.add(p1)
//        arr.add(p4)
//
//        return arr
//    }
//
//    val arrProdukTerlaris: ArrayList<Produk>get(){
//        val arr = ArrayList<Produk>()
//        val p1 = Produk()
//        p1.nama = "HP 14_bs749tu"
//        p1.harga = "Rp.5.500.000"
//        p1.gambar = R.drawable.hp_14_bs749tu
//
//        val p2 = Produk()
//        p2.nama = "Hp Envy_13_aq0019tx"
//        p2.harga = "Rp.15.980.000"
//        p2.gambar = R.drawable.hp_pavilion_15_cx0056wm
//
//        val p3 = Produk()
//        p3.nama = "HP pavilion_13_an0006na"
//        p3.harga = "Rp.14.200.000"
//        p3.gambar = R.drawable.hp_pavilion_14_ce1507sa
//
//        val p4 = Produk()
//        p4.nama = "Hp Envy_13_aq0019tx"
//        p4.harga = "Rp.15.980.000"
//        p4.gambar = R.drawable.hp_pavilion_14_ce1507sa
//
//        arr.add(p4)
//        arr.add(p1)
//        arr.add(p3)
//        arr.add(p2)
//
//        return arr
//    }
}
