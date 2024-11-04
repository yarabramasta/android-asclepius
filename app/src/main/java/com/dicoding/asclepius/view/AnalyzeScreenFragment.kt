package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.dicoding.asclepius.R

/**
 * A simple [Fragment] subclass.
 * Use the [AnalyzeScreenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnalyzeScreenFragment : Fragment() {

	private val REQUEST_IMAGE_PICK = 1001
	private val REQUEST_IMAGE_CROP = 1002

	private var currentImageUri: Uri? = null

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_analyze_screen, container, false)
	}

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @return A new instance of fragment TabItemAnalyze.
		 */
		@JvmStatic
		fun newInstance() = AnalyzeScreenFragment()
	}
}