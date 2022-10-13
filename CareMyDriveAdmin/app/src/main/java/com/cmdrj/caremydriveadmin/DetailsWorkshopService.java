package com.cmdrj.caremydriveadmin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailsWorkshopService extends AppCompatActivity {

    TextView DetWorkshopService_ServiceName, DetWorkshopService_ServicePrice, DetWorkshopService_Rating_TV_Det, DetWorkshopService_ServiceCategory_TV_Det, DetWorkshopService_PickUp_TV_Det, DetWorkshopService_EstTime_TV_Det, DetWorkshopService_Details_TV_Det;
    ImageView DetWorkshopService_IV;
    Button DetWorkshopService_Accept, DetWorkshopService_Reject;
    LinearLayout DetWorkshopService_Rating_LL;
    View DetWorkshopService_Rating_View;
    ProgressBar DetWorkshopService_PB;

    RecVewService itemService;
    RecVewWorkshop itemWorkshop;
    RecVewBookings itemBooking;
    int parentActivity;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_workshop_service);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarDetWorkshopService);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        DetWorkshopService_ServiceName = findViewById(R.id.DetWorkshopService_ServiceName);
        DetWorkshopService_ServicePrice = findViewById(R.id.DetWorkshopService_ServicePrice);
        DetWorkshopService_Rating_TV_Det= findViewById(R.id.DetWorkshopService_Rating_TV_Det);
        DetWorkshopService_ServiceCategory_TV_Det= findViewById(R.id.DetWorkshopService_ServiceCategory_TV_Det);
        DetWorkshopService_PickUp_TV_Det= findViewById(R.id.DetWorkshopService_PickUp_TV_Det);
        DetWorkshopService_EstTime_TV_Det = findViewById(R.id.DetWorkshopService_EstTime_TV_Det);
        DetWorkshopService_Details_TV_Det = findViewById(R.id.DetWorkshopService_Details_TV_Det);
//
        DetWorkshopService_Rating_LL = findViewById(R.id.DetWorkshopService_Rating_LL);

        DetWorkshopService_Rating_View = findViewById(R.id.DetWorkshopService_Rating_View);

        DetWorkshopService_IV = findViewById(R.id.DetWorkshopService_IV);

        DetWorkshopService_Accept = findViewById(R.id.DetWorkshopService_Accept);
        DetWorkshopService_Reject = findViewById(R.id.DetWorkshopService_Reject);


        DetWorkshopService_PB = findViewById(R.id.DetWorkshopService_PB);
        DetWorkshopService_PB.setVisibility(View.VISIBLE);

//        DetWorkshopService_ServiceName.setSelected(true);

        Intent intent = getIntent();
        itemService = intent.getExtras().getParcelable("itemService");
        parentActivity = intent.getExtras().getInt("parentActivity");

        if(parentActivity == 0)
        {
            DetWorkshopService_Accept.setVisibility(View.GONE);
        }
        else if(parentActivity ==1)
        {
            DetWorkshopService_Accept.setVisibility(View.VISIBLE);
            itemBooking = intent.getExtras().getParcelable("itemBooking");

        }



        DocumentReference dReference = itemService.getWorkShopRef().collection("Services").document(itemService.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetWorkshopService_ServiceName, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemService = value.toObject(RecVewService.class);


                    itemService.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);


                            if(parentActivity == 0)
                            {
                                if(itemService.getPic() == null)
                                {
                                    DetWorkshopService_IV.setImageResource(R.drawable.ic_cars_repair_70_green);

                                    DetWorkshopService_PB.setVisibility(View.GONE);
                                }
                                else
                                {
                                    StorageReference sReference = fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("Services").child(itemService.getID()).child("pic1.jpeg");

                                    sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                        @Override
                                        public void onSuccess(StorageMetadata storageMetadata) {

                                            sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {

                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                                    DetWorkshopService_IV.setImageBitmap(bitmap);
                                                    DetWorkshopService_PB.setVisibility(View.GONE);

                                                    DetWorkshopService_IV.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(DetailsWorkshopService.this, ViewImage.class);
                                                            intent.putExtra("path", fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("Services").child(itemService.getID()).child("pic1.jpeg").getPath());
                                                            startActivity(intent);
                                                        }
                                                    });


                                                }
                                            });
                                        }
                                    });
                                }
                            }
                            else if(parentActivity == 1)
                            {
                                if(itemService.getPic() == null)
                                {

                                    if(itemWorkshop.getPic() == null)
                                    {
                                        DetWorkshopService_IV.setImageResource(R.drawable.ic_cars_repair_70_green);

                                        DetWorkshopService_PB.setVisibility(View.GONE);
                                    }
                                    else
                                    {
                                        StorageReference sReference = fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("pic1.jpeg");

                                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                            @Override
                                            public void onSuccess(StorageMetadata storageMetadata) {

                                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                    @Override
                                                    public void onSuccess(byte[] bytes) {

                                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                                        DetWorkshopService_IV.setImageBitmap(bitmap);
                                                        DetWorkshopService_PB.setVisibility(View.GONE);

                                                        DetWorkshopService_IV.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                Intent intent = new Intent(DetailsWorkshopService.this, ViewImage.class);
                                                                intent.putExtra("path", fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("pic1.jpeg").getPath());
                                                                startActivity(intent);
                                                            }
                                                        });


                                                    }
                                                });
                                            }
                                        });
                                    }


                                }
                                else
                                {

                                    StorageReference sReference = fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("Services").child(itemService.getID()).child("pic1.jpeg");

                                    sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                        @Override
                                        public void onSuccess(StorageMetadata storageMetadata) {

                                            sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                                @Override
                                                public void onSuccess(byte[] bytes) {

                                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                                    DetWorkshopService_IV.setImageBitmap(bitmap);
                                                    DetWorkshopService_PB.setVisibility(View.GONE);

                                                    DetWorkshopService_IV.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(DetailsWorkshopService.this, ViewImage.class);
                                                            intent.putExtra("path", fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("Services").child(itemService.getID()).child("pic1.jpeg").getPath());
                                                            startActivity(intent);
                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    });
                                }
                            }
                        }
                    });







                    toolbar.setTitle(itemService.getName());


                    DetWorkshopService_ServiceName.setText(itemService.getName());

                    if(itemService.getPrice() != -1)
                        DetWorkshopService_ServicePrice.setText(Double.toString(itemService.getPrice()));

                    else
                        DetWorkshopService_ServicePrice.setText("Variable");


                    // TODO : complete while Rating
//        if(itemWorkshop.getRating() != -1)
//            DetWorkshopService_Rating_TV_Det.setText(Integer.toString(itemWorkshop.getRating()));
//        else
//        {
////            DetWorkshopService_Rating_Parent_LL.setWeightSum(2);
                    DetWorkshopService_Rating_LL.setVisibility(View.GONE);
                    DetWorkshopService_Rating_View.setVisibility(View.GONE);
//
////            DetWorkshopService_Rating_TV_Det.setText("Not Rated");
//        }

                    // TODO : over

                    String categ = "";
//        Collections.sort(itemService.getCategory());
                    for(int i:itemService.getCategory())
                    {
                        if(i == 2)
                            categ = categ + ", Bikes";

                        else if(i == 4)
                            categ = categ + ", Cars";

                        else if(i == 6)
                            categ = categ + ", Heavy";

                        else if(i == 3)
                            categ = categ + ", Others";
                    }

                    categ = categ.substring(2);
                    DetWorkshopService_ServiceCategory_TV_Det.setText(categ);


                    if(itemService.getPickUp() == 0)
                        DetWorkshopService_PickUp_TV_Det.setText("Unavailable");

                    else if(itemService.getPickUp() == 1)
                        DetWorkshopService_PickUp_TV_Det.setText("Available");



                    DetWorkshopService_EstTime_TV_Det.setText(itemService.getEstTime());

                    DetWorkshopService_Details_TV_Det.setText(itemService.getDetails());

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });


        DetWorkshopService_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DetWorkshopService_Accept.setEnabled(false);
                itemService.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        RecVewWorkshop workshop = documentSnapshot.toObject(RecVewWorkshop.class);

                        Map<String, Object> map = new HashMap<>();
                        map.put("workShopRef",itemService.getWorkShopRef());
                        map.put("serviceRef",fStore.collection("Workshops").document(workshop.getID()).collection("Services").document(itemService.getID()));
                        map.put("status", -3);
                        map.put("details",itemService.getDetails());
                        map.put("price",itemService.getPrice());

                        if(itemService.getPickUp() == 0)
                        {
                            map.put("pickUp", 0);
                            map.put("pickUpAddress", null);
                            map.put("pickUpCity", null);
                            map.put("pickUpCountry", null);
                            map.put("pickUpGeoPoint", null);
                            map.put("pickUpPincode", null);
                            map.put("pickUpState", null);
                        }
                        else
                        {

                            if(itemBooking.getPickUp() == 0)
                            {
                                map.put("pickUp", 0);
                                map.put("pickUpAddress", null);
                                map.put("pickUpCity", null);
                                map.put("pickUpCountry", null);
                                map.put("pickUpGeoPoint", null);
                                map.put("pickUpPincode", null);
                                map.put("pickUpState", null);
                            }

                        }

                        fStore.collection("Bookings").document(itemBooking.getID()).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                finish();

                            }
                        });
                    }
                });

            }
        });

        DetWorkshopService_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsWorkshopService.this, ListBookings.class);
                intent.putExtra("initBookings", itemService.getWorkShopRef().collection("Services").document(itemService.getID()).collection("requestedBookings").getPath());
                intent.putExtra("liveBookings", itemService.getWorkShopRef().collection("Services").document(itemService.getID()).collection("acceptedBookings").getPath());
                intent.putExtra("compBookings", itemService.getWorkShopRef().collection("Services").document(itemService.getID()).collection("completedBookings").getPath());
                startActivity(intent);

            }
        });
    }
}