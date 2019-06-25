package com.example.astro;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.example.astro.data.Data;
import com.example.astro.forecast.DataForecast;
import com.example.astro.fragments.advancedFragment;
import com.example.astro.fragments.dayFragment;
import com.example.astro.fragments.favouriteFragment;
import com.example.astro.fragments.forecastFragment;
import com.example.astro.fragments.nightFragment;
import com.example.astro.fragments.simpleFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements dayFragment.OnFragmentInteractionListener,nightFragment.OnFragmentInteractionListener, simpleFragment.OnFragmentInteractionListener, advancedFragment.OnFragmentInteractionListener, forecastFragment.OnFragmentInteractionListener, favouriteFragment.OnFragmentInteractionListener {

    private TextView actualTime;
    private EditText position1,position2,refresh,cityName;
    private Date currentTime;
    private String time;
    private double pos1,pos2;
    private int refreshTime = 2;
    private dayFragment dayF;
    private nightFragment nightF;
    private simpleFragment simpleF;
    private advancedFragment advancedF;
    private forecastFragment forecastF;
    private favouriteFragment favouriteF;
    boolean is_tablet = false;
    private String apiKey = "46f94484a5750d7cd295671f61987bb9";
    private String units = "metric";
    private Switch switchButton;
    private RealmResults<FavouriteData> favouriteDataRealmResults;
    private Realm realm;
    public static final String FILE_NAME_DATA = "DataObjFromApi.json";
    public static final String FILE_NAME_FORECAST = "ForecastObjFromApi.json";
    DateFormat df = new SimpleDateFormat("HH:mm:ss");

    RestAdapter retrofit;
    MyWebService myWebService;
    private Data dataFromApi;
    private DataForecast dataForecastFromApi;
    private AlertDialog.Builder a_Builder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        is_tablet = getResources().getBoolean(R.bool.is_tablet);

        Log.d("aktywnosc","Is tablet? " +  is_tablet);

        if(is_tablet)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForDay,dayF)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForNight,nightF)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForSimple,simpleF)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForAdvanced,advancedF)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForForecast,forecastF)
                    .commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerForFavourites,favouriteF)
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

                                View view = null;
                                onSaveNewCoordsClick(view);
                            }
                        });
                        Thread.sleep(refreshTime * 1000 * 60);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
        thread2.start();


        System.out.println("IS NET?:" + isNetworkAvailable());
        if(isNetworkAvailable())
        {

        }
        else
        {
            showAlert("No internet connection!");

            this.dataFromApi = loadDataFromStorage();
            simpleF.getDataFromApi(this.dataFromApi);
            advancedF.getDataFromApi(this.dataFromApi);
        }
    }

    public void onSaveNewCoordsClick(View view)
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

            if(switchButton.isChecked()) {
                units = "imperial";
                connectApiToGetDataByCoords();
                connectApiToGetForecastByCoords();
            }
            else
            {
                units = "metric";
                connectApiToGetDataByCoords();
                connectApiToGetForecastByCoords();
            }
        }
    }

    public void onSaveNewNameOfCityClick(View view)
    {
        if(cityName.getText().length()>0)
        {
            if(switchButton.isChecked()) {
                units = "imperial";
                connectApiToGetDataByCityName();
                connectApiToGetForecastByCityName();
            }
            else
            {
                units = "metric";
                connectApiToGetDataByCityName();
                connectApiToGetForecastByCityName();
            }
        }
    }

    private void connectApiToGetDataByCityName() {
        if(cityName!=null) {
            myWebService.getDataByName(String.valueOf(cityName.getText()), units, apiKey, new Callback<Data>() {
                @Override
                public void success(Data data, Response response) {
                    dataFromApi = data;
                    saveDataInStorage(data);
                    simpleF.getDataFromApi(dataFromApi);
                    simpleF.updateData();

                    advancedF.getDataFromApi(dataFromApi);
                    advancedF.updateData();
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.println("NIE DZIALA :(");
                    System.out.println(error.getUrl());
                    showAlert("Wrong name");
                }
            });
        }
    }

    private void connectApiToGetForecastByCityName() {
        myWebService.getForecastByName(String.valueOf(cityName.getText()), units, apiKey, new Callback<DataForecast>() {
            @Override
            public void success(DataForecast dataForecast, Response response) {
                dataForecastFromApi = dataForecast;
                System.out.println("RESPONSE URL " + response.getUrl());

                forecastF.getDataForecastFromApi(dataForecastFromApi);
                forecastF.updateData();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("NIE DZIALA :(");
                System.out.println(error.getUrl());
                showAlert("Wrong name");
            }
        });
    }


    private void connectApiToGetDataByCoords() {
        myWebService.getDataByCoords(pos1,pos2,units,apiKey,new Callback<Data>() {
            @Override
            public void success(Data data, Response response) {
                dataFromApi = data;
                saveDataInStorage(data);
                simpleF.getDataFromApi(dataFromApi);
                simpleF.updateData();

                advancedF.getDataFromApi(dataFromApi);
                advancedF.updateData();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("NIE DZIALA :(");
                System.out.println(error.getUrl());
                showAlert("Wrong coords");
            }
        });
    }

    private void connectApiToGetForecastByCoords(){
        myWebService.getForecastByCoords(pos1, pos2, units, apiKey, new Callback<DataForecast>() {
            @Override
            public void success(DataForecast dataForecast, Response response) {
                dataForecastFromApi = dataForecast;

                forecastF.getDataForecastFromApi(dataForecastFromApi);
                forecastF.updateData();
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("NIE DZIALA :(");
                System.out.println(error.getUrl());
                showAlert("Wrong coords");
            }
        });
    }

    public Data loadDataFromStorage()
    {
        FileInputStream fis = null;
        Data data2 = null;
        ObjectInputStream in = null;
        try {
            fis = openFileInput(FILE_NAME_DATA);
            in = new ObjectInputStream(fis);
            data2 = (Data)in.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data2;
    }

    public void saveDataInStorage(Data data)
    {
        FileOutputStream fos = null;
        ObjectOutputStream out = null;

        try {
            fos = openFileOutput(FILE_NAME_DATA,MODE_PRIVATE);
            out = new ObjectOutputStream(fos);
            out.writeObject(data);
            System.out.println("JSON: " + data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fos.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public void onFavouriteClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,favouriteF)
                .commit();

        favouriteF.getListOfLocalizations(realm,favouriteDataRealmResults);
    }

    public void onSimpleClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,simpleF)
                .commit();

        simpleF.getDataFromApi(dataFromApi);
        simpleF.updateData();
    }

    public void onAdvancedClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,advancedF)
                .commit();

        advancedF.getDataFromApi(dataFromApi);
        advancedF.updateData();
    }

    public void onForecastClick(View view)
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerForAstro,forecastF)
                .commit();

        forecastF.getDataForecastFromApi(dataForecastFromApi);
        forecastF.updateData();
    }

    private void init() {
        actualTime = findViewById(R.id.actTime);
        position1 = findViewById(R.id.pos1);
        position2 = findViewById(R.id.pos2);
        refresh = findViewById(R.id.refreshTime);
        cityName = findViewById(R.id.cityNameEditText);
        switchButton = findViewById(R.id.switchUnits);

        pos1 = Double.parseDouble(position1.getText().toString());
        pos2 = Double.parseDouble(position2.getText().toString());
        refreshTime = Integer.parseInt(refresh.getText().toString());

        dayF = new dayFragment();
        nightF = new nightFragment();
        simpleF = new simpleFragment();
        advancedF = new advancedFragment();
        forecastF = new forecastFragment();
        favouriteF = new favouriteFragment();


        retrofit = new RestAdapter.Builder()
                // adres API
                .setEndpoint("http://api.openweathermap.org")
                // niech Retrofit loguje wszystko co robi
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        // tworzymy klienta
        myWebService = retrofit.create(MyWebService.class);

        initDatabaseRealm();
    }

    private void initDatabaseRealm() {
        Realm.init(this);
        RealmConfiguration realmConfig2 = new RealmConfiguration.Builder()
                .name("location2.db")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig2);
        realm = Realm.getInstance(realmConfig2);

      favouriteDataRealmResults = realm.where(FavouriteData.class).findAll();
//        System.out.println("SIZE: " + favouriteDataRealmResults.size());
//
//        realm.beginTransaction();
//        realm.deleteAll();
//        System.out.println("SIZE1: " + favouriteDataRealmResults.size());
//        FavouriteData favouriteData = realm.createObject(FavouriteData.class);
//        favouriteData.setName("Lodz");
//        favouriteData.setIdLocalization("5");
//        realm.commitTransaction();
//

        boolean ifCityActualExistsInDb = false;
        String value = "Lodz";
        if(this.favouriteDataRealmResults!=null) {
            for (FavouriteData favouriteDataRealmResult : favouriteDataRealmResults) { //sprawdzanie czy lokalizacja aktualnie istnieje w bazie
                if (value.equals(favouriteDataRealmResult.getName())) {
                    ifCityActualExistsInDb = true;
                    break;
                } else {
                    ifCityActualExistsInDb = false;
                }
            }
        }

        if(!ifCityActualExistsInDb) {
            if(value.length()>0) {
                realm.beginTransaction();
                FavouriteData addItemToDB = realm.createObject(FavouriteData.class);
                addItemToDB.setName(value);
                addItemToDB.setIdLocalization("3");
                realm.commitTransaction();

                favouriteF.printLocalizationList();
            }
        }

   //     favouriteDataRealmResults = realm.where(FavouriteData.class).findAll();
//
//        for (FavouriteData favouriteDataRealmResult : favouriteDataRealmResults) {
//            System.out.println("XD: " + favouriteDataRealmResult.getName());
//        }
//
//        System.out.println("SIZE2: " + favouriteDataRealmResults.size());
    }

    @Override
    public void onFragmentInteraction(double pos1, double pos2) {
      //  dayF.updatePosition(pos1,pos2);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void showAlert(String alert) {
        a_Builder = new AlertDialog.Builder(this);
        alertDialog = a_Builder.create();
        alertDialog.setTitle(alert);
        alertDialog.show();
    }

    public void addLocalizationOnClick(View view)
    {
        if(favouriteF.nameOfCity!=null) {
            myWebService.getDataByName(favouriteF.nameOfCity.getText().toString(), units, apiKey, new Callback<Data>() { // walidacja czy nazwa lokalizacji jest poprawna
                @Override
                public void success(Data data, Response response) {

                    boolean ifCityActualExistsInDb = false;
                    String value = favouriteF.nameOfCity.getText().toString();
                    if(favouriteDataRealmResults!=null) {
                        for (FavouriteData favouriteDataRealmResult : favouriteDataRealmResults) { //sprawdzanie czy lokalizacja aktualnie istnieje w bazie
                            if (value.equals(favouriteDataRealmResult.getName())) {
                                ifCityActualExistsInDb = true;
                                break;
                            } else {
                                ifCityActualExistsInDb = false;
                            }
                        }
                    }

                    if(!ifCityActualExistsInDb) {
                        if(value.length()>0) {
                            realm.beginTransaction();
                            FavouriteData addItemToDB = realm.createObject(FavouriteData.class);
                            addItemToDB.setName(value);
                            addItemToDB.setIdLocalization("3");
                            realm.commitTransaction();

                            favouriteF.printLocalizationList();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.println("Zla nazwa lokalizacji :(");
                    System.out.println(error.getUrl());
                    showAlert("Wrong name");
                }
            });
        }
    }

    public void deleteLocalizationOnClick(View view)
    {
        realm.beginTransaction();
            RealmResults<FavouriteData> result = realm.where(FavouriteData.class).equalTo("name",favouriteF.nameOfCity.getText().toString()).findAll();
            result.deleteAllFromRealm();
        realm.commitTransaction();

        favouriteF.printLocalizationList();
    }

}
