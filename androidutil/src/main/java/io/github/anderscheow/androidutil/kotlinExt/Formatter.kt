package io.github.anderscheow.androidutil.kotlinExt

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

//region Extensions

/** Extension for String */
fun String.formatToSpanned(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(this)
    }
}

/** Extension for Long */
fun Long?.formatAmount(format: String? = null): String {
    val d = (this ?: 0) / 100.0
    val formatter = DecimalFormat(format ?: "###,###,##0.00")

    return formatter.format(d)
}

fun Long?.formatDateWithYear(locale: Locale = Locale.getDefault(),
                             timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat("dd MMM yyyy", locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

fun Long?.formatDateWithoutYear(locale: Locale = Locale.getDefault(),
                                timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat("dd MMM", locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

fun Long?.formatTime(locale: Locale = Locale.getDefault(),
                     timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat("h:mm a", locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

fun Long?.formatMinuteSecond(locale: Locale = Locale.getDefault(),
                             timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat("mm:ss", locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

fun Long?.formatSecond(locale: Locale = Locale.getDefault(),
                       timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat("0:ss", locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

fun Long?.formatDateTime(locale: Locale = Locale.getDefault(),
                         timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat("dd MMM yyyy, h:mm a", locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

fun Long?.formatDateTime24Hours(locale: Locale = Locale.getDefault(),
                                timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat("dd MMM yyyy, HH:mm a", locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

fun Long?.formatDate(format: String,
                     locale: Locale = Locale.getDefault(),
                     timeZone: TimeZone = TimeZone.getDefault()): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this ?: 0

    return SimpleDateFormat(format, locale).apply {
        this.timeZone = timeZone
    }.format(calendar.time)
}

/** Extension for Double */
fun Double?.formatAmount(firstFormat: String? = null, secondFormat: String? = null): String {
    val twoZeroFormatted = this.formatAmount(firstFormat ?: "###,###,##0.00")

    if (twoZeroFormatted == "0.00") {
        val fourZeroFormatted = this.formatAmount(firstFormat ?: "###,###,##0.0000")

        if (fourZeroFormatted == "0.0000") {
            return fourZeroFormatted
        }
    }

    return twoZeroFormatted
}

fun Double?.formatAmount(format: String): String {
    val formatter = DecimalFormat(format)
    formatter.roundingMode = RoundingMode.DOWN

    return formatter.format(this ?: 0)
}

/** Extension for CharSequence */
fun CharSequence.trimToString(): String {
    return this.toString().trim()
}
//endregion

//region Non-extension
fun toSpanned(value: String): Spanned {
    return value.formatToSpanned()
}
//endregion