package com.cmdrj.caremydriveadmin;

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

import java.util.ArrayList;
import java.util.Date;

public class DetailsWorkshop extends AppCompatActivity {

    TextView DetWorkshop_WorkshopName, DetWorkshop_WorkshopMbNo, DetWorkshop_Address_TV_Det, DetWorkshop_Rating_TV_Det;
    ImageView DetWorkshop_IV;
    Button DetWorkshop_Reject, DetWorkshop_Accept;
    View DetWorkshop_Rating_View;
    LinearLayout DetWorkshop_Rating_LL;
    FloatingActionButton DetWorkshop_fab, DetWorkshop_images;
    ProgressBar DetWorkshop_PB;

    RecVewWorkshop itemWorkshop;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_workshop);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarDetWorkshop);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();



        DetWorkshop_WorkshopName = findViewById(R.id.DetWorkshop_WorkshopName);
        DetWorkshop_WorkshopMbNo = findViewById(R.id.DetWorkshop_WorkshopMbNo);
        DetWorkshop_Address_TV_Det = findViewById(R.id.DetWorkshop_Address_TV_Det);
        DetWorkshop_Rating_TV_Det = findViewById(R.id.DetWorkshop_Rating_TV_Det);

        DetWorkshop_Reject = findViewById(R.id.DetWorkshop_Reject);
        DetWorkshop_Accept = findViewById(R.id.DetWorkshop_Accept);

        DetWorkshop_IV = findViewById(R.id.DetWorkshop_IV);

        DetWorkshop_fab = findViewById(R.id.DetWorkshop_fab);
        DetWorkshop_images = findViewById(R.id.DetWorkshop_images);

        DetWorkshop_PB = findViewById(R.id.DetWorkshop_PB);
        DetWorkshop_PB.setVisibility(View.VISIBLE);

        DetWorkshop_Rating_View = findViewById(R.id.DetWorkshop_Rating_View);
        DetWorkshop_Rating_LL = findViewById(R.id.DetWorkshop_Rating_LL);

        Intent intent = getIntent();
        itemWorkshop = intent.getExtras().getParcelable("itemWorkshop");







        DocumentReference dReference = fStore.collection("Workshops").document(itemWorkshop.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetWorkshop_WorkshopName, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemWorkshop = value.toObject(RecVewWorkshop.class);



                    if(itemWorkshop.getPic() == null)
                    {
                        DetWorkshop_IV.setImageResource(R.drawable.ic_workshop_70_green);

                        DetWorkshop_PB.setVisibility(View.GONE);

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

                                        DetWorkshop_IV.setImageBitmap(bitmap);
                                        DetWorkshop_PB.setVisibility(View.GONE);

                                        DetWorkshop_IV.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(DetailsWorkshop.this, ViewImage.class);
                                                intent.putExtra("path", fStorage.getReference().child("Workshops").child(itemWorkshop.getID()).child("pic1.jpeg").getPath());
                                                startActivity(intent);
                                            }
                                        });

                                    }
                                });
                            }
                        });
                    }






                    toolbar.setTitle(itemWorkshop.getName());

                    DetWorkshop_WorkshopName.setText(itemWorkshop.getName());

                    DetWorkshop_WorkshopMbNo.setText(itemWorkshop.getMbNo());

                    DetWorkshop_Address_TV_Det.setText(itemWorkshop.getAddress() + ", " + itemWorkshop.getCity() + ", " + itemWorkshop.getState() + ", PinCode: " + itemWorkshop.getPincode() + ", " + itemWorkshop.getCountry());

                    if (itemWorkshop.getRating() != -1)
                    {
                        DetWorkshop_Rating_View.setVisibility(View.VISIBLE);
                        DetWorkshop_Rating_LL.setVisibility(View.VISIBLE);
                        DetWorkshop_Rating_TV_Det.setText(Integer.toString(itemWorkshop.getRating()));
                    }

                    else {
//            DetService_Rating_Parent_LL.setWeightSum(2);
                        DetWorkshop_Rating_View.setVisibility(View.GONE);
                        DetWorkshop_Rating_LL.setVisibility(View.GONE);

//            DetService_Rating_TV_Det.setText("Not Rated");
                    }

                    DetWorkshop_Accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(DetailsWorkshop.this, ServicesWorkshop.class);
                            intent.putExtra("itemWorkshop", itemWorkshop);
                            startActivity(intent);

                        }
                    });

                    DetWorkshop_fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new AlertDialog.Builder(DetailsWorkshop.this)
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

                    DetWorkshop_images.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(DetailsWorkshop.this, ShowWorkshopImages.class);
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


        DetWorkshop_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(DetailsWorkshop.this, ListBookings.class);
                intent.putExtra("initBookings", fStore.collection("Workshops").document(itemWorkshop.getID()).collection("requestedBookings").getPath());
                intent.putExtra("liveBookings", fStore.collection("Workshops").document(itemWorkshop.getID()).collection("acceptedBookings").getPath());
                intent.putExtra("compBookings", fStore.collection("Workshops").document(itemWorkshop.getID()).collection("completedBookings").getPath());
                startActivity(intent);





//                ArrayList<RecVewBookings> initBookings = new ArrayList<>();
//                ArrayList<RecVewBookings> liveBookings = new ArrayList<>();
//                ArrayList<RecVewBookings> compBookings = new ArrayList<>();

//                        initBookings = Workshop.getInitBookings();
//                        liveBookings = Workshop.getPenBookings();
//                        compBookings = Workshop.getPastBookings();


//                ArrayList<RecVewBookings> bookings = new ArrayList<>();
//                // TODO : uncomment when firebase and learn
//
//                bookings.add(new RecVewBookings("0", "CID-0", "WID-0", "SID-2", R.drawable.ic_car_bookings_48, 0, 50, new Date(), 0, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("1", "CID-1", "WID-4", "SID-4", R.drawable.ic_car_bookings_48, 0, 4500, new Date(), 1, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("2", "CID-2", "WID-0", "SID-0", R.drawable.ic_car_bookings_48, 0, 500, new Date(), 0, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("3", 5, "CID-3", "WID-3", "SID-3", R.drawable.ic_car_bookings_48, 3, 150, new Date(), 0, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("4", -1, "CID-4", "WID-4", "SID-3", R.drawable.ic_car_bookings_48, 3, 102, new Date(), 0, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("5", 3, "CID-5", "WID-5", "SID-4", R.drawable.ic_car_bookings_48, 3, 5000, new Date(), 1, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("6", 2, "CID-6", "WID-6", "SID-1", R.drawable.ic_car_bookings_48, 3, 5000, new Date(), 0, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("7", "CID-7","WID-4","SID-0", R.drawable.ic_car_bookings_48, 2, 1000, new Date(), 0, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("8", "CID-8","WID-2","SID-3", R.drawable.ic_car_bookings_48, 2, 100, new Date(), 1, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("9", "CID-9","WID-2","SID-3", R.drawable.ic_car_bookings_48, 1, 100, new Date(), 0, getString(R.string.large_text)));
//                bookings.add(new RecVewBookings("10", "CID-6","WID-9","SID-2", R.drawable.ic_car_bookings_48, 1, 500, new Date(), 1, getString(R.string.large_text)));


//                for (RecVewBookings j : bookings) {
//                    if (j.getWorkShopID().equals(itemWorkshop.getID()) && j.getStatus() == 0) {
//                        initBookings.add(j);
//                    }
//                    else if (j.getWorkShopID().equals(itemWorkshop.getID()) && (j.getStatus() == 1 || j.getStatus() == 2)) {
//                        liveBookings.add(j);
//                    }
//                    else if (j.getWorkShopID().equals(itemWorkshop.getID()) && j.getStatus() == 3) {
//                        compBookings.add(j);
//                    }
//                }

//                Intent intent = new Intent(DetailsWorkshop.this, ListBookings.class);
//                intent.putExtra("initBookings", initBookings);
//                intent.putExtra("liveBookings", liveBookings);
//                intent.putExtra("compBookings", compBookings);
//                startActivity(intent);

                // TODO : uncomment till here firebase and learn


            }
        });










    }
}
