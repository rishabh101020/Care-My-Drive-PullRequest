package com.cmdrj.caremydrive;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class DetailsService extends AppCompatActivity {

    TextView DetService_ServiceName, DetService_ServicePrice, DetService_Workshop_TV_Det, DetService_Rating_TV_Det, DetService_ServiceCategory_TV_Det, DetService_Address_TV_Det, DetService_PickUp_TV_Det, DetService_EstTime_TV_Det, DetService_Details_TV_Det, DetService_No_TV_Det;
    ImageView DetService_IV;
    Button DetService_VewDet, DetService_Contact;
    LinearLayout DetService_Rating_LL;
    View DetService_Rating_View;
    ProgressBar DetService_PB;
    FloatingActionButton DetService_images;

    RecVewWorkshop itemWorkshop;
    RecVewService itemService;
    RecVewCar itemCar;
    RecVewBookings itemBooking = null;
    int parentActivity;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_service);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = findViewById(R.id.toolbarDetService);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        DetService_ServiceName = findViewById(R.id.DetService_ServiceName);
        DetService_ServicePrice = findViewById(R.id.DetService_ServicePrice);
        DetService_Workshop_TV_Det= findViewById(R.id.DetService_Workshop_TV_Det);
        DetService_Rating_TV_Det= findViewById(R.id.DetService_Rating_TV_Det);
        DetService_ServiceCategory_TV_Det = findViewById(R.id.DetService_ServiceCategory_TV_Det);
        DetService_Address_TV_Det= findViewById(R.id.DetService_Address_TV_Det);
        DetService_PickUp_TV_Det= findViewById(R.id.DetService_PickUp_TV_Det);
        DetService_EstTime_TV_Det = findViewById(R.id.DetService_EstTime_TV_Det);
        DetService_Details_TV_Det = findViewById(R.id.DetService_Details_TV_Det);
        DetService_No_TV_Det = findViewById(R.id.DetService_No_TV_Det);

        DetService_VewDet = findViewById(R.id.DetService_VewDet);
        DetService_Contact = findViewById(R.id.DetService_Contact);

        DetService_images = findViewById(R.id.DetService_images);

        DetService_Rating_LL = findViewById(R.id.DetService_Rating_LL);

        DetService_Rating_View = findViewById(R.id.DetService_Rating_View);

        DetService_IV = findViewById(R.id.DetService_IV);
        DetService_PB = findViewById(R.id.DetService_PB);

        DetService_PB.setVisibility(View.VISIBLE);


        DetService_ServiceName.setSelected(true);

        Intent intent = getIntent();
        itemService = intent.getExtras().getParcelable("itemService");
        parentActivity = intent.getExtras().getInt("parentActivity");

        if(parentActivity == 0)
        {
//            itemWorkshop = intent.getExtras().getParcelable("itemWorkshop");
            itemCar = intent.getExtras().getParcelable("itemCar");
        }
        else if(parentActivity == 1)
        {
            itemBooking = intent.getExtras().getParcelable("itemBooking");
        }


        itemService.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);

                DocumentReference dReference = fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID());
                dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            Snackbar.make(DetService_ServiceName, "" + error, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {

                            itemService = value.toObject(RecVewService.class);












                            if(itemService.getPic() == null)
                            {

                                if(itemWorkshop.getPic() == null)
                                {
                                    DetService_IV.setImageResource(R.drawable.ic_cars_repair_70_green);

                                    DetService_PB.setVisibility(View.GONE);
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

                                                    DetService_IV.setImageBitmap(bitmap);
                                                    DetService_PB.setVisibility(View.GONE);

                                                    DetService_IV.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            Intent intent = new Intent(DetailsService.this, ViewImage.class);
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

                                                DetService_IV.setImageBitmap(bitmap);
                                                DetService_PB.setVisibility(View.GONE);

                                                DetService_IV.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        Intent intent = new Intent(DetailsService.this, ViewImage.class);
                                                        intent.putExtra("path", fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("Services").child(itemService.getID()).child("pic1.jpeg").getPath());
                                                        startActivity(intent);
                                                    }
                                                });


                                            }
                                        });
                                    }
                                });
                            }








                            DetService_Workshop_TV_Det.setText(itemWorkshop.getName());

                            if(itemWorkshop.getRating() != -1)
                                DetService_Rating_TV_Det.setText(Integer.toString(itemWorkshop.getRating()));
                            else
                            {
                                DetService_Rating_LL.setVisibility(View.GONE);
                                DetService_Rating_View.setVisibility(View.GONE);

                            }

                            DetService_Address_TV_Det.setText(itemWorkshop.getAddress() + ", " + itemWorkshop.getCity() + ", " + itemWorkshop.getState() + ", PinCode: " + itemWorkshop.getPincode() + ", " + itemWorkshop.getCountry());
                            DetService_No_TV_Det.setText(itemWorkshop.getMbNo());

                            DetService_Contact.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if(itemBooking == null)
                                    {
                                        Intent intent = new Intent(DetailsService.this, ConfirmBooking.class);
                                        intent.putExtra("itemWorkshop", itemWorkshop);
                                        intent.putExtra("itemService",  itemService);
                                        intent.putExtra("itemCar",  itemCar);
                                        startActivity(intent);

                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                    }
                                    else
                                    {
                                        DetService_Contact.setEnabled(false);


                                        Map<String, Object> map1 = new HashMap<>();
                                        map1.put("id", itemBooking.getID());
                                        map1.put("ref", fStore.collection("Bookings").document(itemBooking.getID()));


                                        Map<String, Object> map = new HashMap<>();
                                        map.put("workShopRef",itemService.getWorkShopRef());
                                        map.put("serviceRef",fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID()));
                                        map.put("status", 0);
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


                                        fStore.collection("Workshops").document(itemWorkshop.getID()).collection("requestedBookings").document(itemBooking.getID()).set(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                fStore.collection("Workshops").document(itemWorkshop.getID()).collection("Services").document(itemService.getID()).collection("requestedBookings").document(itemBooking.getID()).set(map1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

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
                                    }
                                }
                            });

                            DetService_VewDet.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    new AlertDialog.Builder(DetailsService.this)
                                            .setTitle("Choose Contact Option")
                                            .setMessage("Choose an application to contact Workshop")
                                            .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemWorkshop.getEmail(), null));
                                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                                    }

                                                }
                                            })
                                            .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemWorkshop.getMbNo(), null));
                                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                                        startActivity(phoneIntent);
                                                    }

                                                }
                                            })
                                            .create().show();
                                }
                            });


                            toolbar.setTitle(itemService.getName());

                            DetService_ServiceName.setText(itemService.getName());


                            String categ = "";
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
                            DetService_ServiceCategory_TV_Det.setText(categ);



                            if(itemService.getPickUp() == 0)
                                DetService_PickUp_TV_Det.setText("Unavailable");

                            else if(itemService.getPickUp() == 1)
                                DetService_PickUp_TV_Det.setText("Available");

                            if(itemService.getPrice() != -1)
                                DetService_ServicePrice.setText(Double.toString(itemService.getPrice()));

                            else
                                DetService_ServicePrice.setText("Variable");

                            DetService_EstTime_TV_Det.setText(itemService.getEstTime());

                            DetService_Details_TV_Det.setText(itemService.getDetails());

                            DetService_images.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(DetailsService.this, ShowWorkshopImages.class);
                                    intent.putExtra("itemWorkshop", itemWorkshop);
                                    startActivity(intent);
                                }
                            });

                        }
                        else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                    }
                });

            }
        });
    }
}