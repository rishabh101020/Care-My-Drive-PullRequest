package com.cmdrj.caremydrive;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.View;

public class ServicesCars extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentManager fragmentManager;
    AdapterFragmentsSerCars adapterFragmentsSerCars;

    RecVewCar item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_cars);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Intent intent = getIntent();
        item = intent.getExtras().getParcelable("item");


        Toolbar toolbar = findViewById(R.id.toolbarSerCars);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        toolbar.setTitle(item.getName());

        tabLayout = findViewById(R.id.SerCars_TL);
        viewPager2 = findViewById(R.id.SerCars_VP2);
        tabLayout.addTab(tabLayout.newTab().setText("Requested"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        fragmentManager = getSupportFragmentManager();



        adapterFragmentsSerCars = new AdapterFragmentsSerCars(fragmentManager,getLifecycle(),item);

//        Bundle bundle = new Bundle();
//        bundle.putString("item","idsssdsdeqqqqqqqqqqqqq");
//        bundle.putParcelable("item",item);

//        FragmentSerCarsInit fragmentSerCarsInit = new FragmentSerCarsInit();
//        fragmentSerCarsInit.setArguments(bundle);

//        getSupportFragmentManager().beginTransaction().add(R.id.id1, FragmentSerCarsInit.newInstance("data1"),"MyFragment").commit();





        viewPager2.setAdapter(adapterFragmentsSerCars);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


    }
}