package com.example.lostfound.data.user

import com.apollographql.apollo3.api.Optional
import com.example.lostfound.data.Result
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.utils.ApolloClientService
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        try {
            var token = ApolloClientService.login(username, password)
            if (token!=null){
                var user = ApolloClientService.getMe(token)

                if(user!= null){
                    user.token = token
                    return Result.Success(user)
                }
            }
        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
        return Result.Error(IOException("Error logging in"))
    }

    suspend fun register(username:String, password: String): Result<LoggedInUser> {
        try {
            var id = ApolloClientService.register(username,password)
            if(id!=null){
                return Result.Success(LoggedInUser(id, email = username, token ="null"))
            }
        } catch (e: Throwable) {
            print(e.stackTrace)
            return Result.Error(IOException("Error logging in", e))
        }
        return Result.Error(IOException("Error logging in"))

    }

    suspend fun setUserInfo(email:String, dateOfBirth: String, firstName: String, lastName: String, phoneNumber: String, token:String, id: String): Result<LoggedInUser> {
        try {
            var msg = ApolloClientService.setUserInfo(Optional.present(dateOfBirth), Optional.present(firstName), Optional.present(lastName), Optional.present(phoneNumber))
            if(msg!=null){
                return Result.Success(
                    LoggedInUser(
                        id,
                        token,
                        "$firstName $lastName",
                        email,
                        dateOfBirth,
                        null,
                        phoneNumber
                    )
                )
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