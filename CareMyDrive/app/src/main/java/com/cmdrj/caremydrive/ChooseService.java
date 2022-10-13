package com.cmdrj.caremydrive;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.cmdrj.caremydrive.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooseService extends AppCompatActivity {

    GridView gridView;
    ArrayList<RecVewService> services;
    AdapterGridVewHomeServices adapter;

    LinearLayout GV_ChooseService_Placeholder_LL;
    TextView GV_ChooseService_Placeholder_TV;

    RecVewCar itemCar;

    private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbarChooseService);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();

        GV_ChooseService_Placeholder_LL = findViewById(R.id.GV_ChooseService_Placeholder_LL);
        GV_ChooseService_Placeholder_TV = findViewById(R.id.GV_ChooseService_Placeholder_TV);

        Intent intent = getIntent();
        itemCar = intent.getExtras().getParcelable("item");

        initDataForGridVew();
        initGridVew();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),serviceName[position], Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChooseService.this, ChooseWorkshop.class);
                intent.putExtra("itemService",services.get(position));
                intent.putExtra("itemCar",itemCar);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),services.get(position).getName(), Toast.LENGTH_SHORT).show();
                Snackbar.make(view,""  + services.get(position).getName(), Snackbar.LENGTH_SHORT).show();

                return true;
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void initDataForGridVew() {

        services = new ArrayList<>();


        CollectionReference cReference = fStore.collection("Services");

        cReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Snackbar.make(gridView, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    services.clear();

                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
//                    services.addAll(snapshotList);

                    for(RecVewService item : snapshotList)
                    {
                        if(item.getVisibility())
                            services.add(item);
                    }

                    if(services.size() == 0)
                    {

                        GV_ChooseService_Placeholder_TV.setText("All Services Slots Booked.");
                        GV_ChooseService_Placeholder_LL.setVisibility(View.VISIBLE);
//                    gridView.setVisibility(View.INVISIBLE);


//                        Snackbar.make(gridView, "All Services Slots Booked.", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        GV_ChooseService_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    gridView.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(services, new Comparator<RecVewService>() {
                        @Override
                        public int compare(RecVewService o1, RecVewService o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });


                    adapter.notifyDataSetChanged();

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }



    private void initGridVew() {

        gridView = findViewById(R.id.GV_ChooseService);
        adapter = new AdapterGridVewHomeServices(ChooseService.this,services);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}