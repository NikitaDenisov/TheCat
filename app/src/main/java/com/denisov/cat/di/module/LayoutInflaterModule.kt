package com.denisov.cat.di.module

import android.content.Context
import android.view.LayoutInflater
import com.denisov.cat.di.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class LayoutInflaterModule {

    @Provides
    fun provideLayoutInflater(@ActivityContext context: Context) = LayoutInflater.from(context)
}