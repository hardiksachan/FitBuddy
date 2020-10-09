package com.hardiksachan.fitbuddy.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hardiksachan.fitbuddy.R
import com.hardiksachan.fitbuddy.databinding.FragmentHeightGraphBinding
import com.hardiksachan.fitbuddy.databinding.FragmentWeightGraphBinding
import com.hardiksachan.fitbuddy.domain.Height
import com.hardiksachan.fitbuddy.domain.Weight
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class HeightGraphFragment : Fragment() {
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
        val binding = FragmentHeightGraphBinding.inflate(inflater)

        binding.htGraph.addSeries(series)

        series.isDrawDataPoints = true
        series.isDrawBackground = true

        binding.htGraph.viewport.isXAxisBoundsManual = true

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -3)

        binding.htGraph.viewport.setMinX(cal.timeInMillis.toDouble())
        binding.htGraph.viewport.setMaxX(Calendar.getInstance().timeInMillis.toDouble())

        binding.htGraph.viewport.isScrollable = true
        binding.htGraph.viewport.setScrollableY(true)

        binding.htGraph.viewport.isScalable = true
        binding.htGraph.viewport.setScalableY(true)

        binding.htGraph.gridLabelRenderer.labelFormatter =
            object : DefaultLabelFormatter() {
                override fun formatLabel(value: Double, isValueX: Boolean): String {
                    return if (isValueX) {
                        sdf.format(Date(value.toLong()))
                    } else {
                        super.formatLabel(value, isValueX)
                    }
                }
            }

        binding.htGraph.gridLabelRenderer.setHorizontalLabelsAngle(90)

        sharedViewModel.heightList.observe(viewLifecycleOwner, {
            series.resetData(getDataPoints(it))
        })

        return binding.root
    }

    private fun getDataPoints(weights: List<Height>): Array<DataPoint> {
        val dp = mutableListOf<DataPoint>()

        Timber.d(weights.toString())

        weights.forEach {
            dp.add(DataPoint(it.date, it.height.toDouble()))
        }

        return dp.toTypedArray()
    }
}