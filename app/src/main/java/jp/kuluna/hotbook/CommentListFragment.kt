package jp.kuluna.hotbook

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import jp.kuluna.hotbook.databinding.FragmentCommentListBinding

class CommentListFragment : Fragment() {

    companion object {
        fun new(url: String): CommentListFragment {
            val f = CommentListFragment()
            f.arguments = Bundle().apply {
                putString("url", url)
            }
            return f
        }
    }

    private lateinit var binding: FragmentCommentListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comment_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
}
