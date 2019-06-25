package com.example.astro.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.astro.R;
import com.example.astro.forecast.DataForecast;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link forecastFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link forecastFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class forecastFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DataForecast dataForecastFromApi;
    private TextView nazwa1,tempMin1,tempMax1,nazwa2,tempMin2,tempMax2,nazwa3,tempMin3,tempMax3,nazwa4,tempMin4,tempMax4;
    private ImageView imageView1,imageView2,imageView3,imageView4;

    private OnFragmentInteractionListener mListener;
    private View view;

    public forecastFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment forecastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static forecastFragment newInstance(String param1, String param2) {
        forecastFragment fragment = new forecastFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forecast, container, false);

        init();

        updateData();
        // Inflate the layout for this fragment
        return view;
    }

    public void updateData() {
        if(this.dataForecastFromApi!=null && nazwa1!=null) {
            nazwa1.setText(String.valueOf(this.dataForecastFromApi.getList().get(0).getDt_txt()).substring(0,10));
            tempMin1.setText("Min: " + this.dataForecastFromApi.getList().get(0).getMain().getTemp_min() + "°");
            tempMax1.setText("Max: " + this.dataForecastFromApi.getList().get(4).getMain().getTemp_max() + "°");
            Picasso.get()
                    .load("http://openweathermap.org/img/w/"+this.dataForecastFromApi.getList().get(5).getWeather().get(0).getIcon()+".png")
                    .into(imageView1);

            nazwa2.setText(String.valueOf(this.dataForecastFromApi.getList().get(8).getDt_txt()).substring(0,10));
            tempMin2.setText("Min: " + this.dataForecastFromApi.getList().get(8).getMain().getTemp_min() + "°");
            tempMax2.setText("Max: " + this.dataForecastFromApi.getList().get(12).getMain().getTemp_max() + "°");
            Picasso.get()
                    .load("http://openweathermap.org/img/w/"+this.dataForecastFromApi.getList().get(13).getWeather().get(0).getIcon()+".png")
                    .into(imageView2);

            nazwa3.setText(String.valueOf(this.dataForecastFromApi.getList().get(16).getDt_txt()).substring(0,10));
            tempMin3.setText("Min: " + this.dataForecastFromApi.getList().get(16).getMain().getTemp_min() + "°");
            tempMax3.setText("Max: " + this.dataForecastFromApi.getList().get(20).getMain().getTemp_max() + "°");
            Picasso.get()
                    .load("http://openweathermap.org/img/w/"+this.dataForecastFromApi.getList().get(21).getWeather().get(0).getIcon()+".png")
                    .into(imageView3);

            nazwa4.setText(String.valueOf(this.dataForecastFromApi.getList().get(24).getDt_txt()).substring(0,10));
            tempMin4.setText("Min: " + this.dataForecastFromApi.getList().get(24).getMain().getTemp_min() + "°");
            tempMax4.setText("Max: " + this.dataForecastFromApi.getList().get(28).getMain().getTemp_max() + "°");
            Picasso.get()
                    .load("http://openweathermap.org/img/w/"+this.dataForecastFromApi.getList().get(29).getWeather().get(0).getIcon()+".png")
                    .into(imageView4);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getDataForecastFromApi(DataForecast data)
    {
        this.dataForecastFromApi = data;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){ //obracanie ekranu
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            try {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(this).attach(this).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void init()
    {
        if(view!=null)
        {
            nazwa1 = view.findViewById(R.id.dayTextView1);
            imageView1 = view.findViewById(R.id.imageViewD1);
            tempMin1 = view.findViewById(R.id.tempMinTextView1);
            tempMax1 = view.findViewById(R.id.tempMaxTextView1);

            nazwa2 = view.findViewById(R.id.dayTextView2);
            imageView2 = view.findViewById(R.id.imageViewD2);
            tempMin2 = view.findViewById(R.id.tempMinTextView2);
            tempMax2 = view.findViewById(R.id.tempMaxTextView2);

            nazwa3 = view.findViewById(R.id.dayTextView3);
            imageView3 = view.findViewById(R.id.imageViewD3);
            tempMin3 = view.findViewById(R.id.tempMinTextView3);
            tempMax3 = view.findViewById(R.id.tempMaxTextView3);

            nazwa4 = view.findViewById(R.id.dayTextView4);
            imageView4 = view.findViewById(R.id.imageViewD4);
            tempMin4 = view.findViewById(R.id.tempMinTextView4);
            tempMax4 = view.findViewById(R.id.tempMaxTextView4);
        }
    }
}
