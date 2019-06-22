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
import android.widget.Button;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.astro.R;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link dayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link dayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class dayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private AstroCalculator.Location location;
    private AstroDateTime astroDateTime;
    private AstroCalculator astroCalculator;
    private View view;
    private Calendar calendar;
    private TimeZone timeZone;

    private double temp_pos1,temp_pos2;

    private TextView azimuthRise,azimuthSet,sunrise,sunset,twilightEvening,twilightMorning;

    private OnFragmentInteractionListener mListener;

    public dayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dayFragment newInstance(String param1, String param2) {
        dayFragment fragment = new dayFragment();
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

    // musi byc pobieranie z kalendarza to co wyzej, musza byc 4 rozne layouty po prostu, dla tableta inne, moze po wpisaniu potwierdzic buttonem
    // trzeba pobierac strefe czasowa w jakis sposob
    // godzina aktualna



    @Override
    public void onConfigurationChanged(Configuration newConfig){
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_day, container, false);
        System.out.println("VIEW: " + view);
        location = new AstroCalculator.Location(temp_pos1, temp_pos2);
        calendar = new GregorianCalendar();
        timeZone = calendar.getTimeZone();
        this.astroDateTime = new AstroDateTime(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH)+1, // bo jest 0-11
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), // albo HOUR jak format 0-11
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND),
                (timeZone.getRawOffset()/3600000),
                true
        );
        astroCalculator = new AstroCalculator(astroDateTime,location);
        System.out.println(Calendar.getInstance().get(Calendar.YEAR) + " " +
                Calendar.getInstance().get(Calendar.MONTH) + " " +
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " +
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + " " +
                Calendar.getInstance().get(Calendar.MINUTE) + " " +
                Calendar.getInstance().get(Calendar.SECOND) + " " +
                (timeZone.getRawOffset()/3600000));

        init();
        updateData();
        // Inflate the layout for this fragment
        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(double pos1, double pos2) {
        if (mListener != null) {
            mListener.onFragmentInteraction(pos1,pos2);
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
    public void onPause() {
        savePositionToRotate(temp_pos1, temp_pos2);
        super.onPause();
    }

    public void updatePosition(double pos1,double pos2)
    {
        location = new AstroCalculator.Location(pos1,pos2);
        calendar = new GregorianCalendar();
        timeZone = calendar.getTimeZone();
        this.astroDateTime = new AstroDateTime(
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH)+1, // bo jest 0-11
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), // albo HOUR jak format 0-11
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND),
                (timeZone.getRawOffset()/3600000),
                true
        );
        astroCalculator = new AstroCalculator(astroDateTime,location);

        savePositionToRotate(pos1,pos2);
    }

    public void savePositionToRotate(double pos1, double pos2)
    {
        this.temp_pos1 = pos1;
        this.temp_pos2 = pos2;
    }

    public void updateData()
    {
        if(azimuthRise != null) {
            azimuthRise.setText("AzimuthRise: " + String.valueOf(astroCalculator.getSunInfo().getAzimuthRise()));
            azimuthSet.setText("AzimuthSet: " + String.valueOf(astroCalculator.getSunInfo().getAzimuthSet()));
            sunrise.setText("Sunrise: " + String.valueOf(astroCalculator.getSunInfo().getSunrise()));
            sunset.setText("Sunset: " + String.valueOf(astroCalculator.getSunInfo().getSunset()));
            twilightEvening.setText("TwilightEvening: " + String.valueOf(astroCalculator.getSunInfo().getTwilightEvening()));
            twilightMorning.setText("TwilightMorning: " + String.valueOf(astroCalculator.getSunInfo().getTwilightMorning()));
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
        void onFragmentInteraction(double pos1, double pos2);
    }


    private void init() {
            azimuthRise = view.findViewById(R.id.azimuthRiseTextView);
            azimuthSet = view.findViewById(R.id.azimuthSetTextView);
            sunrise = view.findViewById(R.id.sunriseTextView);
            sunset = view.findViewById(R.id.sunsetTextView);
            twilightEvening = view.findViewById(R.id.twilightEveningTextView);
            twilightMorning = view.findViewById(R.id.twilightMorningTextView);
    }


}
