package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.RecylerviewItemCardFilterTypeBinding
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel

class ExerciseFilterByWhatListAdapter(
    private val parentLifecycleOwner: LifecycleOwner,
    private val sharedViewModel: MainActivitySharedViewModel,
    val onClickListener: OnClickListener
) :
    RecyclerView.Adapter<ExerciseFilterByWhatListAdapter.ViewHolder>() {
    class ViewHolder private constructor(private val binding: RecylerviewItemCardFilterTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FilterByWhat, sharedViewModel: MainActivitySharedViewModel) {
            binding.tvEquipmentHeader.text = item.type
            binding.chipActiveFilters.text = binding.chipActiveFilters.context.getString(
                R.string.num_active_filters,
                sharedViewModel.getActiveFilters(item)
            )
            binding.btnClearFilters.setOnClickListener {
                sharedViewModel.clearFilters(item)
                binding.chipActiveFilters.text = binding.chipActiveFilters.context.getString(
                    R.string.num_active_filters,
                    sharedViewModel.getActiveFilters(item)
                )
            }
        }

        companion object {

            fun from(parent: ViewGroup, parentLifecycleOwner: LifecycleOwner): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecylerviewItemCardFilterTypeBinding.inflate(
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
        val filter = FilterByWhat.values()[position]
        holder.bind(filter, sharedViewModel)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(filter)
        }
    }

    override fun getItemCount(): Int {
        return FilterByWhat.values().size
    }

    class OnClickListener(val clickListener: (filter: FilterByWhat) -> Unit){
        fun onClick(filter: FilterByWhat) = clickListener(filter)
    }
}