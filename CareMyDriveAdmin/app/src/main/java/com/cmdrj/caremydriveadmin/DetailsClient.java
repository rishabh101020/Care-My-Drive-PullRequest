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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DetailsClient extends AppCompatActivity {

    TextView DetClient_ClientName, DetClient_ClientMbNo, DetClient_Address_TV_Det;
    ImageView DetClient_IV;
    Button DetClient_Reject, DetClient_Accept;
    FloatingActionButton DetClient_fab;
    ProgressBar DetClient_PB;

    RecVewClient itemClient;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


//    ArrayList<RecVewBookings> initBookings = new ArrayList<>();
//    ArrayList<RecVewBookings> liveBookings = new ArrayList<>();
//    ArrayList<RecVewBookings>  compBookings = new ArrayList<>();
//
//    boolean initBookingsRetrieved = false;
//    boolean liveBookingsRetrieved = false;
//    boolean compBookingsRetrieved = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_client);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarDetClient);
//        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();


        DetClient_ClientName = findViewById(R.id.DetClient_ClientName);
        DetClient_ClientMbNo = findViewById(R.id.DetClient_ClientMbNo);
        DetClient_Address_TV_Det= findViewById(R.id.DetClient_Address_TV_Det);

        DetClient_Reject= findViewById(R.id.DetClient_Reject);
        DetClient_Accept= findViewById(R.id.DetClient_Accept);

        DetClient_IV = findViewById(R.id.DetClient_IV);

        DetClient_fab = findViewById(R.id.DetClient_fab);

        DetClient_PB = findViewById(R.id.DetClient_PB);
        DetClient_PB.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        itemClient = intent.getExtras().getParcelable("itemClient");






        DocumentReference dReference = fStore.collection("Clients").document(itemClient.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetClient_ClientName, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemClient = value.toObject(RecVewClient.class);


                    if(itemClient.getPic() == null)
                    {
                        DetClient_IV.setImageResource(R.drawable.ic_person_70_green);

                        DetClient_PB.setVisibility(View.GONE);

                    }
                    else
                    {

                        StorageReference sReference = fStorage.getReference().child("Clients").child(itemClient.getID()).child("pic1.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        DetClient_IV.setImageBitmap(bitmap);
                                        DetClient_PB.setVisibility(View.GONE);

                                        DetClient_IV.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Intent intent = new Intent(DetailsClient.this, ViewImage.class);
                                                intent.putExtra("path", fStorage.getReference().child("Clients").child(itemClient.getID()).child("pic1.jpeg").getPath());
                                                startActivity(intent);
                                            }
                                        });


                                    }
                                });
                            }
                        });
                    }




                    toolbar.setTitle(itemClient.getName());

                    DetClient_ClientName.setText(itemClient.getName());

                    DetClient_ClientMbNo.setText(itemClient.getMbNo());

                    DetClient_Address_TV_Det.setText(itemClient.getAddress() + ", " + itemClient.getCity() + ", " +  itemClient.getState() + ", PinCode: " + itemClient.getPincode() + ", " + itemClient.getCountry());


//                    fStore.collection("Clients").document(itemClient.getID()).collection("requestedBookings")
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    if (error != null) {
//                                        Snackbar.make(DetClient_ClientName, "" + error, Snackbar.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                    if (value != null) {
//
//                                        initBookings.clear();
//
//                                        List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
//                                        initBookings.addAll(snapshotList);
//
////                                                adapterRecVewUsers.notifyDataSetChanged();
//                                        initBookingsRetrieved = true;
//
//                                    }
//                                    else {
////                                              Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                }
//                            });
//
//                    fStore.collection("Clients").document(itemClient.getID()).collection("acceptedBookings")
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    if (error != null) {
//                                        Snackbar.make(DetClient_ClientName, "" + error, Snackbar.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                    if (value != null) {
//
//                                        liveBookings.clear();
//
//                                        List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
//                                        liveBookings.addAll(snapshotList);
//
////                                                adapterRecVewUsers.notifyDataSetChanged();
//                                        liveBookingsRetrieved = true;
//
//                                    }
//                                    else {
////                                              Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                }
//                            });
//
//
//                    fStore.collection("Clients").document(itemClient.getID()).collection("completedBookings")
//                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                @Override
//                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                    if (error != null) {
//                                        Snackbar.make(DetClient_ClientName, "" + error, Snackbar.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                    if (value != null) {
//
//                                        compBookings.clear();
//
//                                        List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
//                                        compBookings.addAll(snapshotList);
//
////                                                adapterRecVewUsers.notifyDataSetChanged();
//                                        compBookingsRetrieved = true;
//
//                                    }
//                                    else {
////                                              Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
//                                        return;
//                                    }
//
//                                }
//                            });


                    DetClient_Accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(DetailsClient.this, CarsClient.class);
                            intent.putExtra("itemClient",itemClient);
                            startActivity(intent);

                        }
                    });

                    DetClient_fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new AlertDialog.Builder(DetailsClient.this)
                                    .setTitle("Choose Contact Option")
                                    .setMessage("Choose an application to contact Client")
                                    .setPositiveButton("E-Mail", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", itemClient.getEmail(), null));
                                            if (intent.resolveActivity(getPackageManager()) != null) {
                                                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                                            }

                                        }
                                    })
                                    .setNeutralButton("Call", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", itemClient.getMbNo(), null));
                                            if (intent.resolveActivity(getPackageManager()) != null) {
                                                startActivity(phoneIntent);
                                            }

                                        }
                                    })
                                    .create().show();

                        }
                    });

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        DetClient_Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailsClient.this, ListBookings.class);

                intent.putExtra("initBookings", fStore.collection("Clients").document(itemClient.getID()).collection("requestedBookings").getPath());
                intent.putExtra("liveBookings", fStore.collection("Clients").document(itemClient.getID()).collection("acceptedBookings").getPath());
                intent.putExtra("compBookings", fStore.collection("Clients").document(itemClient.getID()).collection("completedBookings").getPath());

//                intent.putExtra("initBookingsDocument", false);
//                intent.putExtra("liveBookingsDocument", false);
//                intent.putExtra("compBookingDocuments", false);


                startActivity(intent);





//                for(String i: itemClient.getInitBookings())
//                {
//                    for(RecVewBookings j : bookings)
//                    {
//                        if(i.equals(j.getID()))
//                        {
//                            initBookings.add(j);
//                            break;
//                        }
//                    }
//                }
//
//                for(String i: itemClient.getPenBookings())
//                {
//                    for(RecVewBookings j : bookings)
//                    {
//                        if(i.equals(j.getID()))
//                        {
//                            liveBookings.add(j);
//                            break;
//                        }
//                    }
//                }
//
//                for(String i: itemClient.getPastBookings())
//                {
//                    for(RecVewBookings j : bookings)
//                    {
//                        if(i.equals(j.getID()))
//                        {
//                            compBookings.add(j);
//                            break;
//                        }
//                    }
//                }

            }
        });

    }

}