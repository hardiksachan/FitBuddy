package com.hardiksachan.fitbuddy.firstRun

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentGenderBinding
import kotlinx.android.synthetic.main.fragment_gender.*
import kotlinx.android.synthetic.main.fragment_name.*
import kotlinx.android.synthetic.main.fragment_name.btn_next

class GenderFragment : Fragment() {

    val sharedViewModel: FirstRunActivitySharedViewModel by activityViewModels<FirstRunActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        FirstRunActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGenderBinding.inflate(inflater)

        binding.btnNext.setOnClickListener {
            when {
                binding.radBtnMale.isChecked -> {
                    sharedViewModel.user.gender = 1
                    findNavController().navigate(
                        GenderFragmentDirections
                            .actionGenderFragmentToDOBFragment()
                    )
                }
                binding.radBtnFemale.isChecked -> {
                    sharedViewModel.user.gender = 2
                    findNavController().navigate(
                        GenderFragmentDirections
                            .actionGenderFragmentToDOBFragment()
                    )
                }
                else -> {
                    Toast.makeText(context, "Please Select your gender", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }
}