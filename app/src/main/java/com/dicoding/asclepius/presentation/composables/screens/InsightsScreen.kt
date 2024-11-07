package com.dicoding.asclepius.presentation.composables.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dicoding.asclepius.domain.models.Article
import com.dicoding.asclepius.presentation.composables.atoms.ArticleListItem
import com.dicoding.asclepius.presentation.composables.atoms.ShimmerItem
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
		modifier = modifier.fillMaxSize(),
		contentPadding = PaddingValues(vertical = 24.dp)
	) {
		item {
			Text(
				text = "Insights",
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier.padding(horizontal = 24.dp)
			)
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				text = "Read the latest news about cancer related topics.",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(horizontal = 24.dp)
			)
			Spacer(modifier = Modifier.height(24.dp))
		}

		if (!isLoading && !hasError) {
			if (articles.isEmpty()) {
				item {
					Text(
						text = "No article found. Try to refresh...",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						modifier = Modifier.padding(horizontal = 24.dp)
					)
				}
			} else {
				items(articles, key = { it.url }) { article ->
					Box(modifier = Modifier.padding(horizontal = 8.dp)) {
						ArticleListItem(item = article, onPressed = { onClickNews(article) })
					}
				}
			}
		} else {
			items(5) {
				Box(modifier = Modifier.padding(horizontal = 8.dp)) {
					ShimmerItem(animate = !hasError)
				}
			}
		}
	}
}