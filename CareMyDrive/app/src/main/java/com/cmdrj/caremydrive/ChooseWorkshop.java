package com.cmdrj.caremydrive;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooseWorkshop extends AppCompatActivity {

    RecyclerView RV_ChooseWorkshop;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewService> newService = new ArrayList<>();
    AdapterRecVewWorkshops adapterRecVewWorkshops;

    LinearLayout RV_ChooseWorkshop_Placeholder_LL;
    TextView RV_ChooseWorkshop_Placeholder_TV;

    RecVewService itemService;
    RecVewCar itemCar;

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
        itemCar = intent.getExtras().getParcelable("itemCar");

        initDataForRecVew();
        initRecVew();
    }

    private void initDataForRecVew() {


//        ArrayList<RecVewWorkshop> oldWorkshop = new ArrayList<>();
//
//        oldWorkshop.add(new RecVewWorkshop("0","HiFi Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,3,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("0",null, "Wheel Alignment", getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 Hour(s)", 500, new ArrayList<Integer>(Arrays.asList(4,6))), new RecVewService("2", null, "Tyre Emergency",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "15 Minute(s)", 50, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
//        oldWorkshop.add(new RecVewWorkshop("1","Decent Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,2,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("1", null, "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "2 Day(s)", 5000, new ArrayList<Integer>(Arrays.asList(2,4,6))),new RecVewService("4",null, "Highway Breakdown",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Variable", 5000, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
//        oldWorkshop.add(new RecVewWorkshop("2","7 Star Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,5,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("5",null, "4 Wheeler Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 Hour(s)", 500, new ArrayList<Integer>(Arrays.asList(4))), new RecVewService("6",null, "New Accessories",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "40 Minute(s)", -1, new ArrayList<Integer>(Arrays.asList(2,4,6))),new RecVewService("7",null, "New Keys",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "30 Minute(s)", 100, new ArrayList<Integer>(Arrays.asList(2,3,4,6))),new RecVewService("8",null, "2 Wheeler Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "Variable", -1, new ArrayList<Integer>(Arrays.asList(2)))))));
//        oldWorkshop.add(new RecVewWorkshop("3","Latest Car Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("3",null, "Fuel Emergencies",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Variable", 150, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
//        oldWorkshop.add(new RecVewWorkshop("4","Decor Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,5,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("0",null, "Wheel Servicing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 Hour(s)", 1000, new ArrayList<Integer>(Arrays.asList(4,6))), new RecVewService("1",null, "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "5 Day(s)", 4000, new ArrayList<Integer>(Arrays.asList(2,4))),new RecVewService("4",null, "Towing Facility",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Variable", -1, new ArrayList<Integer>(Arrays.asList(4,6))),new RecVewService("3",null, "Petrol Pump",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "5 Minute(s)", 102, new ArrayList<Integer>(Arrays.asList(2,4,6))),new RecVewService("2",null, "Puncture Removal",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "30 Minute(s)", 50, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
//        oldWorkshop.add(new RecVewWorkshop("5","Car Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("4",null, "Crane Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 Hour(s)", 5000, new ArrayList<Integer>(Arrays.asList(6))), new RecVewService("1",null, "Vehicle Painting",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "2 Day(s)", 2500, new ArrayList<Integer>(Arrays.asList(3,4))),new RecVewService("7",null, "Lost Keys",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "45 Minute(s)", 500, new ArrayList<Integer>(Arrays.asList(3,4,6)))))));
//        oldWorkshop.add(new RecVewWorkshop("6","Maharaja Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("8",null, "Bikes Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "2 Hour(s)", 5000, new ArrayList<Integer>(Arrays.asList(2))), new RecVewService("5",null, "Cars Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "5 Hour(s)", 10000, new ArrayList<Integer>(Arrays.asList(3,4)))))));
//        oldWorkshop.add(new RecVewWorkshop("7","Sonam Car Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("0", null,"Wheel Alignment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "2 Hour(s)", 400, new ArrayList<Integer>(Arrays.asList(4,6))), new RecVewService("2", null,"Flat Tyre",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 Hour(s)", 400, new ArrayList<Integer>(Arrays.asList(4,6)))))));
//        oldWorkshop.add(new RecVewWorkshop("8","5 Star Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,1,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("3", null, "Electric Charging Station",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 Hour(s)", 500, new ArrayList<Integer>(Arrays.asList(4,2))), new RecVewService("0", null, "Wheel Alignment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 Hour(s)", 450, new ArrayList<Integer>(Arrays.asList(4,3))),new RecVewService("5",null, "4 Wheeler Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "3 Hour(s)", 5500, new ArrayList<Integer>(Arrays.asList(4)))))));
//        oldWorkshop.add(new RecVewWorkshop("9","10 Star Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,3,new ArrayList<RecVewService>(Arrays.asList(new RecVewService("2", null, "Puncture Station",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Variable", -1, new ArrayList<Integer>(Arrays.asList(2,4,6)))))));
//
////        oldWorkshop.add(new RecVewWorkshop(0,"HiFi Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,3,2,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(0, "Wheel Alignment", getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 hour(s)", 500, new ArrayList<Integer>(Arrays.asList(4,6))), new RecVewService(2, "Tyre Emergency",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1/2 hour(s)", 50, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
////        oldWorkshop.add(new RecVewWorkshop(1,"Decent Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,2,3,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(1, "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "5 hour(s)", 5000, new ArrayList<Integer>(Arrays.asList(2,4,6))),new RecVewService(4, "Highway Breakdown",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Depends", 5000, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
////        oldWorkshop.add(new RecVewWorkshop(2,"7 Star Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,5,4,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(5, "4 Wheeler Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 hour(s)", 500, new ArrayList<Integer>(Arrays.asList(4))), new RecVewService(6, "New Accessories",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1/2 hour(s)", -1, new ArrayList<Integer>(Arrays.asList(2,4,6))),new RecVewService(7, "New Keys",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1/2 hour(s)", 100, new ArrayList<Integer>(Arrays.asList(2,3,4,6))),new RecVewService(8, "2 Wheeler Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "Depends", -1, new ArrayList<Integer>(Arrays.asList(2)))))));
////        oldWorkshop.add(new RecVewWorkshop(3,"Latest Car Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,1,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(3, "Fuel Emergencies",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Depends", 150, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
////        oldWorkshop.add(new RecVewWorkshop(4,"Decor Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,5,5,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(0, "Wheel Servicing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 hour(s)", 1000, new ArrayList<Integer>(Arrays.asList(4,6))), new RecVewService(1, "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "5 hour(s)", 4000, new ArrayList<Integer>(Arrays.asList(2,4))),new RecVewService(4, "Towing Facility",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Depends", 4500, new ArrayList<Integer>(Arrays.asList(4,6))),new RecVewService(3, "Petrol Pump",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "5 min", 102, new ArrayList<Integer>(Arrays.asList(2,4,6))),new RecVewService(2, "Puncture Removal",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1/2 hour(s)", 50, new ArrayList<Integer>(Arrays.asList(2,3,4,6)))))));
////        oldWorkshop.add(new RecVewWorkshop(5,"Car Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,3,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(4, "Crane Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 hour(s)", 5000, new ArrayList<Integer>(Arrays.asList(6))), new RecVewService(1, "Vehicle Painting",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "2 hour(s)", 2500, new ArrayList<Integer>(Arrays.asList(2,3))),new RecVewService(7, "Lost Keys",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1/2 hour(s)", 500, new ArrayList<Integer>(Arrays.asList(2,34,6)))))));
////        oldWorkshop.add(new RecVewWorkshop(6,"Maharaja Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,2,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(8, "Bikes Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "2 hour(s)", 5000, new ArrayList<Integer>(Arrays.asList(2)), new RecVewService(5, "Cars Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "5 hour(s)", 10000, new ArrayList<Integer>(Arrays.asList(3,4)))))));
////        oldWorkshop.add(new RecVewWorkshop(7,"Sonam Car Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,-1,2,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(0, "Wheel Alignment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "2 hour(s)", 400), new RecVewService(2, "Flat Tyre",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 hour(s)", 400)))));
////        oldWorkshop.add(new RecVewWorkshop(8,"5 Star Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,1,3,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(3, "Electric Charging Station",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 hour(s)", 500, new ArrayList<Integer>(Arrays.asList(4,2))), new RecVewService(0, "Wheel Alignment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "1 hour(s)", 450, new ArrayList<Integer>(Arrays.asList(4,3))),new RecVewService(5, "4 Wheeler Services",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "3 hour(s)", 5500, new ArrayList<Integer>(Arrays.asList(4)))))));
////        oldWorkshop.add(new RecVewWorkshop(9,"10 Star Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,3,1,new ArrayList<RecVewService>(Arrays.asList(new RecVewService(2, "Puncture Station",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Depends", -1, new ArrayList<Integer>(Arrays.asList(2,4,6)))))));
//
//
////        newWorkshop = new ArrayList<>();
////        newWorkshop.add(new RecVewWorkshop(0,"HiFi Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,5));
////        newWorkshop.add(new RecVewWorkshop(1,"Decent Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green));
////        newWorkshop.add(new RecVewWorkshop(2,"7 Star Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,1));
////        newWorkshop.add(new RecVewWorkshop(3,"Latest Car Services ", "9876543210","aservice@exmple.com","321-Street, Sec-7","Jaipur", "Rajasthan", R.drawable.ic_workshop_48_green,4));


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

        CollectionReference cReference = fStore.collection("Services").document(itemService.getID()).collection("Child Services");

        cReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Snackbar.make(RV_ChooseWorkshop, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    newService.clear();

//                    Log.d("TAG" , "RishabhJain + " + "Start");

                    for(QueryDocumentSnapshot document : value)
                    {

//                        Log.d("TAG" , "RishabhJain + " + document.toString());
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

//                                        String Categ = null;
//
//                                        if(itemCar.getWheelType() == 2)
//                                            Categ = "Bikes";
//                                        else if(itemCar.getWheelType() == 4)
//                                            Categ = "Cars";
//                                        else if(itemCar.getWheelType() == 6)
//                                            Categ = "Heavy";
//                                        else if(itemCar.getWheelType() == 3)
//                                            Categ = "Others";
//
////                        Log.d("TAG" , "RishabhJain + here" );

                                        RV_ChooseWorkshop_Placeholder_TV.setText("All Slots are currently Booked.\nPlease Try Again Later.");     // for " + Categ +" category
                                        RV_ChooseWorkshop_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ChooseWorkshop.setVisibility(View.INVISIBLE);


//                        Snackbar.make(v, "All Slots are currently Booked.\nPlease Try Again Later.", Snackbar.LENGTH_SHORT).show();
                                    }
                                    else if(newService.size() == 1)
                                    {
                                        RV_ChooseWorkshop_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_InitBook.setVisibility(View.VISIBLE);
                                    }

                                    Collections.sort(newService, new Comparator<RecVewService>() {
                                        @Override
                                        public int compare(RecVewService o1, RecVewService o2) {
                                            return Double.compare(o1.getPrice(), o2.getPrice());
                                        }
                                    });

                                    adapterRecVewWorkshops.notifyDataSetChanged();

//                                    Log.d("TAG" , "RishabhJain + " + newService.toString());

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


//                        Snackbar.make(findViewById(android.R.id.content), "This Service is not available for " + Categ +" category.", Snackbar.LENGTH_SHORT).show();
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

    private void initRecVew() {

        RV_ChooseWorkshop = findViewById(R.id.RV_ChooseWorkshop);
        layoutManager = new LinearLayoutManager(ChooseWorkshop.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_ChooseWorkshop.setLayoutManager(layoutManager);


        adapterRecVewWorkshops = new AdapterRecVewWorkshops(ChooseWorkshop.this,newService,itemCar);

        RV_ChooseWorkshop.setAdapter(adapterRecVewWorkshops);
        adapterRecVewWorkshops.notifyDataSetChanged();
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}