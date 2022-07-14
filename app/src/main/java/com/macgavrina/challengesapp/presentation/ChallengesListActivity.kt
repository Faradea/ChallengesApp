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

    private val viewModel: ChallengesListViewModel by viewModels()

    private val adapter = ChallengesListAdapter(
        onUpdateIsChecked = { id, isChecked ->
            viewModel.onEvent(
                ChallengesListViewModel.ChallengesListEvent.UpdateChallengeIsCompleted(id, isChecked)
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges_list)

        val binding = ActivityChallengesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addChallengeButton.setOnClickListener {
            viewModel.onEvent(ChallengesListViewModel.ChallengesListEvent.AddNewChallenge)
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
                    binding.emptyTv.visibility =
                        if (state.isEmpty) View.VISIBLE else View.INVISIBLE

                    adapter.submitList(state.items)
                }
            }
        }


    }
}