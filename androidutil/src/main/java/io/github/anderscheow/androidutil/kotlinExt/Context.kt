package io.github.anderscheow.androidutil.kotlinExt

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import org.jetbrains.anko.browse

/**
 * Extension method to rate app on PlayStore for Context.
 */
fun Context.rate(packageName: String = this.packageName): Boolean =
        browse("market://details?id=$packageName") ||
                browse("http://play.google.com/store/apps/details?id=$packageName")

/**
 * Extension method to check network availability
 */
fun Context.isConnectedToInternet(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    return connectivityManager?.activeNetworkInfo?.isConnected ?: false
}

/**
 * Extension method to find color based on color resource.
 */
fun Context.findColor(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

/**
 * Extension method to find drawable based on drawable resource.
 */
fun Context.findDrawable(@DrawableRes resId: Int): Drawable? = ContextCompat.getDrawable(this, resId)

/**
 * Extension method to calculate number of columns
 */
fun Context.calculateNoOfColumns(): Int {
    val displayMetrics = this.resources.displayMetrics
    val dpWidth = displayMetrics.widthPixels / displayMetrics.density

    return (dpWidth / 180).toInt()
}

/**
 * Extension method to show keyboard
 */
fun Context.showKeyboard(view: View?) {
    view?.let {
        it.requestFocus()

        (this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
    }
}

/**
 * Extension method to hide keyboard
 */
fun Context.hideKeyboard(view: View?) {
    view?.let {
        (this.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.hideSoftInputFromWindow(it.windowToken, 0)
    }
}