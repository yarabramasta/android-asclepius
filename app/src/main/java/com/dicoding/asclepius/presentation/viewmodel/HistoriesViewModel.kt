package com.dicoding.asclepius.presentation.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.domain.repositories.HistoriesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoriesViewModel @Inject constructor(
	private val repo: HistoriesRepository,
) : BaseViewModel<
		HistoriesViewModel.State,
		HistoriesViewModel.Event,
		HistoriesViewModel.Effect>
	(initialState = State.initial()) {

	override fun add(event: Event) {
		when (event) {
			Event.OnFetch -> {
				setState { copy(isFetching = true) }
				fetchHistories()
				setState { copy(isFetching = false) }
			}

			Event.OnRefresh -> {
				setState { copy(isRefreshing = true) }
				fetchHistories()
				setState { copy(isRefreshing = false) }
			}

			is Event.OnSaved -> saveResultToHistory(event.result)

			is Event.OnDeleteItem -> removeResultFromHistory(event.result)
		}
	}

	private fun fetchHistories() {
		viewModelScope.launch {
			repo.getHistories().collect { (data, error) ->
				if (error != null) {
					setState { copy(error = error) }
					sendEffect(Effect.ShowError(error))
				} else {
					setState { copy(histories = data) }
				}
			}
		}
	}

	private fun saveResultToHistory(result: AnalyzeResult) {
		viewModelScope.launch {
			val (isSaved, error) = repo.saveResult(result)

			if (error != null) {
				sendEffect(Effect.ShowError(error))
			} else {
				if (isSaved) {
					setState { copy(histories = histories + result) }
				}
			}
		}
	}

	private fun removeResultFromHistory(result: AnalyzeResult) {
		viewModelScope.launch {
			val (isDeleted, error) = repo.deleteResult(result)

			if (error != null) {
				sendEffect(Effect.ShowError(error))
			} else {
				if (isDeleted) {
					setState { copy(histories = histories - result) }
				}
			}
		}
	}

	// ========= CONTRACT =========
	@Immutable
	data class State(
		val isFetching: Boolean,
		val isRefreshing: Boolean,
		val error: Throwable?,
		val histories: List<AnalyzeResult>,
	) {
		companion object {
			internal fun initial() = State(
				isFetching = false,
				isRefreshing = false,
				error = null,
				histories = emptyList()
			)
		}
	}

	sealed class Event {
		data object OnFetch : Event()
		data object OnRefresh : Event()
		data class OnSaved(val result: AnalyzeResult) : Event()
		data class OnDeleteItem(val result: AnalyzeResult) : Event()
	}

	sealed class Effect {
		data class ShowError(val error: Throwable) : Effect()
	}
}