package com.cmdrj.caremydrive;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DetailsCars extends AppCompatActivity {

    TextView DetCars_CarName, DetCars_CarNo, DetCars_Model_TV_Det, DetCars_Manufacturer_TV_Det, DetCars_FuelType_TV_Det, DetCars_Category_TV_Det, DetCars_Details_TV_Det;
    ImageView DetCars_Model_IV, DetCars_Manufacturer_IV, DetCars_FuelType_IV, DetCars_IV, DetCars_Category_IV, DetCars_Details_IV;
    LinearLayout DetCars_Details_LL;
    View DetCars_Details_V;
    Button DetCars_VewDet, DetCars_Contact;
    FloatingActionButton DetCars_fab;
    ProgressBar DetCars_PB;

    RecVewCar item;
    RecVewService itemService;
    int parentActivity;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_cars);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarDetCars);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();



        DetCars_CarName = findViewById(R.id.DetCars_CarName);
        DetCars_CarNo= findViewById(R.id.DetCars_CarNo);
        DetCars_Model_TV_Det= findViewById(R.id.DetCars_Model_TV_Det);
        DetCars_Manufacturer_TV_Det= findViewById(R.id.DetCars_Manufacturer_TV_Det);
        DetCars_FuelType_TV_Det= findViewById(R.id.DetCars_FuelType_TV_Det);
        DetCars_Category_TV_Det= findViewById(R.id.DetCars_Category_TV_Det);
        DetCars_Details_TV_Det= findViewById(R.id.DetCars_Details_TV_Det);

        DetCars_Model_IV = findViewById(R.id.DetCars_Model_IV);
        DetCars_Manufacturer_IV = findViewById(R.id.DetCars_Manufacturer_IV);
        DetCars_FuelType_IV = findViewById(R.id.DetCars_FuelType_IV);
        DetCars_IV = findViewById(R.id.DetCars_IV);
        DetCars_Category_IV = findViewById(R.id.DetCars_Category_IV);
        DetCars_Details_IV = findViewById(R.id.DetCars_Details_IV);

        DetCars_Details_LL = findViewById(R.id.DetCars_Details_LL);
        DetCars_Details_V = findViewById(R.id.DetCars_Details_V);


        DetCars_VewDet = findViewById(R.id.DetCars_VewDet);
        DetCars_Contact = findViewById(R.id.DetCars_Contact);

        DetCars_fab = findViewById(R.id.DetCars_fab);

        DetCars_PB = findViewById(R.id.DetCars_PB);
        DetCars_PB.setVisibility(View.VISIBLE);


        DetCars_CarName.setSelected(true);
        DetCars_Model_TV_Det.setSelected(true);
        DetCars_Manufacturer_TV_Det.setSelected(true);
        DetCars_FuelType_TV_Det.setSelected(true);
        DetCars_Category_TV_Det.setSelected(true);


        Intent intent = getIntent();
        item = intent.getExtras().getParcelable("item");
        itemService = intent.getExtras().getParcelable("itemService");
        parentActivity = intent.getExtras().getInt("parentActivity");



        DocumentReference dReference = fStore.collection("Vehicles").document(item.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    DetCars_PB.setVisibility(View.GONE);

                    Snackbar.make(DetCars_CarName, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    item = value.toObject(RecVewCar.class);

                    toolbar.setTitle(item.getName());

                    if(item.getPic() == null)
                    {
                        if (item.getWheelType() == 2)
                            DetCars_IV.setImageResource(R.drawable.ic_bike_70_green);
                        else if (item.getWheelType() == 4)
                            DetCars_IV.setImageResource(R.drawable.ic_cars_70_green);
                        else if (item.getWheelType() == 6)
                            DetCars_IV.setImageResource(R.drawable.ic_heavy_70_green);
                        else if (item.getWheelType() == 3)
                            DetCars_IV.setImageResource(R.drawable.ic_others_70_green);

                        DetCars_PB.setVisibility(View.GONE);

                    }
                    else
                    {

                        StorageReference sReference = fStorage.getReference().child("Vehicles").child(item.getID()).child("pic1.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        DetCars_IV.setImageBitmap(bitmap);
                                        DetCars_PB.setVisibility(View.GONE);


                                        DetCars_IV.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(DetailsCars.this, ViewImage.class);
                                                intent.putExtra("path", fStorage.getReference().child("Vehicles").child(item.getID()).child("pic1.jpeg").getPath());
                                                startActivity(intent);
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }

                    DetCars_CarName.setText(item.getName());
                    DetCars_CarNo.setText(item.getNo());
                    DetCars_Model_TV_Det.setText(item.getModel());
                    DetCars_Manufacturer_TV_Det.setText(item.getManufacturer());

                    if(item.getFuelType() == 0)
                        DetCars_FuelType_TV_Det.setText("Petrol");

                    else if(item.getFuelType() == 1)
                        DetCars_FuelType_TV_Det.setText("Diesel");

                    else if(item.getFuelType() == 2)
                        DetCars_FuelType_TV_Det.setText("Electric");

                    else if(item.getFuelType() == 3)
                        DetCars_FuelType_TV_Det.setText("CNG");




                    if(item.getWheelType() == 2)
                        DetCars_Category_TV_Det.setText("Bikes");

                    else if(item.getWheelType() == 4)
                        DetCars_Category_TV_Det.setText("Cars");

                    else if(item.getWheelType() == 6)
                        DetCars_Category_TV_Det.setText("Heavy");

                    else if(item.getWheelType() == 3)
                        DetCars_Category_TV_Det.setText("others");


                    if(item.getDetails() != null && !item.getDetails().trim().equals(""))
                    {
                        DetCars_Details_LL.setVisibility(View.VISIBLE);
                        DetCars_Details_V.setVisibility(View.VISIBLE);
                        DetCars_Details_TV_Det.setText(item.getDetails());
                    }
                    else
                    {
                        DetCars_Details_LL.setVisibility(View.GONE);
                        DetCars_Details_V.setVisibility(View.GONE);
                    }

                }
                else {
                    DetCars_PB.setVisibility(View.GONE);

//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });


//        toolbar.setTitle(item.getName());
//
//        DetCars_CarName.setText(item.getName());
//        DetCars_CarNo.setText(item.getNo());
//        DetCars_Model_TV_Det.setText(item.getModel());
//        DetCars_Manufacturer_TV_Det.setText(item.getManufacturer());
//
//        if(item.getFuelType() == 0)
//            DetCars_FuelType_TV_Det.setText("Petrol");
//
//        else if(item.getFuelType() == 1)
//            DetCars_FuelType_TV_Det.setText("Diesel");
//
//        else if(item.getFuelType() == 2)
//            DetCars_FuelType_TV_Det.setText("Electric");
//
//        else if(item.getFuelType() == 3)
//            DetCars_FuelType_TV_Det.setText("CNG");
//
//
//
//
//        if(item.getWheelType() == 2)
//            DetCars_Category_TV_Det.setText("Bikes");
//
//        else if(item.getWheelType() == 4)
//            DetCars_Category_TV_Det.setText("Cars");
//
//        else if(item.getWheelType() == 6)
//            DetCars_Category_TV_Det.setText("Heavy");
//
//        else if(item.getWheelType() == 3)
//            DetCars_Category_TV_Det.setText("others");
//
//
//        if(item.getDetails() != null && !item.getDetails().trim().equals(""))
//        {
//            DetCars_Details_LL.setVisibility(View.VISIBLE);
//            DetCars_Details_V.setVisibility(View.VISIBLE);
//            DetCars_Details_TV_Det.setText(item.getDetails());
//        }
//        else
//        {
//            DetCars_Details_LL.setVisibility(View.GONE);
//            DetCars_Details_V.setVisibility(View.GONE);
//        }




////        Log.d("TAG", "Hre "+ item.getWheelType() + " " + item.getFuelType() + ' ' + R.drawable.ic_bike_48 + ' ' + R.drawable.ic_cars_logo_48);
//
////        if(item.getWheelType() == 4)
////        {
////            DetCars_Model_IV.setImageResource(R.drawable.ic_cars_logo_70);
////            DetCars_Manufacturer_IV.setImageResource(R.drawable.ic_cars_logo_70);
////            DetCars_FuelType_IV.setImageResource(R.drawable.ic_cars_logo_70);
////            DetCars_IV.setImageResource(R.drawable.ic_cars_logo_70);
////
////        }
////
////        else if(item.getWheelType() == 2)
////        {
////            DetCars_Model_IV.setImageResource(R.drawable.ic_bike_48);
////            DetCars_Manufacturer_IV.setImageResource(R.drawable.ic_bike_48);
////            DetCars_FuelType_IV.setImageResource(R.drawable.ic_bike_48);
////            DetCars_IV.setImageResource(R.drawable.ic_bike_48);
////
////        }

        DetCars_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO : start ActivityForResult() wala finish
                Intent intent = new Intent(DetailsCars.this, EditDetCar.class);
                intent.putExtra("itemCar",  item);
                startActivity(intent);

                //Todo : over
            }
        });


        DetCars_VewDet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsCars.this,ServicesCars.class);
                intent.putExtra("item",item);
                startActivity(intent);

            }
        });


        if(parentActivity == 1)
        {
            DetCars_Contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(DetailsCars.this, ChooseWorkshop.class);
                    intent.putExtra("itemService",itemService);
                    intent.putExtra("itemCar",item);
                    startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }

        else if(parentActivity == 0)
        {
            DetCars_Contact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(DetailsCars.this,ChooseService.class);
                    intent.putExtra("item",item);
                    v.getContext().startActivity(intent);

                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            });
        }


//
//        DetCars_Contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(DetailsCars.this,ServicesCars.class);
//                intent.putExtra("item",item);
//                startActivity(intent);
//
//            }
//        });

    }
}