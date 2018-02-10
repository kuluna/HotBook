package jp.kuluna.hotbook.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.kuluna.hotbook.R

class DarkerFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return View(context).apply { setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorDarker)) }
    }
}
