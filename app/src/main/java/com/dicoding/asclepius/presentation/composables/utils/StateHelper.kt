package com.dicoding.asclepius.presentation.composables.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> rememberFlowWithLifecycle(
	flow: Flow<T>,
	lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
	minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): Flow<T> {
	return remember(flow, lifecycle) {
		flow.flowWithLifecycle(
			lifecycle = lifecycle,
			minActiveState = minActiveState,
		)
	}
}