package com.cmdrj.caremydriveworkshop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RecVewService implements Parcelable {

    private String ID;
    private DocumentReference ParentRef;
    private DocumentReference WorkShopRef;
    private String Name;
    private String Details;

    // -1 -> Variable      else price
    private double Price;

    // 0 -> Not Available   , 1-> Available
    private int PickUp = 0;

    // 0 -> Variable   else -> defined
    private String EstTime;

    // true -> visible      false -> not visible
    private boolean Visibility;

    // true -> disabled      false -> not disabled
    private boolean Disabled;


    // 2-> Bikes     4 -> Cars     6 -> Heavy     3 -> others
    private int categoryLen;
    ArrayList<Integer> category = new ArrayList<Integer>();

//    private int serviceProvidersLen;
//    ArrayList<String> serviceProviders = new ArrayList<String>();

    private String Pic;

    public RecVewService() {
    }

    public RecVewService(String id, String name, String details) {
        ID = id;
        Name = name;
        Details = details;
        Visibility = true;
        Disabled = false;
    }

    public RecVewService(String id, DocumentReference parentRef,DocumentReference workShopRef, String name, String details, int pickUp, String estTime, double price, ArrayList<Integer> category) {
        ID = id;
        ParentRef = parentRef;
        WorkShopRef = workShopRef;
        Name = name;
        Details = details;
        Price = price;
        Visibility = true;
        Disabled = false;
        PickUp = pickUp;
        EstTime = estTime;
        this.categoryLen = category.size();
        this.category = category;

    }

//    public RecVewService(String id, String name, String details, int pic, int pickUp, String estTime, double price, int serviceProvidersLen, ArrayList<String> serviceProviders) {
//        ID = id;
//        Name = name;
//        Details = details;
//        Price = price;
//        this.serviceProvidersLen = serviceProvidersLen;
//        this.serviceProviders = serviceProviders;
//        Pic = pic;
//        PickUp = pickUp;
//        EstTime = estTime;
//
//    }


    protected RecVewService(Parcel in) {
        ID = in.readString();

        String pathParentRef = in.readString();
        ParentRef = pathParentRef == null ? null : FirebaseFirestore.getInstance().document(pathParentRef);

        String pathWorkShopRef = in.readString();
        WorkShopRef = pathWorkShopRef == null ? null : FirebaseFirestore.getInstance().document(pathWorkShopRef);

        Name = in.readString();
        Details = in.readString();
        Price = in.readDouble();
        PickUp = in.readInt();
        EstTime = in.readString();
        Visibility = in.readByte() != 0;
        Disabled = in.readByte() != 0;
        categoryLen = in.readInt();
        in.readList(category, Integer.class.getClassLoader());
//        serviceProvidersLen = in.readInt();
//        serviceProviders = in.createStringArrayList();
        Pic = in.readString();
    }

    public static final Creator<RecVewService> CREATOR = new Creator<RecVewService>() {
        @Override
        public RecVewService createFromParcel(Parcel in) {
            return new RecVewService(in);
        }

        @Override
        public RecVewService[] newArray(int size) {
            return new RecVewService[size];
        }
    };


    public String getID() {
        return ID;
    }

    public DocumentReference getParentRef() {
        return ParentRef;
    }

    public DocumentReference getWorkShopRef() {
        return WorkShopRef;
    }

    public String getName() {
        return Name;
    }

    public String getDetails() {
        return Details;
    }

    public String getPic() {
        return Pic;
    }

    public double getPrice() {
        return Price;
    }

    public int getPickUp() {
        return PickUp;
    }

    public String getEstTime() {
        return EstTime;
    }

    public boolean getVisibility() {
        return Visibility;
    }

    public boolean getDisabled() {
        return Disabled;
    }

    public int getCategoryLen() {
        return categoryLen;
    }

    public ArrayList<Integer> getCategory() {
        return category;
    }

//    public int getServiceProvidersLen() {
//        return serviceProvidersLen;
//    }
//
//    public ArrayList<String> getServiceProviders() {
//        return serviceProviders;
//    }



    public void setID(String ID) {
        this.ID = ID;
    }

    public void setParentRef(DocumentReference parentRef) {
        ParentRef = parentRef;
    }

    public void setWorkShopRef(DocumentReference workShopRef) {
        WorkShopRef = workShopRef;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setPickUp(int pickUp) {
        PickUp = pickUp;
    }

    public void setEstTime(String estTime) {
        EstTime = estTime;
    }

    public void setVisibility(boolean visibility) {
        Visibility = visibility;
    }

    public void setDisabled(boolean disabled) {
        Disabled = disabled;
    }

    public void setCategoryLen() {
        this.categoryLen = category.size();
    }

    public void setCategory(ArrayList<Integer> category) {
        this.category = category;
    }

//    public void setServiceProvidersLen() {
//        this.serviceProvidersLen = serviceProviders.size();
//    }
//
//    public void setServiceProviders(ArrayList<String> serviceProviders) {
//        this.serviceProviders = serviceProviders;
//    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);


        dest.writeString(ParentRef != null ? ParentRef.getPath() : null);


        dest.writeString(WorkShopRef != null ? WorkShopRef.getPath() : null);



        dest.writeString(Name);
        dest.writeString(Details);
        dest.writeDouble(Price);
        dest.writeInt(PickUp);
        dest.writeString(EstTime);
        dest.writeByte((byte) (Visibility ? 1 : 0));
        dest.writeByte((byte) (Disabled ? 1 : 0));
        dest.writeInt(categoryLen);
        dest.writeList(category);
//        dest.writeInt(serviceProvidersLen);
//        dest.writeStringList(serviceProviders);
        dest.writeString(Pic);
    }
}
