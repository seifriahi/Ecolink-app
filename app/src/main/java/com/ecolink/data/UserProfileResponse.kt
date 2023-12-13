package com.ecolink.data

import com.google.gson.annotations.SerializedName

data class UserProfileResponse(
    @SerializedName("id") val id: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("fullname") val fullname: String?,
    @SerializedName("dateofbirth") val dateOfBirth: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("profilepicture") val profilePicture: String?, // Assuming it's a URL or a path
    @SerializedName("profilebio") val profileBio: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("phonenumber") val phoneNumber: String?, // Change the type if needed
    @SerializedName("isActive") val isActive: Boolean?,
    @SerializedName("isBanned") val isBanned: Boolean?,
    @SerializedName("isVerified") val isVerified: Boolean?
)
