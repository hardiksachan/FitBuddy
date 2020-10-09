package com.hardiksachan.fitbuddy.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hardiksachan.fitbuddy.databinding.FragmentSleepTrackerBinding


class SleepTrackerFragment : Fragment() {

    lateinit var binding: FragmentSleepTrackerBinding

    val sharedViewModel: SleepActivitySharedViewModel by activityViewModels<SleepActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        SleepActivitySharedViewModel.Factory(activity.application)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSleepTrackerBinding.inflate(inflater)

        binding.sharedViewModel = sharedViewModel

        binding.btnStart.setOnClickListener {
            sharedViewModel.onStartTracking()
        }
        binding.btnStop.setOnClickListener {
            sharedViewModel.onStopTracking()
            findNavController()
                .navigate(
                    SleepTrackerFragmentDirections
                        .actionSleepTrackerFragmentToSleepQualityFragment()
                )
        }

        binding.btnClear.setOnClickListener {
            sharedViewModel.onClear()
        }

        sharedViewModel.startButtonVisible.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.btnStart.visibility = View.VISIBLE
                binding.btnStop.visibility = View.GONE
            } else {
                binding.btnStart.visibility = View.GONE
                binding.btnStop.visibility = View.VISIBLE
            }
        })

        sharedViewModel.clearButtonVisible.observe(viewLifecycleOwner, Observer {
            binding.btnClear.visibility = if (it == true) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })

        sharedViewModel.nightsString.observe(viewLifecycleOwner, {
            binding.sleepHistory.text = it
        })

        return binding.root
    }
}