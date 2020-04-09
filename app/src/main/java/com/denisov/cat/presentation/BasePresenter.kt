package com.denisov.cat.presentation

import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BasePresenter<View : BaseView> : LifecycleObserver {

    private val disposables = CompositeDisposable()
    protected var view: View? = null

    protected inline fun subscribe(crossinline function: () -> Disposable) {
        subscribe(function())
    }

    protected fun subscribe(disposable: Disposable) {
        disposables.add(disposable)
    }

    protected fun cancelSubscribes() = disposables.clear()

    fun attachView(view: View) {
        this.view = view
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    @CallSuper
    open fun onDestroy() {
        cancelSubscribes()
        view = null
    }
}