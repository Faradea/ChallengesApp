package com.macgavrina.challengesapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.GetChallengesAllUsecase
import com.macgavrina.challengesapp.domain.GetRandomChallengeUsecase
import com.macgavrina.challengesapp.domain.UpdateChallengeIsCompletedUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getChallengesAllUsecase: GetChallengesAllUsecase,
    private val updateChallengeIsCompletedUsecase: UpdateChallengeIsCompletedUsecase
): ViewModel() {

    val state: StateFlow<MainState>

    companion object {
        private const val LOG_TAG = "MainViewModel"
    }

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvents

    init {
        state = getChallengesAllUsecase.execute().flowOn(Dispatchers.IO).map {
            if (it.isEmpty()) MainState.Empty else MainState.Data(it)
        }.stateIn(
            scope = viewModelScope + Dispatchers.IO,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = MainState.Empty
        )
    }

    fun onAddChallenge() {
        Log.d(LOG_TAG, "User wants to add new challenge")
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.AddNewChallenge)
        }
    }

    fun updateChallengeIsCompleted(id: Int, isCompleted: Boolean) =
        viewModelScope.launch {
            updateChallengeIsCompletedUsecase.execute(id = id, isCompleted = isCompleted)
        }

    sealed class NavigationEvent {
        object AddNewChallenge: NavigationEvent()
    }

    sealed class MainState () {
        object Empty: MainState()
        class Data(val items: List<Challenge>): MainState()
    }
}