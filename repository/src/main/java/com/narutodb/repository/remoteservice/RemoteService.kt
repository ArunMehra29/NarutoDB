package com.narutodb.repository.remoteservice

import com.narutodb.repository.response.GetAllCharacterResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


class RemoteService
{

    private var characterDbApi: CharacterDbApi

    init
    {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient
            .Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://narutodb.xyz/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        characterDbApi = retrofit.create(CharacterDbApi::class.java)
    }

    companion object
    {
        @Volatile
        private var instance: RemoteService? = null

        fun getInstance() = instance ?: synchronized(this)
        {
            instance ?: RemoteService()
        }
    }

    suspend fun getAllCharacters(): GetAllCharacterResponse
    {
        return characterDbApi.getALlCharacters()
    }
}

interface CharacterDbApi
{
    @GET("api/character")
    suspend fun getALlCharacters(@Query("limit") limit: Int = 1431): GetAllCharacterResponse
}