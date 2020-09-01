package jp.kuluna.hotbook.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.databinding.ActivityMainBinding
import jp.kuluna.hotbook.fragments.EntryListFragment
import jp.kuluna.hotbook.models.AppPreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // Viewの領域をEdgeToEdgeにする
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding.viewPager.adapter = MainPagerAdapter(this)
        // タブとViewPagerを連動させる
        val categories = resources.getStringArray(R.array.tab_title)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()
        // 最後に表示していたカテゴリに移動する
        binding.viewPager.currentItem = AppPreference(this).openItem.let {
            // 範囲外のポジションの場合は最初に戻す
            if (categories.size <= it) 0 else it
        }

        // ActionBarにメニューを追加
        binding.toolBar.setOnMenuItemClickListener {
            startActivity(Intent(this, AppPreferenceActivity::class.java))
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppPreference(this).openItem = binding.viewPager.currentItem
    }
}

class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    private val rssPaths = activity.resources.getStringArray(R.array.categories)

    override fun getItemCount(): Int = rssPaths.size
    override fun createFragment(position: Int): Fragment = EntryListFragment.createInstance(rssPaths[position])
}
