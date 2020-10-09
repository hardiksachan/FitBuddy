package com.hardiksachan.fitbuddy.sleep

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentSleepQualityBinding

class SleepQualityFragment : Fragment() {

    val sharedViewModel: SleepActivitySharedViewModel by activityViewModels<SleepActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        SleepActivitySharedViewModel.Factory(activity.application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSleepQualityBinding.inflate(inflater)

        sharedViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if(it == true){
                findNavController().navigate(
                    SleepQualityFragmentDirections
                        .actionSleepQualityFragmentToSleepTrackerFragment()
                )
                sharedViewModel.doneNavigating()
            }
        })

        binding.sleepQualityViewModel = sharedViewModel

        return binding.root
    }
}