package com.dicoding.asclepius.presentation.view

import android.content.Intent
import android.graphics.Color
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
import com.dicoding.asclepius.databinding.FragmentAnalyzeBinding
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.presentation.viewmodel.HistoriesViewModel
import com.google.android.material.color.MaterialColors
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.*

/**
 * A simple [Fragment] subclass.
 * Use the [AnalyzeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AnalyzeFragment : Fragment() {

	private var _binding: FragmentAnalyzeBinding? = null
	private val binding get() = _binding!!

	private val vm: HistoriesViewModel by activityViewModels()

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
						showToast(error)
					}
				}

				override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
					activity?.runOnUiThread {
						if (results.isNullOrEmpty()) {
							showToast("Uh oh! Something went wrong...")
						} else {
							showToast("Successfully analyzed image in $inferenceTime ms")
							moveToResult(results)
						}
					}
				}
			}
		)

		binding.galleryButton.setOnClickListener { startGallery() }
		binding.analyzeButton.setOnClickListener { analyzeImage() }
		binding.cancelButton.setOnClickListener { cancelAnalyze() }
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

	private var currentImageUri: Uri? = null

	private val pickMedia = registerForActivityResult(
		ActivityResultContracts.PickVisualMedia()
	) { uri ->
		if (uri != null) {
			currentImageUri = uri
			startCrop(uri)
		} else {
			showToast("Image not found...")
		}
	}

	private val cropImage = registerForActivityResult(
		ActivityResultContracts.StartActivityForResult()
	) { result ->
		val uri = when (result.resultCode) {
			RESULT_OK -> UCrop.getOutput(result.data!!)

			RESULT_CANCELED -> {
				showToast("Image picked without crop.")
				currentImageUri
			}

			else -> {
				showToast("Failed to crop image...")
				null
			}
		}

		uri?.let {
			currentImageUri = it
			showImage()
		}
	}

	private fun startCrop(uri: Uri) {
		val destinationUri = getDestinationUri()
		val uCrop = UCrop.of(uri, destinationUri)
			.withOptions(
				UCrop.Options().apply {
					setCompressionQuality(90)
					setHideBottomControls(true)
					setFreeStyleCropEnabled(false)
					setToolbarColor(
						MaterialColors.getColor(
							requireActivity() as MainActivity,
							com.google.android.material.R.attr.colorPrimary,
							Color.GREEN
						)
					)
				}
			)
			.withAspectRatio(1f, 1f)
			.withMaxResultSize(1024, 1024)

		cropImage.launch(uCrop.getIntent(requireContext()))
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

	private fun showImage() {
		binding.previewImageView.setImageURI(currentImageUri)
		binding.previewImageView.visibility = View.VISIBLE
		binding.analyzeButton.visibility = View.VISIBLE
		binding.cancelButton.visibility = View.VISIBLE
		binding.mediaPickerLayout.visibility = View.GONE
	}

	private fun analyzeImage() {
		binding.analyzeButton.isEnabled = false
		binding.cancelButton.isEnabled = false
		binding.galleryButton.isClickable = false
		binding.progressIndicator.visibility = View.VISIBLE

		if (currentImageUri != null) {
			imageClassifierHelper.classifyStaticImage(currentImageUri!!)
		} else {
			showToast("Cannot analyze empty image...")
		}
	}

	private fun cancelAnalyze() {
		resetViewsState()
	}

	private fun moveToResult(results: List<Classifications>) {
		resetViewsState()

		val categories = results[0].categories
		if (categories.isEmpty()) {
			showToast("No classification categories found")
			return
		}

		val highestCategory = categories.maxByOrNull { it.score }
		if (highestCategory == null) {
			showToast("No highest classification category found")
			return
		}

		val result = AnalyzeResult(
			imageUri = currentImageUri!!,
			label = highestCategory.label.trim(),
			confidenceScore = highestCategory.score,
		)

		vm.add(HistoriesViewModel.Event.OnSaved(result))

		val intent = Intent(requireContext(), ResultActivity::class.java)
		intent.putExtra("analyze_result", result)
		startActivity(intent)
	}

	private fun showToast(message: String) {
		Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
	}

	private fun getDestinationUri(): Uri {
		if (currentImageUri == null) {
			throw IllegalStateException("Current image uri is null")
		}

		val fileName = "asclepius_analyze_${System.currentTimeMillis()}.jpg"
		val file = File(requireContext().filesDir, fileName)

		val resolver = requireContext().contentResolver
		val inputStream: InputStream? = resolver.openInputStream(currentImageUri!!)
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

		binding.analyzeButton.visibility = View.GONE
		binding.analyzeButton.isEnabled = true
		binding.cancelButton.visibility = View.GONE
		binding.cancelButton.isEnabled = true

		binding.mediaPickerLayout.visibility = View.VISIBLE
		binding.progressIndicator.visibility = View.GONE

		binding.galleryButton.isClickable = true
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}