package com.macgavrina.challengesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.macgavrina.challengesapp.databinding.ActivityChallengesListBinding
import kotlinx.coroutines.launch

class ChallengesListActivity : AppCompatActivity() {

    private lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenges_list)

        val binding = ActivityChallengesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm = ViewModelProvider(this)[MainViewModel::class.java]

        binding.addChallengeButton.setOnClickListener {
            vm.onAddChallenge()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.navigationEvent.collect {
                    val modalBottomSheet = AddChallengeBottomSheet()
                    modalBottomSheet.show(supportFragmentManager, "AddChallengeBottomSheet")
                }
            }
        }


    }
}