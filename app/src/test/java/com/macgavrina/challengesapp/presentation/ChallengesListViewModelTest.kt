package com.macgavrina.challengesapp.presentation

import com.macgavrina.challengesapp.repositories.FakeChallengesRepositoryImpl
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class ChallengesListViewModelTest {

    private lateinit var viewModel: ChallengesListViewModel

    @Before
    fun setup() {
        viewModel = ChallengesListViewModel(FakeChallengesRepositoryImpl())
    }
}