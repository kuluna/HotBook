package jp.kuluna.hotbook.models

import android.content.Context
import android.preference.PreferenceManager

class AppPreference(context: Context) {
    private val shared = PreferenceManager.getDefaultSharedPreferences(context)

    var openItem: Int
    get() = shared.getInt("openItem", 0)
    set(value) = shared.edit().putInt("openItem", value).apply()

    var darker: Boolean
    get() = shared.getBoolean("darker", false)
    set(value) = shared.edit().putBoolean("darker", value).apply()

    val blockJsHosts: Set<String>
    get() = shared.getStringSet("blockHosts", emptySet())

    fun addBlock(host: String) {
        val sites = blockJsHosts.toMutableSet()
        sites.add(host)
        shared.edit().putStringSet("blockHosts", sites).apply()
    }

    fun removeBlock(host: String) {
        val sites = blockJsHosts.toMutableSet()
        sites.remove(host)
        shared.edit().putStringSet("blockHosts", sites).apply()
    }
}
