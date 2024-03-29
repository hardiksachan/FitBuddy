package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector.exercisefilter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseFilterByWhatBinding
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel

enum class FilterByWhat(val type: String) {
    Category("Category"),
    Equipment("Equipment"),
    PrimaryMuscle("Primary Muscle"),
    SecondaryMuscle("Secondary Muscle"),
}

class ExerciseFilterByWhatFragment : Fragment() {

    val viewModel: ExerciseFilterByWhatViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ExerciseFilterByWhatViewModel.Factory(activity.application))
            .get(ExerciseFilterByWhatViewModel::class.java)
    }

    val sharedViewModel: MainActivitySharedViewModel by activityViewModels<MainActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        MainActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseFilterByWhatBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        val adapter = ExerciseFilterByWhatListAdapter(
            viewLifecycleOwner,
            sharedViewModel,
            ExerciseFilterByWhatListAdapter.OnClickListener {
                viewModel.onFilterSpecificTitleClicked(it)
            })
        binding.rvFilterByWhat.adapter = adapter

        viewModel.navigateToSpecificFilter.observe(
            viewLifecycleOwner, Observer {
                if (it != null) {
                    binding.root.findNavController()
                        .navigate(
                            ExerciseFilterByWhatFragmentDirections
                                .actionExerciseFilterFragmentToExerciseFilterSpecificFragment(it)
                        )
                    viewModel.navigatedToSpecificFilter()
                }
            }
        )


        return binding.root
    }
}