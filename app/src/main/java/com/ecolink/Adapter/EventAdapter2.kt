package com.ecolink.Adapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecolink.R
import com.ecolink.modéle.Event
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.net.URLEncoder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdapter2(private var events: List<Event>) : RecyclerView.Adapter<EventAdapter2.EventViewHolder>() {

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventNameTextView: TextView = itemView.findViewById(R.id.textViewEventName)
        private val imageViewEvent: ImageView = itemView.findViewById(R.id.imageViewEvent)
        private val eventLocationTextView: TextView = itemView.findViewById(R.id.textViewEventLocation)
        private val eventDateTextView: TextView = itemView.findViewById(R.id.textViewEventDate)
        fun bind(event: Event) {
            eventNameTextView.text = event.eventName
            val formattedDate = formatDate(event.eventDate)
            eventDateTextView.text = formattedDate
            eventLocationTextView.text = event.eventLocation


            val baseUrl = "https://ecolink.onrender.com/" // Partie statique de l'URL
            val imageUrl = "$baseUrl${event.imageURL}"
            Log.d("Picasso", "URL de l'image : $imageUrl")
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.baseline_cached_24)
                .error(R.drawable.ic_gallery_background)
                .into(imageViewEvent, object : Callback {
                    override fun onSuccess() {
                    }
                    override fun onError(e: Exception?) {
                        Log.e("Picasso", "Erreur de chargement d'image: ${e?.localizedMessage}")
                    }
                })




        }

        fun formatDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            return outputFormat.format(date)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.event_item2, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun updateEvents(newEvents: List<Event>) {
        events = newEvents
        notifyDataSetChanged()
    }
}