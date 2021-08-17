package com.proximity.aqi.custom

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Gagan
 */
class DateFormat : ValueFormatter() {

    // override this for custom formatting of XAxis or YAxis labels
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        val date = Date(value.toLong())
        //Specify the format you'd like
        val sdf = SimpleDateFormat("hh:mm a")
        return sdf.format(date)
    }
    // ... override other methods for the other chart types
}