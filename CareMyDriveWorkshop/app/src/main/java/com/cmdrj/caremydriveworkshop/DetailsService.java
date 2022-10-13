package com.cmdrj.caremydriveworkshop;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Collections;

public class DetailsService extends AppCompatActivity {

    TextView DetService_ServiceName, DetService_ServicePrice, DetService_Rating_TV_Det, DetService_ServiceCategory_TV_Det, DetService_PickUp_TV_Det, DetService_EstTime_TV_Det, DetService_Details_TV_Det;
    ImageView DetService_IV;
    Button DetService_Accept;
    LinearLayout DetService_Rating_LL;
    View DetService_Rating_View;
    ProgressBar DetService_PB;

    RecVewService itemService;
    RecVewWorkshop itemWorkshop;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
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

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        DetService_ServiceName = findViewById(R.id.DetService_ServiceName);
        DetService_ServicePrice = findViewById(R.id.DetService_ServicePrice);
        DetService_Rating_TV_Det= findViewById(R.id.DetService_Rating_TV_Det);
        DetService_ServiceCategory_TV_Det= findViewById(R.id.DetService_ServiceCategory_TV_Det);
        DetService_PickUp_TV_Det= findViewById(R.id.DetService_PickUp_TV_Det);
        DetService_EstTime_TV_Det = findViewById(R.id.DetService_EstTime_TV_Det);
        DetService_Details_TV_Det = findViewById(R.id.DetService_Details_TV_Det);
//
        DetService_Rating_LL = findViewById(R.id.DetService_Rating_LL);

        DetService_Rating_View = findViewById(R.id.DetService_Rating_View);

        DetService_IV = findViewById(R.id.DetService_IV);

        DetService_Accept = findViewById(R.id.DetService_Accept);

        DetService_PB = findViewById(R.id.DetService_PB);
        DetService_PB.setVisibility(View.VISIBLE);

//        DetService_ServiceName.setSelected(true);

        Intent intent = getIntent();
        itemService = intent.getExtras().getParcelable("itemService");


        DocumentReference dReference = fStore.collection("Workshops").document(fUser.getUid()).collection("Services").document(itemService.getID());
        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Snackbar.make(DetService_ServiceName, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    itemService = value.toObject(RecVewService.class);

                    itemService.getWorkShopRef().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            itemWorkshop = documentSnapshot.toObject(RecVewWorkshop.class);


                            if(itemService.getPic() == null)
                            {
                                DetService_IV.setImageResource(R.drawable.ic_cars_repair_70_green);

                                DetService_PB.setVisibility(View.GONE);

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




                            if(itemWorkshop.getRating() != -1)
                                DetService_Rating_TV_Det.setText(Integer.toString(itemWorkshop.getRating()));
                            else
                            {
                                DetService_Rating_LL.setVisibility(View.GONE);
                                DetService_Rating_View.setVisibility(View.GONE);

                            }

                        }
                    });




                    toolbar.setTitle(itemService.getName());


                    DetService_ServiceName.setText(itemService.getName());

                    if(itemService.getPrice() != -1)
                        DetService_ServicePrice.setText(Double.toString(itemService.getPrice()));

                    else
                        DetService_ServicePrice.setText("Variable");


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
                    DetService_ServiceCategory_TV_Det.setText(categ);


                    if(itemService.getPickUp() == 0)
                        DetService_PickUp_TV_Det.setText("Unavailable");

                    else if(itemService.getPickUp() == 1)
                        DetService_PickUp_TV_Det.setText("Available");



                    DetService_EstTime_TV_Det.setText(itemService.getEstTime());

                    DetService_Details_TV_Det.setText(itemService.getDetails());

                    DetService_Accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            // TODO : start ActivityForResult() wala finish
                            Intent intent = new Intent(DetailsService.this, EditDetService.class);
                            intent.putExtra("itemService",  itemService);
                            startActivity(intent);

                            //Todo : over

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
}