package com.cmdrj.caremydrive;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class DetailsBooking extends AppCompatActivity implements DialogRating.onInputListenerR{

    TextView DetBooking_ServiceName, DetBooking_ServicePrice, DetBooking_ServiceNotice, DetBooking_Workshop_TV_Det, DetBooking_Rating_TV_Det, DetBooking_Address_TV_Det, DetBooking_PickUp_TV_Det, DetBooking_ServiceDate_TV_Det, DetBooking_Status_TV_Det, DetBooking_Details_TV_Det, DetBooking_No_TV_Det, DetBooking_CarName_TV_Det, DetBooking_NextDate_TV_Det, DetBooking_NextKM_TV_Det;
    ImageView DetBooking_IV;
    LinearLayout DetBooking_Rating_LL, DetBooking_Workshop_LL, DetBooking_No_LL, DetBooking_Address_LL, DetBooking_ServiceDate_LL, DetBooking_PickUp_LL, DetBooking_Details_LL, DetBooking_LL, DetBooking_NextDate_LL, DetBooking_NextKM_LL;
    View DetBooking_Rating_View, DetBooking_Workshop_V, DetBooking_No_V, DetBooking_Address_V, DetBooking_ServiceDate_V, DetBooking_PickUp_V, DetBooking_Details_V, DetBooking_NextDate_V, DetBooking_NextKM_V;
    Button DetBooking_Rating_Button, DetBooking_Reject, DetBooking_Accept;
    FloatingActionButton DetBooking_fab;

    RecVewBookings itemBooking;
    RecVewWorkshop itemWorkshop;
    RecVewService itemService;
    RecVewCar itemCar;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;

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

        DetBooking_ServiceName = findViewById(R.id.DetBooking_ServiceName);
        DetBooking_ServicePrice = findViewById(R.id.DetBooking_ServicePrice);
        DetBooking_Workshop_TV_Det= findViewById(R.id.DetBooking_Workshop_TV_Det);
        DetBooking_Rating_TV_Det= findViewById(R.id.DetBooking_Rating_TV_Det);
        DetBooking_Address_TV_Det= findViewById(R.id.DetBooking_Address_TV_Det);
        DetBooking_CarName_TV_Det= findViewById(R.id.DetBooking_CarName_TV_Det);
        DetBooking_PickUp_TV_Det= findViewById(R.id.DetBooking_PickUp_TV_Det);
        DetBooking_ServiceDate_TV_Det= findViewById(R.id.DetBooking_ServiceDate_TV_Det);
        DetBooking_Status_TV_Det = findViewById(R.id.DetBooking_Status_TV_Det);
        DetBooking_Details_TV_Det = findViewById(R.id.DetBooking_Details_TV_Det);
        DetBooking_No_TV_Det = findViewById(R.id.DetBooking_No_TV_Det);
        DetBooking_NextDate_TV_Det = findViewById(R.id.DetBooking_NextDate_TV_Det);
        DetBooking_NextKM_TV_Det = findViewById(R.id.DetBooking_NextKM_TV_Det);

        DetBooking_Rating_LL = findViewById(R.id.DetBooking_Rating_LL);
        DetBooking_LL = findViewById(R.id.DetBooking_LL);
        DetBooking_NextDate_LL = findViewById(R.id.DetBooking_NextDate_LL);
        DetBooking_NextKM_LL = findViewById(R.id.DetBooking_NextKM_LL);

        DetBooking_Rating_View = findViewById(R.id.DetBooking_Rating_View);

        DetBooking_IV = findViewById(R.id.DetBooking_IV);

        DetBooking_Rating_Button = findViewById(R.id.DetBooking_Rating_Button);
        DetBooking_Reject = findViewById(R.id.DetBooking_Reject);
        DetBooking_Accept = findViewById(R.id.DetBooking_Accept);
        DetBooking_fab = findViewById(R.id.DetBooking_fab);


        DetBooking_ServiceNotice = findViewById(R.id.DetBooking_ServiceNotice);
        DetBooking_Workshop_V = findViewById(R.id.DetBooking_Workshop_V);
        DetBooking_No_V = findViewById(R.id.DetBooking_No_V);
        DetBooking_Address_V = findViewById(R.id.DetBooking_Address_V);
        DetBooking_ServiceDate_V = findViewById(R.id.DetBooking_ServiceDate_V);
        DetBooking_PickUp_V = findViewById(R.id.DetBooking_PickUp_V);
        DetBooking_Details_V = findViewById(R.id.DetBooking_Details_V);
        DetBooking_NextDate_V = findViewById(R.id.DetBooking_NextDate_V);
        DetBooking_NextKM_V = findViewById(R.id.DetBooking_NextKM_V);
        DetBooking_Workshop_LL = findViewById(R.id.DetBooking_Workshop_LL);
        DetBooking_No_LL = findViewById(R.id.DetBooking_No_LL);
        DetBooking_Address_LL = findViewById(R.id.DetBooking_Address_LL);
        DetBooking_ServiceDate_LL = findViewById(R.id.DetBooking_ServiceDate_LL);
        DetBooking_PickUp_LL = findViewById(R.id.DetBooking_PickUp_LL);
        DetBooking_Details_LL = findViewById(R.id.DetBooking_Details_LL);



        Intent intent = getIntent();
        itemBooking = intent.getExtras().getParcelable("itemBooking");

        DocumentReference dReference = fStore.collection("Bookings").document(itemBooking.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetBooking_ServiceName, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemBooking = value.toObject(RecVewBookings.class);

                    itemBooking.getVehicleRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            itemCar = documentSnapshot.toObject(RecVewCar.class);

                            DetBooking_CarName_TV_Det.setText(itemCar.getName());


                        }
                    });

                    if(itemBooking.getStatus() == -1)
                    {

                        DetBooking_ServiceNotice.setVisibility(View.VISIBLE);
                        DetBooking_ServiceNotice.setText("Note: Due to unavoidable circumstances we have to reassign you a different workshop / service. Please cooperate with us. Thanks.");
                        DetBooking_Accept.setVisibility(View.GONE);
                        DetBooking_Reject.setVisibility(View.VISIBLE);
                        DetBooking_ServicePrice.setVisibility(View.GONE);

                        DetBooking_Workshop_V.setVisibility(View.GONE);
                        DetBooking_No_V.setVisibility(View.GONE);
                        DetBooking_Address_V.setVisibility(View.GONE);
                        DetBooking_Workshop_LL.setVisibility(View.GONE);
                        DetBooking_No_LL.setVisibility(View.GONE);
                        DetBooking_Address_LL.setVisibility(View.GONE);

                        DetBooking_ServiceDate_V.setVisibility(View.GONE);
                        DetBooking_ServiceDate_LL.setVisibility(View.GONE);

                        DetBooking_PickUp_V.setVisibility(View.GONE);
                        DetBooking_PickUp_LL.setVisibility(View.GONE);

                        DetBooking_Details_V.setVisibility(View.GONE);
                        DetBooking_Details_LL.setVisibility(View.GONE);



                        itemBooking.getParentServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                itemService = documentSnapshot.toObject(RecVewService.class);

                                toolbar.setTitle(itemService.getName());

                                DetBooking_ServiceName.setText(itemService.getName());


                            }
                        });

                        DetBooking_Reject.setText("Contact Admin");

                        DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                new AlertDialog.Builder(DetailsBooking.this)
                                        .setTitle("Choose Contact Option")
                                        .setMessage("Choose an application to contact Admin")
                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getResources().getString(R.string.admin_Email), null));
                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                }

                                            }
                                        })
                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getResources().getString(R.string.admin_PhoneNo) , null));
                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                    startActivity(phoneIntent);
                                                }

                                            }
                                        })
                                        .create().show();

                            }
                        });

                    }
                    else
                    {
                        if(itemBooking.getStatus() == -3)
                        {
                            DetBooking_ServiceNotice.setVisibility(View.VISIBLE);
                            DetBooking_ServiceNotice.setText("Note: Due to unavoidable circumstances we have to reassign you a different workshop / service. Please cooperate with us. Thanks.");
                            DetBooking_Accept.setVisibility(View.VISIBLE);
                            DetBooking_Reject.setVisibility(View.VISIBLE);

                            DetBooking_Reject.setText("Change\nWorkshop");
                            DetBooking_Accept.setText("Accept\nWorkshop");

                            itemBooking.getParentServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    RecVewService itemParentService = documentSnapshot.toObject(RecVewService.class);

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



                                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {

                                                    if(itemBooking.getStatus() == -3)
                                                    {
                                                        Intent intent = new Intent(DetailsBooking.this, ChooseRemainingWorkshop.class);
                                                        intent.putExtra("itemService",itemParentService);
                                                        intent.putExtra("itemBooking",itemBooking);
                                                        intent.putStringArrayListExtra("rejectedWorkshops",rejectedWorkshops);
                                                        intent.putStringArrayListExtra("rejectedServices",rejectedServices);
                                                        startActivity(intent);
                                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                    }
                                                }
                                            });
                                        }
                                    });

                                }
                            });





                            DetBooking_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("id", itemBooking.getID());
                                    map.put("ref", fStore.collection("Bookings").document(itemBooking.getID()));

                                    Map<String, Object> map1 = new HashMap<>();
                                    map1.put("status", 0);

                                    itemBooking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

                                            fStore.collection("Workshops").document(itemWorkshop.getID()).collection("requestedBookings").document(itemBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {


                                                    itemBooking.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                            itemService = documentSnapshot.toObject(RecVewService.class);

                                                            fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID()).collection("requestedBookings").document(itemBooking.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    fStore.collection("Bookings").document(itemBooking.getID()).update(map1);
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
                        else if(itemBooking.getStatus() == -2)
                        {
                            DetBooking_ServiceNotice.setVisibility(View.VISIBLE);
                            DetBooking_ServiceNotice.setText("Note: Due to unavailability of slots we have to reschedule the servicing date. Please cooperate with us. Thanks.");
                            DetBooking_Accept.setVisibility(View.VISIBLE);
                            DetBooking_Reject.setVisibility(View.VISIBLE);

                            DetBooking_Reject.setText("Change\nDate");
                            DetBooking_Accept.setText("Accept\nDate");


                            DetBooking_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("status", 0);

                                    fStore.collection("Bookings").document(itemBooking.getID()).update(map);
                                }
                            });

                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    long timestamp = itemBooking.getServiceDate().toDate().getTime();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTimeInMillis(timestamp);

                                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                                            v.getContext(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

//                            month =  month +1;
//                            startDate = dayOfMonth+" /"+month+" /"+year;
//                            yy[0] = year;
//                            mm[0] = month-1;
//                            dd[0] = dayOfMonth;
//                            ConfirmBooking_Date_MACT.setText(startDate);

                                            Date serviceDate = null;
                                            Calendar cal = GregorianCalendar.getInstance();
                                            cal.set(year, month, dayOfMonth);
                                            serviceDate = cal.getTime();

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("serviceDate", new Timestamp(serviceDate));
                                            map.put("status", 0);


                                            fStore.collection("Bookings").document(itemBooking.getID()).update(map);

                                        }
                                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    datePickerDialog.setCancelable(false);
                                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                    datePickerDialog.show();


                                }
                            });
                        }
                        else
                        {
                            DetBooking_Accept.setVisibility(View.GONE);
                            DetBooking_Reject.setVisibility(View.VISIBLE);
                            DetBooking_ServiceNotice.setVisibility(View.GONE);

                            if(itemBooking.getStatus() == 3)
                            {
                                DetBooking_Reject.setText("Contact Admin");

                            }
                            else if(itemBooking.getStatus() == 0 || itemBooking.getStatus()== 1 || itemBooking.getStatus() == 2)
                            {
                                DetBooking_Reject.setText("Contact");

                            }

                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(itemBooking.getStatus() == 3)
                                    {

                                        new AlertDialog.Builder(DetailsBooking.this)
                                                .setTitle("Choose Contact Option")
                                                .setMessage("Choose an application to contact Admin")
                                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", getResources().getString(R.string.admin_Email), null));
                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                        }

                                                    }
                                                })
                                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getResources().getString(R.string.admin_PhoneNo) , null));
                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                            startActivity(phoneIntent);
                                                        }

                                                    }
                                                })
                                                .create().show();



                                    }
                                    else if(itemBooking.getStatus() == 0 || itemBooking.getStatus()== 1 || itemBooking.getStatus() == 2)
                                    {
                                        itemBooking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

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

                                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemWorkshop.getMbNo() , null));
                                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                                    startActivity(phoneIntent);
                                                                }

                                                            }
                                                        })
                                                        .create().show();

                                            }
                                        });
                                    }
                                }
                            });

                        }


                        DetBooking_ServicePrice.setVisibility(View.VISIBLE);

                        DetBooking_Workshop_V.setVisibility(View.VISIBLE);
                        DetBooking_No_V.setVisibility(View.VISIBLE);
                        DetBooking_Address_V.setVisibility(View.VISIBLE);
                        DetBooking_Workshop_LL.setVisibility(View.VISIBLE);
                        DetBooking_No_LL.setVisibility(View.VISIBLE);
                        DetBooking_Address_LL.setVisibility(View.VISIBLE);

                        DetBooking_ServiceDate_V.setVisibility(View.VISIBLE);
                        DetBooking_ServiceDate_LL.setVisibility(View.VISIBLE);

                        DetBooking_PickUp_V.setVisibility(View.VISIBLE);
                        DetBooking_PickUp_LL.setVisibility(View.VISIBLE);

                        DetBooking_Details_V.setVisibility(View.VISIBLE);
                        DetBooking_Details_LL.setVisibility(View.VISIBLE);

                        itemBooking.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                itemService = documentSnapshot.toObject(RecVewService.class);

                                toolbar.setTitle(itemService.getName());

                                DetBooking_ServiceName.setText(itemService.getName());


                            }
                        });

                        itemBooking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

                                DetBooking_Workshop_TV_Det.setText(itemWorkshop.getName());
                                DetBooking_No_TV_Det.setText(itemWorkshop.getMbNo());
                                DetBooking_Address_TV_Det.setText(itemWorkshop.getAddress() + ", " + itemWorkshop.getCity() + ", " + itemWorkshop.getState() + ", PinCode: " + itemWorkshop.getPincode() + ", " + itemWorkshop.getCountry());

                            }
                        });


//        DetBooking_ServicePrice.setText(Double.toString(itemBooking.getPrice()));
                        if(itemBooking.getPrice() == -1)
                            DetBooking_ServicePrice.setText("Variable");

                        else
                            DetBooking_ServicePrice.setText(Double.toString(itemBooking.getPrice()));

                        if(itemBooking.getPickUp() == 0)
                            DetBooking_PickUp_TV_Det.setText("Not Chosen");

                        else if(itemBooking.getPickUp() == 1)
                            DetBooking_PickUp_TV_Det.setText("Chosen");


                        String pattern = "dd-MM-yyyy";
                        DateFormat df = new SimpleDateFormat(pattern);
                        DetBooking_ServiceDate_TV_Det.setText(df.format(itemBooking.getServiceDate().toDate()));

                        DetBooking_Details_TV_Det.setText(itemBooking.getDetails());



                    }




                    if(itemBooking.getStatus() == 3 )
                    {


//                        DetBooking_NextDate_V.setVisibility(View.VISIBLE);
//                        DetBooking_NextDate_LL.setVisibility(View.VISIBLE);

                        DetBooking_NextKM_V.setVisibility(View.VISIBLE);
                        DetBooking_NextKM_LL.setVisibility(View.VISIBLE);


//                        String pattern = "dd-MM-yyyy";
//                        DateFormat df = new SimpleDateFormat(pattern);
//
//                        DetBooking_NextDate_TV_Det.setText(df.format(itemBooking.getNextScheduledDate().toDate()));

                        DetBooking_NextKM_TV_Det.setText(Double.toString(itemBooking.getNextScheduledKM()));

                        DetBooking_Rating_View.setVisibility(View.VISIBLE);
                        DetBooking_Rating_LL.setVisibility(View.VISIBLE);

                        if(itemBooking.getRating() != -1)
                        {
                            DetBooking_Rating_TV_Det.setText(Integer.toString(itemBooking.getRating()));
                            DetBooking_Rating_TV_Det.setVisibility(View.VISIBLE);
                            DetBooking_Rating_Button.setVisibility(View.GONE);

                        }
                        else
                        {
                            DetBooking_Rating_TV_Det.setVisibility(View.GONE);
                            DetBooking_Rating_Button.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {

//                        DetBooking_NextDate_V.setVisibility(View.GONE);
//                        DetBooking_NextDate_LL.setVisibility(View.GONE);

                        DetBooking_NextKM_V.setVisibility(View.GONE);
                        DetBooking_NextKM_LL.setVisibility(View.GONE);

                        DetBooking_Rating_View.setVisibility(View.GONE);
                        DetBooking_Rating_LL.setVisibility(View.GONE);
                    }


                    DetBooking_Rating_Button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Bundle bundle = new Bundle();
                            bundle.putParcelable("itemBooking", itemBooking);

                            DialogRating dialogRating = new DialogRating();
//                    dialogAddRemark.setTargetFragment(fragmentInitiated,1);
                            dialogRating.setArguments(bundle);
                            dialogRating.show(getSupportFragmentManager(), "Rate: ");

                        }
                    });



                    if(itemBooking.getStatus() == 0 || itemBooking.getStatus() == -1)
                        DetBooking_Status_TV_Det.setText("Requested");

                    else if(itemBooking.getStatus() == -2)
                        DetBooking_Status_TV_Det.setText("Rescheduled");

                    else if(itemBooking.getStatus() == -3)
                        DetBooking_Status_TV_Det.setText("Recommended");

                    else if(itemBooking.getStatus() == 1)
                        DetBooking_Status_TV_Det.setText("Accepted");

                    else if(itemBooking.getStatus() == 2)
                        DetBooking_Status_TV_Det.setText("In-Progress");

                    else if(itemBooking.getStatus() == 3)
                        DetBooking_Status_TV_Det.setText("Completed");


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
                    else
                    {
                        DetBooking_fab.setVisibility(View.GONE);
                    }
                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }

    @Override
    public void sendInputR(int rating, RecVewBookings booking) {

        itemBooking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

                Map<String, Object> map = new HashMap<>();

                Map<String, Object> map1 = new HashMap<>();

                Map<String, Object> map2 = new HashMap<>();
                map2.put("rating", rating);

                map1.put("numRated", FieldValue.increment(1));

                if(itemWorkshop.getRating() != -1)
                {
                    map.put("rating", Math.ceil( ((float)((itemWorkshop.getNumRated() * itemWorkshop.getRating()) + rating)) / (itemWorkshop.getNumRated() + 1)  ) );

                }
                else
                {
                    map.put("rating", rating);
                }

                fStore.collection("Bookings").document(itemBooking.getID()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {


                        itemBooking.getWorkShopRef().update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                itemBooking.getWorkShopRef().update(map1);
                            }
                        });

                    }
                });

            }
        });

    }
}