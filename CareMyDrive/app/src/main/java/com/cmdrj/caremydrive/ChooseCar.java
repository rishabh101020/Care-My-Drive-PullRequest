package com.cmdrj.caremydrive;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.cmdrj.caremydrive.ui.cars.CarsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;

public class ChooseCar extends AppCompatActivity {

//    RecVewWorkshop workshop;
    RecVewService service;
    int parentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_car);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbarChooseCar);
//        setSupportActionBar(toolbar);

        toolbar.setTitle("Choose Vehicle");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Intent intent = getIntent();
//        workshop = intent.getExtras().getParcelable("itemWorkshop");
        service = intent.getExtras().getParcelable("itemService");
        parentActivity = intent.getExtras().getInt("parentActivity");


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.choose_car_frameLayout, CarsFragment.newInstance(parentActivity, service));
            // or ft.add(R.id.your_placeholder, new FooFragment());
            // Complete the changes added above
        fragmentTransaction.commit();



//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}