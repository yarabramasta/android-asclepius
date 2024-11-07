package com.dicoding.asclepius.presentation.viewmodel

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.domain.models.Article
import com.dicoding.asclepius.domain.models.getBrowsableUri
import com.dicoding.asclepius.domain.repositories.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
	private val repo: NewsRepository,
) : BaseViewModel<
		NewsViewModel.State,
		NewsViewModel.Event,
		NewsViewModel.Effect>
	(initialState = State.initial()) {

	override fun add(event: Event) {
		when (event) {
			Event.OnFetch -> {
				setState { copy(isFetching = true) }
				fetchNews()
				setState { copy(isFetching = false) }
			}

			Event.OnRefresh -> {
				setState { copy(isRefreshing = true) }
				fetchNews()
				setState { copy(isRefreshing = false) }
			}

			is Event.OnClickNews -> {
				sendEffect(Effect.RedirectToArticle(event.article.getBrowsableUri()))
			}
		}
	}

	private fun fetchNews() {
		viewModelScope.launch {
			repo.getNews().collect { (data, error) ->
				if (error != null) {
					sendEffect(Effect.ShowError(error))
				} else {
					setState { copy(articles = data) }
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
		val articles: List<Article>,
	) {
		companion object {
			internal fun initial() = State(
				isFetching = false,
				isRefreshing = false,
				error = null,
				articles = emptyList()
			)
		}
	}

	sealed class Event {
		data object OnFetch : Event()
		data object OnRefresh : Event()
		data class OnClickNews(val article: Article) : Event()
	}

	sealed class Effect {
		data class ShowError(val error: Throwable) : Effect()
		data class RedirectToArticle(val uri: Uri) : Effect()
	}
}