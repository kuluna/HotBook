package jp.kuluna.hotbook.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.analytics.FirebaseAnalytics
import jp.kuluna.hotbook.R
import jp.kuluna.hotbook.databinding.ActivityMainBinding
import jp.kuluna.hotbook.fragments.EntryListFragment
import jp.kuluna.hotbook.models.AppPreference

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val firebase = FirebaseAnalytics.getInstance(this)
        val categories = resources.getStringArray(R.array.tab_title)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewPager.adapter = MainPagerAdapter(supportFragmentManager, this, categories)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        // 最後に表示していたカテゴリに移動する
        binding.viewPager.currentItem = AppPreference(this).openItem.let {
            // 範囲外のポジションの場合は最初に戻す
            if (categories.size <= it) 0 else it
        }

        binding.viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // 表示しているカテゴリをAnalyticsに上げる
                val bundle = Bundle().apply {
                    putString("category", categories[position])
                }
                firebase.logEvent("change_category", bundle)
            }

            override fun onPageSelected(position: Int) {}
        })

        // ActionBarにメニューを追加
        binding.toolBar.inflateMenu(R.menu.activity_main)
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

class MainPagerAdapter(fm: androidx.fragment.app.FragmentManager, context: Context, private val titles: Array<String>) : androidx.fragment.app.FragmentPagerAdapter(fm) {
    private val rssPaths = context.resources.getStringArray(R.array.categories)

    override fun getCount(): Int = titles.size

    override fun getItem(position: Int): androidx.fragment.app.Fragment = EntryListFragment.createInstance(rssPaths[position])

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}
