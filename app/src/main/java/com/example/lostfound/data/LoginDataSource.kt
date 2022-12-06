package com.example.lostfound.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
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
            if (token!=null)return Result.Success(LoggedInUser(token, email = username))
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
        return Result.Error(IOException("Error logging in"))
    }

    suspend fun register(username:String, password: String):Result<LoggedInUser>{
        try {
            var id = ApolloClientService.register(username,password)
            if(id!=null){
                return Result.Success(LoggedInUser(id, email = username))
            }
        } catch (e: Throwable) {
            print(e.stackTrace)
            return Result.Error(IOException("Error logging in", e))
        }
        return Result.Error(IOException("Error logging in"))

    }

    suspend fun setUserInfo(email:String, dateOfBirth: String, firstName: String, lastName: String, phoneNumber: String, usrId: Int):Result<LoggedInUser>{
        try {
            var id = ApolloClientService.setUserInfo(Optional.present(dateOfBirth), Optional.present(firstName), Optional.present(lastName), Optional.present(phoneNumber), usrId)
            if(id!=null){
                return Result.Success(LoggedInUser(id, "$firstName $lastName", email, dateOfBirth, null, phoneNumber))
            }
        } catch (e: Throwable) {
            print(e.stackTrace)
            return Result.Error(IOException("Error setting up user info", e))
        }
        return Result.Error(IOException("Error setting up user info"))
    }

    fun logout() {
        // TODO: revoke authentication
    }
}