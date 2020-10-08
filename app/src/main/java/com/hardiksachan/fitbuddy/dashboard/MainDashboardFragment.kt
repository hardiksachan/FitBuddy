package com.hardiksachan.fitbuddy.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.DialogWithTextBoxBinding
import com.hardiksachan.fitbuddy.databinding.FragmentMainDashboardBinding
import java.util.*


class MainDashboardFragment : Fragment() {

    lateinit var binding: FragmentMainDashboardBinding

    val viewModel: MainDashboardViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, MainDashboardViewModel.Factory(activity.application))
            .get(MainDashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainDashboardBinding.inflate(inflater)

        viewModel.user.observe(viewLifecycleOwner, {
            binding.tvUsername.text = it?.name ?: "John Doe"
            binding.tvUserAge.text = requireContext().getString(
                R.string.age_format_years,
                getDiffYears(it.dob, Calendar.getInstance().time)
            )
            binding.tvUserGender.text = when (it.gender) {
                1 -> "Male"
                2 -> "Female"
                else -> "ERROR!!"
            }
        })

        viewModel.currentHeight.observe(viewLifecycleOwner, {
            binding.tvUserHt.text = it.toString()
        })

        viewModel.currentWeight.observe(viewLifecycleOwner, {
            binding.tvUserWt.text = it.toString()
        })


        binding.btnUpdateHeight.setOnClickListener {
            val dialogBuilder: AlertDialog = AlertDialog.Builder(context).create()
            val dialogBinding = DialogWithTextBoxBinding.inflate(inflater)
            val dialogView: View = dialogBinding.root

            dialogBinding.tvHeading.text = "HEIGHT"
            dialogBinding.etValue.hint = "Height"
            dialogBinding.etValue.inputType = InputType.TYPE_CLASS_NUMBER
            dialogBinding.btnCancel.setOnClickListener(View.OnClickListener { dialogBuilder.dismiss() })
            dialogBinding.btnSubmit.setOnClickListener(View.OnClickListener {
                if (dialogBinding.etValue.text.toString() != "") {
                    viewModel.updateHeight(listOf(dialogBinding.etValue.text.toString().toInt()))
                    dialogBuilder.dismiss()
                } else {
                    Toast.makeText(
                        dialogBinding.root.context,
                        "Please Set Height",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            dialogBuilder.setView(dialogView)
            dialogBuilder.show()
        }

        binding.btnUpdateWeight.setOnClickListener {
            val dialogBuilder: AlertDialog = AlertDialog.Builder(context).create()
            val dialogBinding = DialogWithTextBoxBinding.inflate(inflater)
            val dialogView: View = dialogBinding.root

            dialogBinding.tvHeading.text = "WEIGHT"
            dialogBinding.etValue.hint = "Weight"
            dialogBinding.btnCancel.setOnClickListener(View.OnClickListener { dialogBuilder.dismiss() })
            dialogBinding.btnSubmit.setOnClickListener(View.OnClickListener {
                if (dialogBinding.etValue.text.toString() != "") {
                    viewModel.updateWeight(listOf(dialogBinding.etValue.text.toString().toFloat()))
                    dialogBuilder.dismiss()
                } else {
                    Toast.makeText(
                        dialogBinding.root.context,
                        "Please Set Weight",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            dialogBuilder.setView(dialogView)
            dialogBuilder.show()
        }

        return binding.root
    }
}