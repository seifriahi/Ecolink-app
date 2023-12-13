package com.ecolink.modéle

import com.google.gson.annotations.SerializedName
data class EventResponse(
    val message: String,
    val list: List<Event>
)

data class Event(
    var isFavorites: Boolean,
    val _id: String,
    val eventName: String,
    val eventDate: String,
    val textwhats: String,
    val eventLocation: String,
    val eventDescription: String,
    val imageURL: String,
    val __v: Int
)
