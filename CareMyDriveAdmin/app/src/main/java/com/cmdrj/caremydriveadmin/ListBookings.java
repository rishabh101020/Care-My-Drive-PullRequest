package com.cmdrj.caremydriveadmin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.cmdrj.caremydriveadmin.ui.bookings.BookingsFragment;
import com.cmdrj.caremydriveadmin.ui.entities.ClientsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.view.View;

import java.util.ArrayList;

public class ListBookings extends AppCompatActivity {

    String initBookings;
    String liveBookings;
    String compBookings;

//    boolean initBookingsDocument;
//    boolean liveBookingsDocument;
//    boolean compBookingDocuments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bookings);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarListBookings);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        initBookings = intent.getExtras().getString("initBookings");
        liveBookings = intent.getExtras().getString("liveBookings");
        compBookings = intent.getExtras().getString("compBookings");

//        initBookingsDocument = intent.getExtras().getBoolean("initBookingsDocument");
//        liveBookingsDocument = intent.getExtras().getBoolean("liveBookingsDocument");
//        compBookingDocuments = intent.getExtras().getBoolean("compBookingDocuments");


//        initBookings = intent.getExtras().getParcelableArrayList("initBookings");
//        liveBookings = intent.getExtras().getParcelableArrayList("liveBookings");
//        compBookings = intent.getExtras().getParcelableArrayList("compBookings");

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment




//        fragmentTransaction.replace(R.id.ListBookings_frameLayout, BookingsFragment.newInstance(initBookings, liveBookings, compBookings, initBookingsDocument, liveBookingsDocument, compBookingDocuments));
        fragmentTransaction.replace(R.id.ListBookings_frameLayout, BookingsFragment.newInstance(initBookings, liveBookings, compBookings));





        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        fragmentTransaction.commit();

    }
}