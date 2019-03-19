package io.github.anderscheow.androidutil.kotlinExt

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

fun Fragment.isNotThere(): Boolean = (activity?.isFinishing ?: true) || isDetached

inline fun Fragment.isAdded(block: () -> Unit) {
    if (isAdded) block.invoke()
}

inline fun Fragment.isAdded(block: () -> Unit, fallback: () -> Unit) {
    if (isAdded) block.invoke()
    else fallback.invoke()
}

inline fun Fragment.withContext(block: (Context) -> Unit) {
    context?.let(block)
}

@Suppress("UNCHECKED_CAST")
inline fun <T> Fragment.withContextAs(block: (T) -> Unit) {
    (context as? T)?.let(block)
}

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.withContextAs(): T? {
    return (context as? T)
}

inline fun Fragment.withActivity(block: (Activity) -> Unit) {
    activity?.let(block)
}

@Suppress("UNCHECKED_CAST")
inline fun <T> Fragment.withActivityAs(block: (T) -> Unit) {
    (activity as? T)?.let(block)
}

@Suppress("UNCHECKED_CAST")
fun <T> Fragment.withActivityAs(): T? {
    return (activity as? T)
}

fun Fragment.toast(textResource: Int) = requireActivity().toast(textResource)

fun Fragment.toast(text: CharSequence) = requireActivity().toast(text)

fun Fragment.longToast(textResource: Int) = requireActivity().longToast(textResource)

fun Fragment.longToast(text: CharSequence) = requireActivity().longToast(text)