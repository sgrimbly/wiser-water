package com.example.stjohngrimbly.watersaving;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Utilities {

    public static ArrayList<Entry> LoadDiaryEntries(Context context, String fileName) throws Exception {

        final String TAG = "LoadDiaryEntries";

        ArrayList<Entry> Entries = new ArrayList<Entry>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            String st;

            while ((st = br.readLine()) != null) {
                String[] list = st.split(",");
                Log.d("", "LoadDiaryEntries: "+st);
                Entries.add(new Entry(list[0],
                        Integer.parseInt(list[1]), Integer.parseInt(list[2]),
                        Integer.parseInt(list[3]), Integer.parseInt(list[4]),
                        Integer.parseInt(list[5]), Integer.parseInt(list[6]),
                        Integer.parseInt(list[7]), Integer.parseInt(list[8]),
                        Integer.parseInt(list[9]))
                );
            }
            br.close();
        } catch(IOException e1){
            System.out.println(e1);
        }

        //Ensure entries are sorted.
        Log.d(TAG, "LoadDiaryEntries: sorting");
        Collections.sort(Entries);
        return Entries;
    }

    public static void addDiaryEntry(Context context, String fileName, String date,String totals){
        try {
            Log.d("Utilities", "addDiaryEntry: Writing " + date+totals);
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true)); //Add ,true when the file starts being written to correctly
            bw.write(date+totals);
            bw.newLine();
            bw.close();
        }
        catch (Exception e) {
            Log.d("Exception", "File write failed: " + e.toString());
        }
    }

    public static int getValue(String totalUsed){
        totalUsed=totalUsed.substring(7);
        totalUsed=totalUsed.substring(0, totalUsed.length()-7);
        return Integer.parseInt(totalUsed);
    }

}