package com.dicoding.asclepius.presentation.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.presentation.composables.screens.HistoryScreen
import com.dicoding.asclepius.presentation.composables.theme.AppTheme
import com.dicoding.asclepius.presentation.viewmodel.AnalyzeResultsViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

	private var _binding: FragmentHistoryBinding? = null
	private val binding get() = _binding!!

	private val vm: AnalyzeResultsViewModel by activityViewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				vm.add(AnalyzeResultsViewModel.Event.OnFetch)
			}
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentHistoryBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.composeView.setContent {
			AppTheme {
				HistoryScreen(vm = vm)
			}
		}
	}

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @return A new instance of fragment HistoryFragment.
		 */
		@JvmStatic
		fun newInstance() = HistoryFragment()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}