package com.cmdrj.caremydriveworkshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailsBooking extends AppCompatActivity implements DialogAddRemark.onInputListenerAR , DialogUploadImage.onInputListenerUI{

    TextView DetBooking_CarModel, DetBooking_ServicePrice, DetBooking_Client_TV_Det, DetBooking_MbNo_TV_Det, DetBooking_Address_TV_Det, DetBooking_CarNo_TV_Det, DetBooking_Fuel_Type_TV_Det, DetBooking_ServiceName_TV_Det, DetBooking_ServiceDate_TV_Det, DetBooking_Rating_TV, DetBooking_PickUp_TV_Det, DetBooking_Details_TV_Det, DetBooking_Commission_TV_Det, DetBooking_CommissionablePrice_TV_Det, DetBooking_NextDate_TV_Det, DetBooking_NextKM_TV_Det;
    ImageView DetBooking_IV;
    Button DetBooking_Accept, DetBooking_Reject;
    LinearLayout DetBooking_Rating_LL, DetBooking_LL, DetBooking_Commission_LL, DetBooking_CommissionablePrice_LL, DetBooking_NextDate_LL, DetBooking_NextKM_LL;
    View DetBooking_Rating_View, DetBooking_Commission_V, DetBooking_CommissionablePrice_V, DetBooking_NextDate_V, DetBooking_NextKM_V;
    FloatingActionButton DetBooking_fab;

    RecVewBookings itemBooking;
    RecVewService itemService;
    RecVewCar itemCar;
    RecVewClient itemClient;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;
    DocumentReference dReference;

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
        DetBooking_Client_TV_Det = findViewById(R.id.DetBooking_Client_TV_Det);
        DetBooking_MbNo_TV_Det = findViewById(R.id.DetBooking_MbNo_TV_Det);
        DetBooking_Address_TV_Det = findViewById(R.id.DetBooking_Address_TV_Det);
        DetBooking_CarNo_TV_Det = findViewById(R.id.DetBooking_CarNo_TV_Det);
        DetBooking_Fuel_Type_TV_Det = findViewById(R.id.DetBooking_Fuel_Type_TV_Det);
        DetBooking_ServiceName_TV_Det = findViewById(R.id.DetBooking_ServiceName_TV_Det);
        DetBooking_ServiceDate_TV_Det = findViewById(R.id.DetBooking_ServiceDate_TV_Det);
        DetBooking_Rating_TV = findViewById(R.id.DetBooking_Rating_TV);
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
        DetBooking_LL = findViewById(R.id.DetBooking_LL);
        DetBooking_Commission_LL = findViewById(R.id.DetBooking_Commission_LL);
        DetBooking_CommissionablePrice_LL = findViewById(R.id.DetBooking_CommissionablePrice_LL);
        DetBooking_NextDate_LL = findViewById(R.id.DetBooking_NextDate_LL);
        DetBooking_NextKM_LL = findViewById(R.id.DetBooking_NextKM_LL);


        DetBooking_Rating_View = findViewById(R.id.DetBooking_Rating_View);
        DetBooking_Commission_V = findViewById(R.id.DetBooking_Commission_V);
        DetBooking_CommissionablePrice_V = findViewById(R.id.DetBooking_CommissionablePrice_V);
        DetBooking_NextDate_V = findViewById(R.id.DetBooking_NextDate_V);
        DetBooking_NextKM_V = findViewById(R.id.DetBooking_NextKM_V);


        DetBooking_CarModel.setSelected(true);

        Intent intent = getIntent();
        itemBooking = intent.getExtras().getParcelable("itemBooking");


        dReference = fStore.collection("Bookings").document(itemBooking.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetBooking_CarModel, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemBooking = value.toObject(RecVewBookings.class);

                    if(itemBooking.getStatus() != -1)
                    {
                        itemBooking.getVehicleRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                itemCar = documentSnapshot.toObject(RecVewCar.class);

                                DetBooking_CarModel.setText(itemCar.getModel() + ", " + itemCar.getManufacturer());

                                DetBooking_CarNo_TV_Det.setText(itemCar.getNo());

                                if (itemCar.getFuelType() == 0)
                                    DetBooking_Fuel_Type_TV_Det.setText("Petrol");

                                else if (itemCar.getFuelType() == 1)
                                    DetBooking_Fuel_Type_TV_Det.setText("Diesel");

                                else if (itemCar.getFuelType() == 2)
                                    DetBooking_Fuel_Type_TV_Det.setText("Electric");

                                else if (itemCar.getFuelType() == 3)
                                    DetBooking_Fuel_Type_TV_Det.setText("CNG");


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

                        itemBooking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                itemClient = documentSnapshot.toObject(RecVewClient.class);

                                DetBooking_Client_TV_Det.setText(itemClient.getName());
                                DetBooking_MbNo_TV_Det.setText(itemClient.getMbNo());
                                DetBooking_Address_TV_Det.setText(itemClient.getAddress() + ", " + itemClient.getCity() + ", " + itemClient.getState() + ", PinCode: " + itemClient.getPincode() + ", " + itemClient.getCountry());



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


                        if (itemBooking.getPrice() != -1)
                            DetBooking_ServicePrice.setText(Double.toString(itemBooking.getPrice()));
                        else
                            DetBooking_ServicePrice.setText("Variable");








                        String pattern = "dd-MM-yyyy";
                        DateFormat df = new SimpleDateFormat(pattern);
                        DetBooking_ServiceDate_TV_Det.setText(df.format(itemBooking.getServiceDate().toDate()));


                        if (itemBooking.getStatus() == 3 && itemBooking.getRating() != -1) {
                            DetBooking_Rating_View.setVisibility(View.VISIBLE);
                            DetBooking_Rating_LL.setVisibility(View.VISIBLE);
                            DetBooking_Rating_TV.setVisibility(View.VISIBLE);
                            DetBooking_Rating_TV.setText(Integer.toString(itemBooking.getRating()));

                        } else {
                            DetBooking_Rating_View.setVisibility(View.GONE);
                            DetBooking_Rating_LL.setVisibility(View.GONE);
                            DetBooking_Rating_TV.setVisibility(View.GONE);
                        }


                        if (itemBooking.getPickUp() == 0)
                            DetBooking_PickUp_TV_Det.setText("Not Chosen");
                        else
                            DetBooking_PickUp_TV_Det.setText("Chosen");


                        DetBooking_Details_TV_Det.setText(itemBooking.getDetails());

                        if (itemBooking.getStatus() == 0) {

                            DetBooking_LL.setVisibility(View.VISIBLE);

                            DetBooking_Accept.setText("Accept");

                            DetBooking_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Map<String, Object> map1 = new HashMap<>();
                                    map1.put("status", 1);

                                    Map<String, Object> map2 = new HashMap<>();
                                    map2.put("id", itemBooking.getID());
                                    map2.put("ref", dReference);

                                    dReference.update(map1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    itemBooking.getClientRef().collection("requestedBookings").document(itemBooking.getID()).delete();
                                                    itemBooking.getClientRef().collection("acceptedBookings").document(itemBooking.getID()).set(map2);

                                                    itemBooking.getWorkShopRef().collection("requestedBookings").document(itemBooking.getID()).delete();
                                                    itemBooking.getWorkShopRef().collection("acceptedBookings").document(itemBooking.getID()).set(map2);

                                                    itemBooking.getServiceRef().collection("requestedBookings").document(itemBooking.getID()).delete();
                                                    itemBooking.getServiceRef().collection("acceptedBookings").document(itemBooking.getID()).set(map2);

                                                    itemBooking.getParentServiceRef().collection("requestedBookings").document(itemBooking.getID()).delete();
                                                    itemBooking.getParentServiceRef().collection("acceptedBookings").document(itemBooking.getID()).set(map2);

                                                    itemBooking.getVehicleRef().collection("requestedBookings").document(itemBooking.getID()).delete();
                                                    itemBooking.getVehicleRef().collection("acceptedBookings").document(itemBooking.getID()).set(map2);

                                                }
                                            });

                                }
                            });


                            DetBooking_Reject.setText("Reject");

                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Bundle bundle = new Bundle();
                                    bundle.putParcelable("itemBooking", itemBooking);

                                    DialogAddRemark dialogAddRemark = new DialogAddRemark();
//                    dialogAddRemark.setTargetFragment(fragmentInitiated,1);
                                    dialogAddRemark.setArguments(bundle);
                                    dialogAddRemark.show(getSupportFragmentManager(), "Give Reason");
                                }
                            });

                        }
                        else if (itemBooking.getStatus() == 1)
                        {
                            DetBooking_LL.setVisibility(View.VISIBLE);
                            DetBooking_Reject.setVisibility(View.VISIBLE);
                            DetBooking_Accept.setText("Start\nServicing");
                            DetBooking_Reject.setText("Contact\nClient");

                            DetBooking_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {









                                    Bundle bundle = new Bundle();
                                    bundle.putInt("parentActivity",0);
                                    bundle.putParcelable("itemBooking", itemBooking);

                                    DialogUploadImage dialogUploadImage = new DialogUploadImage();
//                    dialogAddRemark.setTargetFragment(fragmentInitiated,1);
                                    dialogUploadImage.setArguments(bundle);
                                    dialogUploadImage.show(getSupportFragmentManager(), "Upload job Card");























                                }
                            });

                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(itemClient == null)
                                    {
                                        itemBooking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                RecVewClient client = documentSnapshot.toObject(RecVewClient.class);

                                                new AlertDialog.Builder(DetailsBooking.this)
                                                        .setTitle("Choose Contact Option")
                                                        .setMessage("Choose an application to contact Client")
                                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", client.getEmail(), null));
                                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                                }

                                                            }
                                                        })
                                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", client.getMbNo() , null));
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
                                        new AlertDialog.Builder(DetailsBooking.this)
                                                .setTitle("Choose Contact Option")
                                                .setMessage("Choose an application to contact Client")
                                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                            v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                        }

                                                    }
                                                })
                                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo() , null));
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
                        else if (itemBooking.getStatus() == 2)
                        {
                            DetBooking_LL.setVisibility(View.VISIBLE);
                            DetBooking_Reject.setVisibility(View.VISIBLE);
                            DetBooking_Accept.setText("Finish\nServicing");
                            DetBooking_Reject.setText("Contact\nClient");

                            DetBooking_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {





                                    Bundle bundle = new Bundle();
                                    bundle.putInt("parentActivity",1);
                                    bundle.putParcelable("itemBooking", itemBooking);

                                    DialogUploadImage dialogUploadImage = new DialogUploadImage();
//                    dialogAddRemark.setTargetFragment(fragmentInitiated,1);
                                    dialogUploadImage.setArguments(bundle);
                                    dialogUploadImage.show(getSupportFragmentManager(), "Upload Invoice");












                                }
                            });

                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(itemClient == null)
                                    {
                                        itemBooking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                RecVewClient client = documentSnapshot.toObject(RecVewClient.class);

                                                new AlertDialog.Builder(DetailsBooking.this)
                                                        .setTitle("Choose Contact Option")
                                                        .setMessage("Choose an application to contact Client")
                                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", client.getEmail(), null));
                                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                                }

                                                            }
                                                        })
                                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", client.getMbNo() , null));
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
                                        new AlertDialog.Builder(DetailsBooking.this)
                                                .setTitle("Choose Contact Option")
                                                .setMessage("Choose an application to contact Client")
                                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                            v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                        }

                                                    }
                                                })
                                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo() , null));
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
                        else if (itemBooking.getStatus() == 3)
                        {
                            DetBooking_LL.setVisibility(View.VISIBLE);
                            DetBooking_Reject.setVisibility(View.GONE);
                            DetBooking_Accept.setText("View Invoice");

                            DetBooking_Accept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(DetailsBooking.this, ViewImage.class);
                                    intent.putExtra("path", fStorage.getReference().child("Bookings").child(itemBooking.getID()).child("invoice.jpeg").getPath());
                                    startActivity(intent);

                                }
                            });
                        }
                        else if(itemBooking.getStatus() == -2)
                        {
                            DetBooking_LL.setVisibility(View.VISIBLE);
                            DetBooking_Accept.setVisibility(View.GONE);
                            DetBooking_Reject.setVisibility(View.VISIBLE);
                            DetBooking_Reject.setText("Contact Client");

                            DetBooking_Reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(itemClient == null)
                                    {
                                        itemBooking.getClientRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                RecVewClient client = documentSnapshot.toObject(RecVewClient.class);

                                                new AlertDialog.Builder(DetailsBooking.this)
                                                        .setTitle("Choose Contact Option")
                                                        .setMessage("Choose an application to contact Client")
                                                        .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", client.getEmail(), null));
                                                                if (intent.resolveActivity(getPackageManager()) != null) {
                                                                    v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                                }

                                                            }
                                                        })
                                                        .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", client.getMbNo() , null));
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
                                        new AlertDialog.Builder(DetailsBooking.this)
                                                .setTitle("Choose Contact Option")
                                                .setMessage("Choose an application to contact Client")
                                                .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
                                                        if (intent.resolveActivity(getPackageManager()) != null) {
                                                            v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                        }

                                                    }
                                                })
                                                .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo() , null));
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

                        if(itemBooking.getStatus() == 2/* || itemBooking.getStatus() == 3*/)
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
//                                    else if(itemBooking.getStatus() == 3)
//                                    {
//                                        Intent intent = new Intent(DetailsBooking.this, ViewImage.class);
//                                        intent.putExtra("path", fStorage.getReference().child("Bookings").child(itemBooking.getID()).child("invoice.jpeg").getPath());
//                                        startActivity(intent);
//                                    }


                                }
                            });

                        }
                        else
                        {
                            DetBooking_fab.setVisibility(View.GONE);
                        }
                    }

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });

    }

    @Override
    public void sendInputAR(String s, RecVewBookings booking) {

        DetBooking_Reject.setEnabled(false);
        DetBooking_Accept.setEnabled(false);

        Map<String, Object> map = new HashMap<>();
        map.put("reason",s);

        Map<String, Object> map1 = new HashMap<>();
        map1.put("status", -1);

        Map<String, Object> map2 = new HashMap<>();
        map2.put("id", booking.getID());
        map2.put("ref", fStore.collection("Bookings").document(booking.getID()));

        booking.getServiceRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                RecVewService service = documentSnapshot.toObject(RecVewService.class);

                map.put("serviceId",service.getID());
                map.put("serviceRef",booking.getServiceRef());

                booking.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RecVewWorkshop workshop = documentSnapshot.toObject(RecVewWorkshop.class);

                        map.put("workShopId",workshop.getID());
                        map.put("workShopRef",booking.getWorkShopRef());

                        fStore.collection("Bookings").document(booking.getID()).collection("rejectedBy").document(workshop.getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                fStore.collection("Workshops").document(workshop.getID()).collection("Services").document(service.getID()).collection("requestedBookings").document(booking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        fStore.collection("Workshops").document(workshop.getID()).collection("Services").document(service.getID()).collection("rejectedBookings").document(booking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                fStore.collection("Workshops").document(workshop.getID()).collection("requestedBookings").document(booking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        fStore.collection("Workshops").document(workshop.getID()).collection("rejectedBookings").document(booking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                fStore.collection("Bookings").document(booking.getID()).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        finish();

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
                });

            }
        });

    }

    @Override
    public void sendInputUI(boolean valid, RecVewBookings booking) {

        if(valid)
        {
            DetBooking_Accept.setEnabled(false);
            Map<String, Object> map1 = new HashMap<>();
            map1.put("status", 2);

            dReference.update(map1)         ;
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            itemBooking.getClientRef().collection("acceptedBookings").document(itemBooking.getID()).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                    itemBooking.getWorkShopRef().collection("acceptedBookings").document(itemBooking.getID()).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//
//                                            itemBooking.getServiceRef().collection("acceptedBookings").document(itemBooking.getID()).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//
//                                                    itemBooking.getParentServiceRef().collection("acceptedBookings").document(itemBooking.getID()).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
//
//                                                            itemBooking.getVehicleRef().collection("acceptedBookings").document(itemBooking.getID()).update(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void aVoid) {
//
                                                                    DetBooking_Accept.setEnabled(true);
//
//                                                                }
//                                                            });
//
//                                                        }
//                                                    });
//
//                                                }
//                                            });
//
//                                        }
//                                    });
//
//                                }
//                            });
//
//                        }
//                    });
        }
        else
        {
            Snackbar.make(DetBooking_CarModel, "Please Upload a job Card first." , Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendInputUI(boolean valid, RecVewBookings booking, double commissionablePrice, double commission, double nextKM){//, Date nextDate) {

        if(valid)
        {
            DetBooking_Accept.setEnabled(false);

            Map<String, Object> map1 = new HashMap<>();
            map1.put("status", 3);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("id", itemBooking.getID());
            map2.put("ref", dReference);

            dReference.update(map1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            itemBooking.getClientRef().collection("acceptedBookings").document(itemBooking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    itemBooking.getClientRef().collection("completedBookings").document(itemBooking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            itemBooking.getWorkShopRef().collection("acceptedBookings").document(itemBooking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    itemBooking.getWorkShopRef().collection("completedBookings").document(itemBooking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            itemBooking.getServiceRef().collection("acceptedBookings").document(itemBooking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    itemBooking.getServiceRef().collection("completedBookings").document(itemBooking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            itemBooking.getParentServiceRef().collection("acceptedBookings").document(itemBooking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                    itemBooking.getParentServiceRef().collection("completedBookings").document(itemBooking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            itemBooking.getVehicleRef().collection("acceptedBookings").document(itemBooking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

                                                                                                    itemBooking.getVehicleRef().collection("completedBookings").document(itemBooking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {

                                                                                                            DetBooking_Accept.setEnabled(true);

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
            Snackbar.make(DetBooking_CarModel, "Please Fill All Details." , Snackbar.LENGTH_SHORT).show();

        }
    }
}