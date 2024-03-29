package com.example.lostfound.data.user

import com.example.lostfound.data.Result
import com.example.lostfound.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        // handle login
        val result = dataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun register(username: String, password: String): Result<LoggedInUser> {
        val result = dataSource.register(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    suspend fun setUserInfo(dateOfBirth: String, firstName: String, lastName: String, phoneNumber: String, key:String?): Result<LoggedInUser> {
        val result = user?.let { it.token?.let { it1 ->
            dataSource.setUserInfo(it.email, dateOfBirth, firstName, lastName, phoneNumber,
                it1, it.userId, key
            )
        } }
            ?: return Result.Error(IOException("User doesnt exist"))
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

     fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}