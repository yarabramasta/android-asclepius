package com.dicoding.asclepius.presentation.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.dicoding.asclepius.databinding.FragmentInsightsBinding
import com.dicoding.asclepius.presentation.composables.screens.InsightsScreen
import com.dicoding.asclepius.presentation.composables.theme.AppTheme
import com.dicoding.asclepius.presentation.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [InsightsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class InsightsFragment : Fragment() {

	private var _binding: FragmentInsightsBinding? = null
	private val binding get() = _binding!!

	private val vm: NewsViewModel by activityViewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				vm.add(NewsViewModel.Event.OnFetch)
			}
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentInsightsBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.composeView.setContent {
			AppTheme {
				InsightsScreen(viewModel = vm)
			}
		}
	}

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @return A new instance of fragment InsightsScreenFragment.
		 */
		@JvmStatic
		fun newInstance() = InsightsFragment()
	}
}