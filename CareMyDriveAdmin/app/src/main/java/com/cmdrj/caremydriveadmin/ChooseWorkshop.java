package com.cmdrj.caremydriveadmin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ChooseWorkshop extends AppCompatActivity {

    RecyclerView RV_ChooseWorkshop;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewService> newService = new ArrayList<>();
    AdapterRecVewServices adapterRecVewServices;

    RecVewService itemService;
    RecVewBookings itemBooking;
    ArrayList<String> rejectedWorkshops = new ArrayList<>();
    ArrayList<String> rejectedServices = new ArrayList<>();

    LinearLayout RV_ChooseWorkshop_Placeholder_LL;
    TextView RV_ChooseWorkshop_Placeholder_TV;

    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_workshop);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarChooseWorkshop);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();

        RV_ChooseWorkshop_Placeholder_LL = findViewById(R.id.RV_ChooseWorkshop_Placeholder_LL);
        RV_ChooseWorkshop_Placeholder_TV = findViewById(R.id.RV_ChooseWorkshop_Placeholder_TV);


        Intent intent = getIntent();
        itemService = intent.getExtras().getParcelable("itemService");
        itemBooking = intent.getExtras().getParcelable("itemBooking");
        rejectedWorkshops = intent.getExtras().getStringArrayList("rejectedWorkshops");
        rejectedServices = intent.getExtras().getStringArrayList("rejectedServices");


        DocumentReference dReference = fStore.collection("Bookings").document(itemBooking.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
//                    Snackbar.make(findViewById(android.R.id.content), "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemBooking = value.toObject(RecVewBookings.class);

                    if(itemBooking.getStatus() != -1)
                        finish();

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        initDataForRecVew();
        initRecVew();

    }

    private void initDataForRecVew() {

        // TODO : change it to chooseService ( Not general service but a particular service given by a workshop     may be 2 or more services given by a workshop under same general service and also show detail of that particular service instead of workshop details and inside that show details and rating of workshop)


        newService = new ArrayList<>();


//        CollectionReference cReference = fStore.collection("Workshops");
//        DocumentReference dReference = fStore.collection("Services").document(itemService.getID());
//
//
//        cReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                if (error != null) {
//                    Snackbar.make(RV_ChooseWorkshop, "" + error, Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (value != null) {
//
//                    newService.clear();
//
//                    for(QueryDocumentSnapshot document : value)
//                    {
//                        fStore.collection("Workshops").document(document.getId()).collection("Services").whereEqualTo("visibility",true).whereEqualTo("disabled",false).whereEqualTo("parentRef",dReference).whereArrayContains("category",itemCar.getWheelType()).addSnapshotListener(new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                if (error != null) {
//                                    Snackbar.make(RV_ChooseWorkshop, "" + error, Snackbar.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                                if (value != null) {
//
//                                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
//                                    newService.addAll(snapshotList);
//
//                                }
//                                else
//                                {
////                                  Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
//                                    return;
//                                }
//
//                            }
//                        });
//                    }
//
//                    if(newService.size() == 0)
//                    {
//                        String Categ = null;
//
//                        if(itemCar.getWheelType() == 2)
//                            Categ = "Bikes";
//                        else if(itemCar.getWheelType() == 4)
//                            Categ = "Cars";
//                        else if(itemCar.getWheelType() == 6)
//                            Categ = "Heavy";
//                        else if(itemCar.getWheelType() == 3)
//                            Categ = "Others";
//
//
//                        Snackbar.make(RV_ChooseWorkshop, "This Service is not available for " + Categ +" category.", Snackbar.LENGTH_SHORT).show();
//                    }
//
//                    adapterRecVewWorkshops.notifyDataSetChanged();
//
//                } else {
////                    Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
//                    return;
//                }
//            }
//        });

        itemBooking.getVehicleRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                RecVewCar itemCar = documentSnapshot.toObject(RecVewCar.class);

                CollectionReference cReference = fStore.collection("Services").document(itemService.getID()).collection("Child Services");

                cReference.whereNotIn("id",rejectedServices).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {

                            Snackbar.make(RV_ChooseWorkshop, "" + error, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {
                            newService.clear();

//                            Log.d("TAG" , "RishabhJain + " + "Start");

                            for(QueryDocumentSnapshot document : value)
                            {

//                                Log.d("TAG" , "RishabhJain + " + document.toString());
                                DocumentReference dReference = document.getDocumentReference("ref");

                                dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                        if (error != null) {
                                            Snackbar.make(RV_ChooseWorkshop, "" + error, Snackbar.LENGTH_SHORT).show();
                                            return;
                                        }

                                        if (value != null) {


                                            RecVewService service = value.toObject(RecVewService.class);

                                            if(service.getCategory().contains(itemCar.getWheelType()) && service.getVisibility() && !service.getDisabled())
                                                newService.add(service);


                                            if(newService.size() == 0)
                                            {
                                                RV_ChooseWorkshop_Placeholder_TV.setText("All Slots are currently Booked.\nPlease Try Again Later.");
                                                RV_ChooseWorkshop_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ChooseWorkshop.setVisibility(View.INVISIBLE);


//                        Snackbar.make(v, "All Slots are currently Booked.\nPlease Try Again Later.", Snackbar.LENGTH_SHORT).show();
                                            }
                                            else if(newService.size() == 1)
                                            {
                                                RV_ChooseWorkshop_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ChooseWorkshop.setVisibility(View.VISIBLE);
                                            }

                                            Collections.sort(newService, new Comparator<RecVewService>() {
                                                @Override
                                                public int compare(RecVewService o1, RecVewService o2) {
                                                    return Double.compare(o1.getPrice(), o2.getPrice());
                                                }
                                            });

                                            adapterRecVewServices.notifyDataSetChanged();

//                                            Log.d("TAG" , "RishabhJain + " + newService.toString());

                                        }
                                        else
                                        {
//                                  Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });
                            }

                            if(value.size() == 0)
                            {
                                RV_ChooseWorkshop_Placeholder_TV.setText("All Slots are currently Booked.\nPlease Try Again Later.");
                                RV_ChooseWorkshop_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ChooseWorkshop.setVisibility(View.INVISIBLE);


//                        Snackbar.make(findViewById(android.R.id.content), "All Slots are currently Booked.\nPlease Try Again Later.", Snackbar.LENGTH_SHORT).show();
                            }


                        }
                        else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });

                // TODO : uncommnt following lines and rectify the problem
//        TODO : useful Link: https://stackoverflow.com/questions/49390196/firebase-cloud-firestore-getting-a-document-to-custom-object-and-passing-it-to

//        if(newService.size() == 0)
//        {
//            String Categ = null;
//
//            if(itemCar.getWheelType() == 2)
//                Categ = "Bikes";
//            else if(itemCar.getWheelType() == 4)
//                Categ = "Cars";
//            else if(itemCar.getWheelType() == 6)
//                Categ = "Heavy";
//            else if(itemCar.getWheelType() == 3)
//                Categ = "Others";
//
//            Log.d("TAG" , "RishabhJain + here" );
//            Snackbar.make(findViewById(android.R.id.content), "This Service is not available for " + Categ +" category.", Snackbar.LENGTH_SHORT).show();
//        }

                // TODO : over

                //TODO : over

            }
        });

    }

    private void initRecVew() {

        RV_ChooseWorkshop = findViewById(R.id.RV_ChooseWorkshop);
        layoutManager = new LinearLayoutManager(ChooseWorkshop.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_ChooseWorkshop.setLayoutManager(layoutManager);


        adapterRecVewServices = new AdapterRecVewServices(ChooseWorkshop.this, newService, itemBooking);

        RV_ChooseWorkshop.setAdapter(adapterRecVewServices);
        adapterRecVewServices.notifyDataSetChanged();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}