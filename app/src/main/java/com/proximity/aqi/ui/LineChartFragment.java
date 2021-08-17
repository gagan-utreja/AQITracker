package com.proximity.aqi.ui;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baruckis.cryptolive.database.ListEntity;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.proximity.aqi.R;
import com.proximity.aqi.custom.DateFormat;
import com.proximity.aqi.custom.MyMarkerView;
import com.proximity.aqi.databinding.FragmentLineChartBinding;
import com.proximity.aqi.repository.CityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment extends Fragment implements OnChartValueSelectedListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "city_name";
    private FragmentLineChartBinding fragmentLineChartBinding;

    private String mParam1;
    private LineChart chart;


    public LineChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment LineChartFragment.
     */
    public static LineChartFragment newInstance(String param1) {
        LineChartFragment fragment = new LineChartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentLineChartBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_line_chart, container, false);
        return fragmentLineChartBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        {   // // Chart Style // //
            chart = fragmentLineChartBinding.lineChart;

            // background color
            chart.setBackgroundColor(Color.WHITE);

            // disable description text
            chart.getDescription().setEnabled(false);

            // enable touch gestures
            chart.setTouchEnabled(true);

            // set listeners
            chart.setOnChartValueSelectedListener(this);
            chart.setDrawGridBackground(false);

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(chart);
            chart.setMarker(mv);

            // enable scaling and dragging
            chart.setDragEnabled(true);
            chart.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            chart.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = chart.getXAxis();
            ValueFormatter theFormatYouWant = new DateFormat();
            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
            xAxis.setValueFormatter(theFormatYouWant);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = chart.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            chart.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMinimum(-50f);
        }


        setData();

        // draw points over time
        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setData() {

        ArrayList<Entry> entries = new ArrayList<>();


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                CityRepository cityRepository = CityRepository.Companion.getInstance(getActivity(), Executors.newSingleThreadExecutor());
                List<ListEntity> listEntities = cityRepository.getCityHistory(mParam1);
                for (ListEntity data : listEntities) {
                    entries.add(new Entry(data.getCreatedAt(), data.getAqi()));
                }
                LineDataSet set1 = new LineDataSet(entries, mParam1 + " AQI DataSet");

                set1.setDrawIcons(false);

                // draw dashed line
                set1.enableDashedLine(10f, 5f, 0f);

                // black lines and points
                set1.setColor(Color.BLACK);
                set1.setCircleColor(Color.BLACK);

                // line thickness and point size
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);

                // draw points as solid circles
                set1.setDrawCircleHole(false);

                // customize legend entry
                set1.setFormLineWidth(1f);
                set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
                set1.setFormSize(15.f);

                // text size of values
                set1.setValueTextSize(9f);

                // draw selection line as dashed
                set1.enableDashedHighlightLine(10f, 5f, 0f);

                // set the filled area
                set1.setDrawFilled(true);
                set1.setFillFormatter(new IFillFormatter() {
                    @Override
                    public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                        return chart.getAxisLeft().getAxisMinimum();
                    }
                });

                // set color of filled area
                if (Utils.getSDKInt() >= 18) {
                    // drawables only supported on api level 18 and above
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_red);
                    set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.BLACK);
                }

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1); // add the data sets

                // create a data object with the data sets
                LineData data = new LineData(dataSets);

                // set data
                chart.setData(data);

            }
        });
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}