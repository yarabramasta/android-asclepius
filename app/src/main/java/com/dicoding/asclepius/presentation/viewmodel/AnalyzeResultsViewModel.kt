package com.dicoding.asclepius.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.domain.repositories.AnalyzeResultsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyzeResultsViewModel @Inject constructor(
	private val repo: AnalyzeResultsRepository,
) : BaseViewModel<
		AnalyzeResultsViewModel.State,
		AnalyzeResultsViewModel.Event,
		AnalyzeResultsViewModel.Effect>
	(initialState = State.initial()) {

	override fun add(event: Event) {
		when (event) {
			Event.OnFetch -> {
				setState { copy(isFetching = true) }
				fetchResults()
			}

			Event.OnRefresh -> {
				setState { copy(isRefreshing = true) }
				fetchResults()
			}

			is Event.OnSaved -> saveResultToHistory(event.result)

			is Event.OnDeleted -> removeResultFromHistory(event.result)
		}
	}

	init {
		add(Event.OnFetch)
	}

	private fun fetchResults() {
		viewModelScope.launch {
			repo.getAnalyzeResults().collect { (results, error) ->
				if (error != null) {
					setState { copy(error = error) }
					sendEffect(Effect.ShowError(error))
				} else {
					setState { copy(results = results) }
				}
			}
		}
	}

	private fun saveResultToHistory(result: AnalyzeResult) {
		viewModelScope.launch {
			val (isSaved, error) = repo.saveAnalyzeResult(result)

			if (error != null) {
				sendEffect(Effect.ShowError(error))
			} else {
				if (isSaved) {
					setState { copy(results = results + result) }
				}
			}
		}
	}

	private fun removeResultFromHistory(result: AnalyzeResult) {
		viewModelScope.launch {
			val (isDeleted, error) = repo.deleteAnalyzeResult(result)

			if (error != null) {
				sendEffect(Effect.ShowError(error))
			} else {
				if (isDeleted) {
					setState { copy(results = results - result) }
				}
			}
		}
	}

	// ========= CONTRACT =========

	data class State(
		val isFetching: Boolean,
		val isRefreshing: Boolean,
		val error: Throwable?,
		val results: List<AnalyzeResult>,
	) {
		companion object {
			internal fun initial() = State(
				isFetching = false,
				isRefreshing = false,
				error = null,
				results = emptyList()
			)
		}
	}

	sealed class Event {
		data object OnFetch : Event()
		data object OnRefresh : Event()
		data class OnSaved(val result: AnalyzeResult) : Event()
		data class OnDeleted(val result: AnalyzeResult) : Event()
	}

	sealed class Effect {
		data class ShowError(val error: Throwable) : Effect()
	}
}