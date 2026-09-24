package com.naruto.repository.remote

import com.naruto.repository.response.GetAllCharacterResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Module
@InstallIn(value = [SingletonComponent::class])
object RemoteService
{

    @Provides
    @Singleton
    fun provideCleanOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor = loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl("https://dattebayo-api.onrender.com")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideCharacterApi(retrofit: Retrofit): CharacterDbApi =
        retrofit.create(CharacterDbApi::class.java)

}

interface CharacterDbApi
{
    @GET(value = "/characters")
    suspend fun getALlCharacters(@Query(value = "limit") limit: Int = 1431): GetAllCharacterResponse
}