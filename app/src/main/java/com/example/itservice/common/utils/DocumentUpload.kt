package com.example.itservice.common.utils

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.itservice.application.TAG
import com.example.itservice.common.utils.DbInstance.getStorageInstance

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
        Log.d(TAG, "uploadCVInFireStore: "+ uid)
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


}