package com.example.lostfound.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

object HashingService {
    var algorithm = "SHA-256"
    fun hashString(string: String):String{
        var encodedhash = getDigest(string)
        return bytesToHex(encodedhash)
    }

    fun getDigest(string:String): ByteArray{
        val digest = MessageDigest.getInstance(algorithm)
        val encodedhash = digest.digest(
            string.toByteArray(StandardCharsets.UTF_8)
        )
        return encodedhash
    }

    private fun bytesToHex(hash: ByteArray): String {
        var hexString = ""
        for (i in hash.indices) {
            val hex = Integer.toHexString(0xff and hash[i].toInt())
            if (hex.length == 1) {
                hexString+='0'
            }
            hexString+=hex
        }
        return hexString.toString()
    }
}