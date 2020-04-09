package com.denisov.cat.di.component

import com.denisov.cat.data.api.CatsApi
import com.denisov.cat.data.database.CatsDao
import com.denisov.cat.di.module.ApiModule
import com.denisov.cat.di.module.AppModule
import com.denisov.cat.di.module.DatabaseModule
import com.denisov.cat.di.scope.PerApplication
import com.denisov.cat.presentation.Schedulers
import com.denisov.cat.presentation.utils.ContextProvider
import dagger.Component

@PerApplication
@Component(
    modules = [
        ApiModule::class,
        DatabaseModule::class,
        AppModule::class
    ]
)
interface AppComponent {

    fun getSchedulers(): Schedulers

    fun getCatsDao(): CatsDao

    fun getContextProvider(): ContextProvider

    fun getCatsApi(): CatsApi
}