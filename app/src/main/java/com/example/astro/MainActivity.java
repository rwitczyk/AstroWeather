package com.example.astro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.astro.fragments.dayFragment;
import com.example.astro.fragments.nightFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements dayFragment.OnFragmentInteractionListener,nightFragment.OnFragmentInteractionListener {

    private TextView actualTime;
    private EditText position1,position2,refresh;
    private Date currentTime;
    private String time;
    private double pos1,pos2;
    private int refreshTime = 5;
    private dayFragment dayF;
    private nightFragment nightF;
    boolean is_tablet = false;
    Fragment mContent = new dayFragment();

    //TimeZone timeZone = TimeZone.getTimeZone(String.valueOf(Calendar.getInstance().getTimeZone().useDaylightTime()));

    DateFormat df = new SimpleDateFormat("HH:mm:ss");
    //DateFormat dfForAstro = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss:XX");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

//        if (savedInstanceState != null) {
//            //Restore the fragment's instance
//            mContent = getSupportFragmentManager().getFragment(savedInstanceState, "myFragmentName");
//        }

        is_tablet = getResources().getBoolean(R.bool.is_tablet);

        Log.d("aktywnosc","Is tableto? " +  String.valueOf(is_tablet));

        if(is_tablet)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForDay,dayF)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForNight,nightF)
                    .commit();
        }

      //  Log.d("aktywnosc","TIMEZONE: " + Calendar.getInstance().getTimeZone().useDaylightTime());
        //Calendar.getInstance().getTimeZone().useDaylightTime();


        Thread thread = new Thread() { // odswiezanie zegarka
            @Override
            public void run() {
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
            //      if (pos1 != Double.parseDouble(position1.getText().toString()) || pos2 != Double.parseDouble(position2.getText().toString())) {

            dayF.updateData();
            nightF.updateData();
            //        }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's instance
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

    }

    public void onAdvancedClick(View view)
    {

    }

    public void onForecastClick(View view)
    {

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

       // AstroCalculator.Location location = new AstroCalculator.Location(pos1,pos2);


    }

    @Override
    public void onFragmentInteraction(double pos1, double pos2) {
      //  dayF.updatePosition(pos1,pos2);
    }




}
