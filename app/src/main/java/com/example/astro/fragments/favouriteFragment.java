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
import android.widget.EditText;
import android.widget.TextView;

import com.example.astro.FavouriteData;
import com.example.astro.R;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link favouriteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link favouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favouriteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView localizationList;
    public EditText nameOfCity;
    private Button addLocalization,deleteLocalization;
    private RealmResults<FavouriteData> favouriteDataRealmResults;
    private Realm realm;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View view;

    public favouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favouriteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favouriteFragment newInstance(String param1, String param2) {
        favouriteFragment fragment = new favouriteFragment();
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
        view = inflater.inflate(R.layout.fragment_favourite, container, false);

        init();

        printLocalizationList();

        // Inflate the layout for this fragment
        return view;
    }

    public void printLocalizationList() {
        if(localizationList!=null) {
            localizationList.setText("");
            if (this.favouriteDataRealmResults != null) {
                for (FavouriteData favouriteDataRealmResult : this.favouriteDataRealmResults) {
                    localizationList.setText(localizationList.getText() + "\n" +
                            favouriteDataRealmResult.getName() + "\n");
                }
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        localizationList = view.findViewById(R.id.listFavTextView);
        nameOfCity = view.findViewById(R.id.cityEditText);
        addLocalization = view.findViewById(R.id.addLocalizationButton);
        deleteLocalization = view.findViewById(R.id.deleteLocalizationButton);
    }

    public void getListOfLocalizations(Realm realm, RealmResults<FavouriteData> favouriteDataRealmResults)
    {
        this.realm = realm;
        this.favouriteDataRealmResults = favouriteDataRealmResults;
    }
}
