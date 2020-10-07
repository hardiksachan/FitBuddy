package com.hardiksachan.fitbuddy.firstRun

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.dashboard.DashboardActivity
import com.hardiksachan.fitbuddy.databinding.FragmentHeightWeightBinding

class HeightWeightFragment : Fragment() {

    val prefs by lazy {
        requireActivity().getSharedPreferences("com.hardiksachan.fitbuddy", MODE_PRIVATE)
    }

    val sharedViewModel: FirstRunActivitySharedViewModel by activityViewModels<FirstRunActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        FirstRunActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHeightWeightBinding.inflate(inflater)


        binding.btnNext.setOnClickListener {
            if (binding.etHeight.text.toString() != "" &&
                    binding.etWeight.text.toString() != "") {
                // set in database
                sharedViewModel.saveHeight(listOf(binding.etHeight.text.toString().toInt()))
                sharedViewModel.saveWeight(listOf(binding.etWeight.text.toString().toInt()))

                // Save first run done in prefs
                prefs.edit().putBoolean("firstrun", false).apply()

                // Start Dashboard Activity
                val intent = Intent(requireActivity(), DashboardActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            } else {
                Toast.makeText(context, "Please Enter all the details", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

}