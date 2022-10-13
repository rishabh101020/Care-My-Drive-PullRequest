package com.cmdrj.caremydriveadmin;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class EditIndividualService extends AppCompatActivity {


    int STORAGE_REQUEST_CODE = 10007;
    int READ_REQUEST_CODE = 10008;


    TextInputEditText editIndSer_Name_TIET, editIndSer_Det_TIET;
    Button editIndSer_Accept;
    ImageView editIndSer_IV;
    ProgressBar editIndSer_PB;

    ByteArrayOutputStream baos = null;

    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;

    RecVewService itemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_individual_service);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarEditIndSer);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        editIndSer_Name_TIET = findViewById(R.id.editIndSer_Name_TIET);
        editIndSer_Det_TIET = findViewById(R.id.editIndSer_Det_TIET);

        editIndSer_Accept = findViewById(R.id.editIndSer_Accept);

        editIndSer_IV = findViewById(R.id.editIndSer_IV);
        editIndSer_PB = findViewById(R.id.editIndSer_PB);

        Intent intent = getIntent();
        itemService = intent.getExtras().getParcelable("itemService");

        editIndSer_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(EditIndividualService.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openStorage();

                } else {
                    askStoragePermission();
                }

            }
        });



        if(itemService.getID().equals("new"))
        {
            toolbar.setTitle("Add New Service");

            editIndSer_IV.setImageResource(R.drawable.ic_add_70_green);

            editIndSer_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editIndSer_Accept.setEnabled(false);

                    if (!(editIndSer_Name_TIET.getText().toString().trim().equals("")) && !(editIndSer_Det_TIET.getText().toString().trim().equals(""))) {

                        DocumentReference dReference = fStore.collection("Services").document();
                        String ID = dReference.getId();

                        RecVewService newService = new RecVewService(ID, editIndSer_Name_TIET.getText().toString().trim(), editIndSer_Det_TIET.getText().toString().trim());

                        if(baos == null)
                        {
                            newService.setPic(null);

                            dReference.set(newService)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            // TODO : start ActivityForResult() wala finish
//                                      Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                      intent.putExtra("itemService",  itemService);
//                                      startActivity(intent);
                                            finish();

                                            //Todo : over

                                        }
                                    });

                        }
                        else
                        {
                            StorageReference sReference = fStorage.getReference().child("Services").child(newService.getID()).child("pic1.jpeg");

                            sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {

                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    newService.setPic(uri.toString());

                                                    dReference.set(newService)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    // TODO : start ActivityForResult() wala finish
//                                      Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                      intent.putExtra("itemService",  itemService);
//                                      startActivity(intent);
                                                                    finish();

                                                                    //Todo : over

                                                                }
                                                            });

                                                }
                                            });
                                        }
                                    }


                                }
                            });
                        }

                    } else {

                        editIndSer_Accept.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                    }

                }
            });
        }
        else
        {
            editIndSer_PB.setVisibility(View.VISIBLE);
            toolbar.setTitle(itemService.getName());

            editIndSer_Name_TIET.setText(itemService.getName());

            editIndSer_Det_TIET.setText(itemService.getDetails());

            if(itemService.getPic() == null)
            {
                editIndSer_IV.setImageResource(R.drawable.ic_add_70_green);
                editIndSer_PB.setVisibility(View.GONE);

            }
            else
            {

                StorageReference sReference = fStorage.getReference().child("Services").child(itemService.getID()).child("pic1.jpeg");

                sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {

                        sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                if(baos == null)
                                    editIndSer_IV.setImageBitmap(bitmap);

                                editIndSer_PB.setVisibility(View.GONE);

                            }
                        });
                    }
                });

            }
//
            editIndSer_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editIndSer_Accept.setEnabled(false);

                    if (!(editIndSer_Name_TIET.getText().toString().trim().equals("")) && !(editIndSer_Det_TIET.getText().toString().trim().equals(""))) {

                        RecVewService newService = new RecVewService(itemService.getID(), editIndSer_Name_TIET.getText().toString().trim(), editIndSer_Det_TIET.getText().toString().trim());


                        if(baos == null)
                        {
                            newService.setPic(itemService.getPic());

                            // TODO : below TODO is very important also correct in other 2 apps
                            // TODO : check if the nested collection of all particular service if this general service type is deleted or not        --> Added setOptions.merge() but not checked yet
                            fStore.collection("Services").document(newService.getID()).set(newService, SetOptions.merge())

                                    // TODO : over
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            // TODO : start ActivityForResult() wala finish
//                                      Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                      intent.putExtra("itemService",  itemService);
//                                      startActivity(intent);
                                            finish();

                                            //Todo : over

                                        }
                                    });

                            //Todo : over
                        }
                        else
                        {
                            StorageReference sReference = fStorage.getReference().child("Services").child(newService.getID()).child("pic1.jpeg");

                            sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {

                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    newService.setPic(uri.toString());



                                                    // TODO : below TODO is very important also correct in other 2 apps
                                                    // TODO : check if the nested collection of all particular service if this general service type is deleted or not        --> Added setOptions.merge() but not checked yet
                                                    fStore.collection("Services").document(newService.getID()).set(newService, SetOptions.merge())

                                                            // TODO : over
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    // TODO : start ActivityForResult() wala finish
//                                      Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                      intent.putExtra("itemService",  itemService);
//                                      startActivity(intent);
                                                                    finish();

                                                                    //Todo : over

                                                                }
                                                            });

                                                    //Todo : over

                                                }
                                            });
                                        }
                                    }


                                }
                            });
                        }

                    } else {

                        editIndSer_Accept.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

    private void openStorage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);     -> for gallery

//        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {


                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());


                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        editIndSer_Accept.setEnabled(false);

                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

//                    Bundle bundle = data.getExtras();
//                    Log.d("TAG" , "RishabhJain + " + "here2");

//                    if(bundle != null)
//                    {
//                        Log.d("TAG" , "RishabhJain + " + "here1");


                        editIndSer_PB.setVisibility(View.VISIBLE);
                        editIndSer_IV.setImageURI(data.getData());
                        editIndSer_PB.setVisibility(View.GONE);
                        editIndSer_Accept.setEnabled(true);

//                    }


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Snackbar.make(editIndSer_Name_TIET, "Select a File", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private void askStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Storage Permission Needed")
                    .setMessage("Storage permission is needed to read image files from storage.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditIndividualService.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(EditIndividualService.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editIndSer_Name_TIET, "Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openStorage();
            }
            else
            {
                Snackbar.make(editIndSer_Name_TIET, "Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}