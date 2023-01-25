package com.example.lostfound.utils.crypto

import android.content.Context
import android.util.Pair
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import okio.internal.commonAsUtf8ToByteArray
import okio.internal.commonToUtf8String
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object CryptographyService {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    fun decryptSym(key:String, input:String, civ:String):String{
        var decodedKey = Base64.getDecoder().decode(key)
        var ciphertext = Base64.getDecoder().decode(input)
        var symKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")

        var iv = Base64.getDecoder().decode(civ)

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, symKey, IvParameterSpec(iv))
        val plaintext: ByteArray = cipher.doFinal(ciphertext)
        var p = Base64.getEncoder().encodeToString(plaintext).commonAsUtf8ToByteArray()
        return plaintext.commonToUtf8String()
    }

    fun encryptSym(key: String, input:String):Pair<String,String>{
        var decoder = Base64.getDecoder()
        var decodedKey = decoder.decode(key)
        var symKey = SecretKeySpec(decodedKey, 0, decodedKey.size, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, symKey)
        var iv = Base64.getEncoder().encodeToString(cipher.iv)

        //Text Jumping hoops
        var text = input.commonAsUtf8ToByteArray()
        var decoderText = Base64.getEncoder().encodeToString(text)
        var plaintext = decoder.decode(decoderText)

        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        val str = Base64.getEncoder().encodeToString(ciphertext)
        return Pair(str,iv)
    }

    fun encryptAsym(key: String, input: String):String{
        var decodedKey = Base64.getDecoder().decode(key)
        var kf = KeyFactory.getInstance("RSA")
        var asymKey = X509EncodedKeySpec(decodedKey)
        var public = kf.generatePublic(asymKey)

        var plaintext = Base64.getDecoder().decode(input)

        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, public)
        val ciphertext: ByteArray = cipher.doFinal(plaintext)
        return Base64.getEncoder().encodeToString(ciphertext)
    }

    fun decryptAsym(key: String, input: String):String{
        var decodedKey = Base64.getDecoder().decode(key)
        var kf = KeyFactory.getInstance("RSA")
        var asymKey = PKCS8EncodedKeySpec (decodedKey)
        var private = kf.generatePrivate(asymKey)

        var ciphertext = Base64.getDecoder().decode(input)
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, private)
        val plaintext: ByteArray = cipher.doFinal(ciphertext)
        return plaintext.commonToUtf8String()
    }

    fun generateSymmetricKey(): String{
        val keygen = KeyGenerator.getInstance("AES")
        keygen.init(256)
        val key: SecretKey = keygen.generateKey()
        return Base64.getEncoder().encodeToString(key.encoded)
    }

    fun generateAssymetricKey():Pair<String, String>{
        val keygen = KeyPairGenerator.getInstance("RSA")
        keygen.initialize(2048)
        var pair = keygen.generateKeyPair()
        var private = Base64.getEncoder().encodeToString(pair.private.encoded)
        var public = Base64.getEncoder().encodeToString(pair.public.encoded)
        return Pair(public, private)
    }

    fun saveToSharedPreference(key:String, value:String, context:Context){
        val sharedPreferences = EncryptedSharedPreferences.create(
            // passing a file name to share a preferences
            "preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        sharedPreferences.edit().putString(key, value).apply()

    }

    fun getFromSharedPreference(key:String, context: Context):String?{
        val sharedPreferences = EncryptedSharedPreferences.create(
            // passing a file name to share a preferences
            "preferences",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        // on below line we are storing data in shared preferences file.
        return sharedPreferences.getString(key, "")

    }
}