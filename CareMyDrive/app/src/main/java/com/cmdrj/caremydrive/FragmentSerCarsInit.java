package com.cmdrj.caremydrive;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FragmentSerCarsInit#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentSerCarsInit extends Fragment {


    private static final String ARG_PARAM1 = "param1";

    static RecVewCar newCar;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param itemCar Parameter 1.
     * @return A new instance of fragment FragmentSerCarsInit.
     */

    public static FragmentSerCarsInit newInstance(RecVewCar itemCar) {

        FragmentSerCarsInit fragment = new FragmentSerCarsInit();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, itemCar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            newCar = getArguments().getParcelable(ARG_PARAM1);
        }
    }

    RecyclerView RV_SerCars_InitBook;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> newAppli;

    LinearLayout RV_SerCars_InitBook_Placeholder_LL;
    TextView RV_SerCars_InitBook_Placeholder_TV;

    AdapterRecVewLiveBookings adapterRecVewLiveBookings;
    View v;

    private FirebaseFirestore fStore;

    public FragmentSerCarsInit() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_ser_cars_init, container, false);

        fStore = FirebaseFirestore.getInstance();

        RV_SerCars_InitBook_Placeholder_LL = v.findViewById(R.id.RV_SerCars_InitBook_Placeholder_LL);
        RV_SerCars_InitBook_Placeholder_TV = v.findViewById(R.id.RV_SerCars_InitBook_Placeholder_TV);

        initDataForRecVew();
        initRecVew();

//        Bundle bundleObj = getArguments();
//        String str;

//        if(bundleObj != null)
//            str = bundleObj.getString("item");
//        else
//            Log.d("TAG","haaar ");
//        newCar = bundleObj.getParcelable("item");



        return v;
    }

    private void initDataForRecVew() {

        newAppli = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Bookings");
        DocumentReference dReference = fStore.collection("Vehicles").document(newCar.getID());

        cReference.whereEqualTo("vehicleRef",dReference).whereIn("status",Arrays.asList(0,-1,-2,-3)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Snackbar.make(v, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    newAppli.clear();

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    newAppli.addAll(snapshotList);

                    if(newAppli.size() == 0)
                    {

                        RV_SerCars_InitBook_Placeholder_TV.setText("No Requested Bookings for " + newCar.getName() + ".");
                        RV_SerCars_InitBook_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_SerCars_InitBook.setVisibility(View.INVISIBLE);


//                        Snackbar.make(v, "No Requested Bookings for " + newCar.getName() + ".", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        RV_SerCars_InitBook_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_SerCars_InitBook.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(newAppli, new Comparator<RecVewBookings>() {
                        @Override
                        public int compare(RecVewBookings o1, RecVewBookings o2) {
                            return o1.getServiceDate().compareTo(o2.getServiceDate());
                        }
                    });

                    adapterRecVewLiveBookings.notifyDataSetChanged();

                } else {
//                    Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void initRecVew() {

        RV_SerCars_InitBook = v.findViewById(R.id.RV_SerCars_InitBook);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_SerCars_InitBook.setLayoutManager(layoutManager);

        adapterRecVewLiveBookings = new AdapterRecVewLiveBookings(newAppli,getActivity());

        RV_SerCars_InitBook.setAdapter(adapterRecVewLiveBookings);
        adapterRecVewLiveBookings.notifyDataSetChanged();
    }
}