package com.cmdrj.caremydriveworkshop;

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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditDetService extends AppCompatActivity {

    int GALLERY_CAMERA_PERMISSION_REQUEST_CODE = 10007;
    int GALLERY_PERMISSION_REQUEST_CODE = 10009;
    int CAMERA_PERMISSION_REQUEST_CODE = 10010;
    int READ_GALLERY_REQUEST_CODE = 10008;
    int CAPTURE_CAMERA_REQUEST_CODE = 10011;

    TextInputEditText editDetSer_Name_TIET, editDetSer_Price_TIET, editDetSer_EstTime_TIET, editDetSer_Det_TIET     ,editDetSer_SerType_ACTV, editDetSer_Category_ACTV;
    MaterialAutoCompleteTextView /*editDetSer_SerType_ACTV, editDetSer_Category_ACTV, */editDetSer_EstTimeUnit_ACTV, editDetSer_PickUp_ACTV, editDetSer_PriceType_ACTV;
    Button editDetSer_Accept;
    RelativeLayout editDetSer_RL2;
    ImageView editDetSer_IV;
    ProgressBar editDetSer_PB;

    ByteArrayOutputStream baos = null;


    private ArrayList<String> pickUpFacility = new ArrayList<String>();
    private ArrayAdapter adapterPickUpFacility;

    private ArrayList<String> timeUnit = new ArrayList<String>();
    private ArrayAdapter adapterTimeUnit;

    private ArrayList<String> priceType = new ArrayList<String>();
    private ArrayAdapter adapterPriceType;

    boolean[] selectedVehicleType;
    ArrayList<Integer> vehicleList = new ArrayList<>();
    String[] vehicleArray = {"Bikes", "Cars", "Heavy", "Others"};

    String selectedServiceType, selectedServiceTypeID;
    int selectedService = -1;
    String[] serviceArray, serviceIDArray;
    ArrayList<String> serviceArrayList = new ArrayList<>();
    ArrayList<String> serviceIDArrayList = new ArrayList<>();


    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;


    RecVewService itemService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_det_service);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Toolbar toolbar = findViewById(R.id.toolbarEditDetSer);
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


        editDetSer_Name_TIET = findViewById(R.id.editDetSer_Name_TIET);
        editDetSer_SerType_ACTV = findViewById(R.id.editDetSer_SerType_ACTV);
        editDetSer_Price_TIET = findViewById(R.id.editDetSer_Price_TIET);
        editDetSer_PriceType_ACTV = findViewById(R.id.editDetSer_PriceType_ACTV);
        editDetSer_EstTime_TIET = findViewById(R.id.editDetSer_EstTime_TIET);
        editDetSer_Det_TIET = findViewById(R.id.editDetSer_Det_TIET);
        editDetSer_Category_ACTV = findViewById(R.id.editDetSer_Category_ACTV);
        editDetSer_EstTimeUnit_ACTV = findViewById(R.id.editDetSer_EstTimeUnit_ACTV);
        editDetSer_PickUp_ACTV = findViewById(R.id.editDetSer_PickUp_ACTV);

        editDetSer_Accept = findViewById(R.id.editDetSer_Accept);

        editDetSer_RL2 = findViewById(R.id.editDetSer_RL2);

        editDetSer_IV = findViewById(R.id.editDetSer_IV);
        editDetSer_PB = findViewById(R.id.editDetSer_PB);


        Intent intent = getIntent();
        itemService = intent.getExtras().getParcelable("itemService");

        editDetSer_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(EditDetService.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(EditDetService.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox();

                }
                else if(ContextCompat.checkSelfPermission(EditDetService.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openGallery();
                }
                else if(ContextCompat.checkSelfPermission(EditDetService.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openCamera();
                }
                else {
                    askStoragePermission();
                }

            }
        });

        fStore.collection("Services")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Snackbar.make(findViewById(android.R.id.content), "" + error, Snackbar.LENGTH_SHORT).show();
                            return;
                        }

                        if (value != null) {

                            serviceArrayList.clear();
                            serviceIDArrayList.clear();

                            List<RecVewService> snapshotList = value.toObjects(RecVewService.class);

                            for (RecVewService item : snapshotList) {
                                if (item.getVisibility()) {
                                    serviceArrayList.add(item.getName());
                                    serviceIDArrayList.add(item.getID());
                                }
                            }


                            // TODO: rectify if serviceArrayList is empty
                            serviceArray = serviceArrayList.toArray(new String[serviceArrayList.size()]);
                            serviceIDArray = serviceIDArrayList.toArray(new String[serviceIDArrayList.size()]);

                            if (selectedService != -1) {

                                int pos = 0;
                                boolean valid = false;
                                for (String item : serviceIDArray) {
                                    if (item.equals(selectedServiceTypeID)) {
                                        selectedService = pos;
                                        valid = true;
                                        break;
                                    }
                                    pos++;

                                }

                                if(!valid)
                                {
                                    selectedService = -1;
                                    editDetSer_SerType_ACTV.setText(null);
                                }
                            }

//                            adapterRecVewServices.notifyDataSetChanged();

                        } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                    }
                });


        editDetSer_SerType_ACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditDetService.this);

                builder.setTitle("Select Service Type");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(serviceArray, selectedService, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedServiceType = serviceArray[which];
                        selectedServiceTypeID = serviceIDArray[which];
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int pos = 0;
                        boolean valid = false;
                        for (String item : serviceIDArray) {
                            if (item.equals(selectedServiceTypeID)) {
                                selectedService = pos;
                                valid = true;
                                break;
                            }
                            pos++;

                        }

                        if(!valid)
                        {
                            selectedService = -1;
                            selectedVehicleType = null;
                        }
//                        if (selectedServiceType.equals("change"))
//                            selectedService = 0;
//                        else if (selectedServiceType.equals("Diesel"))
//                            selectedService = 1;
//                        else if (selectedServiceType.equals("Electric"))
//                            selectedService = 2;
//                        else if (selectedServiceType.equals("CNG"))
//                            selectedService = 3;

                        editDetSer_SerType_ACTV.setText(selectedServiceType);

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


        selectedVehicleType = new boolean[vehicleArray.length];
        editDetSer_Category_ACTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditDetService.this);

                builder.setTitle("Select Vehicle Types");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(vehicleArray, selectedVehicleType, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            vehicleList.add(which);
                            Collections.sort(vehicleList);
                        } else {
                            vehicleList.remove((Integer) (which));
                        }
                    }
                });

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringBuilder stringBuilder = new StringBuilder();

                        for (int j = 0; j < vehicleList.size(); j++) {
                            stringBuilder.append(vehicleArray[vehicleList.get(j)]);

                            if (j != vehicleList.size() - 1) {
                                stringBuilder.append(", ");
                            }
                        }

                        editDetSer_Category_ACTV.setText(stringBuilder.toString());
                    }
                });

//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });

                builder.setNeutralButton("CLear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int j = 0; j < selectedVehicleType.length; j++) {
                            selectedVehicleType[j] = false;
                            vehicleList.clear();
                            editDetSer_Category_ACTV.setText("");
                        }
                    }
                });

                builder.show();
            }
        });

        priceType.add("Fixed");
        priceType.add("Variable");

        adapterPriceType = new ArrayAdapter(this, R.layout.list_drop_down, priceType);


        editDetSer_PriceType_ACTV.setAdapter(adapterPriceType);
        editDetSer_PriceType_ACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (priceType.get(position).equals("Fixed")) {
//                    editDetSer_Price_TIET.setInputType(InputType.TYPE_NULL);
//                    editDetSer_Price_TIET.setFocusable(false);
                    editDetSer_Price_TIET.setEnabled(true);
//                    editDetSer_Price_TIET.setText(null);
                } else if (priceType.get(position).equals("Variable")) {
                    editDetSer_Price_TIET.setEnabled(false);
//                    editDetSer_Price_TIET.setFocusableInTouchMode(true);
                    editDetSer_Price_TIET.setText(null);
                }
            }
        });

        timeUnit.add("Minute(s)");
        timeUnit.add("Hour(s)");
        timeUnit.add("Day(s)");
        timeUnit.add("Variable");

        adapterTimeUnit = new ArrayAdapter(this, R.layout.list_drop_down, timeUnit);


        editDetSer_EstTimeUnit_ACTV.setAdapter(adapterTimeUnit);
        editDetSer_EstTimeUnit_ACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (timeUnit.get(position).equals("Variable")) {

                    editDetSer_EstTime_TIET.setText(null);
                    editDetSer_EstTime_TIET.setEnabled(false);

                } else {
                    editDetSer_EstTime_TIET.setEnabled(true);

                }
            }
        });

        pickUpFacility.add("Available");
        pickUpFacility.add("Not Available");

        adapterPickUpFacility = new ArrayAdapter(this, R.layout.list_drop_down, pickUpFacility);


        editDetSer_PickUp_ACTV.setAdapter(adapterPickUpFacility);

        editDetSer_PickUp_ACTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                finalVType = vTypesId.get(position);
            }
        });


        if (itemService.getID().equals("new")) {

            toolbar.setTitle("Add New Service");
            editDetSer_IV.setImageResource(R.drawable.ic_add_70_green);


            editDetSer_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editDetSer_Accept.setEnabled(false);

                    if (!(editDetSer_Name_TIET.getText().toString().trim().equals("")) && !(editDetSer_SerType_ACTV.getText().toString().trim().equals("")) && !(editDetSer_Category_ACTV.getText().toString().trim().equals("")) && !(editDetSer_PickUp_ACTV.getText().toString().trim().equals("")) && !(editDetSer_Det_TIET.getText().toString().trim().equals("")) && !(editDetSer_EstTimeUnit_ACTV.getText().toString().trim().equals("")) && !(editDetSer_PriceType_ACTV.getText().toString().trim().equals(""))) {

                        if (!(editDetSer_PriceType_ACTV.getText().toString().trim().equals("Variable")) && (editDetSer_Price_TIET.getText().toString().trim().equals("")))
                        {

                            editDetSer_Accept.setEnabled(true);
                            Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                        }
                        else if (!(editDetSer_EstTimeUnit_ACTV.getText().toString().trim().equals("Variable")) && (editDetSer_EstTime_TIET.getText().toString().trim().equals("")))
                        {

                            editDetSer_Accept.setEnabled(true);
                            Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                        }
                        else {

                            DocumentReference finalParentRef = fStore.collection("Services").document(serviceIDArrayList.get(selectedService));
                            DocumentReference finalWorkshopRef = fStore.collection("Workshops").document(fUser.getUid());

                            int finalPickUp;

                            if (editDetSer_PickUp_ACTV.getText().toString().trim().equals("Available")) {
                                finalPickUp = 1;
                            } else {
                                finalPickUp = 0;
                            }

                            String finalEstTime;

                            if (editDetSer_EstTimeUnit_ACTV.getText().toString().trim().equals("Variable")) {
                                finalEstTime = editDetSer_EstTimeUnit_ACTV.getText().toString().trim();
                            } else {
                                finalEstTime = editDetSer_EstTime_TIET.getText().toString().trim() + " " + editDetSer_EstTimeUnit_ACTV.getText().toString().trim();
                            }

                            double finalPrice;

                            if (editDetSer_PriceType_ACTV.getText().toString().trim().equals("Variable")) {
                                finalPrice = -1;
                            } else {
                                finalPrice = Double.parseDouble(editDetSer_Price_TIET.getText().toString().trim());
                            }

                            ArrayList<Integer> finalCateg = new ArrayList<>();
                            if (selectedVehicleType[0]) {
                                finalCateg.add(2);
                            }
                            if (selectedVehicleType[1]) {
                                finalCateg.add(4);

                            }
                            if (selectedVehicleType[2]) {
                                finalCateg.add(6);

                            }
                            if (selectedVehicleType[3]) {
                                finalCateg.add(3);

                            }

                            DocumentReference dReference = fStore.collection("Workshops").document(fUser.getUid()).collection("Services").document();
                            String ID = dReference.getId();

                            RecVewService newService = new RecVewService(ID, finalParentRef, finalWorkshopRef, editDetSer_Name_TIET.getText().toString().trim(), editDetSer_Det_TIET.getText().toString().trim(), finalPickUp, finalEstTime, finalPrice, finalCateg);

                            if(baos == null)
                            {
                                newService.setPic(null);

                                dReference.set(newService)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Map<String, Object> map = new HashMap<>();
                                                map.put("id", newService.getID());
                                                map.put("ref", dReference);

                                                finalParentRef.collection("Child Services").document(newService.getID()).set(map)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                // TODO : start ActivityForResult() wala finish

//                                                          Intent intent = new Intent(DetailsService.this, EditDetService.class);
                                                                //                                          intent.putExtra("itemService",  itemService);
                                                                //                                          startActivity(intent);
                                                                finish();

                                                                //Todo : over
                                                            }
                                                        });
                                            }
                                        });
                            }
                            else
                            {
                                StorageReference sReference = fStorage.getReference().child("Workshops").child(fUser.getUid()).child("Services").child(newService.getID()).child("pic1.jpeg");

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

                                                                        Map<String, Object> map = new HashMap<>();
                                                                        map.put("id", newService.getID());
                                                                        map.put("ref", dReference);

                                                                        finalParentRef.collection("Child Services").document(newService.getID()).set(map)
                                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                    @Override
                                                                                    public void onSuccess(Void aVoid) {
                                                                                        // TODO : start ActivityForResult() wala finish

//                                                          Intent intent = new Intent(DetailsService.this, EditDetService.class);
                                                                                        //                                          intent.putExtra("itemService",  itemService);
                                                                                        //                                          startActivity(intent);
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


                    } else {

                        editDetSer_Accept.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                    }

                }
            });

        }
        else {
            editDetSer_PB.setVisibility(View.VISIBLE);

            toolbar.setTitle(itemService.getName());

            if(itemService.getPic() == null)
            {
                editDetSer_IV.setImageResource(R.drawable.ic_add_70_green);
                editDetSer_PB.setVisibility(View.GONE);

            }
            else
            {

                StorageReference sReference = fStorage.getReference().child("Workshops").child(fUser.getUid()).child("Services").child(itemService.getID()).child("pic1.jpeg");

                sReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {

                        sReference.getBytes(storageMetadata.getSizeBytes()).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);

                                if(baos == null)
                                    editDetSer_IV.setImageBitmap(bitmap);

                                editDetSer_PB.setVisibility(View.GONE);

                            }
                        });
                    }
                });

            }

            editDetSer_Name_TIET.setText(itemService.getName());


            // TODO:  Check if itemService.getParentRef().getPath().substring(8) is correct or not

//            selectedServiceTypeID = itemService.getParentRef().getPath().substring(8);
//            Log.d("TAG", "RishabhJain + " + itemService.getParentRef().getPath().substring(8));

            //Todo : over


//            int pos = 0;
//            boolean valid = false;
//            for(String i : serviceIDArrayList)
//            {
//                if(i.equals(selectedServiceTypeID))
//                {
//                    selectedService = pos;
//                    selectedServiceType = serviceArrayList.get(pos);
//                    valid = true;
//                    break;
//                }
//                pos++;
//            }
//
//            if(!valid)
//            {
//                selectedService = -1;
//                selectedServiceType = null;
//            }
//
            editDetSer_RL2.setVisibility(View.GONE);


            String categ = "";
            for (int i : itemService.getCategory()) {
                if (i == 2) {
                    categ = categ + ", Bikes";
                    selectedVehicleType[0] = true;
                    vehicleList.add(0);
                    Collections.sort(vehicleList);
                } else if (i == 4) {
                    categ = categ + ", Cars";
                    selectedVehicleType[1] = true;
                    vehicleList.add(1);
                    Collections.sort(vehicleList);
                } else if (i == 6) {
                    categ = categ + ", Heavy";
                    selectedVehicleType[2] = true;
                    vehicleList.add(2);
                    Collections.sort(vehicleList);
                } else if (i == 3) {
                    categ = categ + ", Others";
                    selectedVehicleType[3] = true;
                    vehicleList.add(3);
                    Collections.sort(vehicleList);
                }
            }
            categ = categ.substring(2);
            editDetSer_Category_ACTV.setText(categ);


            if (itemService.getPrice() != -1) {
                editDetSer_Price_TIET.setText(Double.toString(itemService.getPrice()));
                editDetSer_PriceType_ACTV.setText(editDetSer_PriceType_ACTV.getAdapter().getItem(0).toString(), false);
            } else {
                editDetSer_Price_TIET.setEnabled(false);
                editDetSer_Price_TIET.setText(null);
                editDetSer_PriceType_ACTV.setText(editDetSer_PriceType_ACTV.getAdapter().getItem(1).toString(), false);
            }


            String partTime[] = itemService.getEstTime().split(" ");

            if (partTime[0].equals("Variable")) {
                editDetSer_EstTime_TIET.setText(null);
                editDetSer_EstTime_TIET.setEnabled(false);
                editDetSer_EstTimeUnit_ACTV.setText(editDetSer_EstTimeUnit_ACTV.getAdapter().getItem(3).toString(), false);
            } else {
                editDetSer_EstTime_TIET.setText(partTime[0]);

                if (partTime[1].equals("Minute(s)"))
                    editDetSer_EstTimeUnit_ACTV.setText(editDetSer_EstTimeUnit_ACTV.getAdapter().getItem(0).toString(), false);
                else if (partTime[1].equals("Hour(s)"))
                    editDetSer_EstTimeUnit_ACTV.setText(editDetSer_EstTimeUnit_ACTV.getAdapter().getItem(1).toString(), false);
                else if (partTime[1].equals("Day(s)"))
                    editDetSer_EstTimeUnit_ACTV.setText(editDetSer_EstTimeUnit_ACTV.getAdapter().getItem(2).toString(), false);

            }


            if (itemService.getPickUp() == 0)
                editDetSer_PickUp_ACTV.setText(editDetSer_PickUp_ACTV.getAdapter().getItem(1).toString(), false);

            else if (itemService.getPickUp() == 1)
                editDetSer_PickUp_ACTV.setText(editDetSer_PickUp_ACTV.getAdapter().getItem(0).toString(), false);


            editDetSer_Det_TIET.setText(itemService.getDetails());
//
            editDetSer_Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editDetSer_Accept.setEnabled(false);

                    if (!(editDetSer_Name_TIET.getText().toString().trim().equals("")) && !(editDetSer_Category_ACTV.getText().toString().trim().equals("")) && !(editDetSer_PickUp_ACTV.getText().toString().trim().equals("")) && !(editDetSer_Det_TIET.getText().toString().trim().equals("")) && !(editDetSer_EstTimeUnit_ACTV.getText().toString().trim().equals("")) && !(editDetSer_PriceType_ACTV.getText().toString().trim().equals(""))) {

                        if (!(editDetSer_PriceType_ACTV.getText().toString().trim().equals("Variable")) && (editDetSer_Price_TIET.getText().toString().trim().equals("")))
                        {

                            editDetSer_Accept.setEnabled(true);
                            Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                        }
                        else if (!(editDetSer_EstTimeUnit_ACTV.getText().toString().trim().equals("Variable")) && (editDetSer_EstTime_TIET.getText().toString().trim().equals("")))
                        {
                            editDetSer_Accept.setEnabled(true);
                            Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                        }
                        else {

                            int finalPickUp;

                            if (editDetSer_PickUp_ACTV.getText().toString().trim().equals("Available")) {
                                finalPickUp = 1;
                            } else {
                                finalPickUp = 0;
                            }

                            String finalEstTime;

                            if (editDetSer_EstTimeUnit_ACTV.getText().toString().trim().equals("Variable")) {
                                finalEstTime = editDetSer_EstTimeUnit_ACTV.getText().toString().trim();
                            } else {
                                finalEstTime = editDetSer_EstTime_TIET.getText().toString().trim() + " " + editDetSer_EstTimeUnit_ACTV.getText().toString().trim();
                            }

                            double finalPrice;

                            if (editDetSer_PriceType_ACTV.getText().toString().trim().equals("Variable")) {
                                finalPrice = -1;
                            } else {
                                finalPrice = Double.parseDouble(editDetSer_Price_TIET.getText().toString().trim());
                            }

                            ArrayList<Integer> finalCateg = new ArrayList<>();
                            if (selectedVehicleType[0]) {
                                finalCateg.add(2);
                            }
                            if (selectedVehicleType[1]) {
                                finalCateg.add(4);

                            }
                            if (selectedVehicleType[2]) {
                                finalCateg.add(6);

                            }
                            if (selectedVehicleType[3]) {
                                finalCateg.add(3);

                            }

                            RecVewService newService = new RecVewService(itemService.getID(), itemService.getParentRef(), itemService.getWorkShopRef(), editDetSer_Name_TIET.getText().toString().trim(), editDetSer_Det_TIET.getText().toString().trim(), finalPickUp, finalEstTime, finalPrice, finalCateg);




                            if(baos == null)
                            {
                                newService.setPic(itemService.getPic());

                                // TODO : below TODO is very important also correct in other 2 apps
                                // TODO : check if the nested collection of all particular service if this general service type is deleted or not        --> Added setOptions.merge() but not checked yet
                                fStore.collection("Workshops").document(fUser.getUid()).collection("Services").document(newService.getID()).set(newService, SetOptions.merge())
                                        //Todo : over

                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                // TODO : start ActivityForResult() wala finish

//                                          Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                          intent.putExtra("itemService",  itemService);
//                                          startActivity(intent);
                                                finish();

                                                //Todo : over
                                            }
                                        });
                                //Todo : over
                            }
                            else
                            {
                                StorageReference sReference = fStorage.getReference().child("Workshops").child(fUser.getUid()).child("Services").child(newService.getID()).child("pic1.jpeg");

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
                                                        fStore.collection("Workshops").document(fUser.getUid()).collection("Services").document(newService.getID()).set(newService, SetOptions.merge())
                                                                //Todo : over

                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        // TODO : start ActivityForResult() wala finish

//                                          Intent intent = new Intent(DetailsService.this, EditDetService.class);
//                                          intent.putExtra("itemService",  itemService);
//                                          startActivity(intent);
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

                        }


                    }
                    else {

                        editDetSer_Accept.setEnabled(true);
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

                        editDetSer_Accept.setEnabled(false);

                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);


                        editDetSer_PB.setVisibility(View.VISIBLE);
                        editDetSer_IV.setImageBitmap(bitmap);
                        editDetSer_PB.setVisibility(View.GONE);
                        editDetSer_Accept.setEnabled(true);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar.make(editDetSer_Name_TIET, "Select an Image", Snackbar.LENGTH_SHORT).show();
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

                    editDetSer_Accept.setEnabled(false);

                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                    editDetSer_PB.setVisibility(View.VISIBLE);
                    editDetSer_IV.setImageBitmap(bitmap);
                    editDetSer_PB.setVisibility(View.GONE);
                    editDetSer_Accept.setEnabled(true);

                }

                else{

                    Snackbar.make(editDetSer_Name_TIET, "Capture an Image", Snackbar.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(EditDetService.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);
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
                            ActivityCompat.requestPermissions(EditDetService.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
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
                            ActivityCompat.requestPermissions(EditDetService.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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

            if (ActivityCompat.checkSelfPermission(EditDetService.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditDetService.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(EditDetService.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);

            }
            else if(ActivityCompat.checkSelfPermission(EditDetService.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(EditDetService.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
            }
            else if(ActivityCompat.checkSelfPermission(EditDetService.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(EditDetService.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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
                Snackbar.make(editDetSer_Name_TIET, "Gallery and Camera Permissions Granted" , Snackbar.LENGTH_SHORT).show();
                openUploadChoiceDialogBox();
            }
            else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetSer_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
                openGallery();
            }
            else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetSer_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
                openCamera();
            }
            else
            {
                Snackbar.make(editDetSer_Name_TIET, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetSer_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(editDetSer_Name_TIET, "Gallery Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openCamera();
            }
        }

        else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(editDetSer_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(editDetSer_Name_TIET, "Camera Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openGallery();
            }
        }
    }
}