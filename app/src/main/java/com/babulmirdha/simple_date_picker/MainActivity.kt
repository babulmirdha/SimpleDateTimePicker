package com.babulmirdha.simple_date_picker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.babulmirdha.simple_date_picker.databinding.ActivityMainBinding
import com.alorferi.date_picker_library.EditTextDateMask
import java.util.Calendar

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var mDobEditTextDateMask: EditTextDateMask? = null

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

        val cal = Calendar.getInstance()
        mDobEditTextDateMask = EditTextDateMask(
            this,
            binding.dobEditText,
            binding.datePickerButton,
            cal.time
        )
    }

}