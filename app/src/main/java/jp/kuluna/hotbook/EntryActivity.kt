package jp.kuluna.hotbook

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebViewClient
import android.widget.Toast
import jp.kuluna.hotbook.databinding.ActivityEntryBinding
import jp.kuluna.hotbook.viewmodels.EntryViewModel

class EntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntryBinding
    private lateinit var viewModel: EntryViewModel

    private lateinit var commentFragment: CommentListFragment

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry)
        viewModel = ViewModelProviders.of(this).get(EntryViewModel::class.java)
        binding.viewModel = viewModel

        intent.getStringExtra("url")?.let { url ->
            viewModel.url.set(url)
            // WebView setting
            binding.webView.let {
                it.webViewClient = WebViewClient()
                val setting = it.settings
                setting.javaScriptEnabled = true
                it.loadUrl(url)
            }

            // setup comment fragment
            val ft = supportFragmentManager.beginTransaction().apply {
                commentFragment = CommentListFragment.new(url)
                add(R.id.fragmentContainer, commentFragment, "url")
            }
            ft.commit()

        } ?: run {
            Toast.makeText(this, "URLが設定されていません。", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_entry, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuComment -> {
                if (viewModel.showComment.get()) {
                    val ft = supportFragmentManager.beginTransaction().apply {
                        hide(commentFragment)
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    }
                    ft.commit()
                } else {
                    val ft = supportFragmentManager.beginTransaction().apply {
                        show(commentFragment)
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    }
                    ft.commit()
                }
                viewModel.showComment.set(!viewModel.showComment.get())
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
}
