package jp.kuluna.hotbook.fragments

import androidx.databinding.DataBindingUtil
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.databinding.FragmentWebPageBinding
import jp.kuluna.hotbook.models.AppPreference

/** Webページを読み込んで表示するFragment。 */
class WebPageFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentWebPageBinding

    private val webChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            binding.progress = newProgress
        }
    }

    companion object {
        /** インスタンスを生成します。
         * @param url 表示するURL
         * @return インスタンス
         */
        fun new(url: String): WebPageFragment {
            val f = WebPageFragment()
            f.arguments = Bundle().apply {
                putString("url", url)
            }
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_web_page, container, false)
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = webChromeClient
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (savedInstanceState != null) {
            binding.webView.restoreState(savedInstanceState)
        } else {
            load()
        }
    }

    /** 読み込みを開始します。 */
    fun load() {
        arguments?.getString("url", null)?.let { url ->
            val enableJs = !AppPreference(context!!).blockJsHosts.any { url.contains(it) }
            binding.webView.settings.run { this.javaScriptEnabled = enableJs }
            binding.webView.loadUrl(url)
        }
    }

    // 以下はWebViewの画面回転の対応に必要です

    override fun onResume() {
        super.onResume()
        binding.webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.webView.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }
}
