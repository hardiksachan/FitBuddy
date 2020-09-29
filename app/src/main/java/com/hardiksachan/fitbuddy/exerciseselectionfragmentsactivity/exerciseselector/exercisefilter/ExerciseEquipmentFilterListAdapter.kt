package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.checkbox.MaterialCheckBox
import com.hardiksachan.fitbuddy.databinding.RecyclerviewItemExerciseCategoryFilterBinding
import com.hardiksachan.fitbuddy.domain.Equipment
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel

class ExerciseEquipmentFilterListAdapter(
    private val parentLifecycleOwner: LifecycleOwner,
    private val sharedViewModel: MainActivitySharedViewModel
) :
    ListAdapter<Equipment, ExerciseEquipmentFilterListAdapter.ViewHolder>(ExerciseEquipmentFilterDiffCallback()) {
    class ViewHolder private constructor(private val binding: RecyclerviewItemExerciseCategoryFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Equipment, sharedViewModel: MainActivitySharedViewModel) {
            binding.checkBoxCategoryFilter.text = item.name
            binding.checkBoxCategoryFilter.tag = item.id
            binding.checkBoxCategoryFilter.isChecked = item.id in sharedViewModel.equipmentFilterList

            binding.checkBoxCategoryFilter.setOnClickListener {
                it as MaterialCheckBox
                sharedViewModel.onEquipmentFilterChanged(
                    it.tag as Int,
                    it.isChecked
                )
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
        holder.bind(getItem(position), sharedViewModel)
    }
}

class ExerciseEquipmentFilterDiffCallback : DiffUtil.ItemCallback<Equipment>() {
    override fun areItemsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Equipment, newItem: Equipment): Boolean {
        return oldItem.id == oldItem.id
    }

}