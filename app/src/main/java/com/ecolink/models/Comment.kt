package com.ecolink.models

data class Comment(
    val _id: String,  // Update the property name from _id to id
    val lessonId: String,
    val text: String
)
