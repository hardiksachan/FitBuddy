package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector.exercisefilter

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
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseFilterBinding
import com.hardiksachan.fitbuddy.domain.ExerciseCategory
import com.hardiksachan.fitbuddy.mainactivityfragments.MainActivitySharedViewModel

class ExerciseFilterFragment : Fragment() {

    val viewModel: ExerciseFilterViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ExerciseFilterViewModel.Factory(activity.application))
            .get(ExerciseFilterViewModel::class.java)
    }

    val sharedViewModel: MainActivitySharedViewModel by activityViewModels<MainActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        MainActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseFilterBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        viewModel.navigateToSelector.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.root.findNavController()
                    .navigate(
                        ExerciseFilterFragmentDirections
                            .actionExerciseFilterFragmentToExerciseSelectorFragment()
                    )
            }
        })

        sharedViewModel.exerciseCategories.observe(
            viewLifecycleOwner,
            object : Observer<List<ExerciseCategory>> {
                override fun onChanged(data: List<ExerciseCategory>?) {
                    data ?: return

                    val chipGroup = binding.cvCategoryFilterList
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
                            sharedViewModel.onCategoryFilterChanged(button.tag as Int, isChecked)
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
}