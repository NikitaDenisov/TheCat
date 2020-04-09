package com.denisov.cat.presentation.utils

import android.app.Activity
import android.view.WindowManager

fun Activity.disableTouches() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}
fun Activity.enableTouches() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}