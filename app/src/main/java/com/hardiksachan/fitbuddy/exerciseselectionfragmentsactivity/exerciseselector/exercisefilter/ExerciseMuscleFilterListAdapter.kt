package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.hardiksachan.fitbuddy.databinding.RecyclerviewItemExerciseCategoryFilterBinding
import com.hardiksachan.fitbuddy.domain.Muscle
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel

class ExerciseMuscleFilterListAdapter(
    private val parentLifecycleOwner: LifecycleOwner,
    private val sharedViewModel: MainActivitySharedViewModel,
    private val isSecondary: Boolean
) :
    ListAdapter<Muscle, ExerciseMuscleFilterListAdapter.ViewHolder>(
        ExerciseMuscleFilterDiffCallback()
    ) {
    class ViewHolder private constructor(private val binding: RecyclerviewItemExerciseCategoryFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Muscle, sharedViewModel: MainActivitySharedViewModel, isSecondary: Boolean) {
            binding.checkBoxCategoryFilter.text = item.name
            binding.checkBoxCategoryFilter.tag = item.id
            binding.checkBoxCategoryFilter.isChecked = when (isSecondary) {
                true -> item.id in sharedViewModel.secondaryMuscleFilterList
                false -> item.id in sharedViewModel.primaryMuscleFilterList
            }
            binding.checkBoxCategoryFilter.setOnClickListener {
                it as MaterialCheckBox
                if (isSecondary) {
                    sharedViewModel.onSecndaryMuscleFilterChanged(
                        it.tag as Int,
                        it.isChecked
                    )
                } else {
                    sharedViewModel.onprimaryMuscleFilterChanged(
                        it.tag as Int,
                        it.isChecked
                    )
                }
            }
        }


        companion object {

            fun from(parent: ViewGroup, parentLifecycleOwner: LifecycleOwner): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerviewItemExerciseCategoryFilterBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                binding.lifecycleOwner = parentLifecycleOwner
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, parentLifecycleOwner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), sharedViewModel, isSecondary)
    }
}

class ExerciseMuscleFilterDiffCallback : DiffUtil.ItemCallback<Muscle>() {
    override fun areItemsTheSame(oldItem: Muscle, newItem: Muscle): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Muscle, newItem: Muscle): Boolean {
        return oldItem.id == oldItem.id
    }

}