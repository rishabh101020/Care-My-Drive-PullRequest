package com.cmdrj.caremydriveadmin;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class RecVewWorkshop implements Parcelable {


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

    private String Img1;
    private String Img2;
    private String Img3;
    private String Img4;

    // -1 -> Not Rated        0 to 5  -> Rated
    private int Rating = -1;
    private int NumRated = 0;

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

    private int servicesProvidedLen;

    ArrayList<RecVewService> servicesProvided = new ArrayList<RecVewService>();

    // TODO : over

    public RecVewWorkshop() {
    }


    public RecVewWorkshop(String id, String name, String mbNo, String email, GeoPoint addressGeoPoint, String address, String city, String state, String country, String pincode) {
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

    public RecVewWorkshop(String id, String name, String mbNo, String email, GeoPoint addressGeoPoint, String address, String city, String state, String country, String pincode, int rating) {
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
        Rating = rating;
    }

    // TODO : Remove following constructors after firebase -----> No use

    public RecVewWorkshop(String id, String name, String mbNo, String email, String address, String city, String state, int rating, ArrayList<RecVewService> servicesProvided) {
        ID = id;
        Name = name;
        MbNo = mbNo;
        Email = email;
        Address = address;
        City = city;
        State = state;
        Rating = rating;
        this.servicesProvidedLen = servicesProvided.size();
        this.servicesProvided = servicesProvided;


// TODO : remove following
        AddressGeoPoint = new GeoPoint(24.5697761,73.7347608);
//TODO ; over
    }

    public RecVewWorkshop(String id, String name, String mbNo, String email, String address, String city, String state, int rating, ArrayList<RecVewService> servicesProvided, ArrayList<String> initBookings, ArrayList<String> penBookings, ArrayList<String> rejectedBookings, ArrayList<String> pastBookings) {
        ID = id;
        Name = name;
        MbNo = mbNo;
        Email = email;
        Address = address;
        City = city;
        State = state;
        Rating = rating;
        this.servicesProvidedLen = servicesProvided.size();
        this.servicesProvided = servicesProvided;
        this.initBookingsLen = initBookings.size();
        this.penBookingsLen = penBookings.size();
        this.rejectedBookingsLen = rejectedBookings.size();
        this.pastBookingsLen = pastBookings.size();
        this.initBookings = initBookings;
        this.penBookings = penBookings;
        this.rejectedBookings = rejectedBookings;
        this.pastBookings = pastBookings;


// TODO : remove following
        AddressGeoPoint = new GeoPoint(24.5697761,73.7347608);
//TODO ; over
    }

    // TODO : over

//    protected RecVewWorkshop(Parcel in) {
//        ID = in.readString();
//        Name = in.readString();
//        MbNo = in.readString();
//        Email = in.readString();
//        Address = in.readString();
//        City = in.readString();
//        State = in.readString();
//        Pic = in.readInt();
//        Rating = in.readInt();
//        initBookingsLen = in.readInt();
//        penBookingsLen = in.readInt();
//        rejectedBookingsLen = in.readInt();
//        pastBookingsLen = in.readInt();
//        initBookings = in.createStringArrayList();
//        penBookings = in.createStringArrayList();
//        rejectedBookings = in.createStringArrayList();
//        pastBookings = in.createStringArrayList();
//        servicesProvidedLen = in.readInt();
//        servicesProvided = in.createPar();
//    }
//
//    public static final Creator<RecVewWorkshop> CREATOR = new Creator<RecVewWorkshop>() {
//        @Override
//        public RecVewWorkshop createFromParcel(Parcel in) {
//            return new RecVewWorkshop(in);
//        }
//
//        @Override
//        public RecVewWorkshop[] newArray(int size) {
//            return new RecVewWorkshop[size];
//        }
//    };

    protected RecVewWorkshop(Parcel in) {
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
        Img1 = in.readString();
        Img2 = in.readString();
        Img3 = in.readString();
        Img4 = in.readString();
        Rating = in.readInt();
        NumRated = in.readInt();
        initBookingsLen = in.readInt();
        penBookingsLen = in.readInt();
        rejectedBookingsLen = in.readInt();
        pastBookingsLen = in.readInt();
        initBookings = in.createStringArrayList();
        penBookings = in.createStringArrayList();
        rejectedBookings = in.createStringArrayList();
        pastBookings = in.createStringArrayList();
        servicesProvidedLen = in.readInt();
        servicesProvided = in.createTypedArrayList(RecVewService.CREATOR);
    }

    public static final Creator<RecVewWorkshop> CREATOR = new Creator<RecVewWorkshop>() {
        @Override
        public RecVewWorkshop createFromParcel(Parcel in) {
            return new RecVewWorkshop(in);
        }

        @Override
        public RecVewWorkshop[] newArray(int size) {
            return new RecVewWorkshop[size];
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

    public String getImg1() {
        return Img1;
    }

    public String getImg2() {
        return Img2;
    }

    public String getImg3() {
        return Img3;
    }

    public String getImg4() {
        return Img4;
    }

    public int getRating() {
        return Rating;
    }

    public int getNumRated() {
        return NumRated;
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

    public int getServicesProvidedLen() {
        return servicesProvidedLen;
    }

    public ArrayList<RecVewService> getServicesProvided() {
        return servicesProvided;
    }



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

    public void setImg1(String img1) {
        Img1 = img1;
    }

    public void setImg2(String img2) {
        Img2 = img2;
    }

    public void setImg3(String img3) {
        Img3 = img3;
    }

    public void setImg4(String img4) {
        Img4 = img4;
    }

    public void setRating(int rating) {
        Rating = rating;
    }

    public void setNumRated(int numRated) {
        NumRated = numRated;
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

    public void setServicesProvidedLen() {
        this.servicesProvidedLen = servicesProvided.size();
    }

    public void setServicesProvided(ArrayList<RecVewService> servicesProvided) {
        this.servicesProvided = servicesProvided;
    }

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
        dest.writeString(Img1);
        dest.writeString(Img2);
        dest.writeString(Img3);
        dest.writeString(Img4);
        dest.writeInt(Rating);
        dest.writeInt(NumRated);
        dest.writeInt(initBookingsLen);
        dest.writeInt(penBookingsLen);
        dest.writeInt(rejectedBookingsLen);
        dest.writeInt(pastBookingsLen);
        dest.writeStringList(initBookings);
        dest.writeStringList(penBookings);
        dest.writeStringList(rejectedBookings);
        dest.writeStringList(pastBookings);
        dest.writeInt(servicesProvidedLen);
        dest.writeTypedList(servicesProvided);
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(ID);
//        dest.writeString(Name);
//        dest.writeString(MbNo);
//        dest.writeString(Email);
//        dest.writeString(Address);
//        dest.writeString(City);
//        dest.writeString(State);
//        dest.writeInt(Pic);
//        dest.writeInt(Rating);
//        dest.writeInt(initBookingsLen);
//        dest.writeInt(penBookingsLen);
//        dest.writeInt(rejectedBookingsLen);
//        dest.writeInt(pastBookingsLen);
//        dest.writeStringList(initBookings);
//        dest.writeStringList(penBookings);
//        dest.writeStringList(rejectedBookings);
//        dest.writeStringList(pastBookings);
//        dest.writeInt(servicesProvidedLen);
//        dest.writeStringList(servicesProvided);
//    }
}
