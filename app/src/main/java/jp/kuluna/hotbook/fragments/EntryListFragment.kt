package jp.kuluna.hotbook.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.activities.EntryActivity
import jp.kuluna.hotbook.databinding.FragmentEntryListBinding
import jp.kuluna.hotbook.databinding.ListEntryBinding
import jp.kuluna.hotbook.extensions.DataBindingAdapter
import jp.kuluna.hotbook.models.Entry
import jp.kuluna.hotbook.viewmodels.EntryListViewModel

class EntryListFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentEntryListBinding
    private val viewModel: EntryListViewModel by viewModels()

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
        // RecyclerViewのBottomをNavigationBarの高さ分底上げする
        ViewCompat.setOnApplyWindowInsetsListener(binding.recyclerView, OnApplyWindowInsetsListener { v, insets ->
            val bottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            v.updatePadding(bottom = bottomInset)
            return@OnApplyWindowInsetsListener insets
        })

        binding.viewModel = viewModel

        // setup RecyclerView
        binding.recyclerView.layoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(resources.getInteger(R.integer.grid_columns), androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.adapter = viewModel.adapter
        viewModel.adapter.listener = object : DataBindingAdapter.OnItemClickListener<Entry> {
            override fun onItemClick(selectedItem: Entry, position: Int) {
                EntryActivity.start(context!!, selectedItem.title, selectedItem.url)
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
        val category = arguments!!.getString("category")!!
        viewModel.getEntries(this, category) {
            binding.root.postDelayed({ binding.swipeRefresh.isRefreshing = false }, 1000)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.adapter.listener = null
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
