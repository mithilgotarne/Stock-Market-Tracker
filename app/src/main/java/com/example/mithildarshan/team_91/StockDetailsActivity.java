package com.example.mithildarshan.team_91;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentUris;
import android.content.ContentValues;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithildarshan.team_91.background.FetchGraphData;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StockDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private DataPoint[] dataPoint;

    private GraphView graph;
    private TextView textView;

    private boolean isFavourite = false;
    private boolean isWatchPointSet = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);
        //TextView tv = (TextView) this.findViewById(R.id.TextView03);
        //tv.setSelected(true);
        String ticker = getIntent().getExtras().getString("Ticker");

        Log.d("StockDetailsActivity", ticker);
        new FetchGraphData(){
            @Override
            protected void onPostExecute(DataPoint[] dataPoints) {
                super.onPostExecute(dataPoints);
                dataPoint = dataPoints;
                if(dataPoints!=null){
                    plotGraph(dataPoints);
                }
                else{
                    Log.d("StockDetailsActivity", "Why Null?");
                }

            }

            public void plotGraph(DataPoint[] dataPoints){

                graph = (GraphView) findViewById(R.id.graph);
                graph.setVisibility(View.VISIBLE);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
                linearLayout.setVisibility(View.VISIBLE);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
                progressBar.setVisibility(View.GONE);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);
                if(series != null){
                    series.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    series.setDrawBackground(true);
                    series.setBackgroundColor(Color.LTGRAY);

                   //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setMinX(dataPoints[0].getX());
                    graph.getViewport().setMaxX(dataPoints[dataPoints.length-1].getX());

                    //graph.getGridLabelRenderer().setLabelVerticalWidth(30);
                    graph.getGridLabelRenderer().setTextSize(15);
                   graph.getGridLabelRenderer().setHorizontalLabelsAngle(30);


                    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
                    //graph.getGridLabelRenderer().setNumHorizontalLabels(5);
                    //graph.getGridLabelRenderer().setHumanRounding(false);
                    //graph.getGridLabelRenderer().setNumHorizontalLabels(dataPoints.length);

                   // graph.getViewport().setScalableY(true);
                   // graph.getViewport().setScrollable(true);
                    graph.addSeries(series);

                }
                else{
                    Log.d("StockDetailsActivity", "Wh Null?");
                }

            }
        }.execute(ticker);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.

        getMenuInflater().inflate(R.menu.stock_details_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.favourite);
        if(isFavourite){
            menuItem.setIcon(R.drawable.ic_star_black_24dp);
        }
        else{
            menuItem.setIcon(R.drawable.ic_star_border_black_24dp);
        }

        menuItem = menu.findItem(R.id.set_watch_point);
        if(isWatchPointSet){
            menuItem.setIcon(R.drawable.ic_alarm_on_black_24dp);
        }
        else{
            menuItem.setIcon(R.drawable.ic_alarm_black_24dp);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.favourite:
                if(isFavourite){
                    isFavourite = false;
                    item.setIcon(R.drawable.ic_star_border_black_24dp);
                }
                else{
                    isFavourite = true;
                    item.setIcon(R.drawable.ic_star_black_24dp);
                }

                return true;

            case R.id.set_watch_point:
                item.setIcon(R.drawable.ic_alarm_on_black_24dp);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        textView = (TextView) findViewById(R.id.start_date);
        Calendar c = Calendar.getInstance();

        c.set(year, month, dayOfMonth, 0, 0, 0);
        textView.setText(year + "/"+(month+1)+"/"+dayOfMonth );
        graph = (GraphView) findViewById(R.id.graph);
        graph.removeAllSeries();

        if(dataPoint == null) Log.e("StockDetailsActivity", "DAMN");
        graph.getViewport().setMinX(c.getTimeInMillis());
        //graph.getViewport().setMaxX(dataPoint[dataPoint.length - 1].getX());
        graph.getViewport().setXAxisBoundsManual(true);

        //graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        //graph.getGridLabelRenderer().setLabelVerticalWidth(30);
        graph.getGridLabelRenderer().setTextSize(15);
        graph.getGridLabelRenderer().setHorizontalLabelsAngle(30);


        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoint);
        if(series != null) {

            series.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            series.setDrawBackground(true);
            series.setBackgroundColor(Color.LTGRAY);
            graph.addSeries(series);
            //graph.getGridLabelRenderer().setNumHorizontalLabels(5);
            //graph.getGridLabelRenderer().setHumanRounding(false);
            //graph.getGridLabelRenderer().setNumHorizontalLabels(dataPoints.length);

        }
    }

    public static class DatePickerFragment extends DialogFragment
             {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = 2007;
            int month = 11;
            int day = 1;

            // Create a new instance of DatePickerDialog and return it
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            Calendar cl = Calendar.getInstance();
            Log.d("STockDetailsActivity", Long.toString(cl.getTimeInMillis()));

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);
            DatePicker datePicker = datePickerDialog.getDatePicker();
            try {
                datePicker.setMinDate((df.parse("2007-01-01").getTime()));
                long minusTwentFour = 6*24*60*60*1000;
                datePicker.setMaxDate(cl.getTimeInMillis()-minusTwentFour);
                //datePicker.setMinDate((df.parse("2007-01-01").getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return datePickerDialog;
        }


    }
}

