package jp.kuluna.hotbook.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.databinding.ObservableBoolean
import jp.kuluna.hotbook.fragments.EntryListAdapter
import jp.kuluna.hotbook.models.ApiClient
import jp.kuluna.hotbook.models.RetrofitLiveData

class EntryListViewModel(app: Application) : AndroidViewModel(app) {
    private val api = ApiClient.hatena

    val emptyView = ObservableBoolean(false)
    val adapter = EntryListAdapter(app)

    fun getEntries(owner: LifecycleOwner, category: String, done: () -> Unit) {
        RetrofitLiveData(api.getEntries(category)).observe(owner) { response ->
            response?.body?.let {
                adapter.items = it
                emptyView.set(false)
            }

            response?.error?.let {
                emptyView.set(true)
            }

            done()
        }
    }
}
