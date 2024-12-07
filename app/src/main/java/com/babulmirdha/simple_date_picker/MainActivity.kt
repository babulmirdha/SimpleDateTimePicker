package com.babulmirdha.simple_date_picker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.babulmirdha.simple_date_picker.databinding.ActivityMainBinding
import com.babulmirdha.simple_date_picker_library.DateMaskEditText
import com.babulmirdha.simple_date_picker_library.DatePickerDialogFragment
import com.babulmirdha.simple_date_picker_library.DateTimeUtils
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity(), DatePickerDialogFragment.OnDatePickerListener {

    private lateinit var mCal: Calendar
    private lateinit var mDob2EditTextDateMask: DateMaskEditText
    private lateinit var binding: ActivityMainBinding
    private var mDob1EditTextDateMask: DateMaskEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mCal = Calendar.getInstance()
        mDob1EditTextDateMask = DateMaskEditText(
            this,
            binding.dobEditText,
            binding.dob1PickerButton,
            mCal.time,
            getString(R.string.select_date)
        )

        mDob1EditTextDateMask?.setOnDatePickListener(this)

        mDob2EditTextDateMask = DateMaskEditText(
            this,
            binding.dob2TextView,
            binding.dob2PickerButton,
            mCal.time,
            getString(R.string.select_date_2)
        )

        mDob2EditTextDateMask.setOnDatePickListener(this)
    }

    override fun onPickDate(date: Date?) {
        binding.resultTextView.text = DateTimeUtils.getDifference(this@MainActivity, date, mCal.time)
    }

}