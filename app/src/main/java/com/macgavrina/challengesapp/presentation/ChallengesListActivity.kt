package com.macgavrina.challengesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.macgavrina.challengesapp.R
import com.macgavrina.challengesapp.databinding.ActivityChallengesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChallengesListActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges_list)

        val binding = ActivityChallengesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addChallengeButton.setOnClickListener {
            viewModel.onAddChallenge()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    val modalBottomSheet = AddChallengeBottomSheet()
                    modalBottomSheet.show(supportFragmentManager, "AddChallengeBottomSheet")
                }
            }
        }


    }
}