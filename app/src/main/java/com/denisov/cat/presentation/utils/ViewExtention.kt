package com.denisov.cat.presentation.utils

import android.view.View

fun View.isVisible(): Boolean = visibility == View.VISIBLE

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun View.showIfOrHide(crossinline condition: () -> Boolean) {
    takeIf { condition() }
        ?.show()
        ?: hide()
}