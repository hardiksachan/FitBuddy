package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.chip.Chip
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseSelectorBinding
import com.hardiksachan.fitbuddy.domain.ExerciseCategory
import com.hardiksachan.fitbuddy.mainactivityfragments.MainActivitySharedViewModel

class ExerciseSelectorFragment : Fragment() {

    val viewModel: ExerciseSelectorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ExerciseSelectorViewModel.Factory(activity.application))
            .get(ExerciseSelectorViewModel::class.java)
    }

    val sharedViewModel: MainActivitySharedViewModel by activityViewModels<MainActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        MainActivitySharedViewModel.Factory(activity.application)
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
        binding.viewModel = viewModel
        val adapter = ExerciseListAdapter(viewLifecycleOwner)
        binding.rvExerciseList.adapter = adapter


        sharedViewModel.eventUpdateExerciseLiveDataObserver.observe(viewLifecycleOwner, Observer {
            updateExerciseLiveDataBinding(adapter)
        })

        sharedViewModel.exerciseCategories.observe(
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
                        chip.isChecked = chip.tag in sharedViewModel.categoryFilterList
                        chip.setOnCheckedChangeListener { button, isChecked ->
                            sharedViewModel.onCategoryFilterChanged(
                                button.tag as Int,
                                isChecked
                            )
                        }
                        chip
                    }
                    chipGroup.removeAllViews()

                    for (chip in children) {
                        chipGroup.addView(chip)
                    }
                }
            })

        viewModel.navigateToFilter.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.root.findNavController()
                    .navigate(
                        ExerciseSelectorFragmentDirections
                            .actionExerciseSelectorFragmentToExerciseFilterFragment()
                    )
                viewModel.onNavigateToFilterDone()
            }
        })

        return binding.root
    }

    private fun updateExerciseLiveDataBinding(adapter: ExerciseListAdapter) {
        sharedViewModel.exercises?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}