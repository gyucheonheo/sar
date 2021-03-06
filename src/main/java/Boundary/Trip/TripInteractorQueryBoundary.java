package Boundary.Trip;

import Entity.Boundary.Trip.Car.Car;
import Entity.Boundary.Trip.DateTimeFormat.DateTimeFormat;
import Entity.Boundary.Trip.LocationInformation.LocationInformation;
import Entity.Boundary.Trip.Rules.Rules;
import Entity.Boundary.Trip.Trip;

import java.text.ParseException;
import java.util.List;

public interface TripInteractorQueryBoundary {
    Trip createTrip(String aid, LocationInformation locationInformation, Car carInformation, DateTimeFormat dt, Rules rules);
    Trip getTripById(String tid);
    List<Trip> getAllTrips(String from, String to, String date);
    List<Trip> getTripByUserId(String aid);
    List<Trip> getPostingTripsBetweenDates(String start_date, String end_date) throws ParseException;
    List<Trip> getTakingTripsBetweenDates(String start_date, String end_date) throws ParseException;
    int getTotalRidesByAid(String aid);
}
