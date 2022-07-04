package com.macgavrina.challengesapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.macgavrina.challengesapp.databinding.BottomSheetAddChallengeBinding
import com.macgavrina.challengesapp.domain.Challenge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.macgavrina.challengesapp.presentation.AddNewChallengeViewModel.AddNewChallengeViewState.*

@AndroidEntryPoint
class AddChallengeBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddChallengeBinding

    private val viewModel: AddNewChallengeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddChallengeBinding.inflate(inflater, container, false)
        binding.errorReloadTv.setOnClickListener {
            viewModel.getRandomChallenge()
        }
        binding.acceptChallengeButton.setOnClickListener {
            viewModel.acceptChallenge(Challenge(
                binding.newChallengeTv.text.toString()
            ))
            dialog?.hide()
        }
        binding.skipChallengeButton.setOnClickListener {
            viewModel.getRandomChallenge()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    when (state) {
                        is Data -> {
                            binding.newChallengeTv.text = state.data.name
                        }
                       else -> {}
                    }

                    binding.errorLayout.visibility =
                        if (state is Error) View.VISIBLE else View.GONE

                    listOf(binding.acceptChallengeButton, binding.skipChallengeButton)
                        .forEach { button ->
                            button.visibility = if (state is Error) View.GONE else View.VISIBLE
                            button.alpha = if (state is Data) 1f else 0.5f
                            button.isClickable = (state is Data)
                        }

                    binding.progressBar.visibility =
                        if (state is Loading) View.VISIBLE else View.GONE
                    binding.newChallengeTv.visibility =
                        if (state is Data) View.VISIBLE else View.GONE
                }
            }
        }
    }
}