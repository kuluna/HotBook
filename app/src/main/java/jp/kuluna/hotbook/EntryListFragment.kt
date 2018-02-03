package jp.kuluna.hotbook

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import jp.kuluna.hotbook.databinding.FragmentEntryListBinding
import jp.kuluna.hotbook.databinding.ListEntryBinding
import jp.kuluna.hotbook.models.Entry
import jp.kuluna.hotbook.viewmodels.EntryListViewModel

class EntryListFragment : Fragment() {

    private lateinit var binding: FragmentEntryListBinding
    private lateinit var viewModel: EntryListViewModel

    companion object {
        fun createInstance(category: String): EntryListFragment {
            val f = EntryListFragment()
            f.arguments = Bundle().apply { putString("category", category) }
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_entry_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EntryListViewModel::class.java)
        binding.viewModel = viewModel

        // setup RecyclerView
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = viewModel.adapter

        // setup swipe to refresh
        binding.swipeRefresh.setOnRefreshListener {
            fetch()
        }

        // start fetch
        binding.swipeRefresh.isRefreshing = true
        fetch()
    }

    private fun fetch() {
        val category = arguments!!.getString("category")
        viewModel.getEntries(this, category) {
            Handler().postDelayed({ binding.swipeRefresh.isRefreshing = false }, 1000)
        }
    }
}

class EntryListAdapter(private val context: Context) : RecyclerView.Adapter<EntryItemHolder>() {
    private var animated = 0

    var items = emptyList<Entry>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EntryItemHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_entry, parent, false)
        return EntryItemHolder(v)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: EntryItemHolder, position: Int) {
        holder.binding.item = items[holder.adapterPosition]
        holder.binding.executePendingBindings()
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EntryActivity::class.java).apply {
                putExtra("url", holder.binding.item!!.url)
            }
            context.startActivity(intent)
        }

        // animation
        if (animated < holder.adapterPosition) {
            val slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            holder.itemView.startAnimation(slideUpAnimation)
            animated = holder.adapterPosition
        }
    }
}

class EntryItemHolder(view: View) : RecyclerView.ViewHolder(view) {
    val binding: ListEntryBinding = DataBindingUtil.bind(view)
}
