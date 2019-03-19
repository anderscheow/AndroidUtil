package io.github.anderscheow.androidutil.kotlinExt

import android.app.Activity
import android.os.Build
import android.util.DisplayMetrics

/**
 * Extension method to retrieve current activity's display metrics
 */
private var displayMetrics: DisplayMetrics? = null

fun Activity.getDisplayMetrics(): DisplayMetrics? {
    if (displayMetrics == null) {
        val newDisplayMetrics = DisplayMetrics()
        this.windowManager.defaultDisplay.getMetrics(newDisplayMetrics)

        displayMetrics = newDisplayMetrics
    }

    return displayMetrics
}

fun Activity.getStatusBarHeight(): Int {
    var result = 0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
    }
    return result
}