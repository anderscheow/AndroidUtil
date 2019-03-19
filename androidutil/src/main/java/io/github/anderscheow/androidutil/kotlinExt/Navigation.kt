@file:Suppress("UNCHECKED_CAST")

package io.github.anderscheow.androidutil.kotlinExt

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 *  Redirect to user to specific uri and try fallbackUri if failed
 *
 *  @param uri Primary Uri
 *  @param fallbackUri Fallback Uri
 *  @param intentAction Type of action for intent
 *  @param packageName Package name of your destination
 */
fun Context.redirectTo(uri: Uri,
                       fallbackUri: Uri?,
                       intentAction: String = Intent.ACTION_VIEW,
                       packageName: String? = null,
                       fallbackAction: (() -> Unit)? = null) {
    try {
        startActivity(Intent(intentAction, uri).apply {
            packageName?.let {
                this.`package` = it
            }
        })
    } catch (e: ActivityNotFoundException) {
        redirectToFallbackUri(
                fallbackUri = fallbackUri,
                intentAction = intentAction,
                fallbackAction = fallbackAction
        )
    }
}

private fun Context.redirectToFallbackUri(fallbackUri: Uri?,
                                          intentAction: String = Intent.ACTION_VIEW,
                                          fallbackAction: (() -> Unit)? = null) {
    try {
        fallbackUri?.let {
            startActivity(Intent(intentAction, it))
        }
    } catch (e: ActivityNotFoundException) {
        fallbackAction?.invoke()
    }
}

/**
 *  Redirect to user to specific url and try fallbackUrl if failed
 */
fun Context.redirectTo(url: String,
                       fallbackUrl: String?,
                       intentAction: String = Intent.ACTION_VIEW,
                       packageName: String? = null,
                       fallbackAction: (() -> Unit)? = null) {
    this.redirectTo(
            uri = Uri.parse(url),
            fallbackUri = fallbackUrl?.let {
                Uri.parse(fallbackUrl)
            },
            intentAction = intentAction,
            packageName = packageName,
            fallbackAction = fallbackAction
    )
}