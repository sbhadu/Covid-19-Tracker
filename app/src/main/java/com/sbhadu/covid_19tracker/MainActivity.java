package com.sbhadu.covid_19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.sbhadu.covid_19tracker.API.APIUtilities;
import com.sbhadu.covid_19tracker.API.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView totalConfirm, totalRecovered, totalActive, totalDeath, totalTests;
    private TextView todayConfirm, todayRecovered, todayDeath, date;
    private PieChart pieChart;

    private List<CountryData> list;
    String country = "India";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        if(getIntent().getStringExtra("country") != null)
            country = getIntent().getStringExtra("country");

        init();

        TextView cname = findViewById(R.id.cname);
        cname.setText(country);

        cname.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CountryActivity.class)));

        APIUtilities.getApiInterface().getCountryData().enqueue(new Callback<List<CountryData>>() {
            @Override
            public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                list.addAll(response.body());

                for(int i=0;i<list.size();i++){
                    if(list.get(i).getCountry().equals(country)){
                        int confirm = Integer.parseInt(list.get(i).getCases());
                        int active = Integer.parseInt(list.get(i).getActive());
                        int recovered = Integer.parseInt(list.get(i).getRecovered());
                        int death = Integer.parseInt(list.get(i).getDeaths());

                        totalActive.setText(NumberFormat.getInstance().format(active));
                        totalConfirm.setText(NumberFormat.getInstance().format(confirm));
                        totalRecovered.setText(NumberFormat.getInstance().format(recovered));
                        totalDeath.setText(NumberFormat.getInstance().format(death));

                        todayConfirm.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        todayDeath.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        todayRecovered.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));
                        totalTests.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTests())));

                        setText(list.get(i).getUpdated());

                        pieChart.addPieSlice(new PieModel(confirm,getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel(active,getResources().getColor(R.color.blue)));
                        pieChart.addPieSlice(new PieModel(recovered,getResources().getColor(R.color.green)));
                        pieChart.addPieSlice(new PieModel(death,getResources().getColor(R.color.red)));
                        pieChart.startAnimation();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<CountryData>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error : "+t.getMessage(),Toast.LENGTH_SHORT);
            }
        });

    }

    private void setText(String updated) {
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        long millisecond = Long.parseLong(updated);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millisecond);
        date.setText("updated at "+format.format(calendar.getTime()));
    }

    public void init(){
        totalConfirm = (TextView)findViewById(R.id.TotalConfirm);
        totalRecovered = (TextView)findViewById(R.id.TotalRecovered);
        totalActive = (TextView)findViewById(R.id.TotalActive);
        totalDeath = (TextView)findViewById(R.id.TotalDeath);
        totalTests = (TextView)findViewById(R.id.TotalTests);
        todayConfirm = (TextView)findViewById(R.id.TodayConfirm);
        todayRecovered = (TextView)findViewById(R.id.TodayRecovered);
        todayDeath = (TextView)findViewById(R.id.TodayDeath);
        pieChart = (PieChart) findViewById(R.id.pieChart);
        date =(TextView)findViewById(R.id.date);
    }
}