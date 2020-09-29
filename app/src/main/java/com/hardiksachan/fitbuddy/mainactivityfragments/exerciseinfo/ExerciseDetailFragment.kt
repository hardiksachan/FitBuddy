package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseDetailBinding
import com.hardiksachan.fitbuddy.mainactivityfragments.MainActivitySharedViewModel

class ExerciseDetailFragment : Fragment() {


    val sharedViewModel: MainActivitySharedViewModel by activityViewModels<MainActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        MainActivitySharedViewModel.Factory(activity.application)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseDetailBinding.inflate(inflater)


        val adapter = ExerciseDetailViewPagerAdapter(viewLifecycleOwner)
        binding.viewPagerExerciseDetail.adapter = adapter

        val exerciseList = sharedViewModel.exercises?.value

        adapter.submitList(exerciseList)

        val selectedExercise = sharedViewModel.getExerciseToDisplayOnDetail()
        val selectedExerciseIndex = exerciseList?.indexOf(selectedExercise)

        binding.viewPagerExerciseDetail.setCurrentItem(selectedExerciseIndex ?: 0, false)

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding.root.findNavController()
                    .navigate(
                        ExerciseDetailFragmentDirections
                            .actionExerciseDetailFragmentToExerciseSelectorFragment()
                    )
            }
        })


        return binding.root
    }
}