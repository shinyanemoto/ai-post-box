package com.example.aipostbox

object MemoRepository {
    private val memos = mutableListOf<Memo>()

    fun add(text: String) {
        memos.add(Memo(id = System.currentTimeMillis(), text = text))
    }

    fun all(): List<Memo> = memos.toList()

    fun remove(memo: Memo) {
        memos.removeAll { it.id == memo.id }
    }

    fun clear() {
        memos.clear()
    }
}
