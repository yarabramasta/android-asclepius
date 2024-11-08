package com.dicoding.asclepius.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AnalyzeViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
) : BaseViewModel<
		AnalyzeViewModel.State,
		AnalyzeViewModel.Event,
		AnalyzeViewModel.Effect>
	(initialState = State()) {

	override fun add(event: Event) {
		when (event) {
			is Event.OnImagePreviewChanged -> {
				setState {
					copy(currentPreviewImageUri = event.uri)
				}
			}

			is Event.OnMessage -> {
				sendEffect(
					Effect.ShowToast(event.message)
				)
			}

			is Event.OnClassifierResults -> {
				sendEffect(
					Effect.ShowToast("Successfully analyzed image in ${event.inferenceTime} ms")
				)
			}
		}
	}

	init {
		savedStateHandle.get<Uri>("currentPreviewImageUri")?.let { uri ->
			add(Event.OnImagePreviewChanged(uri))
		}
	}

	// ========= CONTRACT =========
	@Immutable
	data class State(
		val currentPreviewImageUri: Uri? = null,
	)

	sealed class Event {
		data class OnImagePreviewChanged(val uri: Uri?) : Event()
		data class OnMessage(val message: String) : Event()
		data class OnClassifierResults(val inferenceTime: Long) : Event()
	}

	sealed class Effect {
		data class ShowToast(val message: String) : Effect()
	}
}