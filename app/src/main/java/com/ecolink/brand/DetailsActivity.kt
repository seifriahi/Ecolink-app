package com.ecolink.brand

import com.ecolink.api.Api
import NewsAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecolink.R
import com.ecolink.models.News
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsActivity : AppCompatActivity(){
    lateinit var recyclerViewProduit: RecyclerView
    lateinit var projetListAdapter: NewsAdapter
    var produitList: MutableList<News> =
        emptyList<News>().toMutableList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_news)
        val id = intent.getStringExtra("brand id")
        val sharedPreferences = getSharedPreferences("FRIPPY", MODE_PRIVATE)
        val addProductButton = findViewById<Button>(R.id.addProjet)





        // brandAdress.text = client


        //recyclerViewProduit = findViewById(R.id.boutiqueproduitList)
        projetListAdapter = NewsAdapter(produitList)
        recyclerViewProduit.adapter = projetListAdapter
        recyclerViewProduit.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        val apiInterface = Api.create()


        addProductButton.setOnClickListener {
            val intent = Intent(applicationContext, Addproduit::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }


    }




}