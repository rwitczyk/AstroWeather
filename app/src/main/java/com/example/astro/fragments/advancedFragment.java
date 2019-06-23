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
import android.widget.TextView;

import com.example.astro.R;
import com.example.astro.data.Data;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link advancedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link advancedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class advancedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView pressure,humidity,description,windSpeed,windDirection;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;
    private Data dataFromApi;

    public advancedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment advancedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static advancedFragment newInstance(String param1, String param2) {
        advancedFragment fragment = new advancedFragment();
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
        view = inflater.inflate(R.layout.fragment_advanced, container, false);

        init();
        updateData();
        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getDataFromApi(Data data)
    {
        this.dataFromApi = data;
    }

    public void updateData()
    {
        if(this.dataFromApi!=null && pressure!=null) {
            pressure.setText("Pressure: " + this.dataFromApi.getMain().getPressure() + "hPa");
            humidity.setText("Humidity: " + this.dataFromApi.getMain().getHumidity() + "%");
            description.setText("Description: " + this.dataFromApi.getWeather().get(0).getDescription());
            windSpeed.setText("Wind speed: " + this.dataFromApi.getWind().getSpeed());
            windDirection.setText("Wind direction: " + this.dataFromApi.getWind().getDeg() + "°");
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        if(view!=null) {
            pressure = view.findViewById(R.id.pressureTextView);
            humidity = view.findViewById(R.id.humidityTextView);
            description = view.findViewById(R.id.descriptionTextView);
            windSpeed = view.findViewById(R.id.windSpeedTextView);
            windDirection = view.findViewById(R.id.windDirectionTextView);
        }
    }
}
