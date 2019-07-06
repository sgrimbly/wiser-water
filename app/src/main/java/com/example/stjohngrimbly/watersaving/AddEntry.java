package com.example.stjohngrimbly.watersaving;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;

import java.util.ArrayList;
import java.util.Calendar;

public class AddEntry extends Fragment implements DatePickerDialog.OnDateSetListener{

    public static final String TAG = "AddEntry";

    private ArrayList<String> mHeadings = new ArrayList<>();
    private ArrayList<String> mInfo = new ArrayList<>();
    private ArrayList<Drawable> mImages = new ArrayList<>();
    private ArrayList<String> mTotals = new ArrayList<>();
    private ArrayList<Entry> entries = new ArrayList<>();
    private AddEntry.OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private TextView total;
    AddEntry_RecyclerViewAdapter adapter;

    public AddEntry() {
    }

    public static AddEntry newInstance(ArrayList<String> mTotals) {
        AddEntry fragment = new AddEntry();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mTotals", mTotals);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_entry, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initImageBitmaps();
        total = getView().findViewById(R.id.add_entry_total_header);
        if(savedInstanceState != null && savedInstanceState.getSerializable("Saved Totals")!=null){
            Log.d(TAG, "onViewCreated: state restored");


            total.setText(savedInstanceState.getString("Total Header"));

            mTotals.clear();
            mTotals=(ArrayList<String>)savedInstanceState.getSerializable("Saved Totals");
        }

        initRecyclerView();

        if(savedInstanceState!=null && savedInstanceState.getSerializable("Entries")!=null){
            entries = (ArrayList<Entry>)savedInstanceState.getSerializable("Entries");
        }
        else if(getArguments()!=null){
            entries = (ArrayList<Entry>)getArguments().getSerializable("Entries");

        }
        Calendar calendar = Calendar.getInstance();
        int startYear = calendar.get(Calendar.YEAR);
        int startMonth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DATE);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), this, startYear, startMonth, startDay);


        ImageButton addEntry = getView().findViewById(R.id.add_entry);
        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        ImageButton cancelEntry = getView().findViewById(R.id.cancel_entry);
        cancelEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });

    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Entries",entries);
        outState.putString("Current Fragment", "AddEntry");

        Log.d(TAG, "onSaveInstanceState: mTotals size="+mTotals.size());
        Log.d(TAG, "onSaveInstanceState: mTotals String="+mTotals.get(1)+"");

        outState.putSerializable("Saved Totals",mTotals);
        outState.putString("Total Header",total.getText()+"");
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initImageBitmaps(){
        mHeadings.add(getString(R.string.add_entry_shower));
        mInfo.add(getString(R.string.add_entry_shower_info));
        mImages.add(getResources().getDrawable(R.drawable.shower));

        mHeadings.add(getString(R.string.add_entry_toilet));
        mInfo.add(getString(R.string.add_entry_toilet_info));
        mImages.add(getResources().getDrawable(R.drawable.toilet));

        mHeadings.add(getString(R.string.add_entry_hygiene));
        mInfo.add(getString(R.string.add_entry_hygiene_info));
        mImages.add(getResources().getDrawable(R.drawable.hygiene));

        mHeadings.add(getString(R.string.add_entry_laundry));
        mInfo.add(getString(R.string.add_entry_laundry_info));
        mImages.add(getResources().getDrawable(R.drawable.laundry));

        mHeadings.add(getString(R.string.add_entry_dishes));
        mInfo.add(getString(R.string.add_entry_dishes_info));
        mImages.add(getResources().getDrawable(R.drawable.dishes));

        mHeadings.add(getString(R.string.add_entry_drinking));
        mInfo.add(getString(R.string.add_entry_drinking_info));
        mImages.add(getResources().getDrawable(R.drawable.drinking));

        mHeadings.add(getString(R.string.add_entry_cooking));
        mInfo.add(getString(R.string.add_entry_cooking_info));
        mImages.add(getResources().getDrawable(R.drawable.cooking));

        mHeadings.add(getString(R.string.add_entry_cleaning));
        mInfo.add(getString(R.string.add_entry_cleaning_info));
        mImages.add(getResources().getDrawable(R.drawable.cleaning));

        mHeadings.add(getString(R.string.add_entry_other));
        mInfo.add(getString(R.string.add_entry_other_info));
        mImages.add(getResources().getDrawable(R.drawable.other));

        for(int i=0; i<mInfo.size(); i++){
            mTotals.add("TOTAL: 0 litres");
        }
    }

    private void initRecyclerView(){
        recyclerView = getView().findViewById(R.id.add_entry_recycler_view); // This should be diary_recycler_view but it crashes then.
        TextView totalHeading = getView().findViewById(R.id.add_entry_total_header);
        Log.d(TAG, "initRecyclerView: " + mTotals.get(1));
        adapter = new AddEntry_RecyclerViewAdapter(getContext(), mImages, mHeadings, mInfo, mTotals, totalHeading);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onDateSet(DatePicker datePicker, final int year, final int month, final int day) {
        // Run task in background thread
        new Thread(new Runnable() {
            public void run() {
                String[] monthName = {"January", "February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                String date = day +" "+monthName[month]+" "+year;
                String totals = "";


                for(int i=0; i<mTotals.size() ;i++)
                {
                    String text=mTotals.get(i).substring(7);
                    text=text.substring(0, text.length()-7);
                    totals+=","+text;
                }

                String[] temp = totals.substring(1).split(",");
                int [] totalsArray = new int[temp.length];
                for (int i=0; i<temp.length; i++){
                    totalsArray[i]=Integer.parseInt(temp[i]);
                }
                Entry newEntry = new Entry(date,totalsArray[0],totalsArray[1],totalsArray[2],totalsArray[3],totalsArray[4],totalsArray[5],totalsArray[6],totalsArray[7],totalsArray[8]);

                // CHECK IF THIS DATE ENTRY ALREADY EXISTS
                boolean duplicate=false;
                for(int i=0; i<entries.size(); i++){
                    if (newEntry.compareTo(entries.get(i))==0){
                        duplicate=true;
                    }
                }
                if(!duplicate){
                    entries.add(newEntry);

                    Log.d("AddEntry", "onDateSet: "+date+totals);
                    Utilities.addDiaryEntry(getContext(),getContext().getFilesDir().getAbsolutePath()+"/database.txt",date,totals);
                    // Re read data from text file or read entries etc

                    MainActivity.selectDrawerItem(R.id.nav_second_fragment,entries.size()-1);
                }
                else{
                    Toast.makeText(getContext(),"Entry failed. Entry already added for "+date+".",Toast.LENGTH_LONG).show();
                }
            }
        }).start();





    }

}
