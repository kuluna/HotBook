package jp.kuluna.hotbook.extensions

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso

@BindingAdapter("loadImage")
fun ImageView.loadImageAsync(url: String?) {
    url?.let {
        Picasso.get().load(it).into(this)
    }
}
