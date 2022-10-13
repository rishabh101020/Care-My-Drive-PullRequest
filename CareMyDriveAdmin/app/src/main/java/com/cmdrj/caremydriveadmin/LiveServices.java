package com.cmdrj.caremydriveadmin;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class LiveServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_services);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarLiveSer);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.LiveSer_frameLayout, new FragmentLive()  /*FragmentLive.newInstance(null)*/);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        fragmentTransaction.commit();

    }

}


















/*
package com.cmdrj.caremydriveadmin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class LiveServices extends AppCompatActivity {

    RecyclerView RV_LiveSer;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> liveBookings;
    AdapterRecVewBookings adapterRecVewBookings;

    private FirebaseFirestore fStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_services);

        Toolbar toolbar = findViewById(R.id.toolbarLiveSer);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();

        initDataForRecVew();
        initRecVew();

    }

    private void initDataForRecVew() {

        liveBookings = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Bookings");

        cReference.whereIn("status", Arrays.asList(1,2)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Snackbar.make(findViewById(android.R.id.content), "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    liveBookings.clear();

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    liveBookings.addAll(snapshotList);

                    if(liveBookings.size() == 0)
                        Snackbar.make(findViewById(android.R.id.content), "No Accepted or Started Bookings.", Snackbar.LENGTH_SHORT).show();

                    adapterRecVewBookings.notifyDataSetChanged();

                } else {
//                    Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void initRecVew() {

        RV_LiveSer = findViewById(R.id.RV_LiveSer);
        layoutManager = new LinearLayoutManager(LiveServices.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_LiveSer.setLayoutManager(layoutManager);

        adapterRecVewBookings = new AdapterRecVewBookings(liveBookings);

        RV_LiveSer.setAdapter(adapterRecVewBookings);
        adapterRecVewBookings.notifyDataSetChanged();
    }
}*/
