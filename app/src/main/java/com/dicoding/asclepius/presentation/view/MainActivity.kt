package com.dicoding.asclepius.presentation.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.presentation.viewmodel.AnalyzeResultsViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
	private lateinit var binding: ActivityMainBinding

	@Suppress("unused")
	private val _analyzeResultsVm: AnalyzeResultsViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		binding = ActivityMainBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)

		ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { layout, insets ->
			val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			layout.updatePadding(
				top = systemBarsInsets.top,
				bottom = systemBarsInsets.bottom
			)
			WindowInsetsCompat.CONSUMED
		}

		binding.viewPager.adapter = MainPagerAdapter(this)
		TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
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