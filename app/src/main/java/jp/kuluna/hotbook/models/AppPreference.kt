package jp.kuluna.hotbook.models

import android.content.Context
import android.preference.PreferenceManager

class AppPreference(context: Context) {
    private val shared = PreferenceManager.getDefaultSharedPreferences(context)

    var openItem: Int
    get() = shared.getInt("openItem", 0)
    set(value) = shared.edit().putInt("openItem", value).apply()

    val blockJsHosts: Set<String>
    get() = shared.getStringSet("blockHosts", emptySet())

    fun addBlock(host: String) {
        val sites = blockJsHosts.toMutableSet()
        sites.add(host)
        shared.edit().putStringSet("blockHosts", sites).apply()
    }
}