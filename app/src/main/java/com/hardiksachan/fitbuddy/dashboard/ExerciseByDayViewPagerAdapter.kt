package com.hardiksachan.fitbuddy.dashboard

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hardiksachan.fitbuddy.databinding.Viewpager2ItemExerciseByDayBinding
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivity
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.DayOfWeek


class ExerciseByDayViewPagerAdapter(
    private val parentLifecycleOwner: LifecycleOwner,
    private val repository: FitBuddyRepository,
    private val sharedViewModel: DashboardActivitySharedViewModel,
    private val activity: DashboardActivity
) :
    RecyclerView.Adapter<ExerciseByDayViewPagerAdapter.ViewHolder>() {

    class ViewHolder private constructor(
        private val binding: Viewpager2ItemExerciseByDayBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: DayOfWeek,
            repository: FitBuddyRepository,
            activity: DashboardActivity,
            sharedViewModel: DashboardActivitySharedViewModel
        ) {
            binding.tvDayName.text = item.name

            val adapter = ExerciseListByDayAdapter(binding.lifecycleOwner!!,
                ExerciseListByDayAdapter.OnClickListener {
                it.exercise.observe(binding.lifecycleOwner!!, {
                    sharedViewModel.exercise = it
                    binding.root.findNavController()
                        .navigate(
                            ExerciseByDayFragmentDirections
                                .actionExerciseByDayFragmentToSelectedExerciseDetailFragment()
                        )
                })
            }, sharedViewModel)
            binding.rvExerciseListByDay.adapter = adapter

            repository.getExerciseDayByDay(item.ordinal).observe(binding.lifecycleOwner!!, {
                adapter.submitList(it)
            })

            binding.btnAddExercise.setOnClickListener {
                activity.prefs.edit().putInt("exerciseAddDay", item.ordinal).apply()
                val intent = Intent(activity, MainActivity::class.java)
                activity.startActivity(intent)
            }
        }

        companion object {

            fun from(parent: ViewGroup, parentLifecycleOwner: LifecycleOwner): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = Viewpager2ItemExerciseByDayBinding.inflate(
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
        val day = DayOfWeek.values()[position]
        holder.bind(day, repository,  activity, sharedViewModel)
    }

    override fun getItemCount(): Int {
        return DayOfWeek.values().size
    }
}