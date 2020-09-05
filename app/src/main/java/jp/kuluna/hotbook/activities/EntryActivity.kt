package jp.kuluna.hotbook.activities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.commit
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.databinding.ActivityEntryBinding
import jp.kuluna.hotbook.fragments.BookmarkListFragment
import jp.kuluna.hotbook.fragments.DarkerFragment
import jp.kuluna.hotbook.fragments.WebPageFragment
import jp.kuluna.hotbook.models.AppPreference
import jp.kuluna.hotbook.viewmodels.EntryViewModel

/** Webページとブックマークコメント一覧を表示するActivity。 */
class EntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEntryBinding
    private val viewModel: EntryViewModel by viewModels()

    companion object {
        fun start(context: Context, title: String, url: String) {
            val intent = Intent(context, EntryActivity::class.java).apply {
                putExtra("title", title)
                putExtra("url", url)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // URLがなければ表示できないので戻る
        val url = intent.extras!!.getString("url", "")
        if (url.isEmpty()) {
            Toast.makeText(this, R.string.error_url_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_entry)
        viewModel.url.set(url)

        setUpViews(savedInstanceState, url)
        setUpEvents()
    }

    private fun setUpViews(savedInstanceState: Bundle?, url: String) {
        binding.toolBar.run {
            title = intent.getStringExtra("title")
            subtitle = Uri.parse(intent.getStringExtra("url")!!).host
        }

        // Fragmentの初期設定
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                // Webページを表示するFragmentを追加
                add(binding.webContainer.id, WebPageFragment.new(url), "web")

                // Webページの明るさを落とすためのFragmentを追加
                val darkerFragment = DarkerFragment()
                add(binding.darkerContainer.id, darkerFragment, "darker")
                // 暗くしない設定なら非表示にする
                if (!AppPreference(this@EntryActivity).darker) {
                    hide(darkerFragment)
                }

                // ブコメを表示するFragmentを追加
                val bookmarkFragment = BookmarkListFragment.new(url)
                add(binding.commentContainer.id, bookmarkFragment, "bookmark")
            }
        }
    }

    private fun setUpEvents() {
        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
        binding.toolBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuComment -> {
                    toggleBottomSheet()
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

            true
        }
    }

    private fun toggleBottomSheet() {
        binding.commentContainer.isVisible = binding.commentContainer.isVisible.not()
    }
}
