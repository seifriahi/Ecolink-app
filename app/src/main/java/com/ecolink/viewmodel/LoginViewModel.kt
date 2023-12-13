package com.ecolink.viewmodel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecolink.data.RetrofitClient
import com.ecolink.data.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Response

// LoginViewModel.kt
class LoginViewModel : ViewModel() {

    private val _loginResponse = MutableLiveData<Response<LoginResponse>>()
    val loginResponse: LiveData<Response<LoginResponse>> get() = _loginResponse

    fun loginUser(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.login(
                    email = email,
                    password = password
                )
                _loginResponse.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}
