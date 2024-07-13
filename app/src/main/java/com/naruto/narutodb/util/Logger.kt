package com.naruto.narutodb.util

import android.util.Log

object Logger
{
    fun debug(tag: String, message: String)
    {
        Log.d(tag, message)
    }

    fun info(tag: String, message: String)
    {
        Log.i(tag, message)
    }

    fun error(tag: String, message: String)
    {
        Log.e(tag, message)
    }

    fun verbose(tag: String, message: String)
    {
        Log.v(tag, message)
    }
}