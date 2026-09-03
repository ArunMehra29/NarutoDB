package com.naruto.narutodb

import android.app.Application
import com.naruto.repository.localservice.LocalService

class NarutoApp: Application()
{

    override fun onCreate() {
        super.onCreate()

        //initialise Database
        LocalService.invoke(this@NarutoApp)
    }
}