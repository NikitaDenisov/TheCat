package com.denisov.cat.presentation.utils

import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.observers.LambdaConsumerIntrospection
import org.reactivestreams.Subscription

private val emptyOnNext: (Any?) -> Unit = {}
private val emptyOnError: (Throwable) -> Unit = {}
private val emptyComplete: () -> Unit = {}
private val emptySubscribe: (Disposable) -> Unit = {}
private val emptySubscription: (Subscription) -> Unit = {}

@JvmOverloads
fun <T> Observable<T>.subscribeWithoutSuspense(
    onNext: (T) -> Unit = emptyOnNext,
    onError: (Throwable) -> Unit = emptyOnError,
    onComplete: () -> Unit = emptyComplete,
    onSubscribe: (Disposable) -> Unit = emptySubscribe
): Disposable = WithoutSuspenseObserver(LambdaObserver(onNext, onError, onComplete, onSubscribe))
    .also { this.subscribe(it) }

private class WithoutSuspenseObserver<T>(observer: LambdaObserver<T>) :
    Observer<T> by observer,
    Disposable by observer,
    LambdaConsumerIntrospection by observer,
    WithoutSuspenseSubscribe

interface WithoutSuspenseSubscribe