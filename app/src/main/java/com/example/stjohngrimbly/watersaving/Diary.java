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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;


public class Diary extends Fragment implements Serializable{

    public static final String TAG = "Diary";

    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mInfo = new ArrayList<>();
    private ArrayList<String> mTotals = new ArrayList<>();
    private ArrayList<Drawable> mImages = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    private int selectedEntry;
    private RecyclerView recyclerView;
    Diary_RecyclerViewAdapter adapter;

    public Diary() {
        // Required empty public constructor
    }

    public static Diary newInstance(String param1, String param2) {
        Diary fragment = new Diary();
        Bundle args = new Bundle();
        args.putString("", param1);
        args.putString("", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
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
        selectedEntry=getArguments().getInt("Diary Position");
        populateTotalsList(selectedEntry);
        initImageBitmaps(selectedEntry);
        initialiseButtons();
        initRecyclerView();
    }

    public void initialiseButtons(){
        final TextView diaryDate = getView().findViewById(R.id.diary_date);
        final TextView totalHeader = getView().findViewById(R.id.diary_total);
        diaryDate.setText(entries.get(selectedEntry).getDate());
        totalHeader.setText(getString(R.string.total_usage)+": " + entries.get(selectedEntry).calculateTotal());

        final ImageButton left = getView().findViewById(R.id.left);
        final ImageButton right = getView().findViewById(R.id.right);

        if(selectedEntry==0){
            left.setVisibility(View.INVISIBLE);
        }
        else if(selectedEntry==entries.size()-1) {
            right.setVisibility(View.INVISIBLE);
        }

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "onClick: left selectedEntry: " + selectedEntry);


                // TO DO: MOVE TO NEXT ITEM IN LIST
                right.setVisibility(View.VISIBLE);
                if(selectedEntry==1){
                    selectedEntry--;
                    left.setVisibility(View.INVISIBLE);
                }
                else if(selectedEntry>1){
                    Log.d("Click SE>1", "onClick: ");
                    selectedEntry--;
                }

                mTotals.clear();
                diaryDate.setText(entries.get(selectedEntry).getDate());
                totalHeader.setText(getString(R.string.total_usage)+": " + entries.get(selectedEntry).calculateTotal());
                populateTotalsList(selectedEntry);
                adapter.notifyDataSetChanged();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Click", "onClick: right selectedEntry="+selectedEntry);
                // TO DO: MOVE TO PREVIOUS ITEM IN LIST
                left.setVisibility(View.VISIBLE);
                if(selectedEntry==entries.size()-2){
                    selectedEntry++;
                    right.setVisibility(View.INVISIBLE);
                }
                else if(selectedEntry<entries.size()-2){
                    selectedEntry++;
                }
                mTotals.clear();
                diaryDate.setText(entries.get(selectedEntry).getDate());
                totalHeader.setText(getString(R.string.total_usage)+": " + entries.get(selectedEntry).calculateTotal());
                populateTotalsList(selectedEntry);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Entries",entries);
        outState.putString("Current Fragment", "Diary");
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
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
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

    private void populateTotalsList(int position){
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getShower()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getToilet()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getHygiene()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getLaundry()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getDishes()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getDrinking()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getCooking()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getCleaning()+" "+getString(R.string.litres));
        mTotals.add(getString(R.string.total_usage)+": "+entries.get(position).getOther()+" "+getString(R.string.litres));
    }

    private void initImageBitmaps(int position){
        populateTotalsList(0);

        mHeadings.add(getString(R.string.diary_shower));
        mInfo.add(getString(R.string.diary_shower_info));
        mImages.add(getResources().getDrawable(R.drawable.shower));

        mHeadings.add(getString(R.string.diary_toilet));
        mInfo.add(getString(R.string.diary_toilet_info));
        mImages.add(getResources().getDrawable(R.drawable.toilet));

        mHeadings.add(getString(R.string.diary_hygiene));
        mInfo.add(getString(R.string.diary_hygiene_info));
        mImages.add(getResources().getDrawable(R.drawable.hygiene));

        mHeadings.add(getString(R.string.diary_laundry));
        mInfo.add(getString(R.string.diary_laundry_info));
        mImages.add(getResources().getDrawable(R.drawable.laundry));

        mHeadings.add(getString(R.string.diary_dishes));
        mInfo.add(getString(R.string.diary_dishes_info));
        mImages.add(getResources().getDrawable(R.drawable.dishes));

        mHeadings.add(getString(R.string.diary_drinking));
        mInfo.add(getString(R.string.diary_drinking_info));
        mImages.add(getResources().getDrawable(R.drawable.drinking));

        mHeadings.add(getString(R.string.diary_cooking));
        mInfo.add(getString(R.string.diary_cooking_info));
        mImages.add(getResources().getDrawable(R.drawable.cooking));

        mHeadings.add(getString(R.string.diary_cleaning));
        mInfo.add(getString(R.string.diary_cleaning_info));
        mImages.add(getResources().getDrawable(R.drawable.cleaning));

        mHeadings.add(getString(R.string.diary_other));
        mInfo.add(getString(R.string.diary_other_info));
        mImages.add(getResources().getDrawable(R.drawable.other));
    }

    private void initRecyclerView(){
        recyclerView = getView().findViewById(R.id.diary_recycler_view);
        adapter = new Diary_RecyclerViewAdapter(getContext(), mImages, mHeadings, mInfo, mTotals);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
