package com.example.aipostbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var suppressAutoFocus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputField = findViewById<EditText>(R.id.inputField)
        val addButton = findViewById<Button>(R.id.addMemoButton)
        val unsentButton = findViewById<Button>(R.id.unsentListButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        addButton.setOnClickListener {
            val text = inputField.text.toString().trim()
            if (text.isBlank()) {
                Toast.makeText(this, "メモを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            MemoRepository.add(text)
            inputField.text.clear()
            inputField.requestFocus()
            showKeyboard(inputField)
        }

        unsentButton.setOnClickListener {
            startActivity(Intent(this, UnsentMemosActivity::class.java))
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        suppressAutoFocus = handleShareIntent(intent)
        maybeShowKeyboard(inputField)
    }

    override fun onResume() {
        super.onResume()
        val inputField = findViewById<EditText>(R.id.inputField)
        maybeShowKeyboard(inputField)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        if (handleShareIntent(intent)) {
            suppressAutoFocus = true
        }
    }

    private fun handleShareIntent(intent: Intent?): Boolean {
        if (intent?.action != Intent.ACTION_SEND) {
            return false
        }
        val type = intent.type ?: return false
        if (!type.startsWith("text/")) {
            return false
        }
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)?.trim().orEmpty()
        if (sharedText.isBlank()) {
            return false
        }
        MemoRepository.add(sharedText)
        Toast.makeText(this, "メモを追加しました", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun maybeShowKeyboard(view: EditText) {
        if (suppressAutoFocus) {
            suppressAutoFocus = false
            return
        }
        view.requestFocus()
        view.post {
            showKeyboard(view)
        }
    }

    private fun showKeyboard(view: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}
