package jp.kuluna.hotbook.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.databinding.FragmentBookmarkListBinding
import jp.kuluna.hotbook.databinding.ListBookmarkBinding
import jp.kuluna.hotbook.extensions.DataBindingAdapter
import jp.kuluna.hotbook.models.Bookmark
import jp.kuluna.hotbook.viewmodels.EntryViewModel

class BookmarkListFragment : Fragment() {

    companion object {
        fun new(url: String): BookmarkListFragment {
            val f = BookmarkListFragment()
            f.arguments = Bundle().apply {
                putString("url", url)
            }
            return f
        }
    }

    private lateinit var binding: FragmentBookmarkListBinding
    private lateinit var viewModel: EntryViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(activity!!).get(EntryViewModel::class.java)

        val bookmarksAdapter = BookmarkAdapter(context!!)
        // setup RecyclerView
        binding.recyclerView.run {
            layoutManager = LinearLayoutManager(context)
            adapter = bookmarksAdapter
        }

        // load comments
        viewModel.getBookmarks(this, arguments!!.getString("url"))

        // on loaded
        viewModel.bookmarks.observe(this, Observer {
            binding.loaded = true
            it?.let {
                bookmarksAdapter.items = it
                binding.empty = it.isEmpty()
            }
        })
    }
}

class BookmarkAdapter(context: Context) : DataBindingAdapter<Bookmark, ListBookmarkBinding>(context, R.layout.list_bookmark) {
    override fun bind(holder: DataBindingViewHolder<ListBookmarkBinding>, item: Bookmark) {
        holder.binding.bookmark = item
    }
}
