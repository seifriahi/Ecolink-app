package com.ecolink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ecolink.Adapter.EventAdapter2
import com.ecolink.ViewModel.EventViewModel
import com.ecolink.models.Event
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
class MainActivity2 : AppCompatActivity(){

    private lateinit var eventViewModel: EventViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter2
    private lateinit var textViewEventName: TextView
    private lateinit var textViewEventDate: TextView
    private lateinit var textViewEventLocation: TextView

    private val ADD_EVENT_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val picasso = Picasso.Builder(this)
            .loggingEnabled(true)
            .build()

        recyclerView = findViewById(R.id.recyclerView)
        eventAdapter = EventAdapter2(emptyList())
        recyclerView.adapter = eventAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        eventViewModel.events.observe(this, Observer { events ->
            eventAdapter.updateEvents(events)
        })

        eventViewModel.fetchEvents2()


        val swipeRefreshLayout: SwipeRefreshLayout = this.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            // Rafraîchissement des événements
            refreshEvents()
            swipeRefreshLayout.isRefreshing = false // Arrête l'animation de rafraîchissement
        }

    }

    private fun refreshEvents() {
        eventViewModel.fetchEvents2()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Rafraîchir la liste
            // des événements après la création
            eventViewModel.fetchEvents2()
        }
    }



}
