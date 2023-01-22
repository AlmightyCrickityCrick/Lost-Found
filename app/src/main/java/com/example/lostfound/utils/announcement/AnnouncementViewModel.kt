package com.example.lostfound.utils.announcement

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.net.toFile
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lostfound.R
import com.example.lostfound.data.Result
import com.example.lostfound.utils.ApolloClientService
import com.example.lostfound.utils.login.LoginResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.io.*

class AnnouncementViewModel {
    protected val _annResult = MutableLiveData<AnnouncementResult>()
    val annResult: LiveData<AnnouncementResult> = _annResult
    fun create(context: Context, type:String, image:Uri?, content:String, coords:String, adress:String, tag:String, title:String, usrId: Int){
        runBlocking {
            var job = GlobalScope.async{ApolloClientService.createAnnouncement(type, fileFromContentUri(context, image), content, coords, adress, tag, title, usrId)}
            var result = job.await()

            if(result is Result.Success){
                _annResult.value = AnnouncementResult(success = result.data.id?.toInt())
            } else{
                _annResult.value = AnnouncementResult(error = R.string.ann_create_failed)
            }
        }
    }

    fun fileFromContentUri(context: Context, contentUri: Uri?): File? {
        // Preparing Temp file name
        if (contentUri==null) return null
        val fileExtension = getFileExtension(context, contentUri)
        val fileName = "temp_file" + if (fileExtension != null) ".$fileExtension" else ""

        // Creating Temp file
        val tempFile = File(context.cacheDir, fileName)
        tempFile.createNewFile()

        try {
            val oStream = FileOutputStream(tempFile)
            val inputStream = context.contentResolver.openInputStream(contentUri)

            inputStream?.let {
                copy(inputStream, oStream)
            }

            oStream.flush()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return tempFile
    }

    private fun getFileExtension(context: Context, uri: Uri): String? {
        val fileType: String? = context.contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
    }

    @Throws(IOException::class)
    private fun copy(source: InputStream, target: OutputStream) {
        val buf = ByteArray(8192)
        var length: Int
        while (source.read(buf).also { length = it } > 0) {
            target.write(buf, 0, length)
        }
    }

//    fun getFile(context: Context, image: Uri?): File?{
//        return if(image != null){
//            try {
//                val stream = context.contentResolver.openInputStream(image)
//            }
//        }
//        else null
//    }
}