package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseSelectorBinding
import androidx.lifecycle.Observer
import com.google.android.material.chip.Chip
import com.hardiksachan.fitbuddy.domain.ExerciseCategory
import com.hardiksachan.fitbuddy.R

class ExerciseSelectorFragment : Fragment() {

    val viewModel: ExerciseSelectorViewModel by lazy {
        val activity = requireNotNull(this.activity) {

        }
        ViewModelProvider(this, ExerciseSelectorViewModel.Factory(activity.application))
            .get(ExerciseSelectorViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseSelectorBinding.inflate(inflater, container, false)

        val adapter = ExerciseListAdapter(viewLifecycleOwner)
        binding.rvExerciseList.adapter = adapter

        viewModel.eventUpdateExerciseLiveDataObserver.observe(viewLifecycleOwner, Observer {
            updateExerciseLiveDataBinding(adapter)
        })

        viewModel.exerciseCategories.observe(
            viewLifecycleOwner,
            object : Observer<List<ExerciseCategory>> {
                override fun onChanged(data: List<ExerciseCategory>?) {
                    data ?: return

                    val chipGroup = binding.chipviewCategoryFilterList
                    val layoutInflater = LayoutInflater.from(chipGroup.context)

                    val children = data.map {
                        val chip = layoutInflater.inflate(
                            R.layout.filter_chip,
                            chipGroup, false
                        ) as Chip
                        chip.text = it.name
                        chip.tag = it.id
                        chip.setOnCheckedChangeListener { button, isChecked ->
                            viewModel.onCategoryFilterChanged(button.tag as Int, isChecked)
                        }
                        chip
                    }
                    chipGroup.removeAllViews()

                    for (chip in children) {
                        chipGroup.addView(chip)
                    }
                }

            })

        return binding.root
    }

    private fun updateExerciseLiveDataBinding(adapter: ExerciseListAdapter) {
        viewModel.exercises?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}