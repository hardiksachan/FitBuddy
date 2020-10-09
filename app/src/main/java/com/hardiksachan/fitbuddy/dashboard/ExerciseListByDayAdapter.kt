package com.hardiksachan.fitbuddy.dashboard

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.DialogAddExerciseBinding
import com.hardiksachan.fitbuddy.databinding.RecyclervireItemExerciseByDayBinding
import com.hardiksachan.fitbuddy.domain.ExerciseDay
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository


class ExerciseListByDayAdapter(
    val parentLifecycleOwner: LifecycleOwner,
    val onClickListener: OnClickListener,
    private val sharedViewModel: DashboardActivitySharedViewModel
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

        fun bind(item: ExerciseDay, sharedViewModel: DashboardActivitySharedViewModel) {
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


            binding.ivDeleteExerciseDay.setOnClickListener {
                val alertDialog: AlertDialog.Builder = AlertDialog.Builder(binding.root.context)
                alertDialog.setTitle("Confirm Delete")
                alertDialog.setMessage("Do you want to delete this exercise?")
                alertDialog.setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    sharedViewModel.deleteExerciseDay(item.id)
                }
                alertDialog.setNegativeButton(
                    "No"
                ) { _, _ -> }
                val alert: AlertDialog = alertDialog.create()
                alert.setCanceledOnTouchOutside(true)
                alert.show()
            }

            binding.btnEditSetsReps.setOnClickListener {
                val dialogBuilder: AlertDialog = AlertDialog.Builder(binding.root.context).create()
                val dialogBinding =
                    DialogAddExerciseBinding.inflate(LayoutInflater.from(binding.root.context))
                val dialogView: View = dialogBinding.root

                dialogBinding.tvHeading.text = binding.root.context.getString(R.string.update)

                dialogBinding.etSets.setText(item.sets.toString())
                dialogBinding.etReps.setText(item.reps.toString())

                dialogBinding.btnCancel.setOnClickListener(View.OnClickListener { dialogBuilder.dismiss() })
                dialogBinding.btnSubmit.setOnClickListener(View.OnClickListener {
                    if (dialogBinding.etReps.text.toString() == "") {
                        Toast.makeText(
                            dialogBinding.root.context,
                            "Please Enter Number Of Reps", Toast.LENGTH_SHORT
                        ).show()
                    } else if (dialogBinding.etSets.text.toString() == "") {
                        Toast.makeText(
                            dialogBinding.root.context,
                            "Please Enter Number Of Sets", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        item.sets = dialogBinding.etSets.text.toString().toInt()
                        item.reps = dialogBinding.etReps.text.toString().toInt()
                        sharedViewModel.updateExerciseSetsReps(item)
                        dialogBuilder.dismiss()
                    }
                })

                dialogBuilder.setView(dialogView)
                dialogBuilder.show()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent, parentLifecycleOwner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), sharedViewModel)
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