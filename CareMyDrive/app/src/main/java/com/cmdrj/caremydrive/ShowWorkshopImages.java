package com.cmdrj.caremydrive;

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
import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ShowWorkshopImages extends AppCompatActivity {

    CardView showWorkshopImages_CV1, showWorkshopImages_CV2, showWorkshopImages_CV3, showWorkshopImages_CV4;
    ImageView showWorkshopImages_IV1, showWorkshopImages_IV2, showWorkshopImages_IV3, showWorkshopImages_IV4;
    ProgressBar showWorkshopImages_PB1, showWorkshopImages_PB2, showWorkshopImages_PB3, showWorkshopImages_PB4;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;

    RecVewWorkshop workshop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_workshop_images);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarShowWorkshopImages);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        showWorkshopImages_CV1 = findViewById(R.id.showWorkshopImages_CV1);
        showWorkshopImages_CV2 = findViewById(R.id.showWorkshopImages_CV2);
        showWorkshopImages_CV3 = findViewById(R.id.showWorkshopImages_CV3);
        showWorkshopImages_CV4 = findViewById(R.id.showWorkshopImages_CV4);

        showWorkshopImages_IV1 = findViewById(R.id.showWorkshopImages_IV1);
        showWorkshopImages_IV2 = findViewById(R.id.showWorkshopImages_IV2);
        showWorkshopImages_IV3 = findViewById(R.id.showWorkshopImages_IV3);
        showWorkshopImages_IV4 = findViewById(R.id.showWorkshopImages_IV4);

        showWorkshopImages_PB1 = findViewById(R.id.showWorkshopImages_PB1);
        showWorkshopImages_PB2 = findViewById(R.id.showWorkshopImages_PB2);
        showWorkshopImages_PB3 = findViewById(R.id.showWorkshopImages_PB3);
        showWorkshopImages_PB4 = findViewById(R.id.showWorkshopImages_PB4);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        workshop = intent.getExtras().getParcelable("itemWorkshop");

        DocumentReference dReference = fStore.collection("Workshops").document(workshop.getID());

        dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {

                    Snackbar.make(showWorkshopImages_IV1, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    workshop = value.toObject(RecVewWorkshop.class);


                    if( workshop.getImg1() != null)
                    {

                        showWorkshopImages_CV1.setVisibility(View.VISIBLE);
                        showWorkshopImages_PB1.setVisibility(View.VISIBLE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        showWorkshopImages_IV1.setImageBitmap(bitmap);
                                        showWorkshopImages_PB1.setVisibility(View.GONE);

                                        showWorkshopImages_IV1.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if( workshop.getImg1() != null)
                                                {
                                                    Intent intent = new Intent(ShowWorkshopImages.this, ViewImage.class);
                                                    intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img1.jpeg").getPath());
                                                    startActivity(intent);
                                                }

                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        showWorkshopImages_CV1.setVisibility(View.GONE);
                        showWorkshopImages_PB1.setVisibility(View.GONE);
                    }

                    if( workshop.getImg2() != null)
                    {

                        showWorkshopImages_CV2.setVisibility(View.VISIBLE);
                        showWorkshopImages_PB2.setVisibility(View.VISIBLE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        showWorkshopImages_IV2.setImageBitmap(bitmap);
                                        showWorkshopImages_PB2.setVisibility(View.GONE);

                                        showWorkshopImages_IV2.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if( workshop.getImg2() != null)
                                                {
                                                    Intent intent = new Intent(ShowWorkshopImages.this, ViewImage.class);
                                                    intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img2.jpeg").getPath());
                                                    startActivity(intent);
                                                }

                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        showWorkshopImages_CV2.setVisibility(View.GONE);
                        showWorkshopImages_PB2.setVisibility(View.GONE);
                    }

                    if( workshop.getImg3() != null)
                    {

                        showWorkshopImages_CV3.setVisibility(View.VISIBLE);
                        showWorkshopImages_PB3.setVisibility(View.VISIBLE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        showWorkshopImages_IV3.setImageBitmap(bitmap);
                                        showWorkshopImages_PB3.setVisibility(View.GONE);

                                        showWorkshopImages_IV3.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if( workshop.getImg3() != null)
                                                {
                                                    Intent intent = new Intent(ShowWorkshopImages.this, ViewImage.class);
                                                    intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img3.jpeg").getPath());
                                                    startActivity(intent);
                                                }

                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        showWorkshopImages_CV3.setVisibility(View.GONE);
                        showWorkshopImages_PB3.setVisibility(View.GONE);
                    }

                    if( workshop.getImg4() != null)
                    {

                        showWorkshopImages_CV4.setVisibility(View.VISIBLE);
                        showWorkshopImages_PB4.setVisibility(View.VISIBLE);

                        StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg");

                        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                            @Override
                            public void onSuccess(StorageMetadata storageMetadata) {

                                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {

                                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                        showWorkshopImages_IV4.setImageBitmap(bitmap);
                                        showWorkshopImages_PB4.setVisibility(View.GONE);

                                        showWorkshopImages_IV4.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                if( workshop.getImg4() != null)
                                                {
                                                    Intent intent = new Intent(ShowWorkshopImages.this, ViewImage.class);
                                                    intent.putExtra("path", fStorage.getReference().child("Workshops").child(workshop.getID()).child("img4.jpeg").getPath());
                                                    startActivity(intent);
                                                }

                                            }
                                        });



                                    }
                                });
                            }
                        });
                    }
                    else
                    {
                        showWorkshopImages_CV4.setVisibility(View.GONE);
                        showWorkshopImages_PB4.setVisibility(View.GONE);
                    }

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}