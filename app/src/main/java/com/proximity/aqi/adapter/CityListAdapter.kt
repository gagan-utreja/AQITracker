package com.proximity.aqi.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.baruckis.cryptolive.database.CityNameEntity
import com.proximity.aqi.R
import com.proximity.aqi.databinding.CityAqiRowBinding
import com.proximity.aqi.ui.LineChartFragment
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class CityListAdapter :
    RecyclerView.Adapter<CityListAdapter.DataViewHolder>() {

    var resultsList = arrayListOf<CityNameEntity>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var mContext: Context? = null
    fun setContext(ctx: Context) {
        mContext = ctx
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder.newInstance(parent)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.update(resultsList.get(position))

        holder.binding.parentLayout.setOnClickListener {
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(
                    R.id.container,
                    LineChartFragment.newInstance(holder.binding.cityNameTextview.text.toString())
                ).addToBackStack(null)
                .commit()
        }

    }

    override fun getItemCount(): Int {
        return resultsList.size
    }


    class DataViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding = CityAqiRowBinding.bind(itemView)

        fun update(data: CityNameEntity) {
            binding.cityNameTextview.text = data.cityName
            binding.timeTextView.text = lastUpdatedTime(data.updatedAt)
            val decimalFormat = DecimalFormat("0.00")
            binding.aqi.text = decimalFormat.format(data.aqi)
            aqiQualityTextColor(data.aqi.toInt())
            binding.aqi.background = aqiTextViewBackground(data.aqi.toInt())
        }

        /**
         * Set AQI index colors
         */
        private fun aqiQualityTextColor(aqi: Int) {
            var aqiValue = ""
            var color: Int = ContextCompat.getColor(itemView.context, R.color.text_gray)
            when (aqi) {
                in 0..50 -> {
                    color = ContextCompat.getColor(itemView.context, R.color.aqi_good)
                    aqiValue = "Good"
                }
                in 51..100 -> {
                    color = ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_satisfactory
                    )
                    aqiValue = "Satisfactory"
                }
                in 101..200 -> {
                    color = ContextCompat.getColor(itemView.context, R.color.aqi_moderate)
                    aqiValue = "Moderate"
                }
                in 201..300 -> {
                    color = ContextCompat.getColor(itemView.context, R.color.aqi_poor)
                    aqiValue = "Poor"
                }

                in 301..400 -> {
                    color = ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_very_poor
                    )
                    aqiValue = "Very Poor"
                }

                in 401..500 -> {
                    color = ContextCompat.getColor(itemView.context, R.color.aqi_servere)
                    aqiValue = "Severe"
                }
            }

            // 15 is fixed value here to add the text color for aqi strings
            val length: Int = aqiValue.length + 15
            val spannableString = SpannableString("Air Quality is $aqiValue")
            spannableString.setSpan(
                ForegroundColorSpan(color),
                14,
                length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            binding.aqiQualityTextView.text = spannableString
        }

        /**
         * Timestamp convert to readable form.
         */
        private fun lastUpdatedTime(timestamp: Long): StringBuilder {
            val milliseconds: Long = Date().time - timestamp
            var minutes: Long = milliseconds / 1000 / 60
            var time = StringBuilder()
            var seconds: Long = milliseconds / 1000 % 60

            if (minutes > 60)
                time = time.append(SimpleDateFormat("hh:mm a").format(milliseconds))
            else if (minutes == 0L)
                time.append(seconds).append(" seconds ago")
            else if (minutes != 0L) {
                time.append(minutes).append(" minutes ").append(seconds)
                    .append(" seconds ago")
            }
            return time
        }

        /**
         * Add rounded drawable for AQI value
         */
        private fun aqiTextViewBackground(aqi: Int): Drawable {
            val d: Int = R.drawable.rounded_corner_background
            val shape = ContextCompat.getDrawable(itemView.context, d) as GradientDrawable
            when (aqi) {
                in 0..50 -> shape.setColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_good
                    )
                )
                in 51..100 -> shape.setColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_satisfactory
                    )
                )
                in 101..200 -> shape.setColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_moderate
                    )
                )
                in 201..300 -> shape.setColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_poor
                    )
                )
                in 301..400 -> shape.setColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_very_poor
                    )
                )
                in 401..500 -> shape.setColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.aqi_servere
                    )
                )
            }
            return shape
        }

        companion object {
            fun newInstance(parent: ViewGroup): DataViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.city_aqi_row, parent, false)

                return DataViewHolder(view)
            }
        }
    }

}
