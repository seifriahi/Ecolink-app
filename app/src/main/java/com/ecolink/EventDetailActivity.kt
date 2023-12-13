package com.ecolink

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ecolink.models.Event
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.net.HttpURLConnection
import java.net.URL
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Collections.list
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EventDetailActivity : AppCompatActivity() {
    private lateinit var event: Event // Ajouter cette variable pour stocker l'événement actuel
    private val CHANNEL_ID = "channel_id" // Identifiant du canal de notification

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_cancel_24) // Définissez ici votre icône de retour
            title = "Détail de évenement"
// Titre de la Toolbar
        }
        createNotificationChannel()
        val buttonInterested: ImageButton = findViewById(R.id.buttonInterested)
        val textViewEventName: TextView = findViewById(R.id.textViewEventName)
        val textViewEventLocation: TextView = findViewById(R.id.textViewEventLocation)
        val textViewEventDate: TextView = findViewById(R.id.textViewEventDate)
        val textViewEventDesc: TextView = findViewById(R.id.textViewEventDesc)
        val imageViewEvent: ImageView = findViewById(R.id.imageViewEvent)
        val eventName = intent.getStringExtra("eventName")
        val eventLocation = intent.getStringExtra("eventLocation")

        val eventDesc = intent.getStringExtra("eventDesc")
        val event_ID = intent.getStringExtra("eventID")
        var isFavorites = intent.getBooleanExtra("isFavorites", false)


        val eventDate: String? = intent.getStringExtra("eventDate")
        val formattedDate = eventDate?.let { formatDate(it) } ?: "Date indisponible"
        textViewEventDate.text = formattedDate

        textViewEventName.text = eventName
        textViewEventLocation.text = eventLocation
        textViewEventDate.text = eventDate
        textViewEventDesc.text = eventDesc
        val eventImageURL = intent.getStringExtra("eventimageURL")
        Log.d("EventDetailActivity", "Event ID: ${event_ID}")

        val baseUrl = "https://ecolink.onrender.com/"
        val imageUrl = "$baseUrl$eventImageURL"
        Log.d("Picasso", "URL de l'image : $imageUrl")
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.baseline_cached_24)
            .error(R.drawable.baseline_cancel_24)
            .into(imageViewEvent, object : Callback {
                override fun onSuccess() {

                }

                override fun onError(e: Exception?) {

                    Log.e("Picasso", "Error loading image: ${e?.localizedMessage}")
                }
            })

        Log.d("Eventfavorie", "favorie: ${isFavorites}")

        if (isFavorites == true) {
            buttonInterested.isEnabled = true
            buttonInterested.isClickable = true
            buttonInterested.setImageResource(R.drawable.baseline_star_24) // icône dislike
            buttonInterested.imageAlpha = 128 // Rendre l'image du bouton partiellement transparente (floue)
        } else {
            buttonInterested.isEnabled = true
            buttonInterested.isClickable = true
            buttonInterested.setImageResource(R.drawable.baseline_star_24) // icône like
            buttonInterested.imageAlpha = 255 // Remettre l'opacité de l'image du bouton à la normale
        }



        buttonInterested.setOnClickListener {
            val eventID = event_ID
            val task = ToggleFavoriteTask()
            task.execute(eventID)

            // Inverser la valeur de isFavorites après le clic
            val updatedIsFavorites = !isFavorites

            if (updatedIsFavorites) {
                buttonInterested.setImageResource(R.drawable.baseline_star_24) // Changer vers l'icône "dislike"
                buttonInterested.imageAlpha = 128 // Rendre l'image partiellement transparente (floue)
            } else {
                buttonInterested.setImageResource(R.drawable.baseline_star_24) // Changer vers l'icône "like"
                buttonInterested.imageAlpha = 255 // Remettre l'opacité de l'image à la normale
            }


            isFavorites = updatedIsFavorites

            if (checkNotificationPermission()) {
                sendNotification()

            } else {
                requestNotificationPermission()
            }
        }


    }
    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date)
        } catch (e: ParseException) {
            Log.e("DateParsing", "Error parsing date: ${e.localizedMessage}")
            "Date indisponible"
        }
    }


    private fun checkNotificationPermission(): Boolean {
        return NotificationManagerCompat.from(this).areNotificationsEnabled()
    }

    private fun requestNotificationPermission() {

    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Channel Name"
            val descriptionText = "Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
                enableLights(true)
                lightColor = Color.RED
            }
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_cancel_24)
            .setContentTitle("ecoLink")
            .setContentText("vous avez un evenement !!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build()) // L'ID de notification doit être unique pour chaque notification
        }
    }

    class ToggleFavoriteTask : AsyncTask<String, Void, Int>() {
        override fun doInBackground(vararg params: String?): Int {
            val eventID = params[0] ?: ""
            val url = URL("https://ecolink.onrender.com/api/evenement/$eventID/togglefavorites")
            val connection = url.openConnection() as HttpURLConnection

            connection.requestMethod = "PATCH"
            connection.connect()

            return connection.responseCode
        }

        override fun onPostExecute(result: Int) {
            super.onPostExecute(result)
            if (result == HttpURLConnection.HTTP_OK) {
                // Gérer ici les actions à exécuter suite à la mise à jour du statut de favori
            } else {
                // Gérer les erreurs de réponse ici
                Log.e("HTTP_ERROR", "Erreur HTTP : $result")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
