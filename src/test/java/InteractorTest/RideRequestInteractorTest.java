package InteractorTest;

import Boundary.Account.User.AccountInteractorBoundary;
import Boundary.RideRequestInteractorBoundary;
import Boundary.Trip.TripInteractorBoundary;
import Entity.Boundary.Account.User.User;
import Entity.Boundary.RideRequest.RideRequest;
import Entity.Boundary.Trip.Car.Car;
import Entity.Boundary.Trip.DateTimeFormat.DateTimeFormat;
import Entity.Boundary.Trip.LocationInformation.LocationInformation;
import Entity.Boundary.Trip.Rules.Rules;
import Entity.Boundary.Trip.Trip;
import Entity.Bounded.Account.CellPhoneFormat.BoundedCellPhoneFormat;
import Entity.Bounded.Account.User.BoundedUser;
import Entity.Bounded.RideRequest.BoundedRideRequest;
import Entity.Bounded.Trip.Car.BoundedCar;
import Entity.Bounded.Trip.Car.Vehicle.BoundedVehicle;
import Entity.Bounded.Trip.DateTimeFormat.BoundedDateTimeFormat;
import Entity.Bounded.Trip.LocationInformation.BoundedLocationInformation;
import Entity.Bounded.Trip.LocationInformation.Location.BoundedLocation;
import Entity.Bounded.Trip.Rules.BoundedRules;
import Interactor.AccountInteractor;
import Interactor.RideRequestInteractor;
import Interactor.TripInteractor;
import junit.framework.Assert;
import org.junit.Before;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RideRequestInteractorTest {
    private User unauthorizedUser;
    private User authorizedUser;
    private User driver;
    private User rider;
    private User stranger;
    private RideRequestInteractorBoundary rb;
    private TripInteractorBoundary tb;
    @Before
    public void setUp(){
        unauthorizedUser = BoundedUser.Make("unauthroized","test", BoundedCellPhoneFormat.Make("111","222","3333"), "google.com");
        authorizedUser = BoundedUser.Make("authorized","test", BoundedCellPhoneFormat.Make("111","222","3333"), "google.com");
        driver = BoundedUser.Make("driver","test", BoundedCellPhoneFormat.Make("111","222","3333"), "google.com");
        rider = BoundedUser.Make("rider","test", BoundedCellPhoneFormat.Make("111","222","3333"), "google.com");
        stranger = BoundedUser.Make("stranger","test", BoundedCellPhoneFormat.Make("111","222","3333"), "google.com");
        tb = TripInteractor.INSTANCE;
        rb = RideRequestInteractor.INSTANCE;

        AccountInteractorBoundary ai = AccountInteractor.INSTANCE;
        ai.registerUser(unauthorizedUser);
        ai.registerUser(authorizedUser);
        ai.registerUser(driver);
        ai.registerUser(rider);
        ai.registerUser(stranger);
        ai.activateUser(authorizedUser.getAid());
        ai.activateUser(driver.getAid());
        ai.activateUser(rider.getAid());
        ai.activateUser(stranger.getAid());
    }

    @Test(expected=TripInteractorBoundary.NotFoundByTripIdException.class)
    public void requestRide_to_non_existing_ride_throws_RideNotFoundException(){
        RideRequest rr = BoundedRideRequest.Make(authorizedUser.getAid(), 2);
        rb.requestRide("asdf", rr);
    }

    @Test
    public void requestRide_to_existing_ride_return_jid(){
        tb = TripInteractor.INSTANCE;
        List<String> conditions = new ArrayList<>();
        LocationInformation locationInfo = BoundedLocationInformation.Make(BoundedLocation.Make("Chicago", "60616"), BoundedLocation.Make("Los Angeles",""));
        Car carInfo =  BoundedCar.Make(BoundedVehicle.Make("Chevy", "Cruze","White"), "IL", "COVID19");
        Rules passengerInfo = BoundedRules.Make(2, 5, conditions);
        DateTimeFormat dt = BoundedDateTimeFormat.MakeDateTime(2020,5,15,9,20);
        Trip t1 = tb.createTrip( driver.getAid(), locationInfo, carInfo, dt, passengerInfo);
        tb.registerTrip(t1);
        RideRequest rr = BoundedRideRequest.Make(authorizedUser.getAid(), 2);
        rb.requestRide(t1.getTid(), rr);

    }
    @Test(expected=RideRequestInteractorBoundary.JoinRequestNotFoundByJidException.class)
    public void confirmToNonExistingJoinRequest_throws_JoinRequestNotFoundByJidException(){
        tb = TripInteractor.INSTANCE;
        List<String> conditions = new ArrayList<>();
        LocationInformation locationInfo = BoundedLocationInformation.Make(BoundedLocation.Make("Chicago", "60616"), BoundedLocation.Make("Los Angeles",""));
        Car carInfo =  BoundedCar.Make(BoundedVehicle.Make("Chevy", "Cruze","White"), "IL", "COVID19");
        Rules passengerInfo = BoundedRules.Make(2, 5, conditions);
        DateTimeFormat dt = BoundedDateTimeFormat.MakeDateTime(2020,5,15,9,20);
        Trip t1 = tb.createTrip( driver.getAid(), locationInfo, carInfo, dt, passengerInfo);
        tb.registerTrip(t1);
        RideRequest rr = BoundedRideRequest.Make(authorizedUser.getAid(), 2);
        rb.requestRide(t1.getTid(), rr);
        rb.confirmRide(driver.getAid(),t1.getTid(), "asf");
    }

    @Test
    public void confirmRideToExistingRide_must_change_ride_confirmed_to_true(){
        tb = TripInteractor.INSTANCE;
        List<String> conditions = new ArrayList<>();
        LocationInformation locationInfo = BoundedLocationInformation.Make(BoundedLocation.Make("Chicago", "60616"), BoundedLocation.Make("Los Angeles",""));
        Car carInfo =  BoundedCar.Make(BoundedVehicle.Make("Chevy", "Cruze","White"), "IL", "COVID19");
        Rules passengerInfo = BoundedRules.Make(2, 5, conditions);
        DateTimeFormat dt = BoundedDateTimeFormat.MakeDateTime(2020,5,15,9,20);
        Trip t1 = tb.createTrip(driver.getAid(), locationInfo, carInfo, dt, passengerInfo);
        tb.registerTrip(t1);
        RideRequest rr = BoundedRideRequest.Make(authorizedUser.getAid(), 2);
        rb.requestRide(t1.getTid(), rr);
        rb.confirmRide(driver.getAid(), t1.getTid(),rr.getJid());

        Assert.assertTrue(rr.getIsRideConfirmed());
    }

    @Test(expected=RideRequestInteractorBoundary.UserDoNotHavePermissionToConfirmException.class)
    public void confirmRideByStranger_must_throw_UserDoNotHavePermissionToConfirmException(){
        tb = TripInteractor.INSTANCE;
        List<String> conditions = new ArrayList<>();
        LocationInformation locationInfo = BoundedLocationInformation.Make(BoundedLocation.Make("Chicago", "60616"), BoundedLocation.Make("Los Angeles",""));
        Car carInfo =  BoundedCar.Make(BoundedVehicle.Make("Chevy", "Cruze","White"), "IL", "COVID19");
        Rules passengerInfo = BoundedRules.Make(2, 5, conditions);
        DateTimeFormat dt = BoundedDateTimeFormat.MakeDateTime(2020,5,15,9,20);
        Trip t1 = tb.createTrip(driver.getAid(),locationInfo, carInfo, dt, passengerInfo);
        tb.registerTrip(t1);
        RideRequest rr = BoundedRideRequest.Make(rider.getAid(), 2);
        rb.requestRide(t1.getTid(), rr);
        rb.confirmRide(stranger.getAid(), t1.getTid(), rr.getJid());
    }

    @Test(expected=RideRequestInteractorBoundary.UserDoNotHavePermissionToDenyException.class)
    public void denyRideByStranger_must_throw_UserDoNotHavePermissionToDenyException(){
        tb = TripInteractor.INSTANCE;
        List<String> conditions = new ArrayList<>();
        LocationInformation locationInfo = BoundedLocationInformation.Make(BoundedLocation.Make("Chicago", "60616"), BoundedLocation.Make("Los Angeles",""));
        Car carInfo =  BoundedCar.Make(BoundedVehicle.Make("Chevy", "Cruze","White"), "IL", "COVID19");
        Rules passengerInfo = BoundedRules.Make(2, 5, conditions);
        DateTimeFormat dt = BoundedDateTimeFormat.MakeDateTime(2020,5,15,9,20);
        Trip t1 = tb.createTrip(driver.getAid(),locationInfo, carInfo, dt, passengerInfo);
        tb.registerTrip(t1);
        RideRequest rr = BoundedRideRequest.Make(rider.getAid(), 2);
        rb.requestRide(t1.getTid(), rr);
        rb.denyRide(stranger.getAid(), t1.getTid(), rr.getJid());
    }
    @Test(expected=RideRequestInteractorBoundary.JoinRequestNotFoundByJidException.class)
    public void denyToNonExistingJoinRequest_throws_JoinRequestNotFoundByJidException(){
        tb = TripInteractor.INSTANCE;
        List<String> conditions = new ArrayList<>();
        LocationInformation locationInfo = BoundedLocationInformation.Make(BoundedLocation.Make("Chicago", "60616"), BoundedLocation.Make("Los Angeles",""));
        Car carInfo =  BoundedCar.Make(BoundedVehicle.Make("Chevy", "Cruze","White"), "IL", "COVID19");
        Rules passengerInfo = BoundedRules.Make(2, 5, conditions);
        DateTimeFormat dt = BoundedDateTimeFormat.MakeDateTime(2020,5,15,9,20);
        Trip t1 = tb.createTrip(driver.getAid(),locationInfo, carInfo, dt, passengerInfo);
        tb.registerTrip(t1);
        RideRequest rr = BoundedRideRequest.Make(authorizedUser.getAid(), 2);
        rb.requestRide(t1.getTid(), rr);
        rb.denyRide(driver.getAid(), t1.getTid(), "asf");
    }


    @Test
    public void denyRideToExistingRide_must_change_ride_confirmed_to_true(){
        tb = TripInteractor.INSTANCE;
        List<String> conditions = new ArrayList<>();
        LocationInformation locationInfo = BoundedLocationInformation.Make(BoundedLocation.Make("Chicago", "60616"), BoundedLocation.Make("Los Angeles",""));
        Car carInfo =  BoundedCar.Make(BoundedVehicle.Make("Chevy", "Cruze","White"), "IL", "COVID19");
        Rules passengerInfo = BoundedRules.Make(2, 5, conditions);
        DateTimeFormat dt = BoundedDateTimeFormat.MakeDateTime(2020,5,15,9,20);
        Trip t1 = tb.createTrip(driver.getAid(), locationInfo, carInfo, dt, passengerInfo);
        tb.registerTrip(t1);
        RideRequest rr = BoundedRideRequest.Make(authorizedUser.getAid(), 2);
        rb.requestRide(t1.getTid(), rr);
        rb.denyRide(driver.getAid(), t1.getTid(), rr.getJid());
        Assert.assertFalse(rr.getIsRideConfirmed());
    }
}