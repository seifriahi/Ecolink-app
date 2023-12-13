package com.ecolink.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecolink.data.ModifyUserResponse
import com.ecolink.data.RetrofitClient
import com.ecolink.model.User
import kotlinx.coroutines.launch
import retrofit2.Response

class ModifyProfileViewModel : ViewModel() {

    private val _modifyUserResponse = MutableLiveData<Response<ModifyUserResponse>>()
    val modifyUserResponse: LiveData<Response<ModifyUserResponse>> get() = _modifyUserResponse

    fun modifyUser(
        userId: String,
        email: String?,
        profileBio: String?,
        fullname: String?,
        location: String?/*,
        facebookLink: String?,
        instagramLink: String?,
        phoneNumber: String?,
        profilePicture: String?,
        dateofbirth: String?, // Add dateOfBirth parameter
        role: String?*/ // Add role parameter
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.modifyUser(
                    userId = userId,
                    email = email,
                    profileBio = profileBio,
                    fullname = fullname,
                    location = location/*,
                    facebookLink = facebookLink,
                    instagramLink = instagramLink,
                    phoneNumber = phoneNumber,
                    profilePicture = profilePicture,
                    dateofbirth = dateofbirth,
                    role = role*/
                )
                println("goes through viewmodel")
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    println("goes through ")
                    // Check if the response body is not null
                    if (responseBody != null) {
                        // Extract the User object from the ModifyUserResponse
                        val modifiedUser: User = responseBody.user

                        // Update the LiveData with the modified user response
                        _modifyUserResponse.value = Response.success(ModifyUserResponse(modifiedUser))
                    } else {
                        // Handle null response body
                        _modifyUserResponse.value = Response.error(500, response.errorBody()!!)
                    }
                } else {
                    // Handle unsuccessful response
                    _modifyUserResponse.value = Response.error(response.code(), response.errorBody()!!)
                }
            } catch (e: Exception) {
               print("error modifying the user");
            }
        }
    }
}
