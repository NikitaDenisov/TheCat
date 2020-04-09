package com.denisov.cat.di.module

import android.content.Context
import androidx.room.Room
import com.denisov.cat.data.database.AppDatabase
import com.denisov.cat.data.database.DATABASE_NAME
import com.denisov.cat.di.scope.PerApplication
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule(private val applicationContext: Context) {

    @Provides
    @PerApplication
    fun provideDatabase() = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @PerApplication
    fun provideNewsDao(appDatabase: AppDatabase) = appDatabase.newsDao()
}