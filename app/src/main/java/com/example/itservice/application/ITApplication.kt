package com.example.itservice.application

import android.app.Application
import android.util.Log

public const val TAG = "ITApplication"

class ITApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }
}