package com.denisov.cat.di

import android.app.Application
import com.denisov.cat.di.component.AppComponent

private lateinit var appComponent: AppComponent

var initializerAppComponent: (Application) -> AppComponent =
    { ComponentFactory.createAppComponent(it) }

fun initializeAppComponent(application: Application) {
    appComponent = initializerAppComponent(application)
}

fun getAppComponent(): AppComponent {
    return appComponent
}