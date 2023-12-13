package com.ecolink

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ecolink.Adapter.EventAdapter
import com.ecolink.ViewModel.EventViewModel
import com.ecolink.models.Event
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.Locale
class MainActivity22 : AppCompatActivity(), EventAdapter.OnSeeMoreClickListener {

    private lateinit var eventViewModel: EventViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var textViewEventName: TextView
    private lateinit var textViewEventDate: TextView
    private lateinit var textViewEventLocation: TextView
    private lateinit var buttonInterested : ImageButton


    private val ADD_EVENT_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main22)
        val picasso = Picasso.Builder(this)
            .loggingEnabled(true)
            .build()


        Picasso.setSingletonInstance(picasso)
        recyclerView = findViewById(R.id.recyclerView)
        eventAdapter = EventAdapter(emptyList())
        recyclerView.adapter = eventAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter.setOnSeeMoreClickListener(this)

        eventViewModel = ViewModelProvider(this).get(EventViewModel::class.java)
        eventViewModel.events.observe(this, Observer { events ->
            eventAdapter.updateEvents(events)
        })

        eventViewModel.fetchEvents()

        val buttonInToolbar: ImageButton = findViewById(R.id.buttonInToolbar)
        buttonInToolbar.setOnClickListener {
            val intent = Intent(this, Addevent::class.java)
            startActivityForResult(intent, ADD_EVENT_REQUEST_CODE)
        }
        val swipeRefreshLayout: SwipeRefreshLayout = this.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            refreshEvents()
            swipeRefreshLayout.isRefreshing = false // Arrête l'animation de rafraîchissement
        }
        val calendarButton: ImageButton = findViewById(R.id.calendarButtonInToolbar)
        calendarButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

//test

    }


    private fun refreshEvents() {
        eventViewModel.fetchEvents()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_EVENT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Rafraîchir la liste des événements après la création
            eventViewModel.fetchEvents()
        }
    }


    override fun onSeeMoreClick(event: Event) {
        val intent = Intent(this, EventDetailActivity::class.java)
        intent.putExtra("eventName", event.eventName) // Envoyer le nom de l'événement à afficher
        intent.putExtra("eventLocation", event.eventLocation) // Envoyer l'emplacement de l'événement à afficher
        intent.putExtra("eventDate", event.eventDate)
        intent.putExtra("eventDesc", event.eventDescription)
        intent.putExtra("isFavorites",event.isFavorites)
        intent.putExtra("eventimageURL", event.imageURL) // Ajouter l'URL de l'image à l'intent
        intent.putExtra("eventID", event._id)
        intent.putExtra("isFavorites",event.isFavorites)// Ajout de l'ID de l'événement dans l'intent
        val formattedDate = formatDate(event.eventDate)
        intent.putExtra("eventDate", formattedDate)
        startActivity(intent)
    }
    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }


}
