package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseFilterSpecificBinding
import com.hardiksachan.fitbuddy.domain.ExerciseCategory
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel

class ExerciseFilterSpecificFragment : Fragment() {

    val sharedViewModel: MainActivitySharedViewModel by activityViewModels<MainActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        MainActivitySharedViewModel.Factory(activity.application)
    }

    val viewModel: ExerciseFilterSpecificViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ExerciseFilterSpecificViewModel.Factory(activity.application))
            .get(ExerciseFilterSpecificViewModel::class.java)
    }

    val arguments: ExerciseFilterSpecificFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseFilterSpecificBinding.inflate(inflater)


        val categoryFilterListAdapter =
            ExerciseCategoryFilterListAdapter(viewLifecycleOwner, sharedViewModel)

        val equipmentFilterListAdapter =
            ExerciseEquipmentFilterListAdapter(viewLifecycleOwner, sharedViewModel)

        val primaryMuscleFilterListAdapter =
            ExerciseMuscleFilterListAdapter(viewLifecycleOwner, sharedViewModel, false)
        val secondaryMuscleFilterListAdapter =
            ExerciseMuscleFilterListAdapter(viewLifecycleOwner, sharedViewModel, true)

        sharedViewModel.exerciseCategories.observe(
            viewLifecycleOwner, Observer<List<ExerciseCategory>> {
                categoryFilterListAdapter.submitList(it)
            })

        sharedViewModel.equipments.observe(
            viewLifecycleOwner, Observer {
                equipmentFilterListAdapter.submitList(it)
            }
        )

        sharedViewModel.muscles.observe(
            viewLifecycleOwner, Observer {
                primaryMuscleFilterListAdapter.submitList(it)
                secondaryMuscleFilterListAdapter.submitList(it)
            }
        )

        binding.tvSpecificFilterName.text = arguments.filterBy.type

        binding.rvSpecificFilter.adapter = when (arguments.filterBy) {
            FilterByWhat.Category -> categoryFilterListAdapter
            FilterByWhat.Equipment -> equipmentFilterListAdapter
            FilterByWhat.PrimaryMuscle -> primaryMuscleFilterListAdapter
            FilterByWhat.SecondaryMuscle -> secondaryMuscleFilterListAdapter
        }

        return binding.root
    }
}