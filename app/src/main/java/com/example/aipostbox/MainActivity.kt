package com.example.aipostbox

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputField = findViewById<EditText>(R.id.inputField)
        inputField.requestFocus()
        inputField.post {
            showKeyboard(inputField)
        }
    }

    override fun onResume() {
        super.onResume()
        val inputField = findViewById<EditText>(R.id.inputField)
        inputField.requestFocus()
        inputField.post {
            showKeyboard(inputField)
        }
    }

    private fun showKeyboard(view: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}
