package com.hardiksachan.fitbuddy.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.hardiksachan.fitbuddy.databinding.FragmentWeightGraphBinding
import com.hardiksachan.fitbuddy.domain.Weight
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class WeightGraphFragment : Fragment() {

    val sharedViewModel: DashboardActivitySharedViewModel by activityViewModels {
        val activity = requireNotNull(this.activity)
        DashboardActivitySharedViewModel.Factory(activity.application)
    }

    val series = LineGraphSeries<DataPoint>()

    val sdf = SimpleDateFormat("dd/MM/YYYY")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentWeightGraphBinding.inflate(inflater)

        binding.wtGraph.addSeries(series)

        series.isDrawDataPoints = true
        series.isDrawBackground = true

        binding.wtGraph.viewport.isXAxisBoundsManual = true

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -3)

        binding.wtGraph.viewport.setMinX(cal.timeInMillis.toDouble())
        binding.wtGraph.viewport.setMaxX(Calendar.getInstance().timeInMillis.toDouble())

        binding.wtGraph.viewport.isScrollable = true
        binding.wtGraph.viewport.setScrollableY(true)

        binding.wtGraph.viewport.isScalable = true
        binding.wtGraph.viewport.setScalableY(true)

        binding.wtGraph.gridLabelRenderer.labelFormatter =
            object : DefaultLabelFormatter() {
                override fun formatLabel(value: Double, isValueX: Boolean): String {
                    return if (isValueX) {
                        sdf.format(Date(value.toLong()))
                    } else {
                        super.formatLabel(value, isValueX)
                    }
                }
            }

        binding.wtGraph.gridLabelRenderer.setHorizontalLabelsAngle(90)

        sharedViewModel.weightList.observe(viewLifecycleOwner, {
            series.resetData(getDataPoints(it))
        })

        return binding.root
    }

    private fun getDataPoints(weights: List<Weight>): Array<DataPoint> {
        val dp = mutableListOf<DataPoint>()

        Timber.d(weights.toString())

        weights.forEach {
            dp.add(DataPoint(it.date, it.weight.toDouble()))
        }

        return dp.toTypedArray()
    }

}