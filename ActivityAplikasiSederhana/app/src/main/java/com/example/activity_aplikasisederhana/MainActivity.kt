package com.example.activity_aplikasisederhana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var etLength : EditText
    private lateinit var etWidth : EditText
    private lateinit var etHeight : EditText
    private lateinit var btnCalculate : Button
    private lateinit var tvResult : TextView

    companion object{
        private const val STATE_RESULT = "state_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etLength = findViewById(R.id.et_length)
        etWidth = findViewById(R.id.et_width)
        etHeight = findViewById(R.id.et_height)
        btnCalculate = findViewById(R.id.btn_calculate)
        tvResult = findViewById(R.id.tv_result)

        if (savedInstanceState != null){
            val result = savedInstanceState.getString(STATE_RESULT) as String
            tvResult.text = result
        }

        btnCalculate.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_calculate){
            val inputLength = etLength.text.toString().trim()
            val inputWidth = etWidth.text.toString().trim()
            val inputHeight = etHeight.text.toString().trim()

            var isEmptyField = false
            if (inputLength.isEmpty()){
                isEmptyField = true
                etLength.error = "Field ini tidak boleh kosong"
            }
            if (inputWidth.isEmpty()){
                isEmptyField = true
                etWidth.error = "Field ini tidak boleh kosong"
            }
            if (inputHeight.isEmpty()){
                isEmptyField = true
                etHeight.error = "Field ini tidak boleh kosong"
            }

            if (!isEmptyField){
                val volume = inputLength.toDouble() * inputWidth.toDouble() * inputHeight.toDouble()
                tvResult.text = volume.toString()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_RESULT, tvResult.text.toString())
    }
}
