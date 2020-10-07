package com.hardiksachan.fitbuddy.firstRun

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentNameBinding
import kotlinx.android.synthetic.main.fragment_name.*

class NameFragment : Fragment() {

    val sharedViewModel: FirstRunActivitySharedViewModel by activityViewModels<FirstRunActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        FirstRunActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNameBinding.inflate(inflater)

        binding.btnNext.setOnClickListener {
            if(binding.etName.text.toString() == ""){
                Toast.makeText(context, "Please Enter your name", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.user.name = binding.etName.text.toString()
                findNavController().navigate(
                    NameFragmentDirections.actionNameFragmentToGenderFragment()
                )
            }
        }

        return binding.root
    }
}