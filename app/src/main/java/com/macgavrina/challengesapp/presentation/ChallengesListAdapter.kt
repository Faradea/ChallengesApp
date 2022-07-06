package com.macgavrina.challengesapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.macgavrina.challengesapp.databinding.ItemChallengeBinding
import com.macgavrina.challengesapp.domain.Challenge

class ChallengesListAdapter (
    private val onUpdateIsChecked: (Int, Boolean) -> Unit
): ListAdapter<Challenge, TodoViewHolder>(TodoItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, pos: Int): TodoViewHolder {
        val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val viewHolder = TodoViewHolder(binding).apply {
            binding.challengeItemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                getItem(adapterPosition).id?.let { onUpdateIsChecked(it, isChecked) }
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: TodoViewHolder, pos: Int) {
        val item = getItem(pos)

        viewHolder.binding.challengeItemCheckbox.text = item.name
        viewHolder.binding.challengeItemCheckbox.isChecked = item.isCompleted
    }
}

class TodoViewHolder(val binding: ItemChallengeBinding) : RecyclerView.ViewHolder(binding.root)  {}

private class TodoItemCallback : DiffUtil.ItemCallback<Challenge>() {
    override fun areItemsTheSame(item1: Challenge, item2: Challenge): Boolean {
        return item1.id == item2.id
    }

    override fun areContentsTheSame(item1: Challenge, item2: Challenge): Boolean {
        return item1.name == item2.name && item1.isCompleted == item2.isCompleted
    }
}