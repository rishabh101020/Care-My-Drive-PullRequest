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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
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

public class ServicesWorkshop extends AppCompatActivity {

    RecyclerView RV_SerWorkshop;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewService> newServices = new ArrayList<>();;
    AdapterRecVewServices adapterRecVewServices;

    RecVewWorkshop itemWorkshop;
    private FirebaseFirestore fStore;

    LinearLayout RV_SerWorkshop_Placeholder_LL;
    TextView RV_SerWorkshop_Placeholder_TV;

    CardView Services_CV1, Services_CV2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_workshop);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarSerWorkshop);
//        setSupportActionBar(toolbar);
        
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();


        Services_CV1 = findViewById(R.id.Services_CV1);
        Services_CV2 = findViewById(R.id.Services_CV2);

        RV_SerWorkshop_Placeholder_LL = findViewById(R.id.RV_SerWorkshop_Placeholder_LL);
        RV_SerWorkshop_Placeholder_TV = findViewById(R.id.RV_SerWorkshop_Placeholder_TV);


        Intent intent = getIntent();
        itemWorkshop = intent.getExtras().getParcelable("itemWorkshop");


        DocumentReference dReference = fStore.collection("Workshops").document(itemWorkshop.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(RV_SerWorkshop, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemWorkshop = value.toObject(RecVewWorkshop.class);

                    toolbar.setTitle(itemWorkshop.getName());

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

//        newServices = itemWorkshop.getServicesProvided();



        CollectionReference cReference = fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services");

        cReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Snackbar.make(RV_SerWorkshop, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    newServices.clear();

                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
                    newServices.addAll(snapshotList);

                    if(newServices.size() == 0)
                    {

                        RV_SerWorkshop_Placeholder_TV.setText("No Services Enrolled.");
                        RV_SerWorkshop_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_SerWorkshop.setVisibility(View.INVISIBLE);


                    }
                    else
                    {
                        RV_SerWorkshop_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_SerWorkshop.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(newServices, new Comparator<RecVewService>() {
                        @Override
                        public int compare(RecVewService o1, RecVewService o2) {
                            return Double.compare(o1.getPrice(), o2.getPrice());
                        }
                    });


                    adapterRecVewServices.notifyDataSetChanged();

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initRecVew() {

        RV_SerWorkshop = findViewById(R.id.RV_SerWorkshop);
        layoutManager = new LinearLayoutManager(ServicesWorkshop.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_SerWorkshop.setLayoutManager(layoutManager);


        adapterRecVewServices = new AdapterRecVewServices(newServices, Services_CV1.getBackground(), Services_CV2.getBackground());

        RV_SerWorkshop.setAdapter(adapterRecVewServices);
        adapterRecVewServices.notifyDataSetChanged();
    }
}