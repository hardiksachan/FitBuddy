package com.hardiksachan.fitbuddy.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentSelectedExerciseDetailBinding
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseinfo.ExerciseDetailViewPagerAdapter


class SelectedExerciseDetailFragment : Fragment() {

    val sharedViewModel: DashboardActivitySharedViewModel by activityViewModels {
        val activity = requireNotNull(this.activity)
        DashboardActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectedExerciseDetailBinding.inflate(inflater)

        val adapter = ExerciseDetailViewPagerAdapter(viewLifecycleOwner)
        binding.viewPagerExerciseDetail.adapter = adapter

        adapter.submitList(listOf(sharedViewModel.exercise))

        return binding.root
    }

}