package com.dgqstudio.colorpickerdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dgqstudio.colorpickerdemo.databinding.ActivityColorpickerBinding

class ColorPicker : AppCompatActivity() {
    private lateinit var binding: ActivityColorpickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColorpickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBinding()
    }

    private fun initBinding() {
        binding.getColorButton.setOnClickListener() {
            val colorHex = binding.colorPicker.getSelectedColorHex()
            Toast.makeText(this, "Color: $colorHex", Toast.LENGTH_SHORT).show()
        }
    }
}