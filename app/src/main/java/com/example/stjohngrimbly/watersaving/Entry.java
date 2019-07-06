package com.example.stjohngrimbly.watersaving;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.text.DateFormatSymbols;

public class Entry implements Serializable, Comparable<Entry> {
    private String date;
    private int shower;
    private int toilet;
    private int hygiene;
    private int laundry;
    private int dishes;
    private int drinking;
    private int cooking;
    private int cleaning;
    private int other;

    public Entry(String date, int shower, int toilet, int hygiene, int laundry, int dishes, int drinking, int cooking, int cleaning, int other) {
        this.date = date;
        this.shower = shower;
        this.toilet = toilet;
        this.hygiene = hygiene;
        this.laundry = laundry;
        this.dishes = dishes;
        this.drinking = drinking;
        this.cooking = cooking;
        this.cleaning = cleaning;
        this.other = other;
    }
    public Entry() {
    }

    public int calculateTotal(){
        return shower+toilet+hygiene+laundry+dishes+drinking+cooking+cleaning+other;
    }

    // Getting index out of bounds exception
    public String getDateInWords(){
        String[] temp = date.split(".");
        Log.d("TEST", "getDateInWords: "+temp.length);
        int day = Integer.parseInt(temp[0]);
        int month = Integer.parseInt(temp[1]);
        int year = Integer.parseInt(temp[2]);

        return day+" "+ (new DateFormatSymbols().getMonths()[month-1])+" "+year;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "date='" + date + '\'' +
                ", shower=" + shower +
                ", toilet=" + toilet +
                ", hygiene=" + hygiene +
                ", laundry=" + laundry +
                ", dishes=" + dishes +
                ", drinking=" + drinking +
                ", cooking=" + cooking +
                ", cleaning=" + cleaning +
                ", other=" + other +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getShower() {
        return shower;
    }

    public void setShower(int shower) {
        this.shower = shower;
    }

    public int getToilet() {
        return toilet;
    }

    public void setToilet(int toilet) {
        this.toilet = toilet;
    }

    public int getHygiene() {
        return hygiene;
    }

    public void setHygiene(int hygiene) {
        this.hygiene = hygiene;
    }

    public int getLaundry() {
        return laundry;
    }

    public void setLaundry(int laundry) {
        this.laundry = laundry;
    }

    public int getDishes() {
        return dishes;
    }

    public void setDishes(int dishes) {
        this.dishes = dishes;
    }

    public int getDrinking() {
        return drinking;
    }

    public void setDrinking(int drinking) {
        this.drinking = drinking;
    }

    public int getCooking() {
        return cooking;
    }

    public void setCooking(int cooking) {
        this.cooking = cooking;
    }

    public int getCleaning() {
        return cleaning;
    }

    public void setCleaning(int cleaning) {
        this.cleaning = cleaning;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }


    @Override
    public int compareTo(@NonNull Entry entry) {
        String [] thisDate = getDate().split(" "); // 12 August 2018
        String [] otherDate = entry.getDate().split(" "); // 11 May 2017

        // CHECK YEARS FIRST
        int thisYear = Integer.parseInt(thisDate[2]);
        int otherYear = Integer.parseInt(otherDate[2]);
        if (thisYear>otherYear){
            return 1;
        }
        else if(thisYear<otherYear){
            return -1;
        }

        // REACHING HERE MEANS YEARS ARE EQUAL
        // NOW CHECK BY MONTH
        int thisMonth=0;
        int otherMonth=0;
        String[] monthName = {"January", "February","March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for(int i=0;i<monthName.length;i++){
            if(monthName[i].equals(thisDate[1])){
                thisMonth=i;
            }
            if(monthName[i].equals(thisDate[1])){
                otherMonth=i;
            }
        }
        if (thisMonth>otherMonth){
            return 1;
        }
        else if(thisMonth<otherMonth){
            return -1;
        }

        // REACHING HERE MEANS YEARS AND MONTHS ARE EQUAL
        // NOW CHECK BY DATE
        int thisDay=Integer.parseInt(thisDate[0]);
        int otherDay=Integer.parseInt(otherDate[0]);
        if (thisDay>otherDay){
            return 1;
        }
        else if(thisDay<otherDay){
            return -1;
        }
        else{
            return 0;
        }

        // REACHING HERE MEANS DATES ARE EQUAL
        // IF WE WERE TO ALLOW MULTIPLE DAILY ENTRIES, WE COULD NOW FURTHER SORT BY THE LITRES USED.
    }
}