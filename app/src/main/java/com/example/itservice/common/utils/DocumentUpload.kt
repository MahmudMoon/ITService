package com.example.itservice.common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.itservice.application.TAG
import com.example.itservice.common.models.AuthResult
import com.example.itservice.common.utils.DbInstance.getStorageInstance
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class DocumentUpload constructor(val activity: AppCompatActivity){
//    companion object{
//        fun getInstance(activity: AppCompatActivity): PhotoUpload{
//            if(photoUpload==null){
//                photoUpload = PhotoUpload(activity)
//            }
//            return photoUpload as PhotoUpload
//        }
//        private var photoUpload: PhotoUpload? = null
//    }

    fun selectImage(PICK_IMAGE_REQUEST: Int) {
        // Defining Implicit Intent to mobile gallery
        var intent = Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        );
    }

    fun selectPdf(PICK_IMAGE_REQUEST: Int) {
        // Defining Implicit Intent to mobile gallery
        var intent = Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(
            Intent.createChooser(
                intent,
                "Select PDF from here..."
            ),
            PICK_IMAGE_REQUEST
        );
    }

    fun uploadDocumentInFireStore(uid: String, filePath: String?, uploadComplete: MutableLiveData<String>){
        val ref = getStorageInstance().reference.child(uid)
        val uploadTask = ref
            .putFile(Uri.parse(filePath))

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener{task->
            if(task.isSuccessful) {
                Log.d(TAG, "uploadImageInFireStore: success")
                uploadComplete.postValue(task.result.toString())
            }else{
                Log.d(TAG, "uploadImageInFireStore: failed")
                uploadComplete.postValue("")
            }
        }
    }

    fun downloadFile(context: Context, url: String, fileName: String, downloadCompleted: MutableLiveData<AuthResult>) {
        Log.d(TAG, "downloadFile: statred for " + fileName)
        //val path = Environment.getExternalStorageDirectory(Environment.DIRECTORY_DOCUMENTS)
        val path = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(path, fileName)
        try {
            if(!file.exists()){
                file.createNewFile()
            }else{
                file.delete()
                file.createNewFile()
            }
            BufferedInputStream(URL(url).openStream()).use { IN ->
                FileOutputStream(file).use { fileOutputStream ->
                    val dataBuffer = ByteArray(1024)
                    var bytesRead: Int
                    while (IN.read(dataBuffer, 0, 1024).also { bytesRead = it } != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead)
                    }
                }
            }
            downloadCompleted.postValue(AuthResult(true, file.absolutePath))
        } catch (e: IOException) {
            // handle exception
            Log.e(TAG, "downloadFile: failed "+e.localizedMessage )
            downloadCompleted.postValue(AuthResult(false, null))
        }
    }


}