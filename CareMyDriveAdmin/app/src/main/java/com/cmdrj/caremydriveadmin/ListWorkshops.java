package com.cmdrj.caremydriveadmin;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.cmdrj.caremydriveadmin.ui.entities.ClientsFragment;
import com.cmdrj.caremydriveadmin.ui.entities.WorkshopsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class ListWorkshops extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_workshops);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarListWorkshops);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        fragmentTransaction.replace(R.id.ListWorkshops_frameLayout, WorkshopsFragment.newInstance());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        fragmentTransaction.commit();

    }

}