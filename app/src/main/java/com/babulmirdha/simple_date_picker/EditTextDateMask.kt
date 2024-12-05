package com.ushalab.afcommonlibrary.customviews

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
import com.ushalab.afcommonlibrary.utils.DatePickerDialogFragment
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EditTextDateMask : TextWatcher,
//     DatePickerDialog.OnDateSetListener,
//    DatePickerFragment.OnDatePickerListener,
    DatePickerDialogFragment.OnDatePickerXListener {


    private var mddMMyyyyDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("en"))
    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val mCal = Calendar.getInstance()
    private var mDateEditText: EditText? = null

    constructor(dateEditText: EditText) {
        dateEditText.hint = "DD/MM/YYYY"
        this.mDateEditText = dateEditText
        this.mDateEditText?.addTextChangedListener(this)
//        initViewButtonClick(calendarViewButton)

    }

    constructor(
        fragment: Fragment?,
        dateEditText: EditText,
        datePickerButton: ImageButton?,
        date: Date? = null
    ) {
        dateEditText.hint = "DD/MM/YYYY"
        this.mDateEditText = dateEditText
        this.mDateEditText?.addTextChangedListener(this)
        date?.let {
            this.mDateEditText?.setText(mddMMyyyyDateFormat.format(date))
        }

        datePickerButton?.setOnClickListener {
            showDatePickerDialog(fragment)
        }

    }


    constructor(dateEditText: EditText, dateTimeString: String, currentDateFormat: String) {

        val sdf = SimpleDateFormat(currentDateFormat)
        dateEditText.hint = "DD/MM/YYYY"
        this.mDateEditText = dateEditText
        this.mDateEditText!!.addTextChangedListener(this)
        this.mDateEditText!!.setText(mddMMyyyyDateFormat.format(sdf.parse(dateTimeString)))

    }


    constructor(dateEditText: EditText, date: Date) {

       val editTextDateFormat = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(editTextDateFormat)
        dateEditText.hint = "DD/MM/YYYY"
        this.mDateEditText = dateEditText
        this.mDateEditText!!.addTextChangedListener(this)
        this.mDateEditText!!.setText(mddMMyyyyDateFormat.format(sdf.format(date)))

    }

    constructor(
        activity: AppCompatActivity,
        dateEditText: EditText,
        datePickerButton: ImageButton?,
        date: Date
    ){

        dateEditText.hint = "DD/MM/YYYY"
        this.mDateEditText = dateEditText
        this.mDateEditText?.addTextChangedListener(this)
        date?.let {
            this.mDateEditText?.setText(mddMMyyyyDateFormat.format(date))
        }

        datePickerButton?.setOnClickListener {
            showDatePickerDialog(activity)
        }

    }

    private fun showDatePickerDialog(fragment: Fragment?) {
        val newFragment =
            DatePickerDialogFragment.newInstance(
                getDate()
            )
        newFragment.setDatePickerListener(object : DatePickerDialogFragment.OnDatePickerXListener {
            override fun onPickDate(date: Date?) {
                setDate(date)
            }
        })
        fragment?.let { it1 -> newFragment.show(it1.requireFragmentManager(), "datePicker") }
    }

    private fun showDatePickerDialog(activity: AppCompatActivity) {
        val newFragment =
            DatePickerDialogFragment.newInstance(
                getDate()
            )
        newFragment.setDatePickerListener(object : DatePickerDialogFragment.OnDatePickerXListener {
            override fun onPickDate(date: Date?) {
                setDate(date)
            }
        })
         newFragment.show(activity.supportFragmentManager, "datePicker")
    }

//    fun showDatePickerDialog(activity: AppCompatActivity) {
//        val mDatePickerFragment = DatePickerDialogFragment.newInstance(getDate())
//        mDatePickerFragment.setDatePickerListener(this)
//        mDatePickerFragment.show(activity.supportFragmentManager, "datePicker")
//    }


    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() == current) {
            return
        }

        var clean = s.toString().replace("[^\\d.]|\\.".toRegex(), "")
        val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")

        val cl = clean.length
        var sel = cl
        var i = 2
        while (i <= cl && i < 6) {
            sel++
            i += 2
        }
        //Fix for pressing delete next to a forward slash
        if (clean == cleanC) sel--

        if (clean.length < 8) {
            clean += ddmmyyyy.substring(clean.length)
        } else {
            //This part makes sure that when we finish entering numbers
            //the date is correct, fixing it otherwise
            var day = Integer.parseInt(clean.substring(0, 2))
            var mon = Integer.parseInt(clean.substring(2, 4))
            var year = Integer.parseInt(clean.substring(4, 8))

            mon = if (mon < 1) 1 else if (mon > 12) 12 else mon
            mCal.set(Calendar.MONTH, mon - 1)
            year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
            mCal.set(Calendar.YEAR, year)
            // ^ first set year for the line below to work correctly
            //with leap years - otherwise, date e.g. 29/02/2012
            //would be automatically corrected to 28/02/2012

            day =
                if (day > mCal.getActualMaximum(Calendar.DATE)) mCal.getActualMaximum(Calendar.DATE) else day
            clean = String.format(Locale.ENGLISH, "%02d%02d%02d", day, mon, year)
        }

        clean = String.format(Locale.ENGLISH, "%s/%s/%s", clean.substring(0, 2),
                clean.substring(2, 4),
                clean.substring(4, 8))

        sel = if (sel < 0) 0 else sel
        current = clean
        val sb = SpannableStringBuilder(current)

        if (sel <= current.length) {
            sb.setSpan(ForegroundColorSpan(Color.parseColor("#808080")), sel, current.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        mDateEditText!!.text = sb
        mDateEditText!!.setSelection(if (sel < current.length) sel else current.length)
    }

    override fun afterTextChanged(s: Editable) {

    }

    //    public Date getDateFromEditText() {
    //
    //        try {
    //            return mddMMyyyyDateFormat.parse(mDateEditText.getText().toString());
    //        } catch (ParseException e) {
    //            e.printStackTrace();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
    //
    //        return null;
    //    }

    fun getFormattedDate(dateFormat: String): String? {
        return getFormattedDate(SimpleDateFormat(dateFormat, Locale("en")))
    }

    fun getDate(): Date? {
        try {
            return mddMMyyyyDateFormat.parse(mDateEditText!!.text.toString())
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getFormattedDate(simpleDateFormat: SimpleDateFormat): String? {
        return try {
            simpleDateFormat.format(getDate())
        } catch (e: Exception) {
            null
        }

    }

//    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
//
//        val calendar = Calendar.getInstance()
//        calendar.set(year, month, dayOfMonth)
//
//
//        // set selected date into textview
//        mDateEditText!!.setText(mddMMyyyyDateFormat.format(calendar.time))
//
//    }

    fun setDate(time: Date?) {
        time?.let {
            mCal.time = time
            mDateEditText?.setText(mddMMyyyyDateFormat.format(time))
        }

    }

    fun setDate(timeSting: String?, datePattern: String) {

        try {
            val simpleDateFormat = SimpleDateFormat(datePattern)
            val time = simpleDateFormat.parse(timeSting)
            setDate(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }




//    override fun onDatePick(year: Int, month: Int, day: Int) {
//        onDateSet(null, year, month, day)
//    }

    override fun onPickDate(date: Date?) {

        val cal = Calendar.getInstance()

        cal.time = date

        mDateEditText?.setText(mddMMyyyyDateFormat.format(date))

//        onDateSet(
//            null,
//            cal.get(Calendar.YEAR),
//            cal.get(Calendar.MONTH),
//            cal.get(Calendar.DAY_OF_MONTH)
//        )
    }
}
