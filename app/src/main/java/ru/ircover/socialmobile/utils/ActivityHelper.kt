package ru.ircover.socialmobile.utils

import android.app.Activity
import android.content.Intent

inline fun <reified T : Activity> Activity.startActivity() {
    startActivity<T> { this }
}
inline fun <reified T : Activity> Activity.startActivity(intentFiller: Intent.() -> Intent) {
    startActivity(Intent(this, T::class.java).intentFiller())
}