package com.babulmirdha.simple_date_picker_library

import android.graphics.Color
import android.text.Editable
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateMaskEditText : TextWatcher, DatePickerDialogFragment.OnDatePickerListener {

    private var mDatePickerTitle: String? = null
    private val mddMMyyyyDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val mCal = Calendar.getInstance()
    private var mDateEditText: EditText? = null

    private var mPickDateListener: DatePickerDialogFragment.OnDatePickerListener? = null

    fun setOnDatePickListener(listener: DatePickerDialogFragment.OnDatePickerListener) {
        mPickDateListener = listener
    }

    constructor(dateEditText: EditText) {
        initEditText(dateEditText)
    }

    constructor(
        fragment: Fragment?,
        dateEditText: EditText,
        datePickerButton: ImageButton?,
        date: Date? = null
    ) {
        initEditText(dateEditText, date, fragment, datePickerButton)
    }

    constructor(dateEditText: EditText, dateTimeString: String, currentDateFormat: String) {
        val sdf = SimpleDateFormat(currentDateFormat, Locale.ENGLISH)
        initEditText(dateEditText, sdf.parse(dateTimeString))
    }

    constructor(dateEditText: EditText, date: Date) {
        initEditText(dateEditText, date)
    }

    constructor(
        activity: AppCompatActivity,
        dateEditText: EditText,
        datePickerButton: ImageButton?,
        date: Date,
        datePickerTitle:String? = null
    ) {
        initEditText(dateEditText, date, activity, datePickerButton, datePickerTitle)
    }

    private fun initEditText(
        dateEditText: EditText,
        date: Date? = null,
        fragmentOrActivity: Any? = null,
        datePickerButton: ImageButton? = null,
        datePickerTitle:String? = null
    ) {
        dateEditText.hint = "DD/MM/YYYY"
        this.mDateEditText = dateEditText
        this.mDateEditText?.addTextChangedListener(this)
        date?.let { this.mDateEditText?.setText(mddMMyyyyDateFormat.format(date)) }

        this.mDatePickerTitle = datePickerTitle

        datePickerButton?.setOnClickListener {
            when (fragmentOrActivity) {
                is Fragment -> showDatePickerDialog(fragmentOrActivity)
                is AppCompatActivity -> showDatePickerDialog(fragmentOrActivity)
            }
        }
    }

    private fun showDatePickerDialog(fragment: Fragment) {
        DatePickerDialogFragment.newInstance(getDate(), mDatePickerTitle).apply {
            setDatePickerListener(this@DateMaskEditText)
            show(fragment.requireFragmentManager(), "datePicker")
        }
    }

    private fun showDatePickerDialog(activity: AppCompatActivity) {
        val hint = mDateEditText?.hint.toString()
        DatePickerDialogFragment.newInstance(getDate(), mDatePickerTitle).apply {
            setDatePickerListener(this@DateMaskEditText)
            show(activity.supportFragmentManager, "datePicker")
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() == current) return

        var clean = s.toString().replace("[^\\d]".toRegex(), "")
        val cleanC = current.replace("[^\\d]".toRegex(), "")

        val cl = clean.length
        var sel = cl
        var i = 2
        while (i <= cl && i < 6) {
            sel++
            i += 2
        }
        if (clean == cleanC) sel--

        clean = if (clean.length < 8) {
            clean + ddmmyyyy.substring(clean.length)
        } else {
            val dtString = validateDate(clean)
            mPickDateListener?.onPickDate(getDate())
            dtString
        }

        current = formatCleanString(clean)
        val sb = SpannableStringBuilder(current)

        if (sel <= current.length) {
            sb.setSpan(
                ForegroundColorSpan(Color.parseColor("#808080")),
                sel,
                current.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        mDateEditText?.let {
            it.text = sb
            it.setSelection(sel.coerceAtMost(current.length))
        }
    }

    override fun afterTextChanged(s: Editable) {}

    private fun validateDate(clean: String): String {
        var day = clean.substring(0, 2).toInt()
        var mon = clean.substring(2, 4).toInt()
        var year = clean.substring(4, 8).toInt()

        mon = mon.coerceIn(1, 12)
        year = year.coerceIn(1900, 2100)
        mCal.set(Calendar.YEAR, year)
        mCal.set(Calendar.MONTH, mon - 1)
        day = day.coerceAtMost(mCal.getActualMaximum(Calendar.DATE))

        return String.format(Locale.ENGLISH, "%02d%02d%04d", day, mon, year)
    }

    private fun formatCleanString(clean: String): String {
        return String.format(
            Locale.ENGLISH,
            "%s/%s/%s",
            clean.substring(0, 2),
            clean.substring(2, 4),
            clean.substring(4, 8)
        )
    }

    fun getDate(): Date? {
        return try {
            mddMMyyyyDateFormat.parse(mDateEditText?.text.toString())
        } catch (e: Exception) {
            null
        }
    }

    fun setDate(date: Date?) {
        date?.let { mDateEditText?.setText(mddMMyyyyDateFormat.format(it)) }
    }

    override fun onPickDate(date: Date?) {
        setDate(date)
        mPickDateListener?.onPickDate(date)
    }
}
