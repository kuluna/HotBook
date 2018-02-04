package jp.kuluna.hotbook.extensions

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class DataBindingAdapter<E, T : ViewDataBinding>(val context: Context, @LayoutRes private val layoutId: Int) : RecyclerView.Adapter<DataBindingAdapter.DataBindingViewHolder<T>>() {
    var items = emptyList<E>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var listener: OnItemClickListener<E>? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DataBindingViewHolder<T> {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return DataBindingViewHolder(view)
    }

    override fun getItemCount(): Int =items.size

    override fun onBindViewHolder(holder: DataBindingViewHolder<T>, position: Int) {
        val item = items[holder.adapterPosition]
        bind(holder, item)
        holder.binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            listener?.onItemClick(item, holder.adapterPosition)
        }
    }

    abstract fun bind(holder: DataBindingViewHolder<T>, item: E)

    class DataBindingViewHolder<out T : ViewDataBinding>(view: View) : RecyclerView.ViewHolder(view) {
        val binding: T = DataBindingUtil.bind(view)
    }

    interface OnItemClickListener<in E> {
        fun onItemClick(selectedItem: E, position: Int)
    }
}
