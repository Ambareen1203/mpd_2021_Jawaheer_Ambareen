package org.me.gcu.equakestartercode;

//Student name: Ambareen Shabnam Jawaheer
//Student ID: S1903330

import java.util.Calendar;

public class EarthQuake implements java.io.Serializable {


    private String location;
    private int depth;
    private Calendar startDate;
    private double magnitude;
    private double latitude;
    private double longitude;

    public String getLocation() {
        return location;
    }

    public int getDepth() {
        return depth;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }




    public EarthQuake(String[] earthQuake)  {
        for (int i = 0;i<earthQuake.length;i++){
            earthQuake[i] = earthQuake[i].split(": ")[1];
        }

        earthQuake[0]=earthQuake[0].split(", ")[1].split("\\s[0-9]+:[0-9]+:[0-9]+\\s$")[0];
        earthQuake[0] = convertStringDateToInt(earthQuake[0]);

        this.startDate = Calendar.getInstance();
        this.startDate.set(Integer.parseInt(earthQuake[0].substring(6,10)),Integer.parseInt(earthQuake[0].substring(3,5))-1,Integer.parseInt(earthQuake[0].substring(0,2)));
        this.location = earthQuake[1].substring(0, 1).toUpperCase() + earthQuake[1].substring(1).toLowerCase();
        this.latitude= Double.parseDouble(earthQuake[2].split(",")[0]);
        this.longitude= Double.parseDouble(earthQuake[2].split(",")[1]);
        this.depth = Integer.parseInt(earthQuake[3].split(" ")[0]);
        this.magnitude = Double.parseDouble(earthQuake[4]);
    }

    @Override
    public String toString() {
        return "Date: "+this.startDate.toString()+" ; Location: "+this.location+" ; Lat/long: "+
                this.latitude+"/"+this.longitude+" ; Depth: "+this.depth+"; Magnitude : "+this.magnitude;
    }

    private String convertStringDateToInt(String date){
        String[] monthStrings = new String[]{"Jan","Feb","Mar","Apr","May","June","July","Aug","Sep","Oct","Nov","Dec"};
        int i = 0;
        while (i<monthStrings.length) {
            String replacement;
            if (i<9){
                replacement = "0"+(i+1);
            }
            else {
                replacement = Integer.toString(i+1);
            }
            date = date.replace(monthStrings[i],replacement);
            i++;
        }
        return date;
    }
}

