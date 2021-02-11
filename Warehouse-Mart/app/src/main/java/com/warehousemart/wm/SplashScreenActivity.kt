package com.warehousemart.wm

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {

    lateinit var topAnim: Animation
    lateinit var bottomAnim: Animation
    lateinit var image1: ImageView
    lateinit var image3: ImageView
    lateinit var label_depan: TextView
    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_intro)

        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        //Hooks
        image1 = findViewById<ImageView>(R.id.image_logo)
        label_depan = findViewById<TextView>(R.id.label_depan)
        image3 = findViewById<ImageView>(R.id.image_logo_keranjang)

        image1.setAnimation(topAnim)
        label_depan.setAnimation(topAnim)
        image3.setAnimation(bottomAnim)

        handler = Handler()
        handler.postDelayed({

            // Delay and Start Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // here we're delaying to startActivity after 3seconds

    }
}