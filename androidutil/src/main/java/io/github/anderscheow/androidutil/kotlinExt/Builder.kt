package io.github.anderscheow.androidutil.kotlinExt

infix fun <T> Boolean.then(value: T?) = if (this) value else null

inline fun <T> Boolean.then(function: () -> T, default: () -> T) = if (this) function() else default()

inline infix fun <reified T> Boolean.then(function: () -> T) = if (this) function() else null