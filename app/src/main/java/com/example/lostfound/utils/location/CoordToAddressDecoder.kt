package com.example.lostfound.utils.location

import android.content.Context
import android.location.Geocoder
import android.util.Log
import java.io.IOException
import java.util.*
import java.util.concurrent.CyclicBarrier

object CoordToAddressDecoder {
    private val tag = "LocationAddress"
    var bar = CyclicBarrier(2)
    var result: String = null.toString()

    fun getAddressFromLocation(latitude: Double?, longitude: Double?, context: Context) : String {
        if(latitude==null || longitude == null)return result
        val thread = object : Thread() {
            override fun run() {
                val geoCoder = Geocoder(
                    context,
                    Locale.getDefault()
                )
                try {
                    val addressList = geoCoder.getFromLocation(
                        latitude, longitude, 1
                    )
                    if ((addressList != null && addressList.size > 0)) {
                        val address = addressList.get(0)
                        val sb = StringBuilder()
                        for (i in 0 .. address.maxAddressLineIndex) {
                            sb.append(address.getAddressLine(i)).append(" ")
                        }
                       // sb.append(address.locality).append(" ")
                       // sb.append(address.countryName)
                        result = sb.toString()
                    }
                } catch (e: IOException) {
                    Log.e(tag, "Unable connect to GeoCoder", e)
                }
                bar.await()
            }
        }
        thread.start()
        bar.await()
        return result
    }
}