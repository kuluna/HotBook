package jp.kuluna.hotbook

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import jp.kuluna.hotbook.databinding.ActivityEntryBinding
import jp.kuluna.hotbook.models.AppPreference
import jp.kuluna.hotbook.viewmodels.EntryViewModel

class EntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntryBinding
    private lateinit var viewModel: EntryViewModel

    private lateinit var bookmarkFragment: BookmarkListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry)
        viewModel = ViewModelProviders.of(this).get(EntryViewModel::class.java)
        binding.viewModel = viewModel

        intent.getStringExtra("host")?.let {
            title = it
        }

        intent.getStringExtra("url")?.let { url ->
            viewModel.url.set(url)
            // WebView setting
            binding.webView.let {
                setupWebViewClient(it, url)
                it.loadUrl(url)
            }

            // setup comment fragment
            val ft = supportFragmentManager.beginTransaction().apply {
                bookmarkFragment = BookmarkListFragment.new(url)
                add(R.id.fragmentContainer, bookmarkFragment, "url")
                hide(bookmarkFragment)
            }
            ft.commit()

        } ?: run {
            Toast.makeText(this, R.string.error_url_not_found, Toast.LENGTH_SHORT).show()
            finish()
        }

        // コメントの表示、非表示の切り替え
        viewModel.showComment.observe(this, Observer {
            val ft = supportFragmentManager.beginTransaction().apply {
                if (it == true) {
                    show(bookmarkFragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

                } else {
                    hide(bookmarkFragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                }
            }
            ft.commit()
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebViewClient(view: WebView, url: String) {
        view.webViewClient = WebViewClient()
        view.webChromeClient = webChromeClient

        // JSブロックリストを見てJavaScriptを有効にするか決める
        val setting = view.settings
        setting.javaScriptEnabled = !AppPreference(this).blockJsHosts.any { url.contains(it) }
    }

    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            viewModel.progress.set(newProgress)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_entry, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuComment -> {
                val change = viewModel.showComment.value?.let { !it } ?: run { true }
                viewModel.showComment.postValue(change)
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
        if (keyCode == KeyEvent.KEYCODE_BACK && viewModel.showComment.value == true) {
            viewModel.showComment.postValue(false)
            return false
        }
        return super.onKeyDown(keyCode, event)
    }
}
