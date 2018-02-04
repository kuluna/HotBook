package jp.kuluna.hotbook.viewmodels

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt

class EntryViewModel(app: Application): AndroidViewModel(app) {
    var url = ObservableField<String>()
    var showComment = ObservableBoolean(false)
    var progress = ObservableInt(0)
}
