package com.cmdrj.caremydriveworkshop;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    int GALLERY_CAMERA_PERMISSION_REQUEST_CODE = 10012;
    int GALLERY_PERMISSION_REQUEST_CODE = 10013;
    int CAMERA_PERMISSION_REQUEST_CODE = 10014;
    int READ_GALLERY_REQUEST_CODE = 100015;
    int CAPTURE_CAMERA_REQUEST_CODE = 10016;



    int LOCATION_REQUEST_CODE = 10005;
    int GPS_ON_REQUEST_CODE = 10006;

    TextInputEditText register_Name_TIET, register_Email_TIET, register_PhoneNo_TIET, register_Address_TIET, register_City_TIET, register_State_TIET, register_Country_TIET, register_Pincode_TIET;
    Button register_Accept;
    ImageView register_IV, register_Email_IV, register_PhoneNo_IV, register_Address_IV;
    LinearLayout register_currentLocation_LL, register_addLocation_LL;
    ProgressBar register_PB;

    ByteArrayOutputStream baos = null;


    private GeoPoint AddressGeoPoint = null;
    private String Address = null;
    private String City = null;
    private String State = null;
    private String Country = null;
    private String Pincode = null;

    private boolean register_currentLocation = false, register_addLocation = false;


    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Location currentLocation = null, lastLocation = null;


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {

            super.onLocationResult(locationResult);

            stopLocationUpdates();


            if (locationResult != null && locationResult.getLocations().size() >0){

                int index = locationResult.getLocations().size() - 1;
                currentLocation = locationResult.getLocations().get(index);

                double latitude = locationResult.getLocations().get(index).getLatitude();
                double longitude = locationResult.getLocations().get(index).getLongitude();


                Geocoder geocoder = new Geocoder(RegisterActivity.this);
                List<android.location.Address> addressList = null;

                try {
                    addressList = geocoder.getFromLocation(latitude,longitude,1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addressList == null)
                {
                    Snackbar.make(register_Accept, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

                }
                else if (addressList.size() <= 0) {
                    Snackbar.make(register_Accept, "No Locations Found. Please use \"Add Manually\" option.", Snackbar.LENGTH_SHORT).show();

                } else {

                    Address address = addressList.get(0);

                    AddressGeoPoint = new GeoPoint(latitude,longitude);
                    Address = address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality();
                    City = address.getSubAdminArea();
                    State = address.getAdminArea();
                    Country = address.getCountryName();
                    Pincode = address.getPostalCode();

                    register_Address_TIET.setText(Address);
                    register_City_TIET.setText(City);
                    register_State_TIET.setText(State);
                    register_Country_TIET.setText(Country);
                    register_Pincode_TIET.setText(Pincode);


//                    Log.d("TAG", "RishabhJain + " + address.toString());
//                    Log.d("TAG", "RishabhJain + " + Address);

                }

//                AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
            }

            /* TODO : By YoursTruly*/
////            super.onLocationResult(locationResult);

//            if (locationResult == null) {
//                return;
//            }
//
//            for (Location location : locationResult.getLocations()) {
//                // TODO : do something
//
//                Log.d("TAG", "RishabhJain +  " + location.toString());
//
//                AddressText.setText("Latitude: "+ location.getLatitude() + "\n" + "Longitude: "+ location.getLongitude());
//                // TODO : over
//            }

            /* TODO : over: By YoursTruly */
        }
    };

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if(result != null && result.getResultCode() == RESULT_OK)
                    {
                        Intent intent = result.getData();
                        if(intent != null)
                        {

                            boolean itemNewLocation = intent.getBooleanExtra("itemNewLocation",true);
                            double itemLatitude = intent.getDoubleExtra("itemLatitude",-1);
                            double itemLongitude = intent.getDoubleExtra("itemLongitude",-1);
                            Address = intent.getStringExtra("itemAddress");
                            City = intent.getStringExtra("itemCity");
                            State = intent.getStringExtra("itemState");
                            Country = intent.getStringExtra("itemCountry");
                            Pincode = intent.getStringExtra("itemPincode");



                            if(itemNewLocation)
                            {
                                lastLocation = new Location("");  //provider name is unnecessary
                                lastLocation.setLatitude(itemLatitude);
                                lastLocation.setLongitude(itemLongitude);
                            }



                            AddressGeoPoint = new GeoPoint(itemLatitude,itemLongitude);




                            register_Address_TIET.setText(Address);
                            register_City_TIET.setText(City);
                            register_State_TIET.setText(State);
                            register_Country_TIET.setText(Country);
                            register_Pincode_TIET.setText(Pincode);


//                            Toast.makeText(RegisterActivity.this,Address + "\nLat:  " + itemLatitude + "\nLong: "+ itemLongitude,Toast.LENGTH_SHORT).show();

                        }
                    }

                }
            }
    );









    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;
    private FirebaseStorage fStorage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();
        fStorage = FirebaseStorage.getInstance();

        register_Name_TIET = findViewById(R.id.register_Name_TIET);
        register_Email_TIET = findViewById(R.id.register_Email_TIET);
        register_PhoneNo_TIET = findViewById(R.id.register_PhoneNo_TIET);
        register_Address_TIET = findViewById(R.id.register_Address_TIET);
        register_City_TIET = findViewById(R.id.register_City_TIET);
        register_State_TIET = findViewById(R.id.register_State_TIET);
        register_Country_TIET = findViewById(R.id.register_Country_TIET);
        register_Pincode_TIET = findViewById(R.id.register_Pincode_TIET);

        register_IV = findViewById(R.id.register_IV);
        register_PB = findViewById(R.id.register_PB);
        register_Email_IV = findViewById(R.id.register_Email_IV);
        register_PhoneNo_IV = findViewById(R.id.register_PhoneNo_IV);
        register_Address_IV = findViewById(R.id.register_Address_IV);


        register_currentLocation_LL = findViewById(R.id.register_currentLocation_LL);
        register_addLocation_LL = findViewById(R.id.register_addLocation_LL);



        register_Accept = findViewById(R.id.register_Accept);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        register_PhoneNo_TIET.setText(fUser.getPhoneNumber());
        register_IV.setImageResource(R.drawable.ic_add_70_green);


        register_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openUploadChoiceDialogBox();

                }
                else if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openGallery();
                }
                else if(ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                {
                    askStoragePermission();
//                    openCamera();
                }
                else {
                    askStoragePermission();
                }

            }
        });



        register_currentLocation_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register_addLocation = false;
                register_currentLocation = true;

                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    checkSettingsAndStartUpdates();

                } else {
                    askLocationPermission();
                }

            }
        });

        register_addLocation_LL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                register_addLocation = true;
                register_currentLocation = false;


                if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    checkSettingsAndStartUpdates();

                } else {
                    askLocationPermission();
                }

            }
        });

        register_Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // TODO : in addition to button disabled and enabled implement this too everywhere
//                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
//                    return;
//                }
//                mLastClickTime = SystemClock.elapsedRealtime();

                // TODO ; over

                register_Accept.setEnabled(false);

                if (!(register_Name_TIET.getText().toString().trim().equals("")) && !(register_Email_TIET.getText().toString().trim().equals("")) && AddressGeoPoint != null ) {

                    if(Address == null || City == null || State == null || Country == null || Pincode == null)
                    {
                        register_Accept.setEnabled(true);
                        Snackbar.make(findViewById(android.R.id.content), "Please Select \"Add Manually\" option and long press on maps to select nearby area having all 5 address fields", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {

                        RecVewWorkshop workshop = new RecVewWorkshop(fUser.getUid(), register_Name_TIET.getText().toString().trim(), fUser.getPhoneNumber(), register_Email_TIET.getText().toString().trim(), AddressGeoPoint, Address, City, State, Country, Pincode);




                        if(baos == null)
                        {
                            workshop.setPic(null);

                            fStore.collection("Workshops").document(workshop.getID()).set(workshop).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

//                            Map<String, Object> map = new HashMap<>();
//                            map.put("Len", FieldValue.increment(1));
//
//                            fStore.collection("Size").document("Workshops").update(map)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                                        }
//                                    });
                                }
                            });
                        }
                        else
                        {
                            StorageReference sReference = fStorage.getReference().child("Workshops").child(workshop.getID()).child("pic1.jpeg");

                            sReference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    if (taskSnapshot.getMetadata() != null) {
                                        if (taskSnapshot.getMetadata().getReference() != null) {

                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();

                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    workshop.setPic(uri.toString());




                                                    fStore.collection("Workshops").document(workshop.getID()).set(workshop).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

//                            Map<String, Object> map = new HashMap<>();
//                            map.put("Len", FieldValue.increment(1));
//
//                            fStore.collection("Size").document("Workshops").update(map)
//                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
                                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                                        }
//                                    });
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

                    register_Accept.setEnabled(true);
                    Snackbar.make(findViewById(android.R.id.content), "Please fill all the details", Snackbar.LENGTH_SHORT).show();

                }
            }
        });

    }


















    private void checkSettingsAndStartUpdates() {

        LocationSettingsRequest locationSettingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = settingsClient.checkLocationSettings(locationSettingsRequest);

        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {


                if(register_addLocation && !register_currentLocation)
                {
                    openChooseLocation();
                }
                else if(!register_addLocation && register_currentLocation)
                {
                    getCurrentLocation();

                }
            }
        });

        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(RegisterActivity.this, GPS_ON_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                } else {
                    // TODO : do something
                }

            }
        });

    }

    private void openChooseLocation() {

        Intent intent = new Intent(RegisterActivity.this, ChooseLocation.class);
        intent.putExtra("parentActivity",0);


        if(lastLocation != null)
        {
            intent.putExtra("noOfParameters",1);
            intent.putExtra("itemLastLocation",lastLocation);
        }
        else
        {
            intent.putExtra("noOfParameters",0);
        }

        activityResultLauncher.launch(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

    }

    private void askLocationPermission() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                // TODO : create Alert Dialog box and when click on "OK" ask for permission

                Snackbar.make(findViewById(android.R.id.content),"SHow alert Dialog Box", Snackbar.LENGTH_SHORT).show();
                //TODO : over

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);
            }
            else
            {
                Snackbar.make(findViewById(android.R.id.content),"SHow alert Dialog Box", Snackbar.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_REQUEST_CODE);

            }

        }
        else
        {
            // TODO : enter something here too

            // TODO : over
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                // TODO : Snackbar Permission granted

                checkSettingsAndStartUpdates();
            }
            else
            {
                //TODO : Snackbar Permission not granted + something

            }

        }





        else if (requestCode == GALLERY_CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(register_Name_TIET, "Gallery and Camera Permissions Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(register_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openGallery();
            }
            else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(register_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openCamera();
            }
            else
            {
                Snackbar.make(register_Name_TIET, "Both Gallery and Camera Permissions Denied.\nAtleast one required permission to move forward." , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        else if (requestCode == GALLERY_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(register_Name_TIET, "Gallery Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(register_Name_TIET, "Gallery Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openCamera();
            }
        }

        else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Snackbar.make(register_Name_TIET, "Camera Permission Granted" , Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                performFileSearch();
                openUploadChoiceDialogBox();
            }
            else
            {
                Snackbar.make(register_Name_TIET, "Camera Permission Denied" , Snackbar.LENGTH_SHORT).show();

//                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                openGallery();
            }
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

                        register_Accept.setEnabled(false);

                        baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);

                        register_PB.setVisibility(View.VISIBLE);
                        register_IV.setImageBitmap(bitmap);
                        register_PB.setVisibility(View.GONE);
                        register_Accept.setEnabled(true);


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    Snackbar.make(register_Name_TIET, "Select an Image", Snackbar.LENGTH_SHORT).show();
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

                    register_Accept.setEnabled(false);

                    baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);


                    register_PB.setVisibility(View.VISIBLE);
                    register_IV.setImageBitmap(bitmap);
                    register_PB.setVisibility(View.GONE);
                    register_Accept.setEnabled(true);

                }

                else{

                    Snackbar.make(register_Name_TIET, "Capture an Image", Snackbar.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);
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
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
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
                            ActivityCompat.requestPermissions(RegisterActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
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

            if (ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, GALLERY_CAMERA_PERMISSION_REQUEST_CODE);

            }
            else if(ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_PERMISSION_REQUEST_CODE);
            }
            else if(ActivityCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
            }
//            else {
//                askStoragePermission();
//            }
        }
    }
}