package jp.kuluna.hotbook.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.util.Log
import jp.kuluna.hotbook.EntryListAdapter
import jp.kuluna.hotbook.models.ApiClient
import jp.kuluna.hotbook.models.Entry
import jp.kuluna.hotbook.models.RetrofitLiveData

class EntryListViewModel(app: Application) : AndroidViewModel(app) {
    private val api = ApiClient.hatena

    var adapter = EntryListAdapter(app)

    fun getEntries(owner: LifecycleOwner, category: String, result: (List<Entry>) -> Unit) {
        RetrofitLiveData(api.getEntries(category)).observe(owner, Observer {
            it?.body?.let { result(it) }
        })
    }

    override fun onCleared() {
        Log.d(EntryListViewModel::class.java.simpleName, "Clear")
    }
}
