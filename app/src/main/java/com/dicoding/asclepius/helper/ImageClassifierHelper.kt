package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import androidx.exifinterface.media.ExifInterface
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
	private var threshold: Float = 0.1f,
	private var maxResults: Int = 3,
	private val modelName: String = "cancer_classification.tflite",
	private val context: Context,
	private val classifierListener: ClassifierListener?,
) {

	interface ClassifierListener {
		fun onError(error: String)
		fun onResults(
			results: List<Classifications>?,
			inferenceTime: Long,
		)
	}

	private var imageClassifier: ImageClassifier? = null

	private fun setupImageClassifier() {
		val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
			.setScoreThreshold(threshold)
			.setMaxResults(maxResults)
		val baseOptionsBuilder = BaseOptions.builder().setNumThreads(4)
		optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

		try {
			imageClassifier = ImageClassifier.createFromFileAndOptions(
				context,
				modelName,
				optionsBuilder.build()
			)
		} catch (e: IllegalStateException) {
			classifierListener?.onError(context.getString(R.string.image_classifier_failed))
			Log.e(TAG, e.message.toString())
		}
	}

	init {
		setupImageClassifier()
	}

	fun classifyStaticImage(imageUri: Uri) {
		if (imageClassifier == null) {
			setupImageClassifier()
		}

		val imageProcessor = ImageProcessor.Builder()
			.add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
			.add(CastOp(DataType.UINT8))
			.build()

		val bitmap = uriToBitmap(context, imageUri)
		if (bitmap != null) {
			val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

			val imageProcessingOptions = ImageProcessingOptions.builder()
				.setOrientation(getOrientationFromRotation(context, imageUri))
				.build()

			var inferenceTime = SystemClock.uptimeMillis()
			val results = imageClassifier?.classify(tensorImage, imageProcessingOptions)
			inferenceTime = SystemClock.uptimeMillis() - inferenceTime

			classifierListener?.onResults(
				results,
				inferenceTime
			)
		} else {
			throw IllegalStateException("Bitmap is null")
		}
	}

	private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
		return try {
			val inputStream = context.contentResolver.openInputStream(uri)
			BitmapFactory.decodeStream(inputStream).also {
				inputStream?.close()
			}
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}

	private fun getOrientationFromRotation(
		context: Context,
		uri: Uri,
	): ImageProcessingOptions.Orientation {
		val rotationDegrees = try {
			context.contentResolver.openInputStream(uri)?.use { inputStream ->
				val exif = ExifInterface(inputStream)
				when (exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL
				)) {
					ExifInterface.ORIENTATION_ROTATE_90 -> 90
					ExifInterface.ORIENTATION_ROTATE_180 -> 180
					ExifInterface.ORIENTATION_ROTATE_270 -> 270
					else -> 0
				}
			} ?: 0
		} catch (e: Exception) {
			e.printStackTrace()
			0
		}

		return when (rotationDegrees) {
			Surface.ROTATION_270 -> ImageProcessingOptions.Orientation.BOTTOM_RIGHT
			Surface.ROTATION_180 -> ImageProcessingOptions.Orientation.RIGHT_BOTTOM
			Surface.ROTATION_90 -> ImageProcessingOptions.Orientation.TOP_LEFT
			else -> ImageProcessingOptions.Orientation.RIGHT_TOP
		}
	}

	companion object {
		private const val TAG = "ImageClassifierHelper"
	}
}