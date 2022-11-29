package com.example.lostfound.utils.register

data class RegisterFormState(val usernameError: Int? = null,
                             val passwordError: Int? = null,
                             val isDataValid: Boolean = false)