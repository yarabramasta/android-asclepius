package com.dicoding.asclepius.presentation.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

abstract class BaseViewModel<State, Event, Effect>(initialState: State) : ViewModel() {

	private val mutableState = MutableStateFlow(initialState)
	val state: StateFlow<State>
		get() = mutableState.asStateFlow()

	protected fun setState(reducer: State.() -> State) {
		mutableState.value = reducer(mutableState.value)
	}

	private val effectFlow = Channel<Effect>(capacity = Channel.CONFLATED)
	val effect: Flow<Effect> = effectFlow.receiveAsFlow()

	protected fun sendEffect(effect: Effect) {
		effectFlow.trySend(effect)
	}

	abstract fun add(event: Event)
}