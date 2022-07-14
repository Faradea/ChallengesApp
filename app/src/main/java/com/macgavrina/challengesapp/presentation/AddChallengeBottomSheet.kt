package com.macgavrina.challengesapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.macgavrina.challengesapp.databinding.BottomSheetAddChallengeBinding
import com.macgavrina.challengesapp.domain.Challenge
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddChallengeBottomSheet: BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddChallengeBinding

    private val viewModel: AddNewChallengeViewModel by viewModels()

    private var currentlyDisplayedChallenge: Challenge? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetAddChallengeBinding.inflate(inflater, container, false)
        binding.errorReloadTv.setOnClickListener {
            viewModel.onEvent(AddNewChallengeViewModel.AddNewChallengeEvent.ShowNextChallenge)
        }
        binding.acceptChallengeButton.setOnClickListener {
            currentlyDisplayedChallenge?.let {
                viewModel.onEvent(AddNewChallengeViewModel.AddNewChallengeEvent.AcceptChallenge(it))
                dismiss()
            }
        }
        binding.skipChallengeButton.setOnClickListener {
            viewModel.onEvent(AddNewChallengeViewModel.AddNewChallengeEvent.ShowNextChallenge)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    state.challenge?.let {
                        binding.newChallengeTv.text = it.name
                        currentlyDisplayedChallenge = it
                    }
                    binding.newChallengeTv.visibility =
                        if (state.challenge != null) View.VISIBLE else View.GONE

                    binding.errorLayout.visibility =
                        if (state.errorMessage != null) View.VISIBLE else View.GONE

                    listOf(binding.acceptChallengeButton, binding.skipChallengeButton)
                        .forEach { button ->
                            button.visibility =
                                if (state.errorMessage != null) View.GONE else View.VISIBLE
                            button.alpha = if (state.buttonsAreClickable) 1f else 0.5f
                            button.isClickable = state.buttonsAreClickable
                        }

                    binding.progressBar.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
    }
}