@file:Suppress("UNCHECKED_CAST")

package io.github.anderscheow.androidutil.kotlinExt

import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

class ReferencedProperty<T>(private val get: () -> T,
                            private val set: (T) -> Unit = {}) {

    operator fun getValue(thisRef: Any?,
                          property: KProperty<*>): T = get()

    operator fun setValue(thisRef: Any?,
                          property: KProperty<*>,
                          value: T) = set(value)
}

fun <T> ref(property: KMutableProperty0<T>) = ReferencedProperty(property::get,
        property::set)

fun <T> ref(property: KProperty0<T>) = ReferencedProperty(property::get)