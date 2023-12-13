package com.ecolink.ViewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecolink.R
import com.ecolink.modéle.Event
import com.squareup.picasso.Picasso

class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val eventNameTextView: TextView = itemView.findViewById(R.id.textViewEventName)
    private val eventDateTextView: TextView = itemView.findViewById(R.id.textViewEventDate)
    private val eventLocationTextView: TextView = itemView.findViewById(R.id.textViewEventLocation)
    private val eventImageView: ImageView = itemView.findViewById(R.id.imageViewEvent)

    interface OnEventClickListener {
        fun onEventClick(event: Event)
    }

    private var eventClickListener: OnEventClickListener? = null

    fun setOnEventClickListener(listener: OnEventClickListener) {
        this.eventClickListener = listener
    }


    fun bind(event: Event) {
        eventNameTextView.text = event.eventName
        eventDateTextView.text = event.eventDate
        Picasso.get().load(event.imageURL).into(eventImageView)

        eventLocationTextView.text = event.eventLocation

        Picasso.get().load(event.imageURL).into(eventImageView)

        itemView.setOnClickListener {
            eventClickListener?.onEventClick(event)
        }
    }


}
