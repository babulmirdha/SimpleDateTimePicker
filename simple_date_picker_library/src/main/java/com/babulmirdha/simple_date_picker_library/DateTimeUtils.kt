package com.babulmirdha.simple_date_picker_library

import android.content.Context
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

object DateTimeUtils {


    fun getDifference(
        context: Context?,
        dateString: String?,
        currentFormat: String = "yyyy-MM-dd",
        showPastFuture: Boolean
    ): String {

        val simpleDateFormat = SimpleDateFormat(currentFormat)

        try {
            val date = simpleDateFormat.parse(dateString)
            return getDifference(context, date, showPastFuture)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return getDifference(context, Date(), showPastFuture)
    }


    fun getDateFromDatePicker(datePicker: DatePicker, timPicker:TimePicker?=null): Date? {
        val day: Int = datePicker.dayOfMonth
        val month: Int = datePicker.month
        val year: Int = datePicker.year

       val hour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
           timPicker?.hour
       } else {
           timPicker?.currentHour
       }
        val minute = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timPicker?.minute
        } else {
            timPicker?.currentHour
        }

        val calendar = Calendar.getInstance()
        calendar[year, month] = day

        hour?.let { calendar.set(Calendar.HOUR_OF_DAY, it) }
        minute?.let { calendar.set(Calendar.MINUTE, it) }

        return calendar.time
    }

    fun dateFromUTC(date: Date?): Date? {
        if (date != null) {
            return Date(date.time + Calendar.getInstance().timeZone.getOffset(Date().time))
        }

        return null
    }

    fun dateToUTC(date: Date?): Date? {
        if (date != null) {
            return Date(date.time - Calendar.getInstance().timeZone.getOffset(date.time))
        }

        return null
    }

    fun getDifference(
        context: Context?,
        startDate: String?,
        endDate: String?,
        dateFormat: String,
        isShowPastFuture: Boolean = false
    ): String {

        return try {
            val simpleDateFormat = SimpleDateFormat(dateFormat)

            val startDT = simpleDateFormat.parse(startDate)
            val endDT = simpleDateFormat.parse(endDate)

            getDifference(context, startDT, endDT, isShowPastFuture)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }


    }


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
                    if (startDate?.let { isPassed(it) } == true) context?.getString(R.string.past) else context?.getString(R.string.future)
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

    fun getDifference(context: Context?, date: Date?, showPastFuture: Boolean=false): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time

        return getDifference(context, date, currentTime, showPastFuture)

//        try {
//            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
//            val dateStr = simpleDateFormat.format(currentTime)
//
//            currentTime = simpleDateFormat.parse(dateStr)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//        val diff = Math.abs(currentTime.time - date.time)
//        var diffDays = (diff / (24 * 60 * 60 * 1000)).toInt().toFloat()
//
//
//        val years = diffDays.toInt() / 365
//
//        diffDays = diffDays % 365
//
//        val months = diffDays.toInt() / 30
//
//        diffDays = diffDays % 365
//
//        val days = (diffDays % 30).toInt()
//
//        val bnYears = years.toString() + ""
//        val bnMonths = months.toString() + ""
//        val bnDays = days.toString() + ""
//
//        val isPastOrFuture = if (isPassed(date)) "অতীত" else "বাকি"
//        return if (years != 0) {
//            String.format("%s বছর %s মাস %s দিন %s", bnYears, bnMonths, bnDays, isPastOrFuture)
//        } else if (months != 0) {
//            String.format("%s মাস %s দিন %s", bnMonths, bnDays, isPastOrFuture)
//        } else {
//            String.format("%s দিন %s", bnDays, isPastOrFuture)
//        }

    }

    fun isPassed(date: Date?): Boolean {
        return Date().time > date?.time!!
    }

    fun isPassed(dateTime: String?, dateFormat: String): Boolean {

        val sdf = SimpleDateFormat(dateFormat)

        val date = sdf.parse(dateTime)

        return Date().time > date.time
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
