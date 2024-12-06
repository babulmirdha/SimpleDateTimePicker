package com.alorferi.simple_date_picker_library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.babulmirdha.simple_date_picker_library.databinding.FragmentDialogDatePickerBinding


import java.util.*

class DatePickerDialogFragment : DialogFragment() {

    private var mPlaceholderText: String? = null
    private var mDate: Date? = null
    private var mListener: OnDatePickerListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments

        mDate = bundle?.getSerializable(K_DATE) as? Date
        mPlaceholderText = arguments?.getString(K_PLACEHOLDER)

//        var setFullScreen = false
//        if (arguments != null) {
//            setFullScreen = requireNotNull(arguments?.getBoolean("fullScreen"))
//        }
//        if (setFullScreen)
//            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }


    private lateinit var mBinding: FragmentDialogDatePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDialogDatePickerBinding.inflate(inflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (mPlaceholderText != null)
            mBinding.placeholderTextView.text = mPlaceholderText

        mDate?.let { dtr ->

            val cal = Calendar.getInstance()
            cal.time = dtr

            mBinding.datePicker.updateDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }


        mBinding.cancelButton.setOnClickListener {
            dialog?.dismiss()
        }


        mBinding.okButton.setOnClickListener {

            val dt = getDate()
            mListener?.onPickDate(dt)
            dialog?.dismiss()
        }

    }


    private fun getDate(): Date? {
        return DateTimeUtils.getDateFromDatePicker(mBinding.datePicker)
    }

    fun setDatePickerListener(listener: OnDatePickerListener) {
        mListener = listener
    }

    companion object {
        const val K_DATE = "DATE"
        const val K_PLACEHOLDER = "PLACEHOLDER"
        fun newInstance(date: Date?, placeholder: String? = null): DatePickerDialogFragment {
            val fragment = DatePickerDialogFragment()

            val args = Bundle()
            args.putSerializable(K_DATE, date)
            args.putString(K_PLACEHOLDER, placeholder)
            fragment.arguments = args
            return fragment
        }
    }

    interface OnDatePickerListener {
        fun onPickDate(date: Date?)
    }

}
