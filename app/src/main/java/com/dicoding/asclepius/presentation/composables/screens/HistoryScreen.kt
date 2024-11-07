package com.dicoding.asclepius.presentation.composables.screens

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
import com.dicoding.asclepius.domain.models.AnalyzeResult
import com.dicoding.asclepius.presentation.composables.atoms.HistoryListItem
import com.dicoding.asclepius.presentation.composables.atoms.ShimmerItem
import com.dicoding.asclepius.presentation.composables.utils.rememberFlowWithLifecycle
import com.dicoding.asclepius.presentation.viewmodel.HistoriesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
	modifier: Modifier = Modifier,
	viewModel: HistoriesViewModel,
) {
	val state by viewModel.state.collectAsStateWithLifecycle()
	val effect = rememberFlowWithLifecycle(viewModel.effect)
	val context = LocalContext.current

	LaunchedEffect(effect) {
		effect.collect {
			when (it) {
				is HistoriesViewModel.Effect.ShowError -> {
					Toast.makeText(
						context,
						it.error.message ?: "Something went wrong...",
						Toast.LENGTH_SHORT
					).show()
				}
			}
		}
	}

	PullToRefreshBox(
		isRefreshing = state.isRefreshing,
		onRefresh = { viewModel.add(HistoriesViewModel.Event.OnRefresh) },
		modifier = Modifier.fillMaxSize()
	) {
		HistoryScreenContent(
			modifier = modifier,
			histories = state.histories.reversed(),
			isLoading = state.isFetching || state.isRefreshing,
			hasError = state.error != null,
			onDeleteHistory = {
				viewModel.add(HistoriesViewModel.Event.OnDeleteItem(it))
			}
		)
	}
}

@Composable
private fun HistoryScreenContent(
	modifier: Modifier = Modifier,
	histories: List<AnalyzeResult>,
	isLoading: Boolean,
	hasError: Boolean,
	onDeleteHistory: (AnalyzeResult) -> Unit,
) {
	LazyColumn(
		modifier = modifier.fillMaxSize(),
		contentPadding = PaddingValues(vertical = 24.dp)
	) {
		item {
			Text(
				text = "History",
				style = MaterialTheme.typography.headlineSmall,
				modifier = Modifier.padding(horizontal = 24.dp)
			)
			Spacer(modifier = Modifier.height(4.dp))
			Text(
				text = "This is a list of all the images you've analyzed.",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(horizontal = 24.dp)
			)
			Spacer(modifier = Modifier.height(24.dp))
		}

		if (!isLoading && !hasError) {
			if (histories.isEmpty()) {
				item {
					Text(
						text = "No history yet. Analyze an image to see it here!",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						modifier = Modifier.padding(horizontal = 24.dp)
					)
				}
			} else {
				items(items = histories, key = { it.id }) { result ->
					Box(modifier = Modifier.padding(horizontal = 8.dp)) {
						HistoryListItem(
							item = result,
							onDeletePressed = onDeleteHistory,
						)
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