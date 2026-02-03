package com.example.aipostbox

import android.content.Context

object AiTargetStore {
    private const val PREFS_NAME = "ai_target_prefs"
    private const val KEY_SELECTED_INDEX = "selected_index"

    val targets: List<String> = listOf(
        "選択してください",
        "ChatGPT",
        "Claude",
        "Gemini",
        "その他"
    )

    fun selectedIndex(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val index = prefs.getInt(KEY_SELECTED_INDEX, 0)
        return index.coerceIn(0, targets.lastIndex)
    }

    fun saveSelectedIndex(context: Context, index: Int) {
        val safeIndex = index.coerceIn(0, targets.lastIndex)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_SELECTED_INDEX, safeIndex).apply()
    }
}
