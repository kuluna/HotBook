package jp.kuluna.hotbook.activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.fragments.BookmarkListFragment
import jp.kuluna.hotbook.fragments.WebPageFragment
import jp.kuluna.hotbook.models.AppPreference
import jp.kuluna.hotbook.viewmodels.EntryViewModel

class EntryActivity : AppCompatActivity() {
    private lateinit var viewModel: EntryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EntryViewModel::class.java)

        val url = intent.extras.getString("url", "")
        if (url.isEmpty()) {
            Toast.makeText(this, R.string.error_url_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        viewModel.url.set(url)

        // URlをタイトルに設定
        intent.getStringExtra("host")?.let {
            title = it
        }

        // Fragmentの初期設定
        if (savedInstanceState == null) {
            val ft = supportFragmentManager.beginTransaction().apply {
                add(android.R.id.content, WebPageFragment.new(url), "web")

                val bookmarkFragment = BookmarkListFragment.new(url)
                add(android.R.id.content, bookmarkFragment, "bookmark")
                hide(bookmarkFragment)
            }
            ft.commit()
        }

        // コメントの表示、非表示の切り替え
        viewModel.showBookmark.observe(this, switchComment)
    }

    private val switchComment = Observer<Boolean> {
        val ft = supportFragmentManager.beginTransaction().apply {
            val bookmarkFragment = supportFragmentManager.findFragmentByTag("bookmark")
            if (it == true) {
                show(bookmarkFragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

            } else {
                hide(bookmarkFragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
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

            R.id.menuBlockJs -> {
                val host = Uri.parse(viewModel.url.get()).host
                AppPreference(this).addBlock(host)
                Toast.makeText(this, "${host}はJavaScriptを実行しません。", Toast.LENGTH_SHORT).show()
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
