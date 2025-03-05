package com.babulmirdha.simple_date_picker

import android.content.Context
import com.babulmirdha.simple_date_picker_dialog_library.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object DateTimeUtils {


    fun getDifference(
        context: Context?,
        startDate: Date?,
        endDate: Date?,
        isShowPastFuture: Boolean = false
    ): String {


        startDate?.let{ stDt->

            endDate?.let { enDt->


                val diff = abs(enDt.time - stDt.time)
                var diffDays = (diff / (24 * 60 * 60 * 1000)).toInt().toFloat()


                val years = diffDays.toInt() / 365

                diffDays %= 365

                val months = diffDays.toInt() / 30

                diffDays %= 365

                val days = (diffDays % 30).toInt()

                val bnYears = years.toString() + ""
                val bnMonths = months.toString() + ""
                val bnDays = days.toString() + ""

                val isPastOrFuture = if (isShowPastFuture) {
                    if (startDate.let { isPassed(it) } == true) context?.getString(R.string.past) else context?.getString(
                        R.string.future
                    )
                } else {
                    ""
                }
                return when {
                    years != 0 -> {
                        String.format(
                            Locale.getDefault(),
                            context?.getString(R.string.format_years_months_days)!!,
                            bnYears,
                            bnMonths,
                            bnDays,
                            isPastOrFuture
                        )
                    }
                    months != 0 -> {
                        String.format(
                            Locale.getDefault(),
                            context?.getString(R.string.format_months_days)!!,
                            bnMonths,
                            bnDays,
                            isPastOrFuture
                        )
                    }
                    else -> {
                        String.format(
                            Locale.getDefault(),
                            context?.getString(R.string.format_days)!!,
                            bnDays,
                            isPastOrFuture
                        )
                    }
                }

            }


        }
 return ""
    }

    fun isPassed(date: Date?): Boolean {
        return Date().time > date?.time!!
    }

    fun formatDate(date: Date?, expectedFormat: String, lang: Lang = Lang.en): String? {

        date?.let {
            val expectedDateFormat = SimpleDateFormat(expectedFormat, Locale(lang.name))
            return expectedDateFormat.format(date)
        } ?: kotlin.run {
            return null
        }

    }

    fun formatDate(
        date: kotlinx.datetime.LocalDateTime?,
        expectedFormat: String,
        lang: Lang = Lang.en
    ): String? {
        date?.let {
            val expectedDateFormat = SimpleDateFormat(expectedFormat, Locale(lang.name))
            return expectedDateFormat.format(date)
        } ?: kotlin.run {
            return null
        }

    }

    fun formatDate(
        dateString: String?,
        currentFormat: String,
        expectedFormat: String = "d MMM yyyy",
        lang: Lang = Lang.en
    ): String? {


        try {
            val currentDateFormat = SimpleDateFormat(currentFormat)
            val date = currentDateFormat.parse(dateString)

            return formatDate(date, expectedFormat, lang)
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun formatDateRange(startsAt: Date?, endsAt: Date?, expectedDateFormant: String = "d MMM yyyy", lang: Lang = Lang.en): String {

        val formDate = formatDate(startsAt, expectedDateFormant, lang)

        val toDate = formatDate(endsAt, expectedDateFormant, lang)

        return String.format("%s ~ %s", formDate, toDate)

    }

    fun formatDateRange(startsAt: String?, endsAt: String?, currentFormant: String = "yyyy-MM-dd", expectedFormant: String = "d MMM yyyy", lang: Lang = Lang.en): String {


        if (startsAt.isNullOrEmpty()) {
            return ""
        }

        val formDate = formatDate(startsAt, currentFormant, expectedFormant, lang)

        val toDate = formatDate(endsAt, currentFormant, expectedFormant, lang)

        return String.format("%s ~ %s", formDate, toDate)
    }

    fun getDate(dateString: String?, currentFormat: String): Date? {
        val sdf = SimpleDateFormat(currentFormat)
        return sdf.parse(dateString)
    }
}
