package com.example.aipostbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val memos = mutableListOf<Memo>()
    private lateinit var adapter: UnsentMemoAdapter
    private var suppressAutoFocus = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputField = findViewById<EditText>(R.id.inputField)
        val addButton = findViewById<Button>(R.id.addMemoButton)
        val recyclerView = findViewById<RecyclerView>(R.id.unsentList)

        adapter = UnsentMemoAdapter(
            memos,
            onSend = { memo ->
                removeMemo(memo)
                Toast.makeText(this, "送信しました", Toast.LENGTH_SHORT).show()
            },
            onDelete = { memo ->
                removeMemo(memo)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            val text = inputField.text.toString().trim()
            if (text.isBlank()) {
                Toast.makeText(this, "メモを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val memo = Memo(id = System.currentTimeMillis(), text = text)
            memos.add(memo)
            adapter.notifyItemInserted(memos.size - 1)
            inputField.text.clear()
            inputField.requestFocus()
            showKeyboard(inputField)
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

    private fun removeMemo(memo: Memo) {
        val index = memos.indexOfFirst { it.id == memo.id }
        if (index != -1) {
            memos.removeAt(index)
            adapter.notifyItemRemoved(index)
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
        val memo = Memo(id = System.currentTimeMillis(), text = sharedText)
        memos.add(memo)
        adapter.notifyItemInserted(memos.size - 1)
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

data class Memo(
    val id: Long,
    val text: String
)
