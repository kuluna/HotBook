package jp.kuluna.hotbook.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.ObservableField
import jp.kuluna.hotbook.models.ApiClient
import jp.kuluna.hotbook.models.Bookmark
import jp.kuluna.hotbook.models.RetrofitLiveData

class EntryViewModel(app: Application) : AndroidViewModel(app) {
    private val api = ApiClient.hatena

    val url = ObservableField<String>()
    val showBookmark = MutableLiveData<Boolean>()
    val bookmarks = MutableLiveData<List<Bookmark>>()

    fun getBookmarks(owner: LifecycleOwner, url: String) {
        RetrofitLiveData(api.getComments(url)).observe(owner, Observer { response ->
            response?.body?.let {
                bookmarks.postValue(it.bookmarks?.filter { it.comment.isNotEmpty() })
            }
        })
    }
}
