package io.github.anderscheow.androidutil.kotlinExt

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Handler
import java.util.*

//region Extensions
//endregion

//region Non-extension

// Generate empty String
fun empty(): String {
    return ""
}

// Generate empty List
fun <T> emptyList(): List<T> {
    return ArrayList()
}

// Delay action within period of time
fun delay(timeInMilli: Long, action: () -> Unit) {
    Handler().postDelayed(action, timeInMilli)
}

// Throw NPE when detect is null, otherwise invoke action
inline fun assertNull(value: Any?, message: String? = null, action: () -> Unit) {
    if (value == null) {
        if (message == null) {
            throw NullPointerException()
        } else {
            throw NullPointerException(message)
        }
    }

    action.invoke()
}

// To detect internet connectivity
val connectivityIntentFilter = IntentFilter().apply {
    this.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
}

inline fun getConnectivityReceiver(crossinline action: () -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            action.invoke()
        }
    }
}

// Thread safe lazy initializer
fun <T> lazyThreadSafetyNone(initializer: () -> T): Lazy<T> = lazy(LazyThreadSafetyMode.NONE, initializer)

/**
 * Method to check is aboveApi.
 */
inline fun aboveApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT > included then api - 1 ?: api) {
        block()
    }
}

/**
 * Method to check is belowApi.
 */
inline fun belowApi(api: Int, included: Boolean = false, block: () -> Unit) {
    if (Build.VERSION.SDK_INT < included then api + 1 ?: api) {
        block()
    }
}

/**
 * Method to check is version at least K.
 */
inline fun isAtLeastK(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.KITKAT, true, block)
}

/**
 * Method to check is version at least L.
 */
inline fun isAtLeastL(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.LOLLIPOP, true, block)
}

/**
 * Method to check is version at least L MR1.
 */
inline fun isAtLeastL2(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.LOLLIPOP_MR1, true, block)
}

/**
 * Method to check is version at least M.
 */
inline fun isAtLeastM(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.M, true, block)
}

/**
 * Method to check is version at least N.
 */
inline fun isAtLeastN(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.N, true, block)
}

/**
 * Method to check is version at least N MR1.
 */
inline fun isAtLeastN2(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.N_MR1, true, block)
}

/**
 * Method to check is version at least O.
 */
inline fun isAtLeastO(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.O, true, block)
}

/**
 * Method to check is version at least O MR1.
 */
inline fun isAtLeastO2(block: () -> Unit) {
    aboveApi(Build.VERSION_CODES.O_MR1, true, block)
}

/**
 * Method to get the TAG name for all object
 */
val <T : Any> T.TAG
    get() = this::class.simpleName!!

fun <T> copyIterator(iterator: Iterator<T>?): List<T> {
    val copy = ArrayList<T>()
    if (iterator != null) {
        while (iterator.hasNext())
            copy.add(iterator.next())
    }
    return copy
}
//endregion