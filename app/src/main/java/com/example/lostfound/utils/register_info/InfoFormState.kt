package com.example.lostfound.utils.register_info

data class InfoFormState(val dateError: Int? = null,
                         val phoneError: Int? = null,
                         val usernameError:Int?= null,
                         val isDataValid: Boolean = false)