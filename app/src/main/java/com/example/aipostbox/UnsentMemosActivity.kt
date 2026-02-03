package com.example.aipostbox

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UnsentMemosActivity : AppCompatActivity() {
    private lateinit var adapter: UnsentMemoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unsent_memos)

        val spinner = findViewById<Spinner>(R.id.aiTargetSpinner)
        val sendAllButton = findViewById<Button>(R.id.sendAllButton)
        val recyclerView = findViewById<RecyclerView>(R.id.unsentList)

        val aiTargets = AiTargetStore.targets
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            aiTargets
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.setSelection(AiTargetStore.selectedIndex(this))
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                AiTargetStore.saveSelectedIndex(this@UnsentMemosActivity, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) = Unit
        }

        adapter = UnsentMemoAdapter(
            MemoRepository.all(),
            onDelete = { memo ->
                MemoRepository.remove(memo)
                refreshList()
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        sendAllButton.setOnClickListener {
            val selection = spinner.selectedItemPosition
            AiTargetStore.saveSelectedIndex(this, selection)
            if (selection == 0) {
                Toast.makeText(this, "AIを選択してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val memos = MemoRepository.all()
            if (memos.isEmpty()) {
                Toast.makeText(this, "未送信メモがありません", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            MemoRepository.clear()
            refreshList()
            Toast.makeText(this, "送信しました: ${aiTargets[selection]}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun refreshList() {
        adapter = UnsentMemoAdapter(
            MemoRepository.all(),
            onDelete = { memo ->
                MemoRepository.remove(memo)
                refreshList()
            }
        )
        val recyclerView = findViewById<RecyclerView>(R.id.unsentList)
        recyclerView.adapter = adapter
    }
}
