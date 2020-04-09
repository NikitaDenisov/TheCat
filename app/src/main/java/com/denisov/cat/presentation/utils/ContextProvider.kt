package com.denisov.cat.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import com.denisov.cat.di.scope.PerApplication
import javax.inject.Inject

class ContextProvider(val context: Context) {

    fun getString(@StringRes resId: Int): String =
        context.getString(resId)

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        context.resources.getString(resId, *formatArgs)
}