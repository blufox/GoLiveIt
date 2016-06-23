package me.captivelabs.goliveit.utils;

/**
 * Created by Aron on 10/28/2015.
 */
public class Functions {
    public static String deriveVenueImage(double latitude, double longitude){
        return new StringBuilder().append("https://maps.googleapis.com/maps/api/staticmap?zoom=18&scale=2&size=517x210&maptype=roadmap%20&markers=color:red%7C").append(latitude).append(",").append(longitude).toString();

    }
    public static String deriveMyLocationImage(double latitude, double longitude){
        return new StringBuilder().append("https://maps.googleapis.com/maps/api/staticmap?zoom=12&scale=2&size=517x210&maptype=roadmap%20&markers=color:red%7C").append(latitude).append(",").append(longitude).toString();

    }

}
