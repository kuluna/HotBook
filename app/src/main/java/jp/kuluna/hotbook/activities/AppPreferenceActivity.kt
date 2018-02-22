package jp.kuluna.hotbook.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceFragmentCompat
import jp.kuluna.hotbook.R

/** アプリの設定画面を表示するためのActivity。 */
class AppPreferenceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
                .add(android.R.id.content, AppPreferenceFragment(), "pref")
                .commit()
    }
}

/** 実際の設定画面を表示するFragment。 */
class AppPreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref)
    }
}
