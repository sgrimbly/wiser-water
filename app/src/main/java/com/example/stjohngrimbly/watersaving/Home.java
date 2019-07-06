package com.example.stjohngrimbly.watersaving;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends Fragment implements Serializable{

    public static final String TAG = "Home";

    private OnFragmentInteractionListener mListener;
    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<Drawable> mImages = new ArrayList<>();
    private Home_RecyclerViewAdapter adapter;

    public Home() {
    }

    public static Home newInstance() {
        Home fragment = new Home();
        Bundle args = new Bundle();
        //args.putString("", param1);
        //args.putString("", param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*
    public void refreshRecyclerView(){
        adapter.notifyDataSetChanged();
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null){
            entries=(ArrayList<Entry>) savedInstanceState.getSerializable("Entries");
        }
        //this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if(savedInstanceState!=null && savedInstanceState.getSerializable("Entries")!=null){
            entries = (ArrayList<Entry>)savedInstanceState.getSerializable("Entries");
        }
        else if(getArguments()!=null){
            entries = (ArrayList<Entry>)getArguments().getSerializable("Entries");

        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initImageBitmaps();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Entries",entries);
        outState.putString("Current Fragment", "Home");
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void initImageBitmaps(){
        TextView total = getView().findViewById(R.id.home_total);
        int totalUsed = 0;
        for (int i=0; i<entries.size(); i++){
            totalUsed+=entries.get(i).calculateTotal();
        }
        total.setText(getString(R.string.total_usage) + ": " + totalUsed+" "+getString(R.string.litres).toUpperCase());

        for (int i=0; i<entries.size(); i++){
            int litresUsed =entries.get(i).calculateTotal();
            if (litresUsed>100){ // Bad water usage
                mImages.add(getResources().getDrawable(R.drawable.sad_drop));
            }
            else if(litresUsed<=50){ // Great water usage
                mImages.add(getResources().getDrawable(R.drawable.happy_drop));
            }
            else{ // Average water usage
                mImages.add(getResources().getDrawable(R.drawable.average_drop));
            }
            mNames.add(entries.get(i).getDate() + " - " + litresUsed + " " + getString(R.string.litres));
        }

        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = getView().findViewById(R.id.home_recycler_view);
        adapter = new Home_RecyclerViewAdapter(getContext(), mNames, mImages);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
