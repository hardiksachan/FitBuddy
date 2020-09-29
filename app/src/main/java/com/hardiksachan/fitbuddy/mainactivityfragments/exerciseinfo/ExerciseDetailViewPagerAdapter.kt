package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseinfo

import android.text.Html
import android.transition.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.Viewpager2ItemExerciseDetailBinding
import com.hardiksachan.fitbuddy.domain.Equipment
import com.hardiksachan.fitbuddy.domain.Exercise
import com.hardiksachan.fitbuddy.domain.Muscle
import com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector.ExerciseDiffCallback
import com.hardiksachan.fitbuddy.repository.FitBuddyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ExerciseDetailViewPagerAdapter(val parentLifecycleOwner: LifecycleOwner) :
    ListAdapter<Exercise, ExerciseDetailViewPagerAdapter.ViewHolder>(ExerciseDiffCallback()) {
    class ViewHolder private constructor(val binding: Viewpager2ItemExerciseDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {

            fun from(parent: ViewGroup, parentLifecycleOwner: LifecycleOwner): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    Viewpager2ItemExerciseDetailBinding.inflate(layoutInflater, parent, false)
                binding.lifecycleOwner = parentLifecycleOwner
                return ViewHolder(binding)
            }
        }

        private val viewHolderJob = Job()
        private val viewHolderScope = CoroutineScope(viewHolderJob + Dispatchers.IO)

        fun bind(item: Exercise) {
//            binding.exerciseItem = item
            binding.tvName.text = item.name

            FitBuddyRepository(binding.root.context.applicationContext).getExerciseCategoryFromId(
                item.category
            )
                .observe(binding.lifecycleOwner!!, Observer {
                    binding.chipCategory.text = it
                })

            item.equipment.observe(binding.lifecycleOwner!!, object : Observer<List<Equipment>> {
                override fun onChanged(data: List<Equipment>?) {

                    val chipGroup = binding.chipGroupEquipments
                    val inflator = LayoutInflater.from(chipGroup.context)

                    if (data == null) {
                        chipGroup.removeAllViews()
                        val chip = inflator.inflate(R.layout.simple_chip, chipGroup, false) as Chip
                        chip.text = binding.root.context.getString(R.string.none_item)
                        chipGroup.addView(chip)
                    } else {

                        val children = data.map {
                            val chip =
                                inflator.inflate(R.layout.simple_chip, chipGroup, false) as Chip
                            chip.text = it.name
                            chip
                        }

                        chipGroup.removeAllViews()

                        for (chip in children) {
                            chipGroup.addView(chip)
                        }
                    }
                }
            })

            item.muscles.observe(binding.lifecycleOwner!!, object : Observer<List<Muscle>> {
                override fun onChanged(data: List<Muscle>?) {

                    val chipGroup = binding.chipGroupPrimaryMuscles
                    val inflator = LayoutInflater.from(chipGroup.context)

                    if (data == null) {
                        chipGroup.removeAllViews()
                        val chip = inflator.inflate(R.layout.simple_chip, chipGroup, false) as Chip
                        chip.text = binding.root.context.getString(R.string.none_item)
                        chipGroup.addView(chip)
                    } else {

                        val children = data.map {
                            val chip =
                                inflator.inflate(R.layout.simple_chip, chipGroup, false) as Chip
                            chip.text = it.name
                            chip
                        }

                        chipGroup.removeAllViews()

                        for (chip in children) {
                            chipGroup.addView(chip)
                        }
                    }
                }
            })

            item.muscles_secondary.observe(
                binding.lifecycleOwner!!,
                object : Observer<List<Muscle>> {
                    override fun onChanged(data: List<Muscle>?) {
                        val chipGroup = binding.chipGroupSecondaryMuscles
                        val inflator = LayoutInflater.from(chipGroup.context)

                        if (data == null) {
                            chipGroup.removeAllViews()
                            val chip =
                                inflator.inflate(R.layout.simple_chip, chipGroup, false) as Chip
                            chip.text = binding.root.context.getString(R.string.none_item)
                            chipGroup.addView(chip)
                        } else {

                            val children = data.map {
                                val chip =
                                    inflator.inflate(R.layout.simple_chip, chipGroup, false) as Chip
                                chip.text = it.name
                                chip
                            }

                            chipGroup.removeAllViews()

                            for (chip in children) {
                                chipGroup.addView(chip)
                            }
                        }
                    }
                })

            if (item.description == ""){
                binding.wvDescription.visibility = View.GONE
            } else {
                binding.wvDescription.visibility = View.VISIBLE
                binding.wvDescription.text = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Html.fromHtml(item.description,Html.FROM_HTML_MODE_LEGACY)
                } else {
                   Html.fromHtml(item.description)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent, parentLifecycleOwner)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}