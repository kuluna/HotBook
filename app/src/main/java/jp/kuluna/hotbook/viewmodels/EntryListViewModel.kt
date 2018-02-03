package jp.kuluna.hotbook.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import jp.kuluna.hotbook.EntryListAdapter
import jp.kuluna.hotbook.models.ApiClient
import jp.kuluna.hotbook.models.RetrofitLiveData

class EntryListViewModel(app: Application) : AndroidViewModel(app) {
    private val api = ApiClient.hatena

    var emptyView = ObservableBoolean(false)
    var adapter = EntryListAdapter(app)

    fun getEntries(owner: LifecycleOwner, category: String, done: () -> Unit) {
        RetrofitLiveData(api.getEntries(category)).observe(owner, Observer { response ->
            response?.body?.let {
                adapter.items = it.filter { it.is_pr == 0 }
            }

            response?.error?.let {
                emptyView.set(true)
            }

            done()
        })
    }
}
