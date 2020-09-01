package jp.kuluna.hotbook.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import jp.kuluna.hotbook.models.AppPreference

/** JavaScriptをブロックするURL一覧を表示するActivity。 */
class BlockJavaScriptListActivity : AppCompatActivity(), DeleteConfirmDialogFragment.Callback {
    private lateinit var listView: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listView = ListView(this)
        setContentView(listView)
        // 戻るボタンを表示
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // リストを表示
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        adapter.addAll(AppPreference(this).blockJsHosts)
        listView.adapter = adapter
        listView.setOnItemClickListener { adapterView, _, position, _ ->
            val url = adapterView.getItemAtPosition(position) as String
            DeleteConfirmDialogFragment.new(url).show(supportFragmentManager, "dialog")
        }
    }

    override fun onRemoveBlockUrl(url: String) {
        adapter.remove(url)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}

/** 削除の確認を行うダイアログ */
class DeleteConfirmDialogFragment : DialogFragment() {
    companion object {
        /**
         * インスタンスを生成します。
         * @param url URL
         */
        fun new(url: String): DeleteConfirmDialogFragment {
            val f = DeleteConfirmDialogFragment()
            f.arguments = Bundle().apply {
                putString("url", url)
            }
            return f
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val url = arguments!!.getString("url")!!
        return AlertDialog.Builder(activity)
                .setTitle("このURLをブロックリストから削除してよろしいですか?")
                .setMessage(url)
                .setPositiveButton("削除") { _, _ ->
                    AppPreference(activity!!).removeBlock(url)
                    (activity as? Callback)?.onRemoveBlockUrl(url)
                }
                .setNegativeButton("キャンセル", null)
                .create()
    }

    interface Callback {
        fun onRemoveBlockUrl(url: String)
    }
}
