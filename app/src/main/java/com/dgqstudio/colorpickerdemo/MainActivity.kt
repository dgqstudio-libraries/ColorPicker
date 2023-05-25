package com.dgqstudio.colorpickerdemo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dgqstudio.colorpickerdemo.databinding.ActivityMainBinding
import com.dgqstudio.colorpickerdemo.databinding.ColorPickerDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBinding()
    }

    private fun initBinding() {
        binding.activityCardView.setOnClickListener() {
            openActivity()
        }

        binding.dialogCardView.setOnClickListener() {
            openDialog()
        }
    }

    private fun openActivity() {
        val intent = Intent(this, ColorPicker::class.java)
        startActivity(intent)
    }

    private fun openDialog() {
        val dialogBinding =
            ColorPickerDialogBinding.inflate(LayoutInflater.from(this), null, false)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Accept") { dialog, _ ->
                val colorHex = dialogBinding.colorPicker.getSelectedColorHex()

                // Do something with $colorHex
                Toast.makeText(this, "Color: $colorHex", Toast.LENGTH_SHORT).show()

                dialog.dismiss()
            }
            .show()

        val paddingStart = dialog.window?.decorView?.paddingStart ?: 0
        val paddingEnd = dialog.window?.decorView?.paddingEnd ?: 0
        val paddingTop = paddingStart
        dialogBinding.root.setPadding(paddingStart, paddingTop, paddingEnd, 0)
    }
}