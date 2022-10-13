package com.cmdrj.caremydriveworkshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChooseLocation extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private int ACCESS_LOCATION_REQUEST_CODE = 10004;

    private GoogleMap mMap;

    int parentActivity, noOfParameters;
    Location currentLocation = null, lastLocation = null, permanentLocation = null;
    Marker currentMarker = null, permanentMarker = null;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;


    private View mMapView;




    androidx.appcompat.widget.SearchView chooseLocation_SV;
    ProgressBar progressBar;
    View locationButton;
    boolean performActionProgressBar = false;

    Button chooseLocation_SubmitLocation;

    String selectedLocationAddress;
    int selectedLocation = -1;
    String[] locationArray = new String[]{};
    ArrayList<String> locationArrayList = new ArrayList<>();
    ArrayList<Marker> markerArrayList = new ArrayList<>();
    ArrayList<ArrayList<String>> addressesArrayList = new ArrayList<ArrayList<String>>();
    private int pos = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        this.setRequestedOrientation(  ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mMapView = mapFragment.getView();
        chooseLocation_SV = findViewById(R.id.chooseLocation_SV);
        chooseLocation_SubmitLocation = findViewById(R.id.chooseLocation_SubmitLocation);




        Intent intent = getIntent();

        if (intent != null && intent.getExtras() != null) {

            parentActivity = intent.getExtras().getInt("parentActivity");
            noOfParameters = intent.getExtras().getInt("noOfParameters");

            // parentActivity = 0  -> registerActivity         parentActivity = 1  ->  confirmBooking
            if(parentActivity == 0)
            {
                if(noOfParameters == 1)
                {
                    lastLocation = intent.getExtras().getParcelable("itemLastLocation");
                }
            }
            else if(parentActivity == 1)
            {
                if(noOfParameters == 1)
                {
                    permanentLocation = intent.getExtras().getParcelable("itemPermanentLocation");
                }
                else if(noOfParameters == 2)
                {
                    permanentLocation = intent.getExtras().getParcelable("itemPermanentLocation");
                    lastLocation = intent.getExtras().getParcelable("itemLastLocation");
                }
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ChooseLocation.this);
        geocoder = new Geocoder(ChooseLocation.this);



        chooseLocation_SubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseLocation.this);

                builder.setTitle("Select Location");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(locationArray, selectedLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedLocationAddress = locationArray[which];
//                        selectedServiceTypeID = serviceIDArray[which];
                    }
                });

                builder.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int pos = 0;
                        boolean valid = false;
                        boolean newLocationSelected = true;
                        for (String item : locationArray) {
                            if (item.equals(selectedLocationAddress)) {
                                selectedLocation = pos;
                                valid = true;


                                if((permanentMarker != null && markerArrayList.get(pos).equals(permanentMarker)) || (currentMarker != null && markerArrayList.get(pos).equals(currentMarker)))
                                {
                                    newLocationSelected = false;
                                }
                                break;
                            }
                            pos++;

                        }

                        if (!valid) {
                            selectedLocation = -1;
                            selectedLocationAddress = null;

                            Snackbar.make(v, "Please Select Some Location.", Snackbar.LENGTH_SHORT).show();

                        } else {


                            Intent intent = new Intent();

                            intent.putExtra("itemNewLocation", newLocationSelected);
                            intent.putExtra("itemLatitude", markerArrayList.get(selectedLocation).getPosition().latitude);
                            intent.putExtra("itemLongitude", markerArrayList.get(selectedLocation).getPosition().longitude);
                            intent.putExtra("itemAddress", addressesArrayList.get(selectedLocation).get(0));
                            intent.putExtra("itemCity", addressesArrayList.get(selectedLocation).get(1));
                            intent.putExtra("itemState", addressesArrayList.get(selectedLocation).get(2));
                            intent.putExtra("itemCountry", addressesArrayList.get(selectedLocation).get(3));
                            intent.putExtra("itemPincode", addressesArrayList.get(selectedLocation).get(4));
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedLocationAddress = null;
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });

        if (chooseLocation_SV != null && chooseLocation_SV.findViewById(R.id.search_close_btn) != null) {

            performActionProgressBar = true;
            locationButton = ((View) chooseLocation_SV.findViewById(R.id.search_close_btn));


            progressBar = new ProgressBar(ChooseLocation.this, null, android.R.attr.progressBarStyleLarge);
            progressBar.setScaleY(.5f);
            progressBar.setScaleX(.5f);
//            progressBar.getIndeterminateDrawable().setColorFilter(0xFF6689F9, android.graphics.PorterDuff.Mode.SRC_ATOP);

            ViewGroup.LayoutParams layoutParams = locationButton.getLayoutParams();
            progressBar.setLayoutParams(layoutParams);

            ((ViewGroup) chooseLocation_SV.findViewById(R.id.search_close_btn).getParent()).addView(progressBar, 1);
            progressBar.setVisibility(View.GONE);

        } else {
            performActionProgressBar = false;
        }

        if (mMapView != null && mMapView.findViewById(Integer.parseInt("1")) != null) {

            DisplayMetrics metrics = getResources().getDisplayMetrics();


            View locationButton = ((View) mMapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.setMargins(0, ((metrics.heightPixels) / 2) - locationButton.getHeight(), 16, 0);


            View locationButton4 = ((View) mMapView.findViewById(Integer.parseInt("2")).getParent()).findViewById(Integer.parseInt("4"));
            RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) locationButton4.getLayoutParams();
            layoutParams4.setMargins(0, 0, 16, ((metrics.heightPixels) / 2) - locationButton4.getHeight());


            View locationButton5 = ((View) mMapView.findViewById(Integer.parseInt("2")).getParent()).findViewById(Integer.parseInt("5"));
            RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) locationButton5.getLayoutParams();
            layoutParams5.setMargins(0, 0, 16, ((metrics.heightPixels) / 2) - locationButton4.getHeight());

        }

        chooseLocation_SV.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                chooseLocation_SV.clearFocus();
                showProgressBar();


                String loc = chooseLocation_SV.getQuery().toString();
                List<Address> addressList = null;

                if (loc != null || !loc.trim().equals("")) {

                    try {
                        addressList = geocoder.getFromLocationName(loc, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList == null) {
                        Snackbar.make(chooseLocation_SV, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

                    } else if (addressList.size() <= 0) {
                        Snackbar.make(chooseLocation_SV, "No Locations Found. Please enter some popular places.", Snackbar.LENGTH_SHORT).show();

                    } else {

                        Address address = addressList.get(0);

                        LatLng addLatlong = new LatLng(address.getLatitude(), address.getLongitude());
                        MarkerOptions addMarkerOptions = new MarkerOptions().position(addLatlong).title(address.getAddressLine(0)).draggable(true).snippet("New Location");

                        CameraUpdate addcameraUpdate = CameraUpdateFactory.newLatLngZoom(addLatlong, 16);
                        Marker marker = mMap.addMarker(addMarkerOptions);
                        mMap.animateCamera(addcameraUpdate);

                        locationArrayList.add(address.getAddressLine(0));
                        locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);
                        markerArrayList.add(marker);

                        ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
                        tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
                        tempAddressesArrayList.add(address.getSubAdminArea());
                        tempAddressesArrayList.add(address.getAdminArea());
                        tempAddressesArrayList.add(address.getCountryName());
                        tempAddressesArrayList.add(address.getPostalCode());

                        addressesArrayList.add(tempAddressesArrayList);

                    }
                }

                hideProgressBar();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }




    public void showProgressBar()
    {
        if(performActionProgressBar) {
            locationButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
//            if (sv_location.findViewById(R.id.search_close_btn).findViewById(R.id.search_progress_bar) != null)
//            {
//                sv_location.findViewById(R.id.search_close_btn).findViewById(R.id.search_progress_bar).animate().setDuration(200).alpha(1).start();
//            }
//
//            else
//            {
//                ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleLarge);
//                progressBar.setScaleY(.5f);
//                progressBar.setScaleX(.5f);
//
////                View v = LayoutInflater.from(context).inflate(R.layout.loading_icon_search_view, null);
//
//                ViewGroup.LayoutParams layoutParams = locationButton.getLayoutParams();
//                progressBar.setLayoutParams(layoutParams);
//
//                ((ViewGroup) sv_location.findViewById(R.id.search_close_btn).getParent()).addView(progressBar, 1);
//            }
//
//
//        }
    }

    public void hideProgressBar()
    {
        if(performActionProgressBar) {
            locationButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
//        int id = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
//        if (searchView.findViewById(id).findViewById(R.id.search_progress_bar) != null)
//            searchView.findViewById(id).findViewById(R.id.search_progress_bar).animate().setDuration(200).alpha(0).start();
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        if (lastLocation != null) {

            LatLng latLng = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(lastLocation.getLatitude(),lastLocation.getLongitude(), 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses == null)
            {
                Snackbar.make(chooseLocation_SV, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

            }
            else if (addresses.size() <= 0) {
                Snackbar.make(chooseLocation_SV, "Last Location can not be Found.", Snackbar.LENGTH_SHORT).show();

            } else {
                Address address = addresses.get(0);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address.getAddressLine(0)).snippet("Last Saved Location");

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                Marker marker = mMap.addMarker(markerOptions);
                mMap.animateCamera(cameraUpdate);


                locationArrayList.add(address.getAddressLine(0));
                locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);

                markerArrayList.add(marker);

                ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
                tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
                tempAddressesArrayList.add(address.getSubAdminArea());
                tempAddressesArrayList.add(address.getAdminArea());
                tempAddressesArrayList.add(address.getCountryName());
                tempAddressesArrayList.add(address.getPostalCode());

                addressesArrayList.add(tempAddressesArrayList);

            }







//            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title().snippet("Last Saved Location");
//
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
//            Marker marker = mMap.addMarker(markerOptions);
//            mMap.animateCamera(cameraUpdate);
//
//
//            Address address = (geocoder.getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1)).get(0);
//
//            locationArrayList.add(address.getAddressLine(0));
//            locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);
//
//            markerArrayList.add(marker);
//
//
//            ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
//            tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
//            tempAddressesArrayList.add(address.getSubAdminArea());
//            tempAddressesArrayList.add(address.getAdminArea());
//            tempAddressesArrayList.add(address.getCountryName());
//            tempAddressesArrayList.add(address.getPostalCode());
//
//            addressesArrayList.add(tempAddressesArrayList);

        }



        if (permanentLocation != null) {


            LatLng latLng = new LatLng(permanentLocation.getLatitude(),permanentLocation.getLongitude());

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(permanentLocation.getLatitude(),permanentLocation.getLongitude(), 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses == null)
            {
                Snackbar.make(chooseLocation_SV, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

            }
            else if (addresses.size() <= 0) {
                Snackbar.make(chooseLocation_SV, "Permanent Location can not be Found.", Snackbar.LENGTH_SHORT).show();

            } else {
                Address address = addresses.get(0);
                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address.getAddressLine(0)).snippet("Permanent Address");

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                Marker marker = mMap.addMarker(markerOptions);
                mMap.animateCamera(cameraUpdate);


                locationArrayList.add(address.getAddressLine(0));
                locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);

                markerArrayList.add(marker);
                permanentMarker = marker;

                ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
                tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
                tempAddressesArrayList.add(address.getSubAdminArea());
                tempAddressesArrayList.add(address.getAdminArea());
                tempAddressesArrayList.add(address.getCountryName());
                tempAddressesArrayList.add(address.getPostalCode());

                addressesArrayList.add(tempAddressesArrayList);

            }






//            LatLng latLng = new LatLng(permanentLocation.getLatitude(), permanentLocation.getLongitude());
//            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address.getAddressLine(0)).snippet("Permanent Address");
//
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
//            Marker marker = mMap.addMarker(markerOptions);
//            mMap.animateCamera(cameraUpdate);
//
//            Address address = (geocoder.getFromLocation(permanentLocation.getLatitude(), permanentLocation.getLongitude(), 1)).get(0);
//
//            locationArrayList.add(address.getAddressLine(0));
//            locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);
//
//            markerArrayList.add(marker);
//
//            ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
//            tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
//            tempAddressesArrayList.add(address.getSubAdminArea());
//            tempAddressesArrayList.add(address.getAdminArea());
//            tempAddressesArrayList.add(address.getCountryName());
//            tempAddressesArrayList.add(address.getPostalCode());
//
//            addressesArrayList.add(tempAddressesArrayList);



        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            enableUserLocation();
            zoomToUserLocation();
        }
        else
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // TODO : create Alert Dialog box and when click on "OK" ask for permission

//                    Snackbar.make(R.id.content,"SHow alert Dialog Box", Snackbar.LENGTH_SHORT).show();
                //TODO : over

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);

            }
        }

    }




    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses == null)
        {
            Snackbar.make(chooseLocation_SV, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

        }
        else if (addresses.size() <= 0) {
            Snackbar.make(chooseLocation_SV, "No Locations Found. Please enter some popular places.", Snackbar.LENGTH_SHORT).show();

        } else {
            Address address = addresses.get(0);
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address.getAddressLine(0)).draggable(true).snippet("New Location");

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            Marker marker = mMap.addMarker(markerOptions);
            mMap.animateCamera(cameraUpdate);


            locationArrayList.add(address.getAddressLine(0));
            locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);
            markerArrayList.add(marker);

            ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
            tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
            tempAddressesArrayList.add(address.getSubAdminArea());
            tempAddressesArrayList.add(address.getAdminArea());
            tempAddressesArrayList.add(address.getCountryName());
            tempAddressesArrayList.add(address.getPostalCode());

            addressesArrayList.add(tempAddressesArrayList);

        }

    }




    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {

        LatLng latLng = marker.getPosition();

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses == null)
        {
            Snackbar.make(chooseLocation_SV, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

        }
        else if (addresses.size() <= 0) {
            Snackbar.make(chooseLocation_SV, "No Locations Found. Please enter some popular places.", Snackbar.LENGTH_SHORT).show();

        } else {
            Address address = addresses.get(0);

            locationArrayList.set(pos,address.getAddressLine(0));
            locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);
            marker.setTitle(address.getAddressLine(0));


            CameraUpdate addcameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            mMap.animateCamera(addcameraUpdate);


            ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
            tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
            tempAddressesArrayList.add(address.getSubAdminArea());
            tempAddressesArrayList.add(address.getAdminArea());
            tempAddressesArrayList.add(address.getCountryName());
            tempAddressesArrayList.add(address.getPostalCode());

            addressesArrayList.set(pos,tempAddressesArrayList);
//            pos = -1;




//            Toast.makeText(ChooseLocation.this,marker.getTitle(),Toast.LENGTH_SHORT).show();
//
//            Log.d("TAG", "RishabhJain + marker ------------- " + marker.getPosition().latitude + "  " + marker.getPosition().longitude  );
//            Log.d("TAG", "RishabhJain + addres ------------- " + address.getLatitude() + "  " + address.getLongitude()  );
//
//            String addressLocality = address.getLocality();
//            Log.d("TAG", "RishabhJain + localityString " + addressLocality);
//
//
//            String name = address.getFeatureName();
//            Log.d("TAG", "RishabhJain + name " + name);
//
//            String subLocality = address.getSubLocality();
//            Log.d("TAG", "RishabhJain + subLocality " + subLocality);
//
//            String country = address.getCountryName();
//            Log.d("TAG", "RishabhJain +country " + country);
//
//            String country_code = address.getCountryCode();
//            Log.d("TAG", "RishabhJain +region_code " + country_code);
//
//            String zipcode = address.getPostalCode();
//            Log.d("TAG", "RishabhJain +zipcode " + zipcode);
//
//            String state = address.getAdminArea();
//            Log.d("TAG", "RishabhJain +state " + state);
//
//            String city = address.getSubAdminArea();
//            Log.d("TAG", "RishabhJain +subAdminArea " + city);
        }

    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {


        pos = 0;
        for(Marker item : markerArrayList)
        {

            // TODO : Check if it is correct way to equate marker
            if(item.equals(marker))
            {
                break;
            }

            //TODO : over
            pos++;
        }

    }




    private void enableUserLocation() {
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
        mMap.setMyLocationEnabled(true);
    }





    private void zoomToUserLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                currentLocation = location;


                if(currentLocation != null)
                {
                    if(permanentLocation == null || (permanentLocation != null && currentLocation.distanceTo(permanentLocation) > 0.1))
                    {
                        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                        List<Address> addresses = null;

                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(addresses == null)
                        {
                            Snackbar.make(chooseLocation_SV, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();

                        }
                        else if (addresses.size() <= 0) {
                            Snackbar.make(chooseLocation_SV, "Current Location can not be Found.", Snackbar.LENGTH_SHORT).show();

                        } else {
                            Address address = addresses.get(0);
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(address.getAddressLine(0)).snippet("Current Location" + "(At the time of opening of map)");// TODO : try this once (2 differences) :  MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Current Location" + "\n(At the time of opening of map)").snippet(); // TODO : over

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
                            Marker marker = mMap.addMarker(markerOptions);
                            mMap.animateCamera(cameraUpdate);


                            locationArrayList.add(address.getAddressLine(0));
                            locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);

                            markerArrayList.add(marker);
                            currentMarker = marker;

                            ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
                            tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
                            tempAddressesArrayList.add(address.getSubAdminArea());
                            tempAddressesArrayList.add(address.getAdminArea());
                            tempAddressesArrayList.add(address.getCountryName());
                            tempAddressesArrayList.add(address.getPostalCode());


//                    Log.d("TAG", "RishabhJain + " + address.toString());
//                    Log.d("TAG", "RishabhJain + " + tempAddressesArrayList.get(0));

                            addressesArrayList.add(tempAddressesArrayList);

                        }







//                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title().snippet("Current Location" + "(At the time of opening of map)");
//
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
//                Marker marker = mMap.addMarker(markerOptions);
//                mMap.animateCamera(cameraUpdate);
//
//
//                    locationArrayList.add(address.getAddressLine(0));
//                    locationArray = locationArrayList.toArray(new String[locationArrayList.size()]);
//
//                    markerArrayList.add(marker);
//
//                    ArrayList<String> tempAddressesArrayList = new ArrayList<String>();
//                    tempAddressesArrayList.add(address.getFeatureName() + ", " + address.getSubLocality() + ", " + address.getLocality());
//                    tempAddressesArrayList.add(address.getSubAdminArea());
//                    tempAddressesArrayList.add(address.getAdminArea());
//                    tempAddressesArrayList.add(address.getCountryName());
//                    tempAddressesArrayList.add(address.getPostalCode());
//
//                    addressesArrayList.add(tempAddressesArrayList);
                    }
                }
                else
                {
                    Snackbar.make(chooseLocation_SV, "No Internet Connection. Or try Restarting your device once.", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // TODO : do something
                // TODO : over
            }
        });

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO : Snackbar Permission granted

                enableUserLocation();
                zoomToUserLocation();

                // TODO : over
            } else {
                //TODO : Snackbar Permission not granted + something

            }

        }
    }



    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }




// TODO : if problem is " No Internet Connection. Or try Restarting your device once. " and internet is actually on the to avoid user from restarting device use following 2 methods and one call to these functions to resolve the issue without having user to restart the device.    ---------> for getting latitude and longitude
//    https://stackoverflow.com/questions/16119130/android-java-io-ioexception-service-not-available/22061612#22061612

//    https://stackoverflow.com/questions/35602276/android-studio-cannot-resolve-symbol-httpclient

//    public JSONObject getLocationFormGoogle(String placesName) {
//
//        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address=" +placesName+"&ka&sensor=false");
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response;
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//            response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            InputStream stream = entity.getContent();
//            int b;
//            while ((b = stream.read()) != -1) {
//                stringBuilder.append((char) b);
//            }
//        } catch (ClientProtocolException e) {
//        } catch (IOException e) {
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject = new JSONObject(stringBuilder.toString());
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//        }
//
//        return jsonObject;
//    }
//
//    public  LatLng getLatLng(JSONObject jsonObject) {
//
//        Double lon = new Double(0);
//        Double lat = new Double(0);
//
//        try {
//
//            lon = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location")
//                    .getDouble("lng");
//
//            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
//                    .getJSONObject("geometry").getJSONObject("location")
//                    .getDouble("lat");
//
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        return new LatLng(lat,lon);
//
//    }
//
//
//
//    LatLng Source =getLatLng(getLocationFormGoogle(placesName));

//    TODO : over


// TODO : if problem is " No Internet Connection. Or try Restarting your device once. " and internet is actually on the to avoid user from restarting device use following 2 methods and one call to these functions to resolve the issue without having user to restart the device.    ---------> for getting city name from latitude and longitude
//    https://stackoverflow.com/questions/16119130/android-java-io-ioexception-service-not-available/22061612#22061612

//    https://stackoverflow.com/questions/35602276/android-studio-cannot-resolve-symbol-httpclient

//    public static String getLocationCityName( double lat, double lon ){
//        JSONObject result = getLocationFormGoogle(lat + "," + lon );
//        return getCityAddress(result);
//    }
//
//    protected static JSONObject getLocationFormGoogle(String placesName) {
//
//        String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + placesName; //+ "&ka&sensor=false"
//        HttpGet httpGet = new HttpGet(apiRequest);
//        HttpClient client = new DefaultHttpClient();
//        HttpResponse response;
//        StringBuilder stringBuilder = new StringBuilder();
//
//        try {
//            response = client.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            InputStream stream = entity.getContent();
//            int b;
//            while ((b = stream.read()) != -1) {
//                stringBuilder.append((char) b);
//            }
//        } catch (ClientProtocolException e) {
//        } catch (IOException e) {
//        }
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject = new JSONObject(stringBuilder.toString());
//        } catch (JSONException e) {
//
//            e.printStackTrace();
//        }
//
//        return jsonObject;
//    }
//
//    protected static String getCityAddress( JSONObject result ){
//        if( result.has("results") ){
//            try {
//                JSONArray array = result.getJSONArray("results");
//                if( array.length() > 0 ){
//                    JSONObject place = array.getJSONObject(0);
//                    JSONArray components = place.getJSONArray("address_components");
//                    for( int i = 0 ; i < components.length() ; i++ ){
//                        JSONObject component = components.getJSONObject(i);
//                        JSONArray types = component.getJSONArray("types");
//                        for( int j = 0 ; j < types.length() ; j ++ ){
//                            if( types.getString(j).equals("locality") ){
//                                return component.getString("long_name");
//                            }
//                        }
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return null;
//    }

//    TODO : over

}