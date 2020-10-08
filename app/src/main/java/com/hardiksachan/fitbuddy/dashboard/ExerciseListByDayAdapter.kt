package com.hardiksachan.fitbuddy.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.RecyclervireItemExerciseByDayBinding
import com.hardiksachan.fitbuddy.domain.ExerciseDay
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository

class ExerciseListByDayAdapter(
    val parentLifecycleOwner: LifecycleOwner,
    val onClickListener: OnClickListener
) :
    ListAdapter<ExerciseDay, ExerciseListByDayAdapter.ViewHolder>(ExerciseDayDiffCallback()) {
    class ViewHolder private constructor(val binding: RecyclervireItemExerciseByDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {

            fun from(parent: ViewGroup, parentLifecycleOwner: LifecycleOwner): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    RecyclervireItemExerciseByDayBinding.inflate(layoutInflater, parent, false)
                binding.lifecycleOwner = parentLifecycleOwner
                return ViewHolder(binding)
            }
        }

        fun bind(item: ExerciseDay) {
            item.exercise.observe(binding.lifecycleOwner!!, {
                binding.tvExerciseName.text = it.name
                FitBuddyRepository(binding.root.context.applicationContext)
                    .getExerciseCategoryFromId(it.category)
                    .observe(binding.lifecycleOwner!!, Observer {
                        binding.tvExerciseCategory.text =
                            binding.tvExerciseCategory.context.getString(
                                R.string.category_format,
                                it
                            )
                    })
            })
            binding.tvRepsNum.text = item.reps.toString()
            binding.tvSetsNum.text = item.sets.toString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent, parentLifecycleOwner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onClickListener.onClick(getItem(position))
        }
    }

    class OnClickListener(val clickListener: (exerciseDay: ExerciseDay) -> Unit) {
        fun onClick(exerciseDay: ExerciseDay) = clickListener(exerciseDay)
    }
}

class ExerciseDayDiffCallback : DiffUtil.ItemCallback<ExerciseDay>() {
    override fun areItemsTheSame(oldItem: ExerciseDay, newItem: ExerciseDay): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ExerciseDay, newItem: ExerciseDay): Boolean {
        return oldItem.id == newItem.id
    }

}