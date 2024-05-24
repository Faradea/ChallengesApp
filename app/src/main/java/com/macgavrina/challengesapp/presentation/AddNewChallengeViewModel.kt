package com.macgavrina.challengesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macgavrina.challengesapp.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddNewChallengeViewModel @Inject constructor(
    private val challengesRepository: ChallengesRepository,
    private val dispatcherProvider: DispatcherProvider
): ViewModel() {

    private val _state = MutableStateFlow(
        AddNewChallengeState(
            isLoading = false,
            errorMessage = null,
            challenge = null,
            buttonsAreClickable = false
        )
    )
    val state: SharedFlow<AddNewChallengeState> = _state

    init {
        if (_state.value.challenge == null) {
            getRandomChallenge()
        }
    }

    fun onEvent(event: AddNewChallengeEvent) {
        when (event) {
            is AddNewChallengeEvent.AcceptChallenge -> acceptChallenge(event.challenge)
            is AddNewChallengeEvent.ShowNextChallenge -> getRandomChallenge()
        }
    }

    private fun acceptChallenge(challenge: Challenge) {
        viewModelScope.launch(dispatcherProvider.main) {
            withContext(dispatcherProvider.io) {
                if (ChallengeValidationUtils.isChallengeValid(challenge)) {
                    challengesRepository.addChallenge(challenge)
                }
            }
        }
    }

    private fun getRandomChallenge() {
        viewModelScope.launch {
            _state.emit(_state.value.copy(
                isLoading = true, challenge = null, errorMessage = null, buttonsAreClickable = false
            ))
            when (val result = withContext(dispatcherProvider.io) {
                for (i in 0.. 20) {
                    val newChallenge = challengesRepository.getRandomChallenge()
                    if (newChallenge is Resource.Error) {
                        return@withContext newChallenge
                    }
                    if (!challengesRepository.checkIfChallengeAlreadyExist(
                            (newChallenge as Resource.Success).data
                        )) {
                        return@withContext newChallenge
                    }
                }
                return@withContext Resource.Error<Challenge>(message = "Probably there is no new challenges left")
            }) {
                is Resource.Success -> {
                    _state.emit(
                        _state.value.copy(
                            isLoading = false,
                            challenge = result.data,
                            buttonsAreClickable = true
                        )
                    )
                }
                is Resource.Error -> {
                    _state.emit(
                        _state.value.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    )
                }
            }
        }
    }

    data class AddNewChallengeState(
        val isLoading: Boolean,
        val errorMessage: String?,
        val challenge: Challenge?,
        val buttonsAreClickable: Boolean
    )

    sealed class AddNewChallengeEvent {
        class AcceptChallenge(val challenge: Challenge): AddNewChallengeEvent()
        object ShowNextChallenge: AddNewChallengeEvent()
    }
}