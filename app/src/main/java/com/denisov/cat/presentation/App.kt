package com.denisov.cat.presentation

import android.app.Application
import com.denisov.cat.di.initializeAppComponent

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeAppComponent(this)
    }
}