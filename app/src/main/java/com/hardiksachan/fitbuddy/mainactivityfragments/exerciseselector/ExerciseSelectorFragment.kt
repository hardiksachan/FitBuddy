package com.hardiksachan.fitbuddy.mainactivityfragments.exerciseselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseSelectorBinding
import com.hardiksachan.fitbuddy.mainactivityfragments.MainActivitySharedViewModel
import timber.log.Timber


class ExerciseSelectorFragment : Fragment() {

    val viewModel: ExerciseSelectorViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, ExerciseSelectorViewModel.Factory(activity.application))
            .get(ExerciseSelectorViewModel::class.java)
    }

    val sharedViewModel: MainActivitySharedViewModel by activityViewModels<MainActivitySharedViewModel> {
        val activity = requireNotNull(this.activity)
        MainActivitySharedViewModel.Factory(activity.application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentExerciseSelectorBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        val adapter = ExerciseListAdapter(viewLifecycleOwner)
        binding.rvExerciseList.adapter = adapter


        sharedViewModel.eventUpdateExerciseLiveDataObserver.observe(viewLifecycleOwner, Observer {
            if (it) {
                updateExerciseLiveDataBinding(adapter)
                binding.chipActiveFilters.text = context?.getString(
                    R.string.num_active_filters,
                    sharedViewModel.getActiveFilters(null)
                )
                binding.btnClearFilters.setOnClickListener {
                    sharedViewModel.clearFilters(null)
                    binding.chipActiveFilters.text = context?.getString(
                        R.string.num_active_filters,
                        sharedViewModel.getActiveFilters(null)
                    )
                }
            }
        })


        viewModel.navigateToFilter.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.root.findNavController()
                    .navigate(
                        ExerciseSelectorFragmentDirections
                            .actionExerciseSelectorFragmentToExerciseFilterFragment()
                    )
                viewModel.onNavigateToFilterDone()
            }
        })

        binding.exerciseSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Timber.i("Query Changed: $newText")
                sharedViewModel.searchFor(newText)
                return false
            }

        })



        return binding.root
    }

    private fun updateExerciseLiveDataBinding(adapter: ExerciseListAdapter) {
        sharedViewModel.exercises?.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}