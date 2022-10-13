package com.cmdrj.caremydriveadmin;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class RecVewCar implements Parcelable {

    private String ID;

    private DocumentReference ClientRef;

    private String Name;

    private String No;

    private String Model;
    private String Manufacturer;

    // 0 -> Petrol     1-> Diesel     2 -> Electric     3 -> CNG
    private int FuelType;

    // 2-> Bikes     4 -> Cars     6 -> Heavy     3 -> Others
    private int WheelType;

    private String Pic;

    // true -> visible      false -> not visible
    private boolean Visibility;

    private String Details;

    public RecVewCar() {
    }

    private RecVewCar(Parcel in) {
        ID = in.readString();

        String pathClientRef = in.readString();
        ClientRef = pathClientRef == null ? null : FirebaseFirestore.getInstance().document(pathClientRef);

        Name = in.readString();
        No = in.readString();
        Model = in.readString();
        Manufacturer = in.readString();
        FuelType = in.readInt();
        WheelType = in.readInt();
        Pic = in.readString();
        Visibility = in.readByte() != 0;
        Details = in.readString();


//        https://www.sitepoint.com/transfer-data-between-activities-with-android-parcelable/
//        previousOwners = in.createStringArrayList();
//        object = in.readParcelable(Bar.class.getClassLoader());
    }



    public RecVewCar(String id, DocumentReference clientRef, String name, String no, String model, String manufacturer, int fuelType, int wheelType, String details) {
        ID = id;
        ClientRef = clientRef;
        Name = name;
        No = no;
        Model = model;
        Manufacturer = manufacturer;
        FuelType = fuelType;
        WheelType = wheelType;
        Visibility = true;
        Details = details;

    }

    public static final Creator<RecVewCar> CREATOR = new Creator<RecVewCar>() {
        @Override
        public RecVewCar createFromParcel(Parcel in) {
            return new RecVewCar(in);
        }

        @Override
        public RecVewCar[] newArray(int size) {
            return new RecVewCar[size];
        }
    };

    public String getID() {
        return ID;
    }

    public DocumentReference getClientRef() {
        return ClientRef;
    }

    public String getName() {
        return Name;
    }

    public String getNo() {
        return No;
    }

    public String getModel() {
        return Model;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public int getFuelType() {
        return FuelType;
    }

    public String getPic() {
        return Pic;
    }

    public int getWheelType() {
        return WheelType;
    }

    public boolean getVisibility() {
        return Visibility;
    }

    public String getDetails() {
        return Details;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setClientRef(DocumentReference clientRef) {
        ClientRef = clientRef;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setNo(String no) {
        No = no;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public void setFuelType(int fuelType) {
        FuelType = fuelType;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public void setWheelType(int wheelType) {
        WheelType = wheelType;
    }

    public void setVisibility(boolean visibility) {
        Visibility = visibility;
    }

    public void setDetails(String details) {
        Details = details;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

//        dest.writeArray(new Object[]{
//                ID, Name, No, Model, Manufacturer, FuelType, WheelType, Pic
//        });

        dest.writeString(ID);

        dest.writeString(ClientRef != null ? ClientRef.getPath() : null);

        dest.writeString(Name);
        dest.writeString(No);
        dest.writeString(Model);
        dest.writeString(Manufacturer);
        dest.writeInt(FuelType);
        dest.writeInt(WheelType);
        dest.writeString(Pic);
        dest.writeByte((byte) (Visibility ? 1 : 0));
        dest.writeString(Details);

    }

}
