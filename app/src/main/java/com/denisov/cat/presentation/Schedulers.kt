package com.denisov.cat.presentation

import com.denisov.cat.di.scope.PerApplication
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@PerApplication
class Schedulers @Inject constructor() {

    val io = Schedulers.io()
    val ui = AndroidSchedulers.mainThread()
}