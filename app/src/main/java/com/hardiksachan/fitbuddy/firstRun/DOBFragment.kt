package com.hardiksachan.fitbuddy.firstRun

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hardiksachan.fitbuddy.databinding.FragmentDOBBinding
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel
import java.text.SimpleDateFormat
import java.util.*


class DOBFragment : Fragment() {

    val sharedViewModel: FirstRunActivitySharedViewModel by activityViewModels<FirstRunActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        FirstRunActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDOBBinding.inflate(inflater)

        val myCalendar = Calendar.getInstance()

        val date =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy"

                val sdf = SimpleDateFormat(myFormat, Locale.US)

                binding.etDob.setText(sdf.format(myCalendar.getTime()));
            }

        binding.etDob.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        binding.btnNext.setOnClickListener {
            if(binding.etDob.text.toString() == ""){
                Toast.makeText(context, "Please Enter your DOB", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.user.dob = myCalendar.time
                sharedViewModel.saveUser()
                findNavController()
                    .navigate(DOBFragmentDirections.actionDOBFragmentToHeightWeightFragment())
            }
        }

        return binding.root
    }

}