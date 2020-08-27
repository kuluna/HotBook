package jp.kuluna.hotbook.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.fragments.BookmarkListFragment
import jp.kuluna.hotbook.fragments.DarkerFragment
import jp.kuluna.hotbook.fragments.WebPageFragment
import jp.kuluna.hotbook.models.AppPreference
import jp.kuluna.hotbook.viewmodels.EntryViewModel

/** Webページとブックマークコメント一覧を表示するActivity。 */
class EntryActivity : AppCompatActivity() {
    private val viewModel: EntryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // URLがなければ表示できないので戻る
        val url = intent.extras!!.getString("url", "")
        if (url.isEmpty()) {
            Toast.makeText(this, R.string.error_url_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        viewModel.url.set(url)

        // URLをタイトルに設定
        intent.getStringExtra("host")?.let {
            title = it
        }

        // Fragmentの初期設定
        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction().apply {
                // Webページを表示するFragmentを追加
                add(android.R.id.content, WebPageFragment.new(url), "web")

                // Webページの明るさを落とすためのFragmentを追加
                val darkerFragment = DarkerFragment()
                add(android.R.id.content, darkerFragment, "darker")
                // 暗くしない設定なら非表示にする
                if (!AppPreference(this@EntryActivity).darker) {
                    hide(darkerFragment)
                }

                // ブコメを表示するFragmentを追加(最初は非表示)
                val bookmarkFragment = BookmarkListFragment.new(url)
                add(android.R.id.content, bookmarkFragment, "bookmark")
                hide(bookmarkFragment)
            }
            ft.commit()
        }

        // コメントの表示、非表示の切り替え
        viewModel.showBookmark.observe(this, switchComment)
    }

    /** コメント一覧の表示・非表示を切り替える */
    private val switchComment = Observer<Boolean> {
        val ft = supportFragmentManager.beginTransaction().apply {
            val bookmarkFragment = supportFragmentManager.findFragmentByTag("bookmark")!!
            if (it == true) {
                show(bookmarkFragment)
                setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

            } else {
                hide(bookmarkFragment)
                setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            }
        }
        ft.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_entry, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuComment -> {
                val change = viewModel.showBookmark.value?.let { !it } ?: run { true }
                viewModel.showBookmark.postValue(change)
            }

            R.id.menuReload -> {
                // Webを再読み込み
                (supportFragmentManager.findFragmentByTag("web") as WebPageFragment).load()
            }

            R.id.menuBlockJs -> {
                val host = Uri.parse(viewModel.url.get()).host!!
                AppPreference(this).addBlock(host)
                Toast.makeText(this, "${host}はJavaScriptを実行しません。", Toast.LENGTH_SHORT).show()

                // Webを再読み込み
                (supportFragmentManager.findFragmentByTag("web") as WebPageFragment).load()
            }

            R.id.menuBrightness -> {
                // 表示/非表示を切り替える
                val pref = AppPreference(this@EntryActivity)
                val ft = supportFragmentManager.beginTransaction().apply {
                    val f = supportFragmentManager.findFragmentByTag("darker")!!
                    if (pref.darker) hide(f) else show(f)
                    setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                }
                ft.commit()
                pref.darker = !pref.darker
            }

            R.id.menuShare -> {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, viewModel.url.get())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, getString(R.string.share_url)))
            }

            R.id.menuOpenBrowser -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(viewModel.url.get())))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // コメントを表示している場合はBackキーで非表示にする
        if (keyCode == KeyEvent.KEYCODE_BACK && viewModel.showBookmark.value == true) {
            viewModel.showBookmark.postValue(false)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}
