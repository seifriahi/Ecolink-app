package com.ecolink.models

data class News(
    val id: String,
    val imageRes: String,
    val title: String,
    val description: String,
    val path: String? = null,

)

data class ProjectModel(val news: List<News>)
