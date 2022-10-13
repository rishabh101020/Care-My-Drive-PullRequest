package com.cmdrj.caremydriveadmin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CarsClient extends AppCompatActivity {

    RecyclerView RV_CarsClient;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewCar> newCars = new ArrayList<>();
    AdapterRecVewCars adapterRecVewCars;

    RecVewClient itemClient;
    private FirebaseFirestore fStore;

    CardView Cars_CV1, Cars_CV2;
    TextView Cars_TV_All, Cars_TV_Bikes, Cars_TV_Cars, Cars_TV_Heavy, Cars_TV_Others, RV_CarsClient_Placeholder_TV;

    LinearLayout RV_CarsClient_Placeholder_LL;

    ArrayList<RecVewCar> all = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars_client);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarCarsClient);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();

        Cars_CV1 = findViewById(R.id.Cars_CV1);
        Cars_CV2 = findViewById(R.id.Cars_CV2);

        Cars_TV_All = findViewById(R.id.Cars_TV_All);
        Cars_TV_Bikes = findViewById(R.id.Cars_TV_Bikes);
        Cars_TV_Cars = findViewById(R.id.Cars_TV_Cars);
        Cars_TV_Heavy = findViewById(R.id.Cars_TV_Heavy);
        Cars_TV_Others = findViewById(R.id.Cars_TV_Others);

        RV_CarsClient_Placeholder_TV = findViewById(R.id.RV_CarsClient_Placeholder_TV);
        RV_CarsClient_Placeholder_LL = findViewById(R.id.RV_CarsClient_Placeholder_LL);



        Intent intent = getIntent();
        itemClient = intent.getExtras().getParcelable("itemClient");



        DocumentReference dReference = fStore.collection("Clients").document(itemClient.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(Cars_TV_All, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemClient = value.toObject(RecVewClient.class);

                    toolbar.setTitle(itemClient.getName());

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });






        initDataForRecVew();
        initRecVew();

//        for(RecVewCar item:newCars)
//            all.add(item);

        Cars_TV_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_All.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(getResources().getColor(R.color.black));

                newCars.clear();
                for(RecVewCar item:all)
                {
                    newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_CarsClient_Placeholder_TV.setText("No Vehicles Enrolled.");
                    RV_CarsClient_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_CarsClient.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_CarsClient_Placeholder_LL.setVisibility(View.GONE);
//                    RV_CarsClient.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Bikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(getResources().getColor(R.color.black));

                newCars.clear();
                for(RecVewCar item:all)
                {
                    if(item.getWheelType() == 2)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_CarsClient_Placeholder_TV.setText("No Bikes Enrolled.");
                    RV_CarsClient_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_CarsClient.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_CarsClient_Placeholder_LL.setVisibility(View.GONE);
//                    RV_CarsClient.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(getResources().getColor(R.color.black));

//                newCars = cars;

                newCars.clear();
                for(RecVewCar item:all)
                {
                    if(item.getWheelType() == 4)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_CarsClient_Placeholder_TV.setText("No Cars Enrolled.");
                    RV_CarsClient_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_CarsClient.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_CarsClient_Placeholder_LL.setVisibility(View.GONE);
//                    RV_CarsClient.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Heavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(getResources().getColor(R.color.black));

//                newCars = heavy;

                newCars.clear();
                for(RecVewCar item:all)
                {
                    if(item.getWheelType() == 6)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_CarsClient_Placeholder_TV.setText("No Heavy Vehicles Enrolled.");
                    RV_CarsClient_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_CarsClient.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_CarsClient_Placeholder_LL.setVisibility(View.GONE);
//                    RV_CarsClient.setVisibility(View.VISIBLE);
                }


                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Others.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));

//                newCars = Others;
                newCars.clear();
                for(RecVewCar item:all)
                {
                    if(item.getWheelType() == 3)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_CarsClient_Placeholder_TV.setText("No Vehicles Enrolled.");
                    RV_CarsClient_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_CarsClient.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_CarsClient_Placeholder_LL.setVisibility(View.GONE);
//                    RV_CarsClient.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapterRecVewCars.notifyDataSetChanged();
            }
        });
    }

    private void initDataForRecVew() {


        CollectionReference cReference = fStore.collection("Vehicles");
        DocumentReference dReference = fStore.collection("Clients").document(itemClient.getID());

        cReference.whereEqualTo("clientRef",dReference).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Snackbar.make(Cars_TV_All, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    all.clear();
                    newCars.clear();
//                    adapterRecVewCars.notifyDataSetChanged();


                    List<RecVewCar> snapshotList = value.toObjects(RecVewCar.class);
                    all.addAll(snapshotList);

//                    Cars_TV_All.performClick();

                    newCars.addAll(all);

                    if(newCars.size() == 0)
                    {
                        RV_CarsClient_Placeholder_TV.setText("No Vehicles Enrolled.");
                        RV_CarsClient_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_CarsClient.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        RV_CarsClient_Placeholder_LL.setVisibility(View.GONE);
//                    RV_CarsClient.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
//
                    adapterRecVewCars.notifyDataSetChanged();

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void initRecVew() {

        RV_CarsClient = findViewById(R.id.RV_CarsClient);
        layoutManager = new LinearLayoutManager(CarsClient.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_CarsClient.setLayoutManager(layoutManager);

        adapterRecVewCars = new AdapterRecVewCars(newCars, Cars_CV1.getBackground(), Cars_CV2.getBackground());

        RV_CarsClient.setAdapter(adapterRecVewCars);
        adapterRecVewCars.notifyDataSetChanged();
    }
}