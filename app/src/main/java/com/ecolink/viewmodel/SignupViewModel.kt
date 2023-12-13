package com.ecolink.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecolink.data.RetrofitClient
import com.ecolink.data.SignupResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class SignupViewModel : ViewModel() {

    private val _signupResponse = MutableLiveData<Response<SignupResponse>>()
    val signupResponse: LiveData<Response<SignupResponse>> get() = _signupResponse

    fun signupUser(
        fullname: String,
        email: String,
        password: String,
         // Adjust the type if needed
        role: String
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.signup(
                    fullname = fullname,
                    email = email,
                    password = password,

                    role = role

                )
                _signupResponse.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

