package com.cmdrj.caremydriveworkshop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class RecVewClient implements Parcelable {

    private String ID;

    private String Name;

    private String MbNo;

    private String Email;

    private GeoPoint AddressGeoPoint;
    private String Address;
    private String City;
    private String State;
    private String Country;
    private String Pincode;


    private String Pic;


    // TODO : Remove following fields instead collection on firestore
    // 0 -> Requested     -1 -> Rejected     -2 -> Rescheduled     1 -> Accepted     2 -> In-Progress     3 -> Completed
    private int initBookingsLen;
    private int penBookingsLen;
    private int rejectedBookingsLen;
    private int pastBookingsLen;

    ArrayList<String> initBookings = new ArrayList<String>();
    ArrayList<String> penBookings = new ArrayList<String>();
    ArrayList<String> rejectedBookings = new ArrayList<String>();
    ArrayList<String> pastBookings = new ArrayList<String>();


//    private int vehiclesOwnedLen;
//    ArrayList<RecVewCar> vehiclesOwned = new ArrayList<RecVewCar>();

    // TODO : over

    public RecVewClient() {
    }

    public RecVewClient(String id, String name, String mbNo, String email, GeoPoint addressGeoPoint, String address, String city, String state, String country, String pincode) {
        ID = id;
        Name = name;
        MbNo = mbNo;
        Email = email;
        AddressGeoPoint = addressGeoPoint;
        Address = address;
        City = city;
        State = state;
        Country = country;
        Pincode = pincode;
    }


    // TODO : Remove following constructors after firebase -----> No use

    public RecVewClient(String id, String name, String mbNo, String email, String address, String city, String state, ArrayList<RecVewCar> vehiclesOwned) {
        ID = id;
        Name = name;
        MbNo = mbNo;
        Email = email;
        Address = address;
        City = city;
        State = state;
        this.initBookingsLen = initBookings.size();
        this.penBookingsLen = penBookings.size();
        this.rejectedBookingsLen = rejectedBookings.size();
        this.pastBookingsLen = pastBookings.size();
//        this.vehiclesOwnedLen = vehiclesOwned.size();
//        this.vehiclesOwned = vehiclesOwned;
    }

    public RecVewClient(String id, String name, String mbNo, String email, String address, String city, String state, ArrayList<String> initBookings, ArrayList<String> penBookings, ArrayList<String> pastBookings, ArrayList<RecVewCar> vehiclesOwned) {
        ID = id;
        Name = name;
        MbNo = mbNo;
        Email = email;
        Address = address;
        City = city;
        State = state;
        this.initBookingsLen = initBookings.size();
        this.penBookingsLen = penBookings.size();
        this.pastBookingsLen = pastBookings.size();
        this.initBookings = initBookings;
        this.penBookings = penBookings;
        this.pastBookings = pastBookings;
//        this.vehiclesOwnedLen = vehiclesOwned.size();
//        this.vehiclesOwned = vehiclesOwned;
    }

    public RecVewClient(String id, String name, String mbNo, String email, String address, String city, String state, ArrayList<String> initBookings, ArrayList<String> penBookings, ArrayList<String> rejectedBookings, ArrayList<String> pastBookings, ArrayList<RecVewCar> vehiclesOwned) {
        ID = id;
        Name = name;
        MbNo = mbNo;
        Email = email;
        Address = address;
        City = city;
        State = state;
        this.initBookingsLen = initBookings.size();
        this.penBookingsLen = penBookings.size();
        this.rejectedBookingsLen = rejectedBookings.size();
        this.pastBookingsLen = pastBookings.size();
        this.initBookings = initBookings;
        this.penBookings = penBookings;
        this.rejectedBookings = rejectedBookings;
        this.pastBookings = pastBookings;
//        this.vehiclesOwnedLen = vehiclesOwned.size();
//        this.vehiclesOwned = vehiclesOwned;
    }

    // TODO : over

    protected RecVewClient(Parcel in) {
        ID = in.readString();
        Name = in.readString();
        MbNo = in.readString();
        Email = in.readString();

        double latAddressGeoPoint = in.readDouble();
        double longAddressGeoPoint = in.readDouble();
        AddressGeoPoint = new GeoPoint(latAddressGeoPoint,longAddressGeoPoint);

        Address = in.readString();
        City = in.readString();
        State = in.readString();
        Country = in.readString();
        Pincode = in.readString();
        Pic = in.readString();
        initBookingsLen = in.readInt();
        penBookingsLen = in.readInt();
        rejectedBookingsLen = in.readInt();
        pastBookingsLen = in.readInt();
        initBookings = in.createStringArrayList();
        penBookings = in.createStringArrayList();
        rejectedBookings = in.createStringArrayList();
        pastBookings = in.createStringArrayList();
//        vehiclesOwnedLen = in.readInt();
//        vehiclesOwned = in.createTypedArrayList(RecVewCar.CREATOR);
    }

    public static final Creator<RecVewClient> CREATOR = new Creator<RecVewClient>() {
        @Override
        public RecVewClient createFromParcel(Parcel in) {
            return new RecVewClient(in);
        }

        @Override
        public RecVewClient[] newArray(int size) {
            return new RecVewClient[size];
        }
    };

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getMbNo() {
        return MbNo;
    }

    public String getEmail() {
        return Email;
    }

    public GeoPoint getAddressGeoPoint() {
        return AddressGeoPoint;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getCountry() {
        return Country;
    }

    public String getPincode() {
        return Pincode;
    }

    public String getPic() {
        return Pic;
    }

    public int getInitBookingsLen() {
        return initBookingsLen;
    }

    public int getPenBookingsLen() {
        return penBookingsLen;
    }

    public int getrejectedBookingsLen() {
        return rejectedBookingsLen;
    }

    public int getPastBookingsLen() {
        return pastBookingsLen;
    }

    public ArrayList<String> getInitBookings() {
        return initBookings;
    }

    public ArrayList<String> getPenBookings() {
        return penBookings;
    }

    public ArrayList<String> getrejectedBookings() {
        return rejectedBookings;
    }

    public ArrayList<String> getPastBookings() {
        return pastBookings;
    }

//    public int getVehiclesOwnedLen() {
//        return vehiclesOwnedLen;
//    }
//
//    public ArrayList<RecVewCar> getVehiclesOwned() {
//        return vehiclesOwned;
//    }



    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setMbNo(String mbNo) {
        MbNo = mbNo;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setAddressGeoPoint(GeoPoint addressGeoPoint) {
        AddressGeoPoint = addressGeoPoint;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        State = state;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public void setInitBookingsLen() {
        this.initBookingsLen = initBookings.size();
    }

    public void setPenBookingsLen() {
        this.penBookingsLen = penBookings.size();
    }

    public void setrejectedBookingsLen() {
        this.rejectedBookingsLen = rejectedBookings.size();
    }

    public void setPastBookingsLen() {
        this.pastBookingsLen = pastBookings.size();
    }


    public void setInitBookings(ArrayList<String> initBookings) {
        this.initBookings = initBookings;
    }

    public void setPenBookings(ArrayList<String> penBookings) {
        this.penBookings = penBookings;
    }

    public void setrejectedBookings(ArrayList<String> rejectedBookings) {
        this.rejectedBookings = rejectedBookings;
    }

    public void setPastBookings(ArrayList<String> pastBookings) {
        this.pastBookings = pastBookings;
    }

//    public void setVehiclesOwnedLen() {
//        this.vehiclesOwnedLen = vehiclesOwned.size();
//    }
//
//    public void setVehiclesOwned(ArrayList<RecVewCar> vehiclesOwned) {
//        this.vehiclesOwned = vehiclesOwned;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Name);
        dest.writeString(MbNo);
        dest.writeString(Email);

        dest.writeDouble(AddressGeoPoint.getLatitude());
        dest.writeDouble(AddressGeoPoint.getLongitude());


        dest.writeString(Address);
        dest.writeString(City);
        dest.writeString(State);
        dest.writeString(Country);
        dest.writeString(Pincode);
        dest.writeString(Pic);
        dest.writeInt(initBookingsLen);
        dest.writeInt(penBookingsLen);
        dest.writeInt(rejectedBookingsLen);
        dest.writeInt(pastBookingsLen);
        dest.writeStringList(initBookings);
        dest.writeStringList(penBookings);
        dest.writeStringList(rejectedBookings);
        dest.writeStringList(pastBookings);
//        dest.writeInt(vehiclesOwnedLen);
//        dest.writeTypedList(vehiclesOwned);
    }
}
