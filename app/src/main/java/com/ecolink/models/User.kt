package com.ecolink.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("fullname") val fullname: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("dateofbirth") val dateofbirth: String, // Use String for date for simplicity, you may want to use a proper Date type
    @SerializedName("role") val role: String,
    @SerializedName("profilepicture") val profilepicture: String?,
    @SerializedName("profilebio") val profilebio: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("facebooklink") val facebooklink: String?,
    @SerializedName("instagramlink") val instagramlink: String?,
    @SerializedName("linkedinlink") val linkedinlink: String?,
    @SerializedName("phonenumber") val phonenumber: String?, // Use String for phone number for simplicity
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("isBanned") val isBanned: Boolean,
    @SerializedName("isVerified") val isVerified: Boolean
)
