package com.ecolink.brand

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.ecolink.R



class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val imagelink = intent.getStringExtra("imagelink")
        val ti = intent.getStringExtra("producttitle")
        val desc = intent.getStringExtra("productDescription")


        val image = findViewById<ImageView>(R.id.ivDetails)
        val productDescription = findViewById<TextView>(R.id.tvDetailsProductDescription)
        val producttitle = findViewById<TextView>(R.id.tvDetailsProductName)


        producttitle.text = ti
        productDescription.text = desc
        image.load("http://192.168.1.19:5000/images/$imagelink")
    }



}