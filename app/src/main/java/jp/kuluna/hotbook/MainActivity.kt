package jp.kuluna.hotbook

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import jp.kuluna.hotbook.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.viewPager.adapter = MainPagerAdapter(supportFragmentManager, this)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }
}

class MainPagerAdapter(fm: FragmentManager, context: Context) : FragmentPagerAdapter(fm) {
    private val titles = context.resources.getStringArray(R.array.tab_title)
    private val rssPaths = context.resources.getStringArray(R.array.categories)

    override fun getCount(): Int = titles.size

    override fun getItem(position: Int): Fragment = EntryListFragment.createInstance(rssPaths[position])

    override fun getPageTitle(position: Int): CharSequence? = titles[position]
}
