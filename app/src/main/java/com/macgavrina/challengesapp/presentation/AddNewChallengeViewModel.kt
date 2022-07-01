package com.macgavrina.challengesapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val getRandomChallengeUsecase: GetRandomChallengeUsecase
): ViewModel() {

    private val _state = MutableStateFlow<AddNewChallengeViewState>(AddNewChallengeViewState.Loading)
    val state: SharedFlow<AddNewChallengeViewState> = _state

    init {
        getRandomChallenge()
    }

    private fun getRandomChallenge() {
        viewModelScope.launch {
            when (val result = getRandomChallengeUsecase.execute()) {
                is ResultOf.Success -> {
                    _state.emit(
                        AddNewChallengeViewState.Data(result.data)
                    )
                }
            }
        }
    }

    sealed class AddNewChallengeViewState (data: Challenge? = null, errorMessage: String? = null) {
        object Loading: AddNewChallengeViewState()
        class Data(val data: Challenge): AddNewChallengeViewState(data = data)
        class Error(errorMessage: String): AddNewChallengeViewState(errorMessage = errorMessage)
    }
}