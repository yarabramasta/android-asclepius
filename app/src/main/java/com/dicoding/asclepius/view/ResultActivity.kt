package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.domain.models.ResultData

class ResultActivity : AppCompatActivity() {
	private lateinit var binding: ActivityResultBinding

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityResultBinding.inflate(layoutInflater)
		val view = binding.root
		setContentView(view)

		val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getParcelableExtra("result_data", ResultData::class.java)
		} else {
			@Suppress("DEPRECATION") intent.getParcelableExtra("result_data")
		}

		if (result != null) {
			binding.resultText.text = "Terdeteksi: ${result.confidenceScore}"
			binding.resultImage.setImageURI(result.imageUri)
		}

		binding.topAppBar.setNavigationOnClickListener { finish() }
	}
}