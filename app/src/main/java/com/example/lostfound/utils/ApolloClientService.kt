package com.example.lostfound.utils

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.network.okHttpClient
import com.example.lostfound.*
import com.example.lostfound.data.model.Announcement
import com.example.lostfound.data.model.User
import okhttp3.OkHttpClient

object ApolloClientService {
    private val okHttpClient = OkHttpClient.Builder().build()
    private val apolloClient = ApolloClient.Builder().serverUrl("http://10.0.2.2:8000/graphql/").okHttpClient(okHttpClient).build()

    suspend fun register(username: String, password: String): String? {
        val response = apolloClient.mutation(CreateUserMutation(username, password)).execute()
        val id = response.data?.createUser?.id
        return id
    }

    suspend fun login(username:String, password:String): String? {
        val response = apolloClient.mutation(LoginMutation(username, password)).execute()
        val token = response.data?.login?.token
        return token
    }

    suspend fun getAllLostAnn(): ArrayList<Announcement> {
        val response = apolloClient.query(GetLostAnnouncementsQuery()).execute()
        val annList = ArrayList<Announcement>()
        if((response.data!= null) and (response.data?.lostAnnouncements!= null))
            for(r in response.data?.lostAnnouncements!!){
                if (r != null) {
                    annList.add(Announcement((1..100).random(), r.title, User(r.user.id.toInt(), r.user.user.firstName + " " + r.user.user.lastName, R.drawable.ic_account), getLostTags(r.tags), r.content ))
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
        val response = apolloClient.mutation(UpdateUserProfileMutation(dateOfBirth, firstName, Optional.absent(), lastName, phoneNumber, usrId)).execute()
        val id = response.data?.updateUserProfile?.id
        return id
    }
}