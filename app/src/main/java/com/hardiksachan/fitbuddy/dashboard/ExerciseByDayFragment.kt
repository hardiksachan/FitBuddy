package com.hardiksachan.fitbuddy.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.hardiksachan.fitbuddy.databinding.ExerciseByDayFragmentBinding
import java.util.*

class ExerciseByDayFragment : Fragment() {

    val viewModel: ExerciseByDayViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ExerciseByDayViewModel.Factory(activity.application))
            .get(ExerciseByDayViewModel::class.java)
    }

    val sharedViewModel: DashboardActivitySharedViewModel by activityViewModels<DashboardActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        DashboardActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ExerciseByDayFragmentBinding.inflate(inflater)

        val adapter = ExerciseByDayViewPagerAdapter(
            viewLifecycleOwner,
            viewModel.fitBuddyRepository,
            sharedViewModel,
            requireActivity() as DashboardActivity
        )
        binding.viewPagerDay.adapter = adapter

        binding.viewPagerDay.setCurrentItem(
            Calendar.getInstance().get(Calendar.DAY_OF_WEEK).minus(2), false
        )

        TabLayoutMediator(binding.tabLayout, binding.viewPagerDay) { tab, pos ->

        }.attach()

        return binding.root
    }

}