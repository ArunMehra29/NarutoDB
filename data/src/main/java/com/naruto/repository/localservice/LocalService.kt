package com.naruto.repository.localservice

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.naruto.repository.response.CharacterEntity
import com.naruto.repository.local.CharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Database(entities = [CharacterEntity::class], version = 1)
@TypeConverters(value = [Converters::class])
abstract class LocalService: RoomDatabase()
{
    abstract fun characterDao(): CharacterDao
}

@Module
@InstallIn(value = [SingletonComponent::class])
object DatabaseModule
{

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): LocalService {

        return Room.databaseBuilder(
            context,
            LocalService::class.java,
            "naruto_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(
        database: LocalService
    ): CharacterDao {
        return database.characterDao()
    }
}