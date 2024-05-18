package com.macgavrina.challengesapp.presentation

import app.cash.turbine.test
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.repositories.FakeChallengesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddNewChallengeViewModelTest {

    private lateinit var viewModel: AddNewChallengeViewModel

    private lateinit var dispatcherProvider: DispatcherProvider
    private val dispatcher = TestCoroutineDispatcher()
    private val repository = FakeChallengesRepositoryImpl()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        dispatcherProvider = TestDispatcherProvider()
        viewModel = AddNewChallengeViewModel(repository, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `success showing next challenge with unique name`() {
        runTest {
            viewModel.onEvent(AddNewChallengeViewModel.AddNewChallengeEvent.ShowNextChallenge)

            viewModel.state.test {
                assertEquals(
                    AddNewChallengeViewModel.AddNewChallengeState(
                        buttonsAreClickable = true,
                        challenge = Challenge(
                            id = 1,
                            name = "Random challenge from API",
                            isCompleted = false
                        ),
                        errorMessage = null,
                        isLoading = false,
                    ),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `error loading new challenge`() {
        runTest {
            repository.shouldReturnNetworkError = true

            viewModel.onEvent(AddNewChallengeViewModel.AddNewChallengeEvent.ShowNextChallenge)

            viewModel.state.test {
                assertEquals(
                    AddNewChallengeViewModel.AddNewChallengeState(
                        buttonsAreClickable = false,
                        challenge = null,
                        errorMessage = "Some error",
                        isLoading = false,
                    ),
                    awaitItem()
                )
            }
        }
    }

    @Test
    fun `accepted challenge is added to DB`() {
        runTest {
            val challenge = Challenge(
                id = 123,
                name = "Accepted challenge",
                isCompleted = false
            )

            viewModel.onEvent(AddNewChallengeViewModel.AddNewChallengeEvent.AcceptChallenge(
                challenge = challenge
            ))

            repository.getChallengesAll()
                .test {
                    assertEquals(
                        listOf(challenge),
                        awaitItem()
                    )
                }
        }
    }
}