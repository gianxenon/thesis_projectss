package com.example.contact_tracing_two;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {
    private ImageView foundDevice;
    AnimatorSet animatorSet = new AnimatorSet();
    private static final String TAG= dashboard.class.getSimpleName();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //black icon status Bar
        Utils.blackiconStatusBar(dashboard.this, R.color.light_Background);
        ImageView button = findViewById(R.id.centerImage);
        foundDevice = findViewById(R.id.foundDevice);

        button.setOnClickListener(v -> {
                    final RippleBackground rippleBackground = findViewById(R.id.content);
                    rippleBackground.startRippleAnimation();
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            foundDevice();
                        }
                    }, 5000);
                }
        );


//barchart
        BarChart barChart = findViewById(R.id.Barchart);

        ArrayList<String> labelsss = new ArrayList<>();

        labelsss.add("Nov 14");
        labelsss.add("Nov 15");
        labelsss.add("Nov 16");
        labelsss.add("Nov 17");
        labelsss.add("Nov 18");
        labelsss.add("Nov 19");
        labelsss.add("Nov 20");
        labelsss.add("Nov 21");
        labelsss.add("Nov 22");
        labelsss.add("Nov 23");
        ArrayList<Integer> valuesss = new ArrayList<>();

        valuesss.add(50);
        valuesss.add(5);
        valuesss.add(100);
        valuesss.add(49);
        valuesss.add(30);
        valuesss.add(25);
        valuesss.add(5);
        valuesss.add(66);
        valuesss.add(66);
        valuesss.add(66);
        valuesss.add(66);
        ArrayList<BarEntry> visitor = new ArrayList<>();

        for (int i = 0; i <= valuesss.size() - 1; i++) {

            BarEntry barDataSetss = new BarEntry(i, valuesss.get(i).floatValue());
            visitor.add(barDataSetss);

        }

        Log.d(TAG, "sd " + visitor.toString());


        MyBarDataSet set = new MyBarDataSet(visitor, "");
        set.setColors(Color.GREEN,Color.RED);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(8f);




        BarData barData = new BarData(set);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        barData.setBarWidth(0.8f);
        barData.setHighlightEnabled(false);


        barChart.getXAxis().setTextSize(8f);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelsss));
        barChart.setVisibleXRangeMaximum(8); // allow 5 values to be displayed
        barChart.moveViewToX(1);// set the left edge of the chart to x-index 1
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setScaleEnabled(false);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("utang na bar chart");
        barChart.animateY(1000);

        //end of bar chart


    }

    public void sadwasdad(View view) {
        Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_LONG).show();
        startActivity(new Intent(dashboard.this,MainActivity.class));
        finish();
    }


    private void foundDevice(){

        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        ArrayList<Animator> animatorList=new ArrayList<Animator>();
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleX", 0f, 1.2f, 1f);
        animatorList.add(scaleXAnimator);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(foundDevice, "ScaleY", 0f, 1.2f, 1f);
        animatorList.add(scaleYAnimator);
        animatorSet.playTogether(animatorList);
        foundDevice.setVisibility(View.VISIBLE);
        animatorSet.start();


    }
}
