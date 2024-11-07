package com.dicoding.asclepius.presentation.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.databinding.FragmentInsightsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [InsightsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InsightsFragment : Fragment() {

	private var _binding: FragmentInsightsBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentInsightsBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
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

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}