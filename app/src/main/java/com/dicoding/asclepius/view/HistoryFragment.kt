package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.databinding.FragmentHistoryBinding

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {

	private var _binding: FragmentHistoryBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentHistoryBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
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