package com.hardiksachan.fitbuddy.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hardiksachan.fitbuddy.databinding.ExerciseByDayFragmentBinding
import java.util.*

class ExerciseByDayFragment : Fragment() {

    val viewModel: ExerciseByDayViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ExerciseByDayViewModel.Factory(activity.application))
            .get(ExerciseByDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ExerciseByDayFragmentBinding.inflate(inflater)

        val adapter = ExerciseByDayViewPagerAdapter(
            viewLifecycleOwner,
            viewModel.fitBuddyRepository,
            viewModel.viewModelScope,
            requireActivity() as DashboardActivity
        )
        binding.viewPagerDay.adapter = adapter

        binding.viewPagerDay.setCurrentItem(
            Calendar.getInstance().get(Calendar.DAY_OF_WEEK).minus(2), false
        )

        return binding.root
    }

}