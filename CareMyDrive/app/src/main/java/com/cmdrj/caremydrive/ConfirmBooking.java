package com.cmdrj.caremydrive;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfirmBooking extends AppCompatActivity {

    int LOCATION_REQUEST_CODE = 10002;
    int GPS_ON_REQUEST_CODE = 10003;




    TextView ConfirmBooking_ServiceName, ConfirmBooking_ServicePrice;
    TextInputEditText ConfirmBooking_Workshop_TIET, ConfirmBooking_No_TIET, ConfirmBooking_Address_TIET, ConfirmBooking_CarName_TIET, ConfirmBooking_Location_TIET, ConfirmBooking_Details_TIET;
    MaterialAutoCompleteTextView ConfirmBooking_Date_MACT, ConfirmBooking_PickUp_MACT;
    ImageView ConfirmBooking_IV;
    RelativeLayout ConfirmBooking_PickUp_RL, ConfirmBooking_helperAddress_RL, ConfirmBooking_Location_RL;
    LinearLayout ConfirmBooking_currentLocation_LL, ConfirmBooking_addLocation_LL;
    Button ConfirmBooking_VewDet, ConfirmBooking_Contact;




    private GeoPoint AddressGeoPoint = null;
    private String Address = null;
    private String City = null;
    private String State = null;
    private String Country = null;
    private String Pincode = null;

    private boolean ConfirmBooking_currentLocation = false, ConfirmBooking_addLocation = false;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Location currentLocation = null, lastLocation = null, permanentLocation = null;

    private DatePickerDialog.OnDateSetListener setListener;

    String selectedPickUpType;
    int selectedPickUp = -1;
    String[] pickUpArray = {"Required", "Not Required"};

    String startDate;





























    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {

            super.onLocationResult(locationResult);

            stopLocationUpdates();

            if (locationResult != null && locationResult.getLocations().size() >0){

                int index = locationResult.getLocations().size() - 1;
                currentLocation = locationResult.getLocations().get(index);

                double latitude = locationResult.getLocations().get(index).getLatitude();
                double longitude = locationResult.getLocations().get(index).getLongitude();

                Geocoder geocoder = new Geocoder(ConfirmBooking.this);
                List<android.location.Address> addressList = null;

                try {
                    addressList = geocoder.getFromLocation(latitude,longitude,1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addressList == null)
                {
                    Snackbar.make(ConfirmBooking_Contact, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

                }
                else if (addressList.size() <= 0) {
                    Snackbar.make(ConfirmBooking_Contact, "No Locations Found. Please use \"Add Manually\" option.", Snackbar.LENGTH_SHORT).show();

                } else {

                    Address address = addressList.get(0);

                    AddressGeoPoint = new GeoPoint(latitude,longitude);
                    Address = address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality();
                    City = address.getSubAdminArea();
                    State = address.getAdminArea();
                    Country = address.getCountryName();
                    Pincode = address.getPostalCode();

                    ConfirmBooking_Location_TIET.setText(address.getAddressLine(0));


//                    Log.d("TAG", "RishabhJain + " + address.toString());
//                    Log.d("TAG", "RishabhJain + " + Address);

                }



//                AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
            }

            /* TODO : By YoursTruly*/
////            super.onLocationResult(locationResult);

//            if (locationResult == null) {
//                return;
//            }
//
//            for (Location location : locationResult.getLocations()) {
//                // TODO : do something
//
//                Log.d("TAG", "RishabhJain +  " + location.toString());
//
//                AddressText.setText("Latitude: "+ location.getLatitude() + "\n" + "Longitude: "+ location.getLongitude());
//                // TODO : over
//            }

            /* TODO : over: By YoursTruly */
        }
    };

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result != null && result.getResultCode() == RESULT_OK)
                    {
                        Intent intent = result.getData();
                        if(intent != null)
                        {

                            boolean itemNewLocation = intent.getBooleanExtra("itemNewLocation",true);
                            double itemLatitude = intent.getDoubleExtra("itemLatitude",-1);
                            double itemLongitude = intent.getDoubleExtra("itemLongitude",-1);
                            Address = intent.getStringExtra("itemAddress");
                            City = intent.getStringExtra("itemCity");
                            State = intent.getStringExtra("itemState");
                            Country = intent.getStringExtra("itemCountry");
                            Pincode = intent.getStringExtra("itemPincode");



                            if(itemNewLocation)
                            {
                                lastLocation = new Location("");  //provider name is unnecessary
                                lastLocation.setLatitude(itemLatitude);
                                lastLocation.setLongitude(itemLongitude);
                            }



                            AddressGeoPoint = new GeoPoint(itemLatitude,itemLongitude);




                            ConfirmBooking_Location_TIET.setText(Address + ", " + City + ", " + State + ", " + Pincode + ", " + Country);


//                            Toast.makeText(ConfirmBooking.this,Address + "\nLat:  " + itemLatitude + "\nLong: "+ itemLongitude,Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            }
    );
























    RecVewWorkshop itemWorkshop;
    RecVewClient itemClient;
    RecVewService itemService;
    RecVewCar itemCar;




















    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbarConfirmBooking);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();


        ConfirmBooking_ServiceName = findViewById(R.id.ConfirmBooking_ServiceName);
        ConfirmBooking_ServicePrice = findViewById(R.id.ConfirmBooking_ServicePrice);

        ConfirmBooking_Workshop_TIET= findViewById(R.id.ConfirmBooking_Workshop_TIET);
        ConfirmBooking_No_TIET= findViewById(R.id.ConfirmBooking_No_TIET);
        ConfirmBooking_Address_TIET= findViewById(R.id.ConfirmBooking_Address_TIET);
        ConfirmBooking_CarName_TIET= findViewById(R.id.ConfirmBooking_CarName_TIET);
        ConfirmBooking_Location_TIET = findViewById(R.id.ConfirmBooking_Location_TIET);
        ConfirmBooking_Details_TIET = findViewById(R.id.ConfirmBooking_Details_TIET);

        ConfirmBooking_Date_MACT= findViewById(R.id.ConfirmBooking_Date_MACT);
        ConfirmBooking_PickUp_MACT= findViewById(R.id.ConfirmBooking_PickUp_MACT);

        ConfirmBooking_PickUp_RL= findViewById(R.id.ConfirmBooking_PickUp_RL);
        ConfirmBooking_helperAddress_RL= findViewById(R.id.ConfirmBooking_helperAddress_RL);
        ConfirmBooking_Location_RL= findViewById(R.id.ConfirmBooking_Location_RL);

        ConfirmBooking_currentLocation_LL= findViewById(R.id.ConfirmBooking_currentLocation_LL);
        ConfirmBooking_addLocation_LL= findViewById(R.id.ConfirmBooking_addLocation_LL);

        ConfirmBooking_VewDet= findViewById(R.id.ConfirmBooking_VewDet);
        ConfirmBooking_Contact= findViewById(R.id.ConfirmBooking_Contact);

        ConfirmBooking_IV = findViewById(R.id.ConfirmBooking_IV);





        Intent intent = getIntent();
        itemWorkshop = intent.getExtras().getParcelable("itemWorkshop");
        itemService = intent.getExtras().getParcelable("itemService");
        itemCar = intent.getExtras().getParcelable("itemCar");



        // TODO : following lines are wrong , permanentLocation will be the location of client whose car has to be serviced hence retrive clients permanentAddress by ClientsRef field in car object   -> DONE but NOT CHECKED

        fStore.collection("Clients").document(fUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

                    Snackbar.make(ConfirmBooking_Contact, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemClient = value.toObject(RecVewClient.class);

                    permanentLocation = new Location("");
                    permanentLocation.setLatitude(itemClient.getAddressGeoPoint().getLatitude());
                    permanentLocation.setLongitude(itemClient.getAddressGeoPoint().getLongitude());


                } else {
//                    Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        // TODO : over



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);







        ConfirmBooking_currentLocation_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmBooking_addLocation = false;
                ConfirmBooking_currentLocation = true;

                if (ContextCompat.checkSelfPermission(ConfirmBooking.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    checkSettingsAndStartUpdates();

                } else {
                    askLocationPermission();
                }

            }
        });

        ConfirmBooking_addLocation_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ConfirmBooking_addLocation = true;
                ConfirmBooking_currentLocation = false;


                if (ContextCompat.checkSelfPermission(ConfirmBooking.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    checkSettingsAndStartUpdates();

                } else {
                    askLocationPermission();
                }

            }
        });


        ConfirmBooking_ServiceName.setText(itemService.getName());



        if(itemService.getPrice() == -1)
            ConfirmBooking_ServicePrice.setText("Variable");

        else
            ConfirmBooking_ServicePrice.setText(Double.toString(itemService.getPrice()));



        ConfirmBooking_Workshop_TIET.setText(itemWorkshop.getName());
        ConfirmBooking_No_TIET.setText(itemWorkshop.getMbNo());
        ConfirmBooking_Address_TIET.setText(itemWorkshop.getAddress() + ", " + itemWorkshop.getCity() + ", " + itemWorkshop.getState());

        ConfirmBooking_CarName_TIET.setText(itemCar.getName());



        Calendar calendar = Calendar.getInstance();
        final int[] yy = {calendar.get(Calendar.YEAR)};
        final int[] mm = {calendar.get(Calendar.MONTH)};
        final int[] dd = {calendar.get(Calendar.DAY_OF_MONTH)};

        startDate = dd[0] +" /"+ (mm[0] +1) +" /"+ yy[0];
        ConfirmBooking_Date_MACT.setText(startDate);

        ConfirmBooking_Date_MACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ConfirmBooking.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener, yy[0], mm[0], dd[0]);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == DialogInterface.BUTTON_NEGATIVE) {
//                            dialog.dismiss();
//                            onBackPressed();
//                        }
//                    }
//                });
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.setCancelable(false);
                datePickerDialog.show();

            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                month =  month +1;
                startDate = dayOfMonth+" /"+month+" /"+year;
                yy[0] = year;
                mm[0] = month-1;
                dd[0] = dayOfMonth;
                ConfirmBooking_Date_MACT.setText(startDate);

            }
        };


        if(itemService.getPickUp() == 0)
        {
            ConfirmBooking_PickUp_RL.setVisibility(View.GONE);
        }
        else if(itemService.getPickUp() == 1)
        {
            ConfirmBooking_PickUp_RL.setVisibility(View.VISIBLE);
        }

        ConfirmBooking_PickUp_MACT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmBooking.this);

                builder.setTitle("Select PickUp Facility Type");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(pickUpArray, selectedPickUp, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedPickUpType = pickUpArray[which];

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(selectedPickUpType != null)
                        {
                            if (selectedPickUpType.equals("Required"))
                                selectedPickUp = 0;
                            else if (selectedPickUpType.equals("Not Required"))
                                selectedPickUp = 1;
                        }
                        else
                        {
                            Snackbar.make(v, "Please Select Some PickUp Facility Type.", Snackbar.LENGTH_SHORT).show();
                        }



                        ConfirmBooking_PickUp_MACT.setText(selectedPickUpType);

                        if(selectedPickUp == 0)
                        {
                            ConfirmBooking_helperAddress_RL.setVisibility(View.VISIBLE);
                            ConfirmBooking_Location_RL.setVisibility(View.VISIBLE);
                        }
                        else if(selectedPickUp == 1)
                        {
                            ConfirmBooking_helperAddress_RL.setVisibility(View.GONE);
                            ConfirmBooking_Location_RL.setVisibility(View.GONE);
                        }

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });


        ConfirmBooking_Details_TIET.setText(itemService.getDetails());

        ConfirmBooking_VewDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ConfirmBooking.this)
                        .setTitle("Choose Contact Option")
                        .setMessage("Choose an application to contact Workshop")
                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemWorkshop.getEmail(), null));
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                }

                            }
                        })
                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemWorkshop.getMbNo(), null));
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(phoneIntent);
                                }

                            }
                        })
                        .create().show();
            }
        });

        ConfirmBooking_Contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConfirmBooking_Contact.setEnabled(false);

                // TODO : Complete if else block NOW
//                if(itemService.getPrice() <= 0 )
//                {
//                      Snackbar.make(v,"Please First Agree to a service price with workshop", Snackbar.LENGTH_SHORT).show();
//                }
//                else
//                {
//
//                }

                //TODO :over

                // TODO : change price according to above if else while creating newBooking object
                // TODO : over


                if(itemService.getPickUp() == 0)
                {
                    if (!(ConfirmBooking_Date_MACT.getText().toString().trim().equals("")))
                    {
                        Date serviceDate = null;
                        Calendar cal = GregorianCalendar.getInstance();
                        cal.set(yy[0], mm[0], dd[0]);
                        serviceDate = cal.getTime();


                        DocumentReference dReference = fStore.collection("Bookings").document();
                        String ID = dReference.getId();

                        DocumentReference clientReference = fStore.collection("Clients").document(fUser.getUid());
                        DocumentReference carReference = fStore.collection("Vehicles").document(itemCar.getID());
                        DocumentReference workshopReference = fStore.collection("Workshops").document(itemWorkshop.getID());
                        DocumentReference serviceReference = fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID());
                        DocumentReference ParentServiceReference = itemService.getParentRef();


                        RecVewBookings newBooking = new RecVewBookings(ID, clientReference, carReference, workshopReference, serviceReference, ParentServiceReference, 0, itemService.getPrice(), new Timestamp(serviceDate), 0, null, null, null, null, null, null,itemService.getDetails());

                        dReference.set(newBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Map<String, Object> map = new HashMap<>();
                                map.put("id", newBooking.getID());
                                map.put("ref", dReference);


                                clientReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        carReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                workshopReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {


                                                        serviceReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                ParentServiceReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {


                                                                        // TODO : Check / Verify this
                                                                        Intent intent = new Intent(ConfirmBooking.this, DetailsBooking.class);
                                                                        //                    intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                                                        intent.putExtra("itemBooking", newBooking);
                                                                        //                    startActivity(intent);
                                                                        //                finishAffinity();
                                                                        //                    Intent parentIntent = MainActivity.getIntent(Mai.this);
                                                                        Intent parentIntent = new Intent( ConfirmBooking.this, MainActivity.class);
                                                                        TaskStackBuilder.create(ConfirmBooking.this).addNextIntent(parentIntent).addNextIntent(intent).startActivities();

                                                                        // TODO : over


                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });

                                    }
                                });

                            }
                        });
                    }
                    else
                    {
                        ConfirmBooking_Contact.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                    }

                }
                else
                {
                    if (!(ConfirmBooking_PickUp_MACT.getText().toString().trim().equals("")) && !(ConfirmBooking_Date_MACT.getText().toString().trim().equals(""))) {

                        if(ConfirmBooking_PickUp_MACT.getText().toString().trim().equals("Required") && AddressGeoPoint != null)
                        {
                            if(Address == null || City == null || State == null || Country == null || Pincode == null)
                            {
                                ConfirmBooking_Contact.setEnabled(true);
                                Snackbar.make(findViewById(android.R.id.content), "Please Select \"Add Manually\" option and long press on maps to select nearby area having complete address", Snackbar.LENGTH_SHORT).show();
                            }
                            else
                            {

                                Date serviceDate = null;
                                Calendar cal = GregorianCalendar.getInstance();
                                cal.set(yy[0], mm[0], dd[0]);
                                serviceDate = cal.getTime();


                                DocumentReference dReference = fStore.collection("Bookings").document();
                                String ID = dReference.getId();

                                DocumentReference clientReference = fStore.collection("Clients").document(fUser.getUid());
                                DocumentReference carReference = fStore.collection("Vehicles").document(itemCar.getID());
                                DocumentReference workshopReference = fStore.collection("Workshops").document(itemWorkshop.getID());
                                DocumentReference serviceReference = fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID());
                                DocumentReference ParentServiceReference = itemService.getParentRef();


                                RecVewBookings newBooking = new RecVewBookings(ID, clientReference, carReference, workshopReference, serviceReference, ParentServiceReference, 0, itemService.getPrice(), new Timestamp(serviceDate), 1, AddressGeoPoint, Address, City, State, Country, Pincode,itemService.getDetails());

                                dReference.set(newBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Map<String, Object> map = new HashMap<>();
                                        map.put("id", newBooking.getID());
                                        map.put("ref", dReference);


                                        clientReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                carReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        workshopReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {


                                                                serviceReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        ParentServiceReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {


                                                                                // TODO : Check / Verify this
                                                                                Intent intent = new Intent(ConfirmBooking.this, DetailsBooking.class);
                                                                                //                    intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                                                                intent.putExtra("itemBooking", newBooking);
                                                                                //                    startActivity(intent);
                                                                                //                finishAffinity();
                                                                                //                    Intent parentIntent = MainActivity.getIntent(Mai.this);
                                                                                Intent parentIntent = new Intent( ConfirmBooking.this, MainActivity.class);
                                                                                TaskStackBuilder.create(ConfirmBooking.this).addNextIntent(parentIntent).addNextIntent(intent).startActivities();

                                                                                // TODO : over


                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });

                                                    }
                                                });

                                            }
                                        });

                                    }
                                });
                            }
                        }
                        else if(ConfirmBooking_PickUp_MACT.getText().toString().trim().equals("Required") && AddressGeoPoint == null)
                        {

                            ConfirmBooking_Contact.setEnabled(true);
                            Snackbar.make(findViewById(android.R.id.content), "Please Enter Pick-Up Location by choosing \"Detect Location\" or \"Add Manually\" options.", Snackbar.LENGTH_SHORT).show();

                        }
                        else if(ConfirmBooking_PickUp_MACT.getText().toString().trim().equals("Not Required"))
                        {

                            Date serviceDate = null;
                            Calendar cal = GregorianCalendar.getInstance();
                            cal.set(yy[0], mm[0], dd[0]);
                            serviceDate = cal.getTime();


                            DocumentReference dReference = fStore.collection("Bookings").document();
                            String ID = dReference.getId();

                            DocumentReference clientReference = fStore.collection("Clients").document(fUser.getUid());
                            DocumentReference carReference = fStore.collection("Vehicles").document(itemCar.getID());
                            DocumentReference workshopReference = fStore.collection("Workshops").document(itemWorkshop.getID());
                            DocumentReference serviceReference = fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID());
                            DocumentReference ParentServiceReference = itemService.getParentRef();


                            RecVewBookings newBooking = new RecVewBookings(ID, clientReference, carReference, workshopReference, serviceReference, ParentServiceReference, 0, itemService.getPrice(), new Timestamp(serviceDate), 0, null, null, null, null, null, null,itemService.getDetails());

                            dReference.set(newBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", newBooking.getID());
                                    map.put("ref", dReference);


                                    clientReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            carReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    workshopReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {


                                                            serviceReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    ParentServiceReference.collection("requestedBookings").document(newBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {


                                                                            // TODO : Check / Verify this
                                                                            Intent intent = new Intent(ConfirmBooking.this, DetailsBooking.class);
                                                                            //                    intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                                                            intent.putExtra("itemBooking", newBooking);
                                                                            //                    startActivity(intent);
                                                                            //                finishAffinity();
                                                                            //                    Intent parentIntent = MainActivity.getIntent(Mai.this);
                                                                            Intent parentIntent = new Intent( ConfirmBooking.this, MainActivity.class);
                                                                            TaskStackBuilder.create(ConfirmBooking.this).addNextIntent(parentIntent).addNextIntent(intent).startActivities();

                                                                            // TODO : over


                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    });

                                }
                            });
                        }
                        else
                        {

                            ConfirmBooking_Contact.setEnabled(true);
                            Snackbar.make(findViewById(android.R.id.content), "Order Not Placed. Try Again Later", Snackbar.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        ConfirmBooking_Contact.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                    }
                }

            }
        });

    }


    private void checkSettingsAndStartUpdates() {

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {


                if(ConfirmBooking_addLocation && !ConfirmBooking_currentLocation)
                {
                    openChooseLocation();
                }
                else if(!ConfirmBooking_addLocation && ConfirmBooking_currentLocation)
                {
                    getCurrentLocation();

                }
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(ConfirmBooking.this, GPS_ON_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                } else {
                    // TODO : do something
                }

            }
        });

    }

    private void openChooseLocation() {

        Intent intent = new Intent(ConfirmBooking.this, ChooseLocation.class);
        intent.putExtra("parentActivity",1);
        intent.putExtra("itemPermanentLocation",permanentLocation);



        if(lastLocation != null)
        {
            intent.putExtra("noOfParameters",2);
            intent.putExtra("itemLastLocation",lastLocation);
        }
        else
        {
            intent.putExtra("noOfParameters",1);
        }

        activityResultLauncher.launch(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void askLocationPermission() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // TODO : create Alert Dialog box and when click on "OK" ask for permission

                Snackbar.make(findViewById(android.R.id.content),"SHow alert Dialog Box", Snackbar.LENGTH_SHORT).show();
                //TODO : over

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
            else
            {
                Snackbar.make(findViewById(android.R.id.content),"SHow alert Dialog Box", Snackbar.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);

            }

        }
        else
        {
            // TODO : enter something here too

            // TODO : over
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // TODO : Snackbar Permission granted

                checkSettingsAndStartUpdates();
            }
            else
            {
                //TODO : Snackbar Permission not granted + something

            }

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}