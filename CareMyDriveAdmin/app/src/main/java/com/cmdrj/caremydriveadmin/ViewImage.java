package com.cmdrj.caremydriveadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

public class ViewImage extends AppCompatActivity {

    private FirebaseStorage fStorage = FirebaseStorage.getInstance();
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_view_image);

        ImageView viewImage_IV = findViewById(R.id.viewImage_IV);
        ProgressBar viewImage_PB = findViewById(R.id.viewImage_PB);

        Intent intent = getIntent();
        path = intent.getStringExtra("path");

        StorageReference sReference = fStorage.getReference(path);

        sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {

                sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                        viewImage_IV.setImageBitmap(bitmap);

                        viewImage_PB.setVisibility(View.GONE);

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Snackbar.make(viewImage_IV, "Unable to load Image" + e, Snackbar.LENGTH_SHORT).show();

                onBackPressed();
            }
        });;
    }
}