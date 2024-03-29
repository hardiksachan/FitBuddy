package com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.exerciseselector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentExerciseSelectorBinding
import com.hardiksachan.fitbuddy.exerciseselectionfragmentsactivity.MainActivitySharedViewModel
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

    lateinit var binding: FragmentExerciseSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExerciseSelectorBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.viewModel = viewModel
        val adapter = ExerciseListAdapter(viewLifecycleOwner, ExerciseListAdapter.OnClickListener {
            viewModel.navigateToDetail(it)
            sharedViewModel.setExerciseToDisplayOnDetail(it)
        })
        binding.rvExerciseList.adapter = adapter

        try {
            val layoutmanager = binding.rvExerciseList.layoutManager as LinearLayoutManager
            val selectedItem = sharedViewModel.getExerciseToDisplayOnDetail()
            layoutmanager.scrollToPositionWithOffset(
                sharedViewModel.exercises?.value?.indexOf(
                    selectedItem
                ) ?: 0, 0
            )
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
        }


        sharedViewModel.eventUpdateExerciseLiveDataObserver.observe(viewLifecycleOwner, {
            if (it) {
                updateExerciseLiveDataBinding(adapter)
                binding.chipActiveFilters.text = context?.getString(
                    R.string.num_active_filter_long,
                    sharedViewModel.getActiveFilters(null)
                )
                binding.btnClearFilters.setOnClickListener {
                    sharedViewModel.clearFilters(null)
                    binding.chipActiveFilters.text = context?.getString(
                        R.string.num_active_filter_long,
                        sharedViewModel.getActiveFilters(null)
                    )
                }
            }
        })

//        sharedViewModel.repositoryUpdated.observe(viewLifecycleOwner, Observer {
//            if (it) {
//                sharedViewModel.updateExercises()
//                sharedViewModel.repositoryUpdateHandled()
//            }
//        })

        viewModel.navigateToFilter.observe(viewLifecycleOwner, {
            if (it) {
                binding.root.findNavController()
                    .navigate(
                        ExerciseSelectorFragmentDirections
                            .actionExerciseSelectorFragmentToExerciseFilterFragment()
                    )
                viewModel.onNavigateToFilterDone()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, {
            if (it) {
                binding.root.findNavController()
                    .navigate(
                        ExerciseSelectorFragmentDirections
                            .actionExerciseSelectorFragmentToExerciseDetailFragment()
                    )
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
            binding.tvResultsFound.text =
                binding.root.context.getString(R.string.num_results_found, it.size)
            binding.noResultsFoundLayout.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }
}