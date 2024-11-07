package com.dicoding.asclepius.presentation.view

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.domain.models.getConfidenceScoreString

class ResultActivity : AppCompatActivity() {

	private lateinit var binding: ActivityResultBinding

	@SuppressLint("SetTextI18n")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		binding = ActivityResultBinding.inflate(layoutInflater)
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

		val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getParcelableExtra("analyze_result", AnalyzeResult::class.java)
		} else {
			@Suppress("DEPRECATION") intent.getParcelableExtra("analyze_result")
		}

		if (result != null) {
			binding.resultText.text = "Terdeteksi: ${result.getConfidenceScoreString()}"
			binding.resultImage.setImageURI(result.imageUri)
		}

		binding.topAppBar.setNavigationOnClickListener { finish() }
	}
}