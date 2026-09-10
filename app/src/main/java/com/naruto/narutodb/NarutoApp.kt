package com.naruto.narutodb

import android.app.Application
import com.naruto.repository.localservice.LocalService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NarutoApp: Application()
{

    override fun onCreate() {
        super.onCreate()

        //initialise Database
        LocalService.invoke(context = this@NarutoApp)
    }
}