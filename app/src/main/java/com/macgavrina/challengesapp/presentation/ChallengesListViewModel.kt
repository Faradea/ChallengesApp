package com.macgavrina.challengesapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macgavrina.challengesapp.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class ChallengesListViewModel @Inject constructor(
    private val getChallengesAllUsecase: GetChallengesAllUsecase,
    private val updateChallengeIsCompletedUsecase: UpdateChallengeIsCompletedUsecase
): ViewModel() {

    lateinit var state: StateFlow<ChallengesListState>

    companion object {
        private const val LOG_TAG = "MainViewModel"
    }

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvents

    init {
        state = getChallengesAllUsecase.execute().flowOn(Dispatchers.IO).map {
            when (it) {
                is Resource.Error -> state.value.copy(isEmpty = true, items = emptyList())
                is Resource.Success -> {
                    if (it.data.isEmpty()) {
                        state.value.copy(isEmpty = true, items = emptyList())
                    } else {
                        state.value.copy(isEmpty = false, items = it.data)
                    }
                }
            }
        }.stateIn(
            scope = viewModelScope + Dispatchers.IO,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ChallengesListState(isEmpty = true, items = emptyList())
        )
    }

    fun onEvent(event: ChallengesListEvent) {
        when (event) {
            is ChallengesListEvent.AddNewChallenge -> onAddChallenge()
            is ChallengesListEvent.UpdateChallengeIsCompleted -> {
                updateChallengeIsCompleted(event.id, event.isCompleted)
            }
        }
    }

    private fun onAddChallenge() {
        Log.d(LOG_TAG, "User wants to add new challenge")
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.ToAddNewChallenge)
        }
    }

    private fun updateChallengeIsCompleted(id: Int, isCompleted: Boolean) =
        viewModelScope.launch {
            updateChallengeIsCompletedUsecase.execute(id = id, isCompleted = isCompleted)
        }

    data class ChallengesListState (
        val isEmpty: Boolean,
        val items: List<Challenge>
        )

    sealed class ChallengesListEvent {
        object AddNewChallenge: ChallengesListEvent()
        class UpdateChallengeIsCompleted(val id: Int, val isCompleted: Boolean): ChallengesListEvent()
    }

    sealed class NavigationEvent {
        object ToAddNewChallenge: NavigationEvent()
    }
}