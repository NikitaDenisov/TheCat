package com.denisov.cat.di

import android.app.Application
import com.denisov.cat.di.component.AppComponent
import com.denisov.cat.di.component.DaggerAppComponent
import com.denisov.cat.di.module.ApiModule
import com.denisov.cat.di.module.AppModule
import com.denisov.cat.di.module.DatabaseModule

object ComponentFactory {
    fun createAppComponent(application: Application): AppComponent {
        return DaggerAppComponent
            .builder()
            .apiModule(ApiModule())
            .databaseModule(DatabaseModule(application))
            .appModule(AppModule(application))
            .build()
    }
}