package com.cmdrj.caremydriveadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class DetailsBooking extends AppCompatActivity {

    TextView DetBooking_CarModel, DetBooking_ServicePrice, DetBooking_ClientName_TV_Det, DetBooking_ClientMbNo_TV_Det, DetBooking_ClientAddress_TV_Det, DetBooking_WorkshopName_TV_Det, DetBooking_WorkshopMbNo_TV_Det, DetBooking_WorkshopAddress_TV_Det, DetBooking_CarNo_TV_Det, DetBooking_Fuel_Type_TV_Det, DetBooking_ServiceName_TV_Det, DetBooking_ServiceDate_TV_Det, DetBooking_Rating_TV_Det, DetBooking_PickUp_TV_Det, DetBooking_Details_TV_Det, DetBooking_Commission_TV_Det, DetBooking_CommissionablePrice_TV_Det, DetBooking_NextDate_TV_Det, DetBooking_NextKM_TV_Det;
    TextView DetBooking_ServiceNotice, DetBooking_WorkshopName_TV, DetBooking_WorkshopMbNo_TV, DetBooking_WorkshopAddress_TV, DetBooking_ServiceName_TV, DetBooking_Details_TV;
    ImageView DetBooking_IV;
    Button DetBooking_Accept, DetBooking_Reject;
    LinearLayout DetBooking_Rating_LL, DetBooking_Commission_LL,DetBooking_CommissionablePrice_LL, DetBooking_NextDate_LL, DetBooking_NextKM_LL;
    View DetBooking_Rating_View, DetBooking_Commission_V, DetBooking_CommissionablePrice_V, DetBooking_NextDate_V, DetBooking_NextKM_V;

    RecVewBookings itemBooking;
    RecVewCar itemCar;
    RecVewService itemService, itemParentService;
    RecVewClient itemClient;
    RecVewWorkshop itemWorkshop;

    FloatingActionButton DetBooking_fab;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    // TODO : Remove when firebase

    ArrayList<RecVewWorkshop> newWorkshops = new ArrayList<>();;
    ArrayList<RecVewCar> newCars = new ArrayList<>();;

    // TODO : over

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_booking);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarDetBooking);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();


        DetBooking_CarModel = findViewById(R.id.DetBooking_CarModel);
        DetBooking_ServicePrice = findViewById(R.id.DetBooking_ServicePrice);
        DetBooking_ClientName_TV_Det= findViewById(R.id.DetBooking_ClientName_TV_Det);
        DetBooking_ClientMbNo_TV_Det= findViewById(R.id.DetBooking_ClientMbNo_TV_Det);
        DetBooking_ClientAddress_TV_Det= findViewById(R.id.DetBooking_ClientAddress_TV_Det);
        DetBooking_WorkshopName_TV_Det= findViewById(R.id.DetBooking_WorkshopName_TV_Det);
        DetBooking_WorkshopMbNo_TV_Det= findViewById(R.id.DetBooking_WorkshopMbNo_TV_Det);
        DetBooking_WorkshopAddress_TV_Det= findViewById(R.id.DetBooking_WorkshopAddress_TV_Det);
        DetBooking_CarNo_TV_Det = findViewById(R.id.DetBooking_CarNo_TV_Det);
        DetBooking_Fuel_Type_TV_Det = findViewById(R.id.DetBooking_Fuel_Type_TV_Det);
        DetBooking_ServiceName_TV_Det = findViewById(R.id.DetBooking_ServiceName_TV_Det);
        DetBooking_ServiceDate_TV_Det = findViewById(R.id.DetBooking_ServiceDate_TV_Det);
        DetBooking_Rating_TV_Det = findViewById(R.id.DetBooking_Rating_TV_Det);
        DetBooking_PickUp_TV_Det = findViewById(R.id.DetBooking_PickUp_TV_Det);
        DetBooking_Details_TV_Det = findViewById(R.id.DetBooking_Details_TV_Det);
        DetBooking_Commission_TV_Det = findViewById(R.id.DetBooking_Commission_TV_Det);
        DetBooking_CommissionablePrice_TV_Det = findViewById(R.id.DetBooking_CommissionablePrice_TV_Det);
        DetBooking_NextDate_TV_Det = findViewById(R.id.DetBooking_NextDate_TV_Det);
        DetBooking_NextKM_TV_Det = findViewById(R.id.DetBooking_NextKM_TV_Det);


        DetBooking_IV = findViewById(R.id.DetBooking_IV);

        DetBooking_Accept = findViewById(R.id.DetBooking_Accept);
        DetBooking_Reject = findViewById(R.id.DetBooking_Reject);
        DetBooking_fab = findViewById(R.id.DetBooking_fab);

        DetBooking_Rating_LL = findViewById(R.id.DetBooking_Rating_LL);
        DetBooking_Commission_LL = findViewById(R.id.DetBooking_Commission_LL);
        DetBooking_CommissionablePrice_LL = findViewById(R.id.DetBooking_CommissionablePrice_LL);
        DetBooking_NextDate_LL = findViewById(R.id.DetBooking_NextDate_LL);
        DetBooking_NextKM_LL = findViewById(R.id.DetBooking_NextKM_LL);


        DetBooking_Rating_View = findViewById(R.id.DetBooking_Rating_View);
        DetBooking_Commission_V = findViewById(R.id.DetBooking_Commission_V);
        DetBooking_CommissionablePrice_V = findViewById(R.id.DetBooking_CommissionablePrice_V);
        DetBooking_NextDate_V = findViewById(R.id.DetBooking_NextDate_V);
        DetBooking_NextKM_V = findViewById(R.id.DetBooking_NextKM_V);


        DetBooking_ServiceNotice = findViewById(R.id.DetBooking_ServiceNotice);
        DetBooking_WorkshopName_TV = findViewById(R.id.DetBooking_WorkshopName_TV);
        DetBooking_WorkshopMbNo_TV = findViewById(R.id.DetBooking_WorkshopMbNo_TV);
        DetBooking_WorkshopAddress_TV = findViewById(R.id.DetBooking_WorkshopAddress_TV);
        DetBooking_ServiceName_TV = findViewById(R.id.DetBooking_ServiceName_TV);
        DetBooking_Details_TV = findViewById(R.id.DetBooking_Details_TV);


        DetBooking_CarModel.setSelected(true);

        Intent intent = getIntent();
        itemBooking = intent.getExtras().getParcelable("itemBooking");









        DocumentReference dReference = fStore.collection("Bookings").document(itemBooking.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetBooking_CarModel, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemBooking = value.toObject(RecVewBookings.class);

                    if(itemBooking.getStatus() == -1)
                    {
                        DetBooking_ServiceNotice.setText("Note: This Booking was rejected by the Workshop. Please assign a new workshop / service to this client.");
                        DetBooking_ServiceNotice.setVisibility(View.VISIBLE);
                        DetBooking_WorkshopName_TV.setText("Rejecting Workshop Name");
                        DetBooking_WorkshopMbNo_TV.setText("Rejecting Workshop Mobile Number");
                        DetBooking_WorkshopAddress_TV.setText("Rejecting Workshop Address");
                        DetBooking_ServiceName_TV.setText("Rejected Service Name");
                        DetBooking_Details_TV.setText("Rejected Service Details");

                        DetBooking_Accept.setText("Assign New\nWorkshop");
                        DetBooking_Reject.setText("Contact Old\nWorkshop");

                    }
                    else
                    {
                        if(itemBooking.getStatus() == -2)
                        {
                            DetBooking_ServiceNotice.setText("Note: This Booking was rescheduled by the Workshop.");
                            DetBooking_ServiceNotice.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            DetBooking_ServiceNotice.setVisibility(View.GONE);
                        }
                        DetBooking_WorkshopName_TV.setText("Workshop Name");
                        DetBooking_WorkshopMbNo_TV.setText("Workshop Mobile Number");
                        DetBooking_WorkshopAddress_TV.setText("Workshop Address");
                        DetBooking_ServiceName_TV.setText("Service Name");
                        DetBooking_Details_TV.setText("Details");

                        DetBooking_Accept.setText("Contact\nClient");
                        DetBooking_Reject.setText("Contact\nWorkshop");
                    }

                    itemBooking.getVehicleRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            itemCar = documentSnapshot.toObject(RecVewCar.class);

                            DetBooking_CarModel.setText(itemCar.getModel() + ", " + itemCar.getManufacturer()) ;

                            DetBooking_CarNo_TV_Det.setText(itemCar.getNo());

                            if(itemCar.getFuelType() == 0)
                                DetBooking_Fuel_Type_TV_Det.setText("Petrol");
                            else if(itemCar.getFuelType() == 1)
                                DetBooking_Fuel_Type_TV_Det.setText("Diesel");
                            else if(itemCar.getFuelType() == 2)
                                DetBooking_Fuel_Type_TV_Det.setText("Electric");
                            else if(itemCar.getFuelType() == 3)
                                DetBooking_Fuel_Type_TV_Det.setText("CNG");

                        }
                    });

                    itemBooking.getParentServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            itemParentService = documentSnapshot.toObject(RecVewService.class);


                            itemBooking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    itemClient = documentSnapshot.toObject(RecVewClient.class);


                                    DetBooking_ClientName_TV_Det.setText(itemClient.getName());
                                    DetBooking_ClientMbNo_TV_Det.setText(itemClient.getMbNo());
                                    DetBooking_ClientAddress_TV_Det.setText(itemClient.getAddress() + ", " + itemClient.getCity() + ", " +  itemClient.getState() + ", PinCode: " + itemClient.getPincode() + ", " + itemClient.getCountry());


                                    fStore.collection("Bookings").document(itemBooking.getID()).collection("rejectedBy").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                            ArrayList<String> rejectedWorkshops = new ArrayList<>();
                                            ArrayList<String> rejectedServices = new ArrayList<>();

                                            for(QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots )
                                            {
                                                rejectedWorkshops.add(queryDocumentSnapshot.getString("workShopId"));
                                                rejectedServices.add(queryDocumentSnapshot.getString("serviceId"));
                                            }


                                            DetBooking_Accept.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    if(itemBooking.getStatus() == -1)
                                                    {
                                                        Intent intent = new Intent(DetailsBooking.this, ChooseWorkshop.class);
                                                        intent.putExtra("itemService",itemParentService);
                                                        intent.putExtra("itemBooking",itemBooking);
                                                        intent.putStringArrayListExtra("rejectedWorkshops",rejectedWorkshops);
                                                        intent.putStringArrayListExtra("rejectedServices",rejectedServices);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                    }
                                                    else
                                                    {

//                                              itemClient.getEmail();

                                                        new AlertDialog.Builder(DetailsBooking.this)
                                                                .setTitle("Choose Contact Option")
                                                                .setMessage("Choose an application to contact Client")
                                                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {


                                                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
                                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                                        }

                                                                    }
                                                                })
                                                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo(), null));
                                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                                            startActivity(phoneIntent);
                                                                        }

                                                                    }
                                                                })
                                                                .create().show();

                                                    }
                                                }
                                            });


                                        }
                                    });

                                }
                            });


                        }
                    });


                    itemBooking.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            itemService = documentSnapshot.toObject(RecVewService.class);


                            toolbar.setTitle(itemService.getName());
                            DetBooking_ServiceName_TV_Det.setText(itemService.getName());


                        }
                    });


                    itemBooking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);


                            DetBooking_WorkshopName_TV_Det.setText(itemWorkshop.getName());
                            DetBooking_WorkshopMbNo_TV_Det.setText(itemWorkshop.getMbNo());
                            DetBooking_WorkshopAddress_TV_Det.setText(itemWorkshop.getAddress() + ", " + itemWorkshop.getCity() + ", " + itemWorkshop.getState() + ", PinCode: " + itemWorkshop.getPincode() + ", " + itemWorkshop.getCountry());

                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

//                             itemWorkshop.getEmail();

                                    new AlertDialog.Builder(DetailsBooking.this)
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
                        }
                    });


                    if(itemBooking.getStatus() == 3)
                    {
                        DetBooking_Commission_TV_Det.setText(Double.toString(itemBooking.getCommission()));
                        DetBooking_Commission_V.setVisibility(View.VISIBLE);
                        DetBooking_Commission_LL.setVisibility(View.VISIBLE);

                        DetBooking_CommissionablePrice_TV_Det.setText(Double.toString(itemBooking.getCommissionablePrice()));
                        DetBooking_CommissionablePrice_V.setVisibility(View.VISIBLE);
                        DetBooking_CommissionablePrice_LL.setVisibility(View.VISIBLE);


//                        DetBooking_NextDate_V.setVisibility(View.VISIBLE);
//                        DetBooking_NextDate_LL.setVisibility(View.VISIBLE);

                        DetBooking_NextKM_V.setVisibility(View.VISIBLE);
                        DetBooking_NextKM_LL.setVisibility(View.VISIBLE);


//                        String pattern = "dd-MM-yyyy";
//                        DateFormat df = new SimpleDateFormat(pattern);
//
//                        DetBooking_NextDate_TV_Det.setText(df.format(itemBooking.getNextScheduledDate().toDate()));

                        DetBooking_NextKM_TV_Det.setText(Double.toString(itemBooking.getNextScheduledKM()));


                    }
                    else {

                        DetBooking_Commission_V.setVisibility(View.GONE);
                        DetBooking_Commission_LL.setVisibility(View.GONE);
                        DetBooking_CommissionablePrice_V.setVisibility(View.GONE);
                        DetBooking_CommissionablePrice_LL.setVisibility(View.GONE);

//                        DetBooking_NextDate_V.setVisibility(View.GONE);
//                        DetBooking_NextDate_LL.setVisibility(View.GONE);

                        DetBooking_NextKM_V.setVisibility(View.GONE);
                        DetBooking_NextKM_LL.setVisibility(View.GONE);
                    }


                    if(itemBooking.getPrice() != -1)
                    {
                        DetBooking_ServicePrice.setText(Double.toString(itemBooking.getPrice()));
                    }
                    else
                    {
                        DetBooking_ServicePrice.setText("Variable");
                    }

                    String pattern = "dd-MM-yyyy";
                    DateFormat df = new SimpleDateFormat(pattern);
                    DetBooking_ServiceDate_TV_Det.setText(df.format(itemBooking.getServiceDate().toDate()));

                    if(itemBooking.getStatus() == 3 && itemBooking.getRating() != -1)
                    {
                        DetBooking_Rating_View.setVisibility(View.VISIBLE);
                        DetBooking_Rating_LL.setVisibility(View.VISIBLE);
                        DetBooking_Rating_TV_Det.setVisibility(View.VISIBLE);
                        DetBooking_Rating_TV_Det.setText(Integer.toString(itemBooking.getRating()));

                    }
                    else
                    {
                        DetBooking_Rating_View.setVisibility(View.GONE);
                        DetBooking_Rating_LL.setVisibility(View.GONE);
                        DetBooking_Rating_TV_Det.setVisibility(View.GONE);
                    }

                    if(itemBooking.getStatus() == 2 || itemBooking.getStatus() == 3)
                    {
                        DetBooking_fab.setVisibility(View.VISIBLE);
                        DetBooking_fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(itemBooking.getStatus() == 2)
                                {
                                    Intent intent = new Intent(DetailsBooking.this, ViewImage.class);
                                    intent.putExtra("path", fStorage.getReference().child("Bookings").child(itemBooking.getID()).child("jobCard.jpeg").getPath());
                                    startActivity(intent);
                                }
                                else if(itemBooking.getStatus() == 3)
                                {
                                    Intent intent = new Intent(DetailsBooking.this, ViewImage.class);
                                    intent.putExtra("path", fStorage.getReference().child("Bookings").child(itemBooking.getID()).child("invoice.jpeg").getPath());
                                    startActivity(intent);
                                }

                            }
                        });
                    }
                    else {
                        DetBooking_fab.setVisibility(View.INVISIBLE);

                    }


                    if(itemBooking.getPickUp() == 0)
                        DetBooking_PickUp_TV_Det.setText("Not Chosen");
                    else
                        DetBooking_PickUp_TV_Det.setText("Chosen");


                    DetBooking_Details_TV_Det.setText(itemBooking.getDetails());

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
}