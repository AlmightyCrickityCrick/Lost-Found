package com.example.lostfound.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.lostfound.CreateUserMutation
import com.example.lostfound.LoginMutation
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.data.model.User
import com.example.lostfound.utils.ApolloClientService
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            var token = ApolloClientService.login(username, password)
            if (token!=null)return Result.Success(LoggedInUser(token, username))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
        return Result.Error(IOException("Error logging in"))
    }

    suspend fun register(username:String, password: String):Result<LoggedInUser>{
        try {
            var id = ApolloClientService.register(username,password)
            if(id!=null)return Result.Success(LoggedInUser(id, username))
        } catch (e: Throwable) {
            print(e.stackTrace)
            return Result.Error(IOException("Error logging in", e))
        }
        return Result.Error(IOException("Error logging in"))

    }

    fun logout() {
        // TODO: revoke authentication
    }
}