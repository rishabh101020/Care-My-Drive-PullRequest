package com.cmdrj.caremydrive;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditDetCar extends AppCompatActivity {

    int GALLERY_CAMERA_PERMISSION_REQUEST_CODE = 10007;
    int GALLERY_PERMISSION_REQUEST_CODE = 10009;
    int CAMERA_PERMISSION_REQUEST_CODE = 10010;
    int READ_GALLERY_REQUEST_CODE = 10008;
    int CAPTURE_CAMERA_REQUEST_CODE = 10011;

    TextInputEditText editDetCar_Name_TIET, editDetCar_No_TIET, editDetCar_Model_TIET, editDetCar_Manufacturer_TIET, editDetCar_Det_TIET, editDetCar_Category_ACTV, editDetCar_Fuel_ACTV;
//    MaterialAutoCompleteTextView editDetCar_Category_ACTV, editDetCar_Fuel_ACTV;
    Button editDetCar_Accept;
    ImageView editDetCar_IV;
    ProgressBar editDetCar_PB;

    ByteArrayOutputStream baos = null;


    String selectedVehicleType;
    int selectedVehicle = -1, finalVehicle;
    String[] vehicleArray = {"Bikes", "Cars", "Heavy", "Others"};

    String selectedFuelType;
    int selectedFuel = -1;
    String[] fuelArray = {"Petrol", "Diesel", "Electric", "CNG"};

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    RecVewCar itemCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_det_car);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        Toolbar toolbar = findViewById(R.id.toolbarEditDetCar);
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


        editDetCar_Name_TIET = findViewById(R.id.editDetCar_Name_TIET);
        editDetCar_No_TIET = findViewById(R.id.editDetCar_No_TIET);
        editDetCar_Model_TIET = findViewById(R.id.editDetCar_Model_TIET);
        editDetCar_Manufacturer_TIET = findViewById(R.id.editDetCar_Manufacturer_TIET);
        editDetCar_Det_TIET = findViewById(R.id.editDetCar_Det_TIET);

        editDetCar_Category_ACTV = findViewById(R.id.editDetCar_Category_ACTV);
        editDetCar_Fuel_ACTV = findViewById(R.id.editDetCar_Fuel_ACTV);

        editDetCar_Accept = findViewById(R.id.editDetCar_Accept);

        editDetCar_IV = findViewById(R.id.editDetCar_IV);
        editDetCar_PB = findViewById(R.id.editDetCar_PB);


        Intent intent = getIntent();
        itemCar = intent.getExtras().getParcelable("itemCar");

        editDetCar_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox();

                }
                else if(ContextCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openGallery();
                }
                else if(ContextCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openCamera();
                }
                else {
                    askStoragePermission();
                }

            }
        });

        editDetCar_Category_ACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditDetCar.this);

                builder.setTitle("Select Vehicle Type");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(vehicleArray, selectedVehicle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedVehicleType = vehicleArray[which];

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(selectedVehicleType != null)
                        {
                            if (selectedVehicleType.equals("Bikes"))
                                selectedVehicle = 0;
                            else if (selectedVehicleType.equals("Cars"))
                                selectedVehicle = 1;
                            else if (selectedVehicleType.equals("Heavy"))
                                selectedVehicle = 2;
                            else if (selectedVehicleType.equals("Others"))
                                selectedVehicle = 3;
                        }
                        else
                        {
                            Snackbar.make(v, "Please Select Some Vehicle Type.", Snackbar.LENGTH_SHORT).show();
                        }


                        editDetCar_Category_ACTV.setText(selectedVehicleType);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        editDetCar_Fuel_ACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditDetCar.this);

                builder.setTitle("Select Fuel Type");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(fuelArray, selectedFuel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedFuelType = fuelArray[which];

                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(selectedFuelType != null)
                        {
                            if (selectedFuelType.equals("Petrol"))
                                selectedFuel = 0;
                            else if (selectedFuelType.equals("Diesel"))
                                selectedFuel = 1;
                            else if (selectedFuelType.equals("Electric"))
                                selectedFuel = 2;
                            else if (selectedFuelType.equals("CNG"))
                                selectedFuel = 3;
                        }
                        else
                        {
                            Snackbar.make(v, "Please Select Some Fuel Type.", Snackbar.LENGTH_SHORT).show();
                        }


                        editDetCar_Fuel_ACTV.setText(selectedFuelType);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        if (itemCar.getID().equals("new")) {

            toolbar.setTitle("Add New Vehicle");

            editDetCar_IV.setImageResource(R.drawable.ic_add_70_green);


//            final long[] ID = new long[1];

//            fStore.collection("Size").document("Vehicles")
//                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                        @Override
//                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//
//                            if (error != null) {
//                                Snackbar.make(findViewById(android.R.id.content), "" + error, Snackbar.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            if (value != null) {
//
//                                ID[0] = value.getLong("Len");
////                                            Log.d("TAG" , "where + " + ID);
//
//
//                            } else {
//                                Snackbar.make(findViewById(android.R.id.content), "Data Empty", Snackbar.LENGTH_SHORT).show();
//                                return;
//                            }
//                        }
//                    });

            editDetCar_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editDetCar_Accept.setEnabled(false);

                    if (!(editDetCar_Name_TIET.getText().toString().trim().equals("")) && !(editDetCar_No_TIET.getText().toString().trim().equals("")) && !(editDetCar_Model_TIET.getText().toString().trim().equals("")) && !(editDetCar_Manufacturer_TIET.getText().toString().trim().equals("")) && !(editDetCar_Category_ACTV.getText().toString().trim().equals("")) && !(editDetCar_Fuel_ACTV.getText().toString().trim().equals(""))) {

                        if (selectedVehicle == 0)
                            finalVehicle = 2;
                        else if (selectedVehicle == 1)
                            finalVehicle = 4;
                        else if (selectedVehicle == 2)
                            finalVehicle = 6;
                        else if (selectedVehicle == 3)
                            finalVehicle = 3;

//                        Log.d("TAG" , "where + " + ID);

//                        DocumentReference ref = fStore.collection("user_details").document();
//                        String id = ref.getId();
//                        Map map = new HashMap<>();
//                        map.put("username", username);
//                        map.put("email", email);
//                        map.put("id", id);
//                        ref.set(map)...

                        DocumentReference dReference = fStore.collection("Vehicles").document();
                        String ID = dReference.getId();

                        RecVewCar newCar = new RecVewCar(ID,     fStore.collection("Clients").document(fUser.getUid()),       editDetCar_Name_TIET.getText().toString().trim(), editDetCar_No_TIET.getText().toString().trim(), editDetCar_Model_TIET.getText().toString().trim(), editDetCar_Manufacturer_TIET.getText().toString().trim(), selectedFuel, finalVehicle, editDetCar_Det_TIET.getText().toString().trim());

                        if(baos == null)
                        {
                            newCar.setPic(null);

                            dReference.set(newCar)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Map<String, Object> map = new HashMap<>();
                                            map.put("id", newCar.getID());
                                            map.put("ref", dReference);

                                            fStore.collection("Clients").document(fUser.getUid()).collection("Vehicles").document(newCar.getID()).set(map)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {


                                                            // TODO : start ActivityForResult() wala finish
//                                                      Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                                      intent.putExtra("itemService",  itemService);
//                                                      startActivity(intent);
                                                            finish();

                                                            //Todo : over

                                                        }
                                                    });

                                        }
                                    });
                        }
                        else
                        {
                            StorageReference sReference = fStorage.getReference().child("Vehicles").child(newCar.getID()).child("pic1.jpeg");

                            sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {

                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    newCar.setPic(uri.toString());


                                                    dReference.set(newCar)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    Map<String, Object> map = new HashMap<>();
                                                                    map.put("id", newCar.getID());
                                                                    map.put("ref", dReference);

                                                                    fStore.collection("Clients").document(fUser.getUid()).collection("Vehicles").document(newCar.getID()).set(map)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {


                                                                                    // TODO : start ActivityForResult() wala finish
//                                                      Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                                      intent.putExtra("itemService",  itemService);
//                                                      startActivity(intent);
                                                                                    finish();

                                                                                    //Todo : over

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
                        }
                    }
                    else
                    {

                        editDetCar_Accept.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                    }
                }
            });

        }
        else
        {
            editDetCar_PB.setVisibility(View.VISIBLE);
            toolbar.setTitle(itemCar.getName());

            if(itemCar.getPic() == null)
            {
                editDetCar_IV.setImageResource(R.drawable.ic_add_70_green);
                editDetCar_PB.setVisibility(View.GONE);

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

                                if(baos == null)
                                    editDetCar_IV.setImageBitmap(bitmap);

                                editDetCar_PB.setVisibility(View.GONE);

                            }
                        });
                    }
                });

            }


            editDetCar_Name_TIET.setText(itemCar.getName());

            editDetCar_No_TIET.setText(itemCar.getNo());

            editDetCar_Model_TIET.setText(itemCar.getModel());

            editDetCar_Manufacturer_TIET.setText(itemCar.getManufacturer());



            if (itemCar.getWheelType() == 2) {
                selectedVehicleType = "Bikes";
                selectedVehicle = 0;
            } else if (itemCar.getWheelType() == 4) {
                selectedVehicleType = "Cars";
                selectedVehicle = 1;
            } else if (itemCar.getWheelType() == 6) {
                selectedVehicleType = "Heavy";
                selectedVehicle = 2;
            } else if (itemCar.getWheelType() == 3) {
                selectedVehicleType = "Others";
                selectedVehicle = 3;
            }

            editDetCar_Category_ACTV.setText(selectedVehicleType);



            if (itemCar.getFuelType() == 0) {
                selectedFuelType = "Petrol";
                selectedFuel = 0;
            } else if (itemCar.getFuelType() == 1) {
                selectedFuelType = "Diesel";
                selectedFuel = 1;
            } else if (itemCar.getFuelType() == 2) {
                selectedFuelType = "Electric";
                selectedFuel = 2;
            } else if (itemCar.getFuelType() == 3) {
                selectedFuelType = "CNG";
                selectedFuel = 3;
            }

            editDetCar_Fuel_ACTV.setText(selectedFuelType);


            editDetCar_Det_TIET.setText(itemCar.getDetails());

            editDetCar_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editDetCar_Accept.setEnabled(false);

                    if (!(editDetCar_Name_TIET.getText().toString().trim().equals("")) && !(editDetCar_No_TIET.getText().toString().trim().equals("")) && !(editDetCar_Model_TIET.getText().toString().trim().equals("")) && !(editDetCar_Manufacturer_TIET.getText().toString().trim().equals("")) && !(editDetCar_Category_ACTV.getText().toString().trim().equals("")) && !(editDetCar_Fuel_ACTV.getText().toString().trim().equals(""))) {

                        if (selectedVehicle == 0)
                            finalVehicle = 2;
                        else if (selectedVehicle == 1)
                            finalVehicle = 4;
                        else if (selectedVehicle == 2)
                            finalVehicle = 6;
                        else if (selectedVehicle == 3)
                            finalVehicle = 3;

                        RecVewCar newCar = new RecVewCar(itemCar.getID(),     fStore.collection("Clients").document(fUser.getUid()),        editDetCar_Name_TIET.getText().toString().trim(), editDetCar_No_TIET.getText().toString().trim(), editDetCar_Model_TIET.getText().toString().trim(), editDetCar_Manufacturer_TIET.getText().toString().trim(), selectedFuel, finalVehicle, editDetCar_Det_TIET.getText().toString().trim());
//                        newCar.setID(itemCar.getID());


                        if(baos == null)
                        {
                            newCar.setPic(itemCar.getPic());

                            // TODO : below TODO is very important also correct in other 2 apps
                            // TODO : check if the nested collection of all particular service if this general service type is deleted or not        --> Added setOptions.merge() but not checked yet
                            fStore.collection("Vehicles").document(newCar.getID()).set(newCar, SetOptions.merge())
                                    //Todo : over

                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


//                                      TODO : start ActivityForResult() wala finish
//                                     Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                     intent.putExtra("itemService",  itemService);
//                                     startActivity(intent);

                                            finish();

                                            //Todo : over
                                        }
                                    });
                            //Todo : over

                        }
                        else
                        {
                            StorageReference sReference = fStorage.getReference().child("Vehicles").child(newCar.getID()).child("pic1.jpeg");

                            sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {

                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    newCar.setPic(uri.toString());

                                                    // TODO : below TODO is very important also correct in other 2 apps
                                                    // TODO : check if the nested collection of all particular service if this general service type is deleted or not        --> Added setOptions.merge() but not checked yet
                                                    fStore.collection("Vehicles").document(newCar.getID()).set(newCar, SetOptions.merge())
                                                            //Todo : over

                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {


//                                      TODO : start ActivityForResult() wala finish
//                                     Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                     intent.putExtra("itemService",  itemService);
//                                     startActivity(intent);

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

                        editDetCar_Accept.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

//        String[] mimeTypes = {"image/jpeg", "image/png"};
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, READ_GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);     -> for gallery

//        String[] mimeTypes = {"image/jpeg", "image/png"};
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, CAPTURE_CAMERA_REQUEST_CODE);
    }

    private void openUploadChoiceDialogBox() {

        new AlertDialog.Builder(this)
                .setTitle("Choose Image Upload Option")
                .setMessage("Choose an application to Upload Image")
                .setCancelable(false)
                .setPositiveButton("CAMERA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openCamera();

                    }
                })
                .setNegativeButton("GALLERY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openGallery();
                    }
                })
                .create().show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == READ_GALLERY_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());


                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        editDetCar_Accept.setEnabled(false);

                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                        editDetCar_PB.setVisibility(View.VISIBLE);
                        editDetCar_IV.setImageBitmap(bitmap);
                        editDetCar_PB.setVisibility(View.GONE);
                        editDetCar_Accept.setEnabled(true);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar.make(editDetCar_Name_TIET, "Select an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == CAPTURE_CAMERA_REQUEST_CODE)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                if (data != null) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                    editDetCar_Accept.setEnabled(false);

                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);


                    editDetCar_PB.setVisibility(View.VISIBLE);
                    editDetCar_IV.setImageBitmap(bitmap);
                    editDetCar_PB.setVisibility(View.GONE);
                    editDetCar_Accept.setEnabled(true);

                }

                else{

                    Snackbar.make(editDetCar_Name_TIET, "Capture an Image", Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(EditIndividualService.this,"Select a File",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }



    private void askStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Storage and Camera Permissions Needed")
                    .setMessage("Storage permission is needed to read images from gallery.\nCamera permission is needed to access camera application.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditDetCar.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Storage Permission Needed")
                    .setMessage("Storage permission is needed to read images from gallery.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditDetCar.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            openCamera();
                        }
                    })
                    .create().show();
        }
        else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Camera Permission Needed")
                    .setMessage("Camera permission is needed to access camera application.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(EditDetCar.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                            openGallery();
                        }
                    })
                    .create().show();
        }
        else {

            if (ActivityCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(EditDetCar.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);

            }
            else if(ActivityCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(EditDetCar.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
            }
            else if(ActivityCompat.checkSelfPermission(EditDetCar.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(EditDetCar.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
//            else {
//                askStoragePermission();
//            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == GALLERY_CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetCar_Name_TIET, "Gallery and Camera Permissions Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetCar_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openGallery();
            }
            else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetCar_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openCamera();
            }
            else
            {
                Snackbar.make(editDetCar_Name_TIET, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetCar_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(editDetCar_Name_TIET, "Gallery Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openCamera();
            }
        }

        else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetCar_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(editDetCar_Name_TIET, "Camera Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        }
    }
}