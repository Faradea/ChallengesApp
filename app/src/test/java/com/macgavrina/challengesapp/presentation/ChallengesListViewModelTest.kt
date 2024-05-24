package com.macgavrina.challengesapp.presentation

import app.cash.turbine.test
import com.macgavrina.challengesapp.domain.Challenge
import com.macgavrina.challengesapp.domain.ChallengesRepository
import com.macgavrina.challengesapp.repositories.FakeChallengesRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ChallengesListViewModelTest {

    private lateinit var dispatcherProvider: DispatcherProvider
    private val dispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: ChallengesListViewModel

    @Mock
    private lateinit var repository: ChallengesRepository

    @Before
    fun setUp() {
        dispatcherProvider = TestDispatcherProvider()

        Dispatchers.setMain(dispatcher)
        dispatcherProvider = TestDispatcherProvider()
        viewModel = ChallengesListViewModel(repository, dispatcherProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty state as initial state`() {
        runTest {
            //doReturn(flowOf(listOf<Challenge>())).`when`(repository).getChallengesAll()
            viewModel.state.test {
                assertEquals(ChallengesListViewModel.ChallengesListState(
                    isEmpty = true,
                    items = emptyList(),
                ),
                    awaitItem())
            }
            verify(repository).getChallengesAll()
        }
    }

    @Test
    fun `empty state as repository returns empty list`() {
        runTest {
            doReturn(flowOf(listOf<Challenge>())).`when`(repository).getChallengesAll()

            viewModel.loadSavedChallenges()
            viewModel.state.test {
                assertEquals(ChallengesListViewModel.ChallengesListState(
                    isEmpty = true,
                    items = emptyList(),
                ),
                    awaitItem())
            }
        }
    }

    @Test
    fun `state with saved challenges if repository returns not empty list`() {
        // Можно еще вот такой стейт проверить, но в самой вью модели сценарий не обработан
//        val errorMessage = "Error Message For You"
//        doReturn(flow<List<ApiUser>> {
//            throw IllegalStateException(errorMessage)
//        }).`when`(apiHelper).getUsers()
        runTest {
            val challengesList = listOf<Challenge>(
                Challenge(id = 1, name = "name1", isCompleted = false),
                Challenge(id = 2, name = "name2", isCompleted = true)
            )
            doReturn(flowOf(challengesList)).`when`(repository).getChallengesAll()

            viewModel.loadSavedChallenges()
            viewModel.state.test {
                assertEquals(ChallengesListViewModel.ChallengesListState(
                    isEmpty = false,
                    items = challengesList,
                ),
                    awaitItem())
            }
        }
    }
}