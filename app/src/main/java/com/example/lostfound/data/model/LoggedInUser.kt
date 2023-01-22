package com.example.lostfound.data.model

import java.io.Serializable

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    val userId: String,
    var token: String?,
    var displayName: String?=null,
    var email :String,
    var dateOfBirth: String?=null,
    var img :String?=null,
    var phone:String?=null

):Serializable