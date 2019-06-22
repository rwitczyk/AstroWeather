package com.example.astro.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.example.astro.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link nightFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link nightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nightFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    private TextView age,illumination,moonrise,moonset,nextfullmoon,nextnewmoon;

    AstroCalculator.Location location;
    AstroCalculator astroCalculator;
    AstroDateTime astroDateTime;

    private Calendar calendar;
    private TimeZone timeZone;


    private OnFragmentInteractionListener mListener;

    public nightFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nightFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static nightFragment newInstance(String param1, String param2) {
        nightFragment fragment = new nightFragment();
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
        //AstroCalculator.Location location = new AstroCalculator.Location(Double.parseDouble(pos1.toString()),Double.parseDouble(pos2.toString()));
        calendar = new GregorianCalendar();
        timeZone = calendar.getTimeZone();

        location = new AstroCalculator.Location(10,10);
        astroDateTime = new AstroDateTime(
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_night, container, false);
        //pos1 = view.findViewById(R.id.pozycja1Day);
        init();
        updateData();
        // Inflate the layout for this fragment
        return view;
    }


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

    public void updatePosition(double pos1,double pos2)
    {
        location = new AstroCalculator.Location(pos1,pos2);
        astroCalculator = new AstroCalculator(astroDateTime,location);
    }

    public void updateData()
    {
        if(age != null) {
            //      Log.d("aktywnosc", String.valueOf(astroCalculator.getMoonInfo().getAge()));
            age.setText("Age: " + String.valueOf(astroCalculator.getMoonInfo().getAge()));
            illumination.setText("Illumination: " + String.valueOf(astroCalculator.getMoonInfo().getIllumination()));
            moonrise.setText("Moonrise: " + String.valueOf(astroCalculator.getMoonInfo().getMoonrise()));
            moonset.setText("Moonset: " + String.valueOf(astroCalculator.getMoonInfo().getMoonset()));
            nextfullmoon.setText("NextFullMoon: " + String.valueOf(astroCalculator.getMoonInfo().getNextFullMoon()));
            nextnewmoon.setText("NextNewMoon: " + String.valueOf(astroCalculator.getMoonInfo().getNextNewMoon()));
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


    public void init()
    {
        age = view.findViewById(R.id.ageTextView);
        illumination = view.findViewById(R.id.illuminationTextView);
        moonrise = view.findViewById(R.id.moonriseTextView);
        moonset = view.findViewById(R.id.moonsetTextView);
        nextfullmoon = view.findViewById(R.id.nextFullMoonTextView);
        nextnewmoon = view.findViewById(R.id.nextNewMoonEditText);
    }





}
