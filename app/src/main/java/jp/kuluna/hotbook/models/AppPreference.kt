package jp.kuluna.hotbook.models

import android.content.Context
import android.preference.PreferenceManager

class AppPreference(context: Context) {
    private val shared = PreferenceManager.getDefaultSharedPreferences(context)

    var openItem: Int
    get() = shared.getInt("openItem", 0)
    set(value) = shared.edit().putInt("openItem", value).apply()
}
