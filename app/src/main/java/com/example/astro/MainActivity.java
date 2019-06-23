package com.example.astro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.astro.data.Data;
import com.example.astro.fragments.advancedFragment;
import com.example.astro.fragments.dayFragment;
import com.example.astro.fragments.forecastFragment;
import com.example.astro.fragments.nightFragment;
import com.example.astro.fragments.simpleFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements dayFragment.OnFragmentInteractionListener,nightFragment.OnFragmentInteractionListener, simpleFragment.OnFragmentInteractionListener, advancedFragment.OnFragmentInteractionListener, forecastFragment.OnFragmentInteractionListener {

    private TextView actualTime;
    private EditText position1,position2,refresh;
    private Date currentTime;
    private String time;
    private double pos1,pos2;
    private int refreshTime = 5;
    private dayFragment dayF;
    private nightFragment nightF;
    private simpleFragment simpleF;
    private advancedFragment advancedF;
    private forecastFragment forecastF;
    boolean is_tablet = false;
    private String apiKey = "46f94484a5750d7cd295671f61987bb9";

    DateFormat df = new SimpleDateFormat("HH:mm:ss");

    RestAdapter retrofit;
    MyWebService myWebService;
    private Data dataFromApi;
    private AlertDialog.Builder a_Builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        is_tablet = getResources().getBoolean(R.bool.is_tablet);

        Log.d("aktywnosc","Is tablet? " +  String.valueOf(is_tablet));

        if(is_tablet)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForDay,dayF)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForNight,nightF)
                    .commit();
        }


        Thread thread = new Thread() { // odswiezanie zegarka
            @Override
            public void run() { //zegarek
                try{
                    while(!isInterrupted())
                    {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                currentTime = Calendar.getInstance().getTime();
                                time = df.format(currentTime);
                                actualTime.setText(time);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        Thread thread2 = new Thread() {
            @Override
            public void run() { //odswiezanie danych
                try{
                    while(!isInterrupted())
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dayF.updateData();
                                nightF.updateData();
                            }
                        });
                        Thread.sleep(refreshTime * 1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        thread2.start();
    }

    public void onSaveNewPositionClick(View view)
    {
        if (position1.getText().length() > 0 && Double.parseDouble(position1.getText().toString()) > -90 && Double.parseDouble(position1.getText().toString()) < 90) {
            pos1 = Double.parseDouble(position1.getText().toString());
        }
        if (position2.getText().length() > 0 && Double.parseDouble(position2.getText().toString()) > -180 && Double.parseDouble(position2.getText().toString()) < 180) {
            pos2 = Double.parseDouble(position2.getText().toString());
        }
        if (refresh.getText().length() > 0) {
            refreshTime = Integer.parseInt(refresh.getText().toString());
        }
        dayF.updatePosition(pos1, pos2);
        nightF.updatePosition(pos1, pos2);


        if(position1.getText().length() > 0 && position2.getText().length() > 0) {
            dayF.updateData();
            nightF.updateData();

            connectApiToGetData();
        }
    }

    private void connectApiToGetData() {
        myWebService.getDataByCoords(pos1,pos2,apiKey,new Callback<Data>() {
            @Override
            public void success(Data data, Response response) {
                dataFromApi = data;
                simpleF.getDataFromApi(dataFromApi);
                simpleF.updateData();
                System.out.println("FROM API2: " + data.getName());
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("NIE DZIALA :(");
                System.out.println(error.getUrl());
                showAlert();
            }
        });
    }

    public void onSunClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,dayF)
                .commit();
    }

    public void onMoonClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,nightF)
                .commit();
    }

    public void onSimpleClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,simpleF)
                .commit();

        simpleF.getDataFromApi(dataFromApi);
    }

    public void onAdvancedClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,advancedF)
                .commit();
    }

    public void onForecastClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,forecastF)
                .commit();
    }


    private void init() {
        actualTime = findViewById(R.id.actTime);
        position1 = findViewById(R.id.pos1);
        position2 = findViewById(R.id.pos2);
        refresh = findViewById(R.id.refreshTime);

        pos1 = Double.parseDouble(position1.getText().toString());
        pos2 = Double.parseDouble(position2.getText().toString());
        refreshTime = Integer.parseInt(refresh.getText().toString());

        dayF = new dayFragment();
        nightF = new nightFragment();
        simpleF = new simpleFragment();
        advancedF = new advancedFragment();
        forecastF = new forecastFragment();


        retrofit = new RestAdapter.Builder()
                // adres API
                .setEndpoint("http://api.openweathermap.org")
                // niech Retrofit loguje wszystko co robi
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        // tworzymy klienta
        myWebService = retrofit.create(MyWebService.class);
    }

    @Override
    public void onFragmentInteraction(double pos1, double pos2) {
      //  dayF.updatePosition(pos1,pos2);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showAlert() {
        a_Builder = new AlertDialog.Builder(this);
        alertDialog = a_Builder.create();
        alertDialog.setTitle("Wrong coordinate!");
        alertDialog.show();
    }
}
