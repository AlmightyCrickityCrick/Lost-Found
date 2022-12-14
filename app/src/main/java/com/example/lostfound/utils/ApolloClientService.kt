package com.example.lostfound.utils

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.example.lostfound.data.Result
import com.apollographql.apollo3.network.okHttpClient
import com.example.lostfound.*
import com.example.lostfound.data.announcement.AnnouncementCreationRequestResult
import com.example.lostfound.data.model.Announcement
import com.example.lostfound.data.model.LoggedInUser
import com.example.lostfound.data.model.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.IOException

object ApolloClientService {
    private val okHttpClient = OkHttpClient.Builder().build()
    private val URL = "http://10.0.2.2:8000/graphql/"
    private val apolloClient = ApolloClient.Builder().serverUrl(URL).okHttpClient(okHttpClient).build()

    suspend fun register(username: String, password: String): String? {
        val response = apolloClient.mutation(CreateUserMutation(username, HashingService.hashString(password))).execute()
        val id = response.data?.createUser?.id
        return id
    }

    suspend fun login(username:String, password:String): String? {
        val response = apolloClient.mutation(LoginMutation(username, HashingService.hashString(password))).execute()
        val token = response.data?.login?.token
        return token
    }

    suspend fun getAllLostAnn(): ArrayList<Announcement> {
        val response = apolloClient.query(GetLostAnnouncementsQuery()).execute()
        val annList = ArrayList<Announcement>()
        if((response.data!= null) and (response.data?.lostAnnouncements!= null))
            for(r in response.data?.lostAnnouncements!!){
                if (r != null) {
                    annList.add(Announcement(r.id.toInt(), r.title, User(r.user.id.toInt(), r.user.user.firstName + " " + r.user.user.lastName, R.drawable.ic_account), getLostTags(r.tags), r.content ))
                }
            }
        return annList
    }
    private fun getLostTags(tags:List<GetLostAnnouncementsQuery.Tag>):ArrayList<String>{
        var result = ArrayList<String>()
        for (e in tags){
            result.add(e.name)
        }
        return result
    }

    private fun getFoundTags(tags:List<GetFoundAnnouncementsQuery.Tag>):ArrayList<String>{
        var result = ArrayList<String>()
        for (e in tags){
            result.add(e.name)
        }
        return result
    }

    suspend fun getAllFoundAnn(): ArrayList<Announcement> {
        val response = apolloClient.query(GetFoundAnnouncementsQuery()).execute()
        val annList = ArrayList<Announcement>()
        if((response.data!= null) and (response.data?.foundAnnouncements!= null))
            for(r in response.data?.foundAnnouncements!!){
                if (r != null) {
                    annList.add(Announcement((1..100).random(), r.title, User(r.user.id.toInt(), r.user.user.firstName + " " + r.user.user.lastName, R.drawable.ic_account), getFoundTags(r.tags), r.content ))
                }
            }
        return annList
    }

    suspend fun setUserInfo(dateOfBirth: Optional<String?>, firstName: Optional<String?>, lastName: Optional<String?>, phoneNumber: Optional<String?>, usrId: Int): String? {
        val response = apolloClient.mutation(UpdateUserProfileMutation(dateOfBirth, firstName, Optional.present("hello.png"), lastName, phoneNumber, usrId)).execute()
        val id = response.data?.updateUserProfile?.id
        return id
    }

    suspend fun getMe(token:String): LoggedInUser? {
        val interceptHttp = OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor(token)).build()
        val authorizedApolloClient = ApolloClient.Builder().serverUrl(URL).okHttpClient(interceptHttp).build()
        val response = authorizedApolloClient.query(GetMeQuery()).execute()

        if(response.data!=null) {
            return response.data!!.me?.let {
                LoggedInUser(
                    it.id,
                    //"${it.user.firstName} ${it.user.lastName}",
                    email = it.user.email,
                    //it.dateOfBirth.toString(),
                   // it.phone
                )
            }

        }
        return null
    }

    private class AuthorizationInterceptor(val token:String):Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().addHeader("Authorization", "JWT $token").build()

            return chain.proceed(request)
        }

    }

    suspend fun createAnnouncement(type:String, content:String, location:String, tag:String, title:String, usrId: Int): Result<AnnouncementCreationRequestResult> {
        val response = apolloClient.mutation(CreateNewAnnouncementMutation(type, content, Optional.present(null), location, Optional.present(0), tag, title, usrId)).execute()

        if (response.errors.isNullOrEmpty()){
            return Result.Success(AnnouncementCreationRequestResult(response.data?.createNewAnnouncement?.id))
        }
        return Result.Error(IOException("Announcement couldn't be created"))
    }
}