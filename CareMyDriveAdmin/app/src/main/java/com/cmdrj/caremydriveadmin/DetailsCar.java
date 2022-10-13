package com.cmdrj.caremydriveadmin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

public class DetailsCar extends AppCompatActivity {

    TextView DetCar_CarName, DetCar_CarNo, DetCar_Model_TV_Det, DetCar_Manufacturer_TV_Det, DetCar_FuelType_TV_Det, DetCar_Category_TV_Det, DetCar_Details_TV_Det;
    ImageView DetCar_IV;
    Button DetCar_Reject;
    LinearLayout DetCar_Details_LL;
    View DetCar_Details_V;
    ProgressBar DetCar_PB;


    RecVewCar itemCar;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_car);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarDetCar);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        DetCar_CarName = findViewById(R.id.DetCar_CarName);
        DetCar_CarNo= findViewById(R.id.DetCar_CarNo);
        DetCar_Model_TV_Det= findViewById(R.id.DetCar_Model_TV_Det);
        DetCar_Manufacturer_TV_Det= findViewById(R.id.DetCar_Manufacturer_TV_Det);
        DetCar_FuelType_TV_Det= findViewById(R.id.DetCar_FuelType_TV_Det);
        DetCar_Category_TV_Det = findViewById(R.id.DetCar_Category_TV_Det);
        DetCar_Details_TV_Det = findViewById(R.id.DetCar_Details_TV_Det);

        DetCar_IV = findViewById(R.id.DetCar_IV);

        DetCar_Details_LL = findViewById(R.id.DetCar_Details_LL);

        DetCar_Details_V = findViewById(R.id.DetCar_Details_V);

        DetCar_Reject = findViewById(R.id.DetCar_Reject);
//        DetCar_Accept = findViewById(R.id.DetCar_Accept);

        DetCar_PB = findViewById(R.id.DetCar_PB);
        DetCar_PB.setVisibility(View.VISIBLE);

        DetCar_CarName.setSelected(true);
        DetCar_Model_TV_Det.setSelected(true);
        DetCar_Manufacturer_TV_Det.setSelected(true);
        DetCar_FuelType_TV_Det.setSelected(true);

        Intent intent = getIntent();
        itemCar = intent.getExtras().getParcelable("item");












        DocumentReference dReference = fStore.collection("Vehicles").document(itemCar.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetCar_CarName, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemCar = value.toObject(RecVewCar.class);


                    if(itemCar.getPic() == null)
                    {
                        if (itemCar.getWheelType() == 2)
                            DetCar_IV.setImageResource(R.drawable.ic_bike_70_green);
                        else if (itemCar.getWheelType() == 4)
                            DetCar_IV.setImageResource(R.drawable.ic_cars_70_green);
                        else if (itemCar.getWheelType() == 6)
                            DetCar_IV.setImageResource(R.drawable.ic_heavy_70_green);
                        else if (itemCar.getWheelType() == 3)
                            DetCar_IV.setImageResource(R.drawable.ic_others_70_green);

                        DetCar_PB.setVisibility(View.GONE);

                    }
                    else
                    {

                        StorageReference sReference = fStorage.getReference().child("Vehicles").child(itemCar.getID()).child("pic1.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        DetCar_IV.setImageBitmap(bitmap);
                                        DetCar_PB.setVisibility(View.GONE);

                                        DetCar_IV.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(DetailsCar.this, ViewImage.class);
                                                intent.putExtra("path", fStorage.getReference().child("Vehicles").child(itemCar.getID()).child("pic1.jpeg").getPath());
                                                startActivity(intent);
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }


                    toolbar.setTitle(itemCar.getModel());


                    DetCar_CarName.setText(itemCar.getName());
                    DetCar_CarNo.setText(itemCar.getNo());
                    DetCar_Model_TV_Det.setText(itemCar.getModel());
                    DetCar_Manufacturer_TV_Det.setText(itemCar.getManufacturer());

                    if(itemCar.getFuelType() == 0)
                        DetCar_FuelType_TV_Det.setText("Petrol");

                    else if(itemCar.getFuelType() == 1)
                        DetCar_FuelType_TV_Det.setText("Diesel");

                    else if(itemCar.getFuelType() == 2)
                        DetCar_FuelType_TV_Det.setText("Electric");

                    else if(itemCar.getFuelType() == 4)
                        DetCar_FuelType_TV_Det.setText("CNG");



                    if(itemCar.getWheelType() == 2)
                        DetCar_Category_TV_Det.setText("Bikes");

                    else if(itemCar.getWheelType() == 4)
                        DetCar_Category_TV_Det.setText("Cars");

                    else if(itemCar.getWheelType() == 6)
                        DetCar_Category_TV_Det.setText("Heavy");

                    else if(itemCar.getWheelType() == 3)
                        DetCar_Category_TV_Det.setText("others");


                    if(itemCar.getDetails() != null && !itemCar.getDetails().trim().equals(""))
                    {
                        DetCar_Details_LL.setVisibility(View.VISIBLE);
                        DetCar_Details_V.setVisibility(View.VISIBLE);
                        DetCar_Details_TV_Det.setText(itemCar.getDetails());
                    }
                    else
                    {
                        DetCar_Details_LL.setVisibility(View.GONE);
                        DetCar_Details_V.setVisibility(View.GONE);
                    }

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        DetCar_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DetailsCar.this, ListBookings.class);
                intent.putExtra("initBookings", fStore.collection("Vehicles").document(itemCar.getID()).collection("requestedBookings").getPath());
                intent.putExtra("liveBookings", fStore.collection("Vehicles").document(itemCar.getID()).collection("acceptedBookings").getPath());
                intent.putExtra("compBookings", fStore.collection("Vehicles").document(itemCar.getID()).collection("completedBookings").getPath());
                startActivity(intent);

            }
        });

//        DetCar_Accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Intent intent = new Intent(DetailsCar.this,ServicesCars.class);
////                intent.putExtra("itemCar",itemCar);
////                startActivity(intent);
//
//            }
//        });


    }
}