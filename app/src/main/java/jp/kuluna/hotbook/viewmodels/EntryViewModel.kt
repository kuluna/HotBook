package jp.kuluna.hotbook.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import jp.kuluna.hotbook.BookmarkAdapter
import jp.kuluna.hotbook.models.ApiClient
import jp.kuluna.hotbook.models.RetrofitLiveData

class EntryViewModel(app: Application): AndroidViewModel(app) {
    private val api = ApiClient.hatena

    // Webの読み込み
    var url = ObservableField<String>()
    var showComment = ObservableBoolean(false)
    var progress = ObservableInt(0)

    // コメントの読み込み
    var bookmarksAdapter = BookmarkAdapter(app)

    fun getComments(owner: LifecycleOwner, url: String) {
        RetrofitLiveData(api.getComments(url)).observe(owner, Observer { response ->
            response?.body?.let {
                bookmarksAdapter.items = it.bookmarks.filter { it.comment.isNotEmpty() }
            }
        })
    }
}
