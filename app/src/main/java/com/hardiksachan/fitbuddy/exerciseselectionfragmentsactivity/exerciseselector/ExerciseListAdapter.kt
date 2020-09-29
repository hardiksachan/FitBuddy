package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.RecyclerviewItemExerciseBinding
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ExerciseListAdapter(val parentLifecycleOwner: LifecycleOwner, val onClickListener: OnClickListener) :
    ListAdapter<Exercise, ExerciseListAdapter.ViewHolder>(ExerciseDiffCallback()) {
    class ViewHolder private constructor(val binding: RecyclerviewItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {

            fun from(parent: ViewGroup, parentLifecycleOwner: LifecycleOwner): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerviewItemExerciseBinding.inflate(layoutInflater, parent, false)
                binding.lifecycleOwner = parentLifecycleOwner
                return ViewHolder(binding)
            }
        }

        private val viewHolderJob = Job()
        private val viewHolderScope = CoroutineScope(viewHolderJob + Dispatchers.IO)

        fun bind(item: Exercise) {
            binding.exerciseItem = item
            binding.tvExerciseName.text = item.name

            FitBuddyRepository(binding.root.context.applicationContext).getExerciseCategoryFromId(
                item.category
            )
                .observe(binding.lifecycleOwner!!, Observer {
                    binding.tvExerciseCategory.text =
                        binding.tvExerciseCategory.context.getString(R.string.category_format, it)
                })
            item.equipment.observe(binding.lifecycleOwner!!, Observer {
                binding.tvExerciseEquipment.text = if (it.size > 0) {
                    val str = it.joinToString { equipment ->
                        equipment.name
                    }
                    binding.root.context.getString(R.string.equipment_format, str)
                } else {
                    binding.root.context.getString(R.string.equipment_not_found)
                }
            })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent, parentLifecycleOwner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener{
            onClickListener.onClick(getItem(position))
        }
    }

    class OnClickListener(val clickListener : (exercise: Exercise) -> Unit) {
        fun onClick(exercise: Exercise) = clickListener(exercise)
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