package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hardiksachan.fitbuddy.databinding.ListItemExerciseBinding
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.*

class ExerciseListAdapter(val parentLifecycleOwner: LifecycleOwner) :
    ListAdapter<Exercise, ExerciseListAdapter.ViewHolder>(ExerciseDiffCallback()) {
    class ViewHolder private constructor(val binding: ListItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {

            fun from(parent: ViewGroup, parentLifecycleOwner: LifecycleOwner): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemExerciseBinding.inflate(layoutInflater, parent, false)
                binding.lifecycleOwner =parentLifecycleOwner
                return ViewHolder(binding)
            }
        }

        private val viewHolderJob = Job()
        private val viewHolderScope = CoroutineScope(viewHolderJob + Dispatchers.IO)

        fun bind(item: Exercise) {
            binding.exerciseItem = item
            binding.tvExerciseName.text = item.name

            FitBuddyRepository(binding.root.context.applicationContext).getExerciseCategoryFromId(item.category)
                .observe(binding.lifecycleOwner!!, Observer {
                    binding.tvExerciseCategory.text = it
                })
            binding.tvExerciseEquipment.text = "Equipment: ${item.equipment}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent, parentLifecycleOwner)
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