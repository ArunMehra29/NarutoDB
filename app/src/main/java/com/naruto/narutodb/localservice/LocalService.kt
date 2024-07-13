package com.naruto.narutodb.localservice

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.naruto.narutodb.model.common.response.CharacterEntity
import com.naruto.narutodb.model.local.CharacterDao

@Database(entities = [CharacterEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class LocalService: RoomDatabase()
{
    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var instance: LocalService? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        fun getInstance() = instance

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            LocalService::class.java,
            "naruto_db"
        ).build()
    }
}