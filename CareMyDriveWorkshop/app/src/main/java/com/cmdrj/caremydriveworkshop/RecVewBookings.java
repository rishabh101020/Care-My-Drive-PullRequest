package com.cmdrj.caremydriveworkshop;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

public class RecVewBookings implements Parcelable {

    private String ID;

    // -1 -> Not Rated        0 to 5  -> Rated
    private int rating = -1;



    private DocumentReference ClientRef;
    private DocumentReference VehicleRef;
    private DocumentReference WorkShopRef;
    private DocumentReference ServiceRef;
    private DocumentReference ParentServiceRef;


    private String Pic;
    private String JobCard;
    private String Invoice;

    // 0 -> Requested     -1 -> Rejected     -2 -> Rescheduled     -3 -> Recommended     1 -> Accepted     2 -> In-Progress     3 -> Completed
    private int Status = 0;

    // -1 -> Variable      else price
    private double Price;
    private double CommissionablePrice;
    private double Commission;


    // 0 -> Not Required     1-> Required
    private int PickUp = 0;


    private GeoPoint PickUpGeoPoint;
    private String PickUpAddress;
    private String PickUpCity;
    private String PickUpState;
    private String PickUpCountry;
    private String PickUpPincode;



    private Timestamp ServiceDate;

    private String Details;

    private double NextScheduledKM;
    private Timestamp NextScheduledDate;

    public RecVewBookings() {
    }

    public RecVewBookings(String id, DocumentReference clientRef, DocumentReference vehicleRef, DocumentReference workShopRef, DocumentReference serviceRef, DocumentReference parentServiceRef, int status, double price, Timestamp serviceDate, int pickUp, GeoPoint pickUpGeoPoint, String pickUpAddress, String pickUpCity, String pickUpState, String pickUpCountry, String pickUpPincode, String details) {
        ID = id;
        ClientRef = clientRef;
        VehicleRef = vehicleRef;
        WorkShopRef = workShopRef;
        ServiceRef = serviceRef;
        ParentServiceRef = parentServiceRef;
        Status = status;
        Price = price;
        ServiceDate = serviceDate;
        PickUp = pickUp;
        PickUpGeoPoint = pickUpGeoPoint;
        PickUpAddress = pickUpAddress;
        PickUpCity = pickUpCity;
        PickUpState = pickUpState;
        PickUpCountry = pickUpCountry;
        PickUpPincode = pickUpPincode;
        Details = details;
    }

    public RecVewBookings(String id, int rating, DocumentReference clientRef, DocumentReference vehicleRef, DocumentReference workShopRef, DocumentReference parentServiceRef, DocumentReference serviceRef, int status, double price, Timestamp serviceDate, int pickUp, GeoPoint pickUpGeoPoint, String pickUpAddress, String pickUpCity, String pickUpState, String pickUpCountry, String pickUpPincode, String details) {
        ID = id;
        this.rating = rating;
        ClientRef = clientRef;
        VehicleRef = vehicleRef;
        WorkShopRef = workShopRef;
        ServiceRef = serviceRef;
        ParentServiceRef = parentServiceRef;
        Status = status;
        Price = price;
        ServiceDate = serviceDate;
        PickUp = pickUp;
        PickUpGeoPoint = pickUpGeoPoint;
        PickUpAddress = pickUpAddress;
        PickUpCity = pickUpCity;
        PickUpState = pickUpState;
        PickUpCountry = pickUpCountry;
        PickUpPincode = pickUpPincode;
        Details = details;
    }


    protected RecVewBookings(Parcel in) {
        ID = in.readString();
        rating = in.readInt();

        String pathClientRef = in.readString();
        ClientRef = pathClientRef == null ? null : FirebaseFirestore.getInstance().document(pathClientRef);

        String pathVehicleRef = in.readString();
        VehicleRef = pathVehicleRef == null ? null : FirebaseFirestore.getInstance().document(pathVehicleRef);

        String pathWorkShopRef = in.readString();
        WorkShopRef = pathWorkShopRef == null ? null : FirebaseFirestore.getInstance().document(pathWorkShopRef);

        String pathServiceRef = in.readString();
        ServiceRef = pathServiceRef == null ? null : FirebaseFirestore.getInstance().document(pathServiceRef);

        String pathParentServiceRef = in.readString();
        ParentServiceRef = pathParentServiceRef == null ? null : FirebaseFirestore.getInstance().document(pathParentServiceRef);


        Pic = in.readString();
        JobCard = in.readString();
        Invoice = in.readString();
        Status = in.readInt();
        Price = in.readDouble();
        CommissionablePrice = in.readDouble();
        Commission = in.readDouble();

        ServiceDate = in.readParcelable(Timestamp.class.getClassLoader());

        PickUp = in.readInt();

        if(PickUp == 1)
        {
            double latAddressGeoPoint = in.readDouble();
            double longAddressGeoPoint = in.readDouble();
            PickUpGeoPoint = new GeoPoint(latAddressGeoPoint,longAddressGeoPoint);
        }


        PickUpAddress = in.readString();
        PickUpCity = in.readString();
        PickUpState = in.readString();
        PickUpCountry = in.readString();
        PickUpPincode = in.readString();

        Details = in.readString();

        NextScheduledDate = in.readParcelable(Timestamp.class.getClassLoader());
        NextScheduledKM = in.readDouble();

    }

    public static final Creator<RecVewBookings> CREATOR = new Creator<RecVewBookings>() {
        @Override
        public RecVewBookings createFromParcel(Parcel in) {
            return new RecVewBookings(in);
        }

        @Override
        public RecVewBookings[] newArray(int size) {
            return new RecVewBookings[size];
        }
    };

    public String getID() {
        return ID;
    }

    public int getRating() {
        return rating;
    }

    public DocumentReference getClientRef() {
        return ClientRef;
    }

    public DocumentReference getVehicleRef() {
        return VehicleRef;
    }

    public DocumentReference getWorkShopRef() {
        return WorkShopRef;
    }

    public DocumentReference getServiceRef() {
        return ServiceRef;
    }

    public DocumentReference getParentServiceRef() {
        return ParentServiceRef;
    }

    public String getPic() {
        return Pic;
    }

    public String getJobCard() {
        return JobCard;
    }

    public String getInvoice() {
        return Invoice;
    }

    public int getStatus() {
        return Status;
    }

    public double getPrice() {
        return Price;
    }

    public double getCommissionablePrice() {
        return CommissionablePrice;
    }

    public double getCommission() {
        return Commission;
    }

    public Timestamp getServiceDate() {
        return ServiceDate;
    }

    public int getPickUp() {
        return PickUp;
    }

    public GeoPoint getPickUpGeoPoint() {
        return PickUpGeoPoint;
    }

    public String getPickUpAddress() {
        return PickUpAddress;
    }

    public String getPickUpCity() {
        return PickUpCity;
    }

    public String getPickUpState() {
        return PickUpState;
    }

    public String getPickUpCountry() {
        return PickUpCountry;
    }

    public String getPickUpPincode() {
        return PickUpPincode;
    }

    public String getDetails() {
        return Details;
    }

    public double getNextScheduledKM() {
        return NextScheduledKM;
    }

    public Timestamp getNextScheduledDate() {
        return NextScheduledDate;
    }


    public void setID(String ID) {
        this.ID = ID;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setClientRef(DocumentReference clientRef) {
        ClientRef = clientRef;
    }

    public void setVehicleRef(DocumentReference vehicleRef) {
        VehicleRef = vehicleRef;
    }

    public void setWorkShopRef(DocumentReference workShopRef) {
        WorkShopRef = workShopRef;
    }

    public void setServiceRef(DocumentReference serviceRef) {
        ServiceRef = serviceRef;
    }

    public void setParentServiceRef(DocumentReference parentServiceRef) {
        ParentServiceRef = parentServiceRef;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public void setJobCard(String jobCard) {
        this.JobCard = jobCard;
    }

    public void setInvoice(String invoice) {
        this.Invoice = invoice;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setCommissionablePrice(double commissionablePrice) {
        CommissionablePrice = commissionablePrice;
    }

    public void setCommission(double commission) {
        Commission = commission;
    }

    public void setServiceDate(Timestamp serviceDate) {
        ServiceDate = serviceDate;
    }

    public void setPickUp(int pickUp) {
        PickUp = pickUp;
    }

    public void setPickUpGeoPoint(GeoPoint pickUpGeoPoint) {
        PickUpGeoPoint = pickUpGeoPoint;
    }

    public void setPickUpAddress(String pickUpAddress) {
        PickUpAddress = pickUpAddress;
    }

    public void setPickUpCity(String pickUpCity) {
        PickUpCity = pickUpCity;
    }

    public void setPickUpState(String pickUpState) {
        PickUpState = pickUpState;
    }

    public void setPickUpCountry(String pickUpCountry) {
        PickUpCountry = pickUpCountry;
    }

    public void setPickUpPincode(String pickUpPincode) {
        PickUpPincode = pickUpPincode;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public void setNextScheduledKM(double nextScheduledKM) {
        NextScheduledKM = nextScheduledKM;
    }

    public void setNextScheduledDate(Timestamp nextScheduledDate) {
        NextScheduledDate = nextScheduledDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeInt(rating);

        dest.writeString(ClientRef != null ? ClientRef.getPath() : null);

        dest.writeString(VehicleRef != null ? VehicleRef.getPath() : null);

        dest.writeString(WorkShopRef != null ? WorkShopRef.getPath() : null);

        dest.writeString(ServiceRef != null ? ServiceRef.getPath() : null);

        dest.writeString(ParentServiceRef != null ? ParentServiceRef.getPath() : null);

        dest.writeString(Pic);
        dest.writeString(JobCard);
        dest.writeString(Invoice);
        dest.writeInt(Status);
        dest.writeDouble(Price);
        dest.writeDouble(CommissionablePrice);
        dest.writeDouble(Commission);
        dest.writeParcelable(ServiceDate, flags);
        dest.writeInt(PickUp);

        if(PickUp == 1)
        {
            dest.writeDouble(PickUpGeoPoint.getLatitude());
            dest.writeDouble(PickUpGeoPoint.getLongitude());
        }


        dest.writeString(PickUpAddress);
        dest.writeString(PickUpCity);
        dest.writeString(PickUpState);
        dest.writeString(PickUpCountry);
        dest.writeString(PickUpPincode);

        dest.writeString(Details);

        dest.writeParcelable(NextScheduledDate, flags);
        dest.writeDouble(NextScheduledKM);

    }
}
