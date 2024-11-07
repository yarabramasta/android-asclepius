package com.dicoding.asclepius.presentation.composables.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dicoding.asclepius.domain.models.Article
import com.dicoding.asclepius.presentation.composables.utils.rememberFlowWithLifecycle
import com.dicoding.asclepius.presentation.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
	modifier: Modifier = Modifier,
	viewModel: NewsViewModel,
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	val effect = rememberFlowWithLifecycle(viewModel.effect)
	val context = LocalContext.current

	LaunchedEffect(effect) {
		effect.collect {
			when (it) {
				is NewsViewModel.Effect.ShowError -> {
					Toast.makeText(
						context,
						it.error.message ?: "Something went wrong...",
						Toast.LENGTH_SHORT
					).show()
				}

				is NewsViewModel.Effect.RedirectToArticle -> {
					val intent = Intent(Intent.ACTION_VIEW, it.uri)
					context.startActivity(intent)
				}
			}
		}
	}

	PullToRefreshBox(
		isRefreshing = state.isRefreshing,
		onRefresh = { viewModel.add(NewsViewModel.Event.OnRefresh) },
		modifier = Modifier.fillMaxSize()
	) {
		InsightsScreenContent(
			modifier = modifier,
			articles = state.articles,
			isLoading = state.isFetching || state.isRefreshing,
			hasError = state.error != null,
			onClickNews = { viewModel.add(NewsViewModel.Event.OnClickNews(it)) }
		)
	}
}

@Composable
private fun InsightsScreenContent(
	modifier: Modifier,
	articles: List<Article>,
	isLoading: Boolean,
	hasError: Boolean,
	onClickNews: (Article) -> Unit,
) {
	LazyColumn(
		modifier = modifier.fillMaxSize()
	) {
		items(articles, key = { it.url }) { article ->
			Text(article.title)
		}
	}
}

