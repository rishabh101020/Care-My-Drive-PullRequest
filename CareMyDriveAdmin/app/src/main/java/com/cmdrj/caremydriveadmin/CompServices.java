package com.cmdrj.caremydriveadmin;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class CompServices extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_services);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarCompSer);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.CompSer_frameLayout, new FragmentCompleted()  /*FragmentCompleted.newInstance(null)*/);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        fragmentTransaction.commit();

    }

}
















/*
package com.cmdrj.caremydriveadmin;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CompServices extends AppCompatActivity {

    RecyclerView RV_CompSer;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> compBookings;
    AdapterRecVewBookings adapterRecVewBookings;

    private FirebaseFirestore fStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_services);
        
        Toolbar toolbar = findViewById(R.id.toolbarCompSer);
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

        compBookings = new ArrayList<>();


        CollectionReference cReference = fStore.collection("Bookings");

        cReference.whereEqualTo("status",3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Snackbar.make(findViewById(android.R.id.content), "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    compBookings.clear();

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    compBookings.addAll(snapshotList);

                    if(compBookings.size() == 0)
                        Snackbar.make(findViewById(android.R.id.content), "No Completed Bookings.", Snackbar.LENGTH_SHORT).show();

                    adapterRecVewBookings.notifyDataSetChanged();

                } else {
//                    Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void initRecVew() {

        RV_CompSer = findViewById(R.id.RV_CompSer);
        layoutManager = new LinearLayoutManager(CompServices.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_CompSer.setLayoutManager(layoutManager);

        adapterRecVewBookings = new AdapterRecVewBookings(compBookings);

        RV_CompSer.setAdapter(adapterRecVewBookings);
        adapterRecVewBookings.notifyDataSetChanged();
    }
}*/
