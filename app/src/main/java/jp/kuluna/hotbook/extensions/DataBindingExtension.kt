package jp.kuluna.hotbook.extensions

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.squareup.picasso.Picasso
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.models.EntryImage

@BindingAdapter("app:loadImage")
fun loadImageAsync(imageView: ImageView, image: EntryImage?) {
    image?.let {
        Picasso.with(imageView.context).load(it.url).into(imageView)
    }
}
