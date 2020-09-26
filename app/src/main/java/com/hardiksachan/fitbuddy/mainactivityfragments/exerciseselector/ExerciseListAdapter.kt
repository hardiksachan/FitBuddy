package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hardiksachan.fitbuddy.databinding.ListItemExerciseBinding
import com.hardiksachan.fitbuddy.domain.Exercise

class ExerciseListAdapter : ListAdapter<Exercise, ExerciseListAdapter.ViewHolder>(ExerciseDiffCallback()) {
    class ViewHolder private constructor(val binding: ListItemExerciseBinding)
        : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemExerciseBinding.inflate(layoutInflater,  parent ,false)
                return ViewHolder(binding)
            }
        }

        fun bind(item : Exercise){
            binding.exerciseItem = item
            binding.tvExerciseName.text = item.name
            binding.tvExerciseId.text = item.id.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ExerciseDiffCallback : DiffUtil.ItemCallback<Exercise>() {
    override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
        return oldItem.id == newItem.id
    }

}