package com.denisov.cat.di.module

import android.content.Context
import com.denisov.cat.di.scope.PerApplication
import com.denisov.cat.presentation.utils.ContextProvider
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    @PerApplication
    fun provideContextProvider() = ContextProvider(context)
}