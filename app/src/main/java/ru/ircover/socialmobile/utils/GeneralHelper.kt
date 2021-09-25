package ru.ircover.socialmobile.utils

import android.view.View

enum class HideMode {
    GONE, INVISIBLE
}

fun Boolean.toViewVisibility(hideMode: HideMode = HideMode.GONE) =
    if(this) View.VISIBLE else when(hideMode) {
        HideMode.GONE -> View.GONE
        HideMode.INVISIBLE -> View.INVISIBLE
    }