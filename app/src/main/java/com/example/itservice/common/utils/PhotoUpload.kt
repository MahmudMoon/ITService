package com.example.itservice.common.utils

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.itservice.common.utils.DbInstance.getStorageInstance

class PhotoUpload constructor(val activity: AppCompatActivity){
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

    fun uploadImageInFireStore(uid: String, filePath: String?, uploadComplete: MutableLiveData<String>){
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
                uploadComplete.postValue(task.result.toString())
            }else{
                uploadComplete.postValue("")
            }
        }
    }


}