package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseinfo

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.DialogAddExerciseBinding
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseDetailBinding
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivity
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel
import kotlinx.coroutines.launch

class ExerciseDetailFragment : Fragment() {


    val sharedViewModel: MainActivitySharedViewModel by activityViewModels<MainActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        MainActivitySharedViewModel.Factory(activity.application)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseDetailBinding.inflate(inflater)


        val adapter = ExerciseDetailViewPagerAdapter(viewLifecycleOwner)
        binding.viewPagerExerciseDetail.adapter = adapter

        val exerciseList = sharedViewModel.exercises?.value

        adapter.submitList(exerciseList)

        val selectedExercise = sharedViewModel.getExerciseToDisplayOnDetail()
        val selectedExerciseIndex = exerciseList?.indexOf(selectedExercise)

        binding.viewPagerExerciseDetail.setCurrentItem(selectedExerciseIndex ?: 0, false)

        sharedViewModel.exerciseIdsOfSelectedDay.observe(viewLifecycleOwner, {
            val exId = adapter.currentList[binding.viewPagerExerciseDetail.currentItem].id
            if (exId !in it) {
                binding.btnAddExercise.text = getString(R.string.add_exercise)
                binding.btnAddExercise.isClickable = true
            } else {
                binding.btnAddExercise.text = getString(R.string.exercise_selected)
                binding.btnAddExercise.isClickable = false
            }
        })

        binding.viewPagerExerciseDetail.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val exId = adapter.currentList[binding.viewPagerExerciseDetail.currentItem].id
                sharedViewModel.exerciseIdsOfSelectedDay.observe(viewLifecycleOwner, {
                    if (exId !in it) {
                        binding.btnAddExercise.text = getString(R.string.add_exercise)
                        binding.btnAddExercise.isClickable = true
                    } else {
                        binding.btnAddExercise.text = getString(R.string.exercise_selected)
                        binding.btnAddExercise.isClickable = false
                    }
                })
            }
        })

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    binding.root.findNavController()
                        .navigate(
                            ExerciseDetailFragmentDirections
                                .actionExerciseDetailFragmentToExerciseSelectorFragment()
                        )
                }
            })

        binding.btnAddExercise.setOnClickListener {
            val dialogBuilder: AlertDialog = AlertDialog.Builder(context).create()
            val dialogBinding = DialogAddExerciseBinding.inflate(inflater)
            val dialogView: View = dialogBinding.root

            dialogBinding.btnCancel.setOnClickListener(View.OnClickListener { dialogBuilder.dismiss() })
            dialogBinding.btnSubmit.setOnClickListener(View.OnClickListener {
                if (dialogBinding.etReps.text.toString() == "") {
                    Toast.makeText(
                        dialogBinding.root.context,
                        "Please Enter Number Of Reps", Toast.LENGTH_SHORT
                    ).show()
                } else if (dialogBinding.etSets.text.toString() == "") {
                    Toast.makeText(
                        dialogBinding.root.context,
                        "Please Enter Number Of Sets", Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val exId = adapter.currentList[binding.viewPagerExerciseDetail.currentItem].id
                    sharedViewModel.viewModelScope.launch {
                        sharedViewModel.fitBuddyRepository.insertExerciseDayByDay(
                            (requireActivity() as MainActivity).prefs.getInt("exerciseAddDay", -1),
                            exId,
                            dialogBinding.etSets.text.toString().toInt(),
                            dialogBinding.etReps.text.toString().toInt()
                        )
                    }
                    binding.btnAddExercise.text = getString(R.string.exercise_selected)
                    binding.btnAddExercise.isClickable = false
                    dialogBuilder.dismiss()
                }

            })

            dialogBuilder.setView(dialogView)
            dialogBuilder.show()
        }



        return binding.root
    }
}