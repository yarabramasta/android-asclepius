package com.dicoding.asclepius.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)

		binding.viewPager.adapter = MainPagerAdapter(this)
		TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
			tab.text = when (position) {
				0 -> getString(R.string.analyze)
				1 -> getString(R.string.history)
				2 -> getString(R.string.insights)
				else -> null
			}
			tab.icon = when (position) {
				0 -> AppCompatResources.getDrawable(this, R.drawable.ic_emergency_24dp)
				1 -> AppCompatResources.getDrawable(this, R.drawable.ic_image_24dp)
				2 -> AppCompatResources.getDrawable(this, R.drawable.ic_newspaper_24dp)
				else -> null
			}
		}.attach()
	}

	private inner class MainPagerAdapter(activity: AppCompatActivity) :
		FragmentStateAdapter(activity) {

		override fun getItemCount(): Int = 3

		override fun createFragment(position: Int): Fragment {
			return when (position) {
				0 -> AnalyzeFragment.newInstance()
				1 -> HistoryFragment.newInstance()
				2 -> InsightsFragment.newInstance()
				else -> throw IllegalArgumentException("Invalid position")
			}
		}
	}
}