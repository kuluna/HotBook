package jp.kuluna.hotbook

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import jp.kuluna.hotbook.databinding.FragmentEntryListBinding
import jp.kuluna.hotbook.databinding.ListEntryBinding
import jp.kuluna.hotbook.extensions.DataBindingAdapter
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
        viewModel.adapter.listener = object : DataBindingAdapter.OnItemClickListener<Entry> {
            override fun onItemClick(selectedItem: Entry, position: Int) {
                val intent = Intent(context, EntryActivity::class.java).apply {
                    putExtra("title", selectedItem.title)
                    putExtra("url", selectedItem.url)
                }
                startActivity(intent)
            }
        }

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

class EntryListAdapter(context: Context) : DataBindingAdapter<Entry, ListEntryBinding>(context, R.layout.list_entry) {
    private var animated = 0

    override fun onBindViewHolder(holder: DataBindingViewHolder<ListEntryBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        // animation
        if (animated < holder.adapterPosition) {
            val slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
            holder.itemView.startAnimation(slideUpAnimation)
            animated = holder.adapterPosition
        }
    }

    override fun bind(holder: DataBindingViewHolder<ListEntryBinding>, item: Entry) {
        holder.binding.item = item
    }
}
