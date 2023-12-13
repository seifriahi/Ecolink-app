package com.ecolink.data

import com.ecolink.model.User

class ResetPasswordResponse(updatedUser: User, message: String) {
    val updatedUser: User = updatedUser
    val message: String = message
}
