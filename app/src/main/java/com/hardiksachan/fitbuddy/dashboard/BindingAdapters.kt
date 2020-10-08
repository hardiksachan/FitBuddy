package com.hardiksachan.fitbuddy.dashboard

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hardiksachan.fitbuddy.R
import java.util.*
import java.util.Calendar.*

@BindingAdapter("ageFromDate")
fun bindAge(textView: TextView, date: Date?) {
    date.let {
        textView.text = textView.context.getString(
            R.string.age_format_years,
            getDiffYears(Calendar.getInstance().time, date!!)
        )
    }
}

@BindingAdapter("genderText")
fun bindGender(textView: TextView, i: Int?) {
    textView.text = when (i) {
        1 -> "Male"
        2 -> "Female"
        else -> "ERROR!!"
    }
}

fun getDiffYears(first: Date, last: Date): Int {
    val a = getCalendar(first)
    val b = getCalendar(last)
    var diff = b[YEAR] - a[YEAR]
    if (a[MONTH] > b[MONTH] ||
        a[MONTH] == b[MONTH] && a[DATE] > b[DATE]
    ) {
        diff--
    }
    return diff
}

fun getCalendar(date: Date): Calendar {
    val cal = Calendar.getInstance(Locale.US)
    cal.time = date
    return cal
}