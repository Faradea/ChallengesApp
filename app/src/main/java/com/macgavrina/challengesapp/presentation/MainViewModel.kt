package com.macgavrina.challengesapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.GetRandomChallengeUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel() {

    companion object {
        private const val LOG_TAG = "MainViewModel"
    }

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvents

    fun onAddChallenge() {
        Log.d(LOG_TAG, "User wants to add new challenge")
        viewModelScope.launch {
            _navigationEvents.emit(NavigationEvent.AddNewChallenge)
        }
    }

    sealed class NavigationEvent {
        object AddNewChallenge: NavigationEvent()
    }
}