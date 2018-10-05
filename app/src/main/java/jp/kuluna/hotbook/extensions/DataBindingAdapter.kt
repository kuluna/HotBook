package jp.kuluna.hotbook.extensions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class DataBindingAdapter<E, T : ViewDataBinding>(val context: Context, @LayoutRes private val layoutId: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<DataBindingAdapter.DataBindingViewHolder<T>>() {
    var items = emptyList<E>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var listener: OnItemClickListener<E>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<T> {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return DataBindingViewHolder(view)
    }

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        val item = items[holder.layoutPosition]
        bind(holder, item)
        holder.binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            listener?.onItemClick(item, holder.layoutPosition)
        }
    }

    abstract fun bind(holder: DataBindingViewHolder<T>, item: E)

    class DataBindingViewHolder<out T : ViewDataBinding>(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val binding: T = DataBindingUtil.bind(view)!!
    }

    interface OnItemClickListener<in E> {
        fun onItemClick(selectedItem: E, position: Int)
    }
}
