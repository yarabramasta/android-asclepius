package com.dicoding.asclepius.presentation.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity.RESULT_CANCELED
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.dicoding.asclepius.databinding.FragmentAnalyzeBinding
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.presentation.viewmodel.AnalyzeViewModel
import com.dicoding.asclepius.presentation.viewmodel.HistoriesViewModel
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.*

/**
 * A simple [Fragment] subclass.
 * Use the [AnalyzeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AnalyzeFragment : Fragment() {

	private var _binding: FragmentAnalyzeBinding? = null
	private val binding get() = _binding!!

	private val analyzeViewModel: AnalyzeViewModel by activityViewModels()
	private val historiesViewModel: HistoriesViewModel by activityViewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				analyzeViewModel.effect.collect {
					when (it) {
						is AnalyzeViewModel.Effect.ShowToast -> showToast(it.message)
					}
				}
			}
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
		val view = binding.root
		return view
	}

	private lateinit var imageClassifierHelper: ImageClassifierHelper

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		imageClassifierHelper = ImageClassifierHelper(
			context = requireContext(),
			classifierListener = object : ImageClassifierHelper.ClassifierListener {
				override fun onError(error: String) {
					activity?.runOnUiThread {
						analyzeViewModel
							.add(AnalyzeViewModel.Event.OnMessage(message = error))
					}
				}

				override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
					activity?.runOnUiThread {
						if (results.isNullOrEmpty()) {
							analyzeViewModel
								.add(AnalyzeViewModel.Event.OnMessage("Uh oh! Something went wrong..."))
						} else {
							analyzeViewModel.add(AnalyzeViewModel.Event.OnClassifierResults(inferenceTime))
							moveToResult(results)
						}
					}
				}
			}
		)

		binding.galleryButton.setOnClickListener { startGallery() }
		binding.analyzeButton.setOnClickListener { analyzeImage() }
		binding.cancelButton.setOnClickListener { cancelAnalyze() }

		lifecycleScope.launch {
			analyzeViewModel.state
				.flowWithLifecycle(this@AnalyzeFragment.lifecycle)
				.collectLatest {
					binding.previewImageView.setImageURI(it.currentPreviewImageUri)

					if (it.currentPreviewImageUri != null) {
						binding.previewImageView.visibility = View.VISIBLE
						binding.analyzeButton.visibility = View.VISIBLE
						binding.cancelButton.visibility = View.VISIBLE
						binding.mediaPickerLayout.visibility = View.GONE
					}
				}
		}
	}

	companion object {
		/**
		 * Use this factory method to create a new instance of
		 * this fragment using the provided parameters.
		 *
		 * @return A new instance of fragment TabItemAnalyze.
		 */
		@JvmStatic
		fun newInstance() = AnalyzeFragment()
	}

	private val pickMedia = registerForActivityResult(
		ActivityResultContracts.PickVisualMedia()
	) { uri ->
		if (uri != null) {
			analyzeViewModel.add(AnalyzeViewModel.Event.OnImagePreviewChanged(uri))
			startCrop(uri)
		} else {
			analyzeViewModel.add(AnalyzeViewModel.Event.OnMessage("Image not found..."))
		}
	}

	private val cropImage = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		val uri = when (result.resultCode) {
			RESULT_OK -> UCrop.getOutput(result.data!!)

			RESULT_CANCELED -> {
				analyzeViewModel.add(AnalyzeViewModel.Event.OnMessage("Image picked without crop."))
				analyzeViewModel.state.value.currentPreviewImageUri
			}

			else -> {
				analyzeViewModel.add(AnalyzeViewModel.Event.OnMessage("Failed to crop image..."))
				null
			}
		}

		uri?.let {
			analyzeViewModel.add(AnalyzeViewModel.Event.OnImagePreviewChanged(it))
		}
	}

	private fun startCrop(uri: Uri) {
		try {
			val destinationUri = getDestinationUri()
			val uCrop = UCrop.of(uri, destinationUri)
				.withOptions(
					UCrop.Options().apply {
						setCompressionQuality(90)
						setHideBottomControls(false)
						setFreeStyleCropEnabled(false)
						setShowCropGrid(false)
					}
				)
				.withAspectRatio(1f, 1f)
				.withMaxResultSize(1024, 1024)

			cropImage.launch(uCrop.getIntent(requireContext()))
		} catch (e: Exception) {
			analyzeViewModel.add(AnalyzeViewModel.Event.OnMessage("Failed to crop image..."))
		}
	}

	private fun startGallery() {
		binding.progressIndicator.visibility = View.VISIBLE
		pickMedia.launch(
			PickVisualMediaRequest(
				ActivityResultContracts.PickVisualMedia.ImageOnly
			)
		)
		binding.progressIndicator.visibility = View.GONE
	}

	private fun analyzeImage() {
		binding.analyzeButton.isEnabled = false
		binding.cancelButton.isEnabled = false
		binding.galleryButton.isClickable = false
		binding.progressIndicator.visibility = View.VISIBLE

		val imageUri = analyzeViewModel.state.value.currentPreviewImageUri
		if (imageUri != null) {
			imageClassifierHelper.classifyStaticImage(imageUri)
		} else {
			analyzeViewModel.add(AnalyzeViewModel.Event.OnMessage("Cannot analyze empty image..."))
		}
	}

	private fun cancelAnalyze() = resetViewsState()

	private fun moveToResult(results: List<Classifications>) {
		results.let { classification ->
			if (classification.isNotEmpty() && classification[0].categories.isNotEmpty()) {
				val label = classification[0].categories[0].label.trim()
				val score = classification[0].categories[0].score

				val intent = Intent(requireContext(), ResultActivity::class.java)

				val imageUri = analyzeViewModel.state.value.currentPreviewImageUri
				if (imageUri != null) {
					val result = AnalyzeResult(
						imageUri = imageUri,
						label = label,
						confidenceScore = score,
					)
					historiesViewModel.add(HistoriesViewModel.Event.OnSaved(result))
					intent.putExtra("analyze_result", result)
				}

				resetViewsState()
				startActivity(intent)
			} else {
				analyzeViewModel.add(AnalyzeViewModel.Event.OnMessage("No classification categories found"))
			}
		}
	}

	private fun showToast(message: String) {
		Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
	}

	private fun getDestinationUri(): Uri {
		val imageUri = analyzeViewModel.state.value.currentPreviewImageUri
			?: throw IllegalStateException("Current image uri is null")

		val fileName = "asclepius_analyze_${System.currentTimeMillis()}.jpg"
		val file = File(requireContext().filesDir, fileName)

		val resolver = requireContext().contentResolver
		val inputStream: InputStream? = resolver.openInputStream(imageUri)
		inputStream?.use { input ->
			FileOutputStream(file).use { output ->
				input.copyTo(output)
			}
		}

		return Uri.fromFile(file)
	}

	private fun resetViewsState() {
		binding.previewImageView.visibility = View.GONE
		binding.previewImageView.setImageURI(null)
		analyzeViewModel.add(AnalyzeViewModel.Event.OnImagePreviewChanged(null))

		binding.analyzeButton.visibility = View.GONE
		binding.analyzeButton.isEnabled = true
		binding.cancelButton.visibility = View.GONE
		binding.cancelButton.isEnabled = true
		binding.galleryButton.isClickable = true

		binding.mediaPickerLayout.visibility = View.VISIBLE
		binding.progressIndicator.visibility = View.GONE
	}
}
