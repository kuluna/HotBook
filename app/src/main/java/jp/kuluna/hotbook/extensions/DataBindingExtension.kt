package jp.kuluna.hotbook.extensions

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadImage")
fun ImageView.loadImageAsync(url: String?) {
    url?.let {
        Glide.with(context)
                .load(it)
                .centerCrop()
                .fitCenter()
                .into(this)
    }
}
