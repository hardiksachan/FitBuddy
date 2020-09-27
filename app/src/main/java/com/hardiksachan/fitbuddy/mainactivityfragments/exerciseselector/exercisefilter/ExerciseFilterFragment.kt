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

        val categoryFilterListAdapter =
            ExerciseCategoryFilterListAdapter(viewLifecycleOwner, sharedViewModel)
        binding.rvCategoryFilter.adapter = categoryFilterListAdapter

        val equipmentFilterListAdapter =
            ExerciseEquipmentFilterListAdapter(viewLifecycleOwner, sharedViewModel)
        binding.rvEquipmentFilter.adapter = equipmentFilterListAdapter

        sharedViewModel.exerciseCategories.observe(
            viewLifecycleOwner, Observer<List<ExerciseCategory>> {
                categoryFilterListAdapter.submitList(it)
            })

        sharedViewModel.equipments.observe(
            viewLifecycleOwner, Observer {
                equipmentFilterListAdapter.submitList(it)
            }
        )

        return binding.root
    }
}