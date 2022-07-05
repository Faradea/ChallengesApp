package com.macgavrina.challengesapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.macgavrina.challengesapp.R
import com.macgavrina.challengesapp.databinding.ActivityChallengesListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChallengesListActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val adapter = ChallengesListAdapter(
        onUpdateIsChecked = { id, isChecked ->
            // ToDo
        }
    )

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

        binding.challengesRv.adapter = adapter

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is MainViewModel.MainState.Empty -> {
                            binding.challengesRv.visibility = View.INVISIBLE
                            binding.emptyTv.visibility = View.VISIBLE
                        }
                        is MainViewModel.MainState.Data -> {
                            adapter.submitList(state.items)
                            binding.challengesRv.visibility = View.VISIBLE
                            binding.emptyTv.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }


    }
}