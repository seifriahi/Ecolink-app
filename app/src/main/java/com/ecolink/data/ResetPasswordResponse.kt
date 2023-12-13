package com.ecolink.data

import com.ecolink.models.User

class ResetPasswordResponse(updatedUser: User, message: String) {
    val updatedUser: User = updatedUser
    val message: String = message
}
