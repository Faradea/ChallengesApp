package com.macgavrina.challengesapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macgavrina.challengesapp.domain.AcceptChallengeUsecase
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.GetRandomChallengeUsecase
import com.macgavrina.challengesapp.domain.ResultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewChallengeViewModel @Inject constructor(
    private val getRandomChallengeUsecase: GetRandomChallengeUsecase,
    private val acceptChallengeUsecase: AcceptChallengeUsecase
): ViewModel() {

    private val _state = MutableStateFlow<AddNewChallengeViewState>(AddNewChallengeViewState.Loading)
    val state: SharedFlow<AddNewChallengeViewState> = _state

    init {
        if (_state.value !is AddNewChallengeViewState.Data) {
            getRandomChallenge()
        }
    }

    fun acceptChallenge(challenge: Challenge) {
        viewModelScope.launch {
            acceptChallengeUsecase.execute(challenge)
        }
    }

    fun getRandomChallenge() {
        viewModelScope.launch {
            _state.emit(AddNewChallengeViewState.Loading)
            when (val result = getRandomChallengeUsecase.execute()) {
                is ResultOf.Success -> {
                    _state.emit(
                        AddNewChallengeViewState.Data(result.data)
                    )
                }
                is ResultOf.Error -> {
                    _state.emit(
                        AddNewChallengeViewState.Error(result.message)
                    )
                }
            }
        }
    }

    sealed class AddNewChallengeViewState (data: Challenge? = null, errorMessage: String? = null) {
        object Loading: AddNewChallengeViewState()
        class Data(val data: Challenge): AddNewChallengeViewState(data = data)
        class Error(val errorMessage: String): AddNewChallengeViewState(errorMessage = errorMessage)
    }
}