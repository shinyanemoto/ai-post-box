package com.example.aipostbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UnsentMemoAdapter(
    private val memos: List<Memo>,
    private val onDelete: (Memo) -> Unit
) : RecyclerView.Adapter<UnsentMemoAdapter.MemoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_unsent_memo, parent, false)
        return MemoViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoViewHolder, position: Int) {
        val memo = memos[position]
        holder.memoText.text = memo.text
        holder.deleteButton.setOnClickListener { onDelete(memo) }
    }

    override fun getItemCount(): Int = memos.size

    class MemoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val memoText: TextView = view.findViewById(R.id.memoText)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }
}
