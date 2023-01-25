package com.example.lostfound.utils

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.*
import com.example.lostfound.data.Result
import com.apollographql.apollo3.network.okHttpClient
import com.example.lostfound.*
import com.example.lostfound.data.announcement.AnnouncementCreationRequestResult
import com.example.lostfound.data.model.*
import com.example.lostfound.utils.crypto.HashingService
import com.google.android.gms.maps.model.LatLng
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.io.File
import java.io.IOException

object ApolloClientService {
    private val okHttpClient = OkHttpClient.Builder().build()
    private val URL = "http://10.0.2.2:8000/graphql/"
    private val apolloClient = ApolloClient.Builder().serverUrl(URL).okHttpClient(okHttpClient).build()
    lateinit var interceptHttp :OkHttpClient
    lateinit var authorizedApolloClient : ApolloClient
    lateinit var token:String

    suspend fun register(username: String, password: String): String? {
        val response = apolloClient.mutation(CreateUserMutation(username, HashingService.hashString(password))).execute()
        val id = response.data?.createUser?.id
        return id
    }

    suspend fun login(username:String, password:String): String? {
        val response = apolloClient.mutation(LoginMutation(username, HashingService.hashString(password))).execute()
        val token = response.data?.login?.token
        if(token != null) {
            this.token = token
            interceptHttp =
                OkHttpClient.Builder().addInterceptor(AuthorizationInterceptor(token)).build()
            authorizedApolloClient =
                ApolloClient.Builder().serverUrl(URL).okHttpClient(interceptHttp).build()
        }
            return token
    }

    suspend fun getAllMapAnn():ArrayList<MapAnn>{
        val response = apolloClient.query(GetMapAnnouncementsQuery()).execute()
        val annList = ArrayList<MapAnn>()
        if((response.data!= null) and (response.data?.lostAnnouncements!= null))
            for(r in response.data?.lostAnnouncements!!){
                if (r != null) {
                    annList.add(MapAnn(r.id.toInt(), r.title, r.content, getCoords(r.coordonates) ))
                }
            }
        if((response.data!= null) and (response.data?.foundAnnouncements!= null))
            for(r in response.data?.foundAnnouncements!!){
                if (r != null) {
                    annList.add(MapAnn(r.id.toInt(), r.title, r.content, getCoords(r.coordonates) ))
                }
            }
        return annList

    }

    fun getCoords(str:String):LatLng{
        if('|' in str){
            var lat:Double = (str.subSequence(0, str.indexOf('|'))).toString().toDouble()
            var long:Double = str.subSequence(str.indexOf('|')+1, str.length).toString().toDouble()
            return LatLng(lat,long)
        } else return LatLng(47.003670, 28.907089)
    }

    suspend fun getAnnDetails(id:Int):AnnDetails?{
        val response = apolloClient.query(GetAnnouncementDetailsQuery(id)).execute()
        if((response?.data!=null) && (response?.data?.announcement!=null)){
            var r = response?.data?.announcement?:null
            if(r!=null)return AnnDetails(r.id.toInt(), r.title, r.content, User(r.userProfile.id.toInt(), "${r.userProfile.user.firstName} ${r.userProfile.user.lastName}", R.drawable.ic_account, r.userProfile.rating),getDetailsTags(r.tags), r.streetName, r.image)
        }
        return null
    }

    private fun getDetailsTags(tags:List<GetAnnouncementDetailsQuery.Tag>):ArrayList<String>{
        var result = ArrayList<String>()
        for (e in tags){
            result.add(e.name)
        }
        return result
    }

    suspend fun getAllLostAnn(): ArrayList<Announcement> {
        val response = apolloClient.query(GetLostAnnouncementsQuery()).execute()
        val annList = ArrayList<Announcement>()
        if((response.data!= null) and (response.data?.lostAnnouncements!= null))
            for(r in response.data?.lostAnnouncements!!){
                if (r != null) {
                    annList.add(Announcement(r.id.toInt(), r.title, User(r.userProfile.id.toInt(), r.userProfile.user.firstName + " " + r.userProfile.user.lastName, R.drawable.ic_account), getLostTags(r.tags), r.content ))
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
                    annList.add(Announcement(r.id.toInt(), r.title, User(r.userProfile.id.toInt(), r.userProfile.user.firstName + " " + r.userProfile.user.lastName, R.drawable.ic_account), getFoundTags(r.tags), r.content ))
                }
            }
        return annList
    }

    suspend fun setUserInfo(dateOfBirth: Optional<String?>, firstName: Optional<String?>, lastName: Optional<String?>, phoneNumber: Optional<String?>, key:Optional<String?>): String? {
        val response = authorizedApolloClient.mutation(UpdateUserProfileMutation(dateOfBirth, firstName, Optional.present(null), lastName, phoneNumber, key)).execute()
        val msg = response.data?.updateUserProfile?.msg
        return msg
    }

    suspend fun getMe(): LoggedInUser? {
        try {
            val response = authorizedApolloClient.query(GetMeFullQuery()).execute()
            if(response.data!=null && response.errors.isNullOrEmpty()) {
                return response.data!!.me?.let { it ->
                    LoggedInUser(
                        it.id,
                        token=token,
                        "${it.user.firstName} ${it.user.lastName}",
                        email = it.user.email,
                        dateOfBirth = it.dateOfBirth.toString(),
                        phone = it.phone,
                        rating = it.rating
                    )
                }

            }
        } catch (e:Throwable){}

        val response = authorizedApolloClient.query(GetMeQuery()).execute()

        if(response.data!=null) return response.data!!.me?.let {
            LoggedInUser(
                it.id,
                token,
                //"${it.user.firstName} ${it.user.lastName}",
                email = it.user.email,
                //it.dateOfBirth.toString(),
               // it.phone
            )
        }
        return null
    }

    private class AuthorizationInterceptor(val token:String):Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder().addHeader("Authorization", "JWT $token").build()

            return chain.proceed(request)
        }

    }

    suspend fun createAnnouncement(type:String, image: File?, content:String, coords:String, adress:String, tag:String, title:String, usrId: Int): Result<AnnouncementCreationRequestResult> {
//        var response :ApolloResponse<CreateNewAnnouncementMutation.Data>
//        if(image!= null){
////            val upload = DefaultUpload.Builder().fileName(image.name).content(image).build()
////            {sink -> okio.Source.use {
////                sink.writeAll(it)
////            }}
           print("Image is ${image?.name} ${image?.extension}")
           val response= authorizedApolloClient.mutation(CreateNewAnnouncementMutation(type, content, Optional.present(image?.toUpload("image/jpg")), coords, adress, Optional.present(0), tag, title)).execute()

//        } else response= apolloClient.mutation(CreateNewAnnouncementMutation(type, content, Optional.present(null), location, Optional.present(0), tag, title, usrId)).execute()


        if (response.errors.isNullOrEmpty()){
            return Result.Success(AnnouncementCreationRequestResult(response.data?.createNewAnnouncement?.id))
        }
        return Result.Error(IOException("Announcement couldn't be created"))
    }

    suspend fun sendOTP(email:String):Int?{
        var response = apolloClient.mutation(SendOTPCodeMutation(email)).execute()
        if(response.errors.isNullOrEmpty()) return response.data!!.sendOtpVerification!!.id!!.toInt()
        return null
    }

    suspend fun getOTP(id:Int, code:String):String?{
        var response = apolloClient.mutation(GetOTPResultMutation(Optional.present(id), Optional.present(code))).execute()
        if(response.errors.isNullOrEmpty()) return response.data!!.getOtpVerification!!.msg
        return null
    }
}