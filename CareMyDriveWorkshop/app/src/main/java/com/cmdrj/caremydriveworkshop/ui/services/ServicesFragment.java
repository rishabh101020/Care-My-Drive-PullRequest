package com.cmdrj.caremydriveworkshop.ui.services;

import androidx.cardview.widget.CardView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmdrj.caremydriveworkshop.AdapterRecVewServices;
import com.cmdrj.caremydriveworkshop.R;
import com.cmdrj.caremydriveworkshop.RecVewService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicesFragment extends Fragment {

    public static ServicesFragment newInstance() {
        ServicesFragment fragment = new ServicesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    RecyclerView RV_SerProvided;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewService> newServices;
    AdapterRecVewServices adapterRecVewServices;

    LinearLayout RV_SerProvided_Placeholder_LL;
    TextView RV_SerProvided_Placeholder_TV;

    ArrayList<RecVewService> allServices;
    CardView SerProvided_CV1, SerProvided_CV2;
    FloatingActionButton SerProvided_fab;
    boolean fabSelected = false;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;

    View root;

    public ServicesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_services, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        RV_SerProvided_Placeholder_LL = root.findViewById(R.id.RV_SerProvided_Placeholder_LL);
        RV_SerProvided_Placeholder_TV = root.findViewById(R.id.RV_SerProvided_Placeholder_TV);

        SerProvided_fab = root.findViewById(R.id.SerProvided_fab);
        SerProvided_CV1 = root.findViewById(R.id.SerProvided_CV1);
        SerProvided_CV2 = root.findViewById(R.id.SerProvided_CV2);


        initDataForRecVew();
        initRecVew();

        SerProvided_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabSelected = !fabSelected;
                adapterRecVewServices.fabSelected = !adapterRecVewServices.fabSelected;

                if(fabSelected)
                {
                    newServices.clear();
                    newServices.addAll(allServices);
                    SerProvided_fab.setImageResource(R.drawable.ic_save_24);

                    Collections.sort(newServices, new Comparator<RecVewService>() {
                        @Override
                        public int compare(RecVewService o1, RecVewService o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    newServices.add(new RecVewService("new", null, null, "Add Service","", -1000, "", -1000, new ArrayList<Integer>(Arrays.asList())));

                }
                else
                {
                    ArrayList<RecVewService> updatedServices = new ArrayList<>();
                    updatedServices.addAll(allServices);

                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("visibility", true);

                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("visibility", false);

                    for(RecVewService i:updatedServices)
                    {
                        if(i.getVisibility())
                        {
                            fStore.collection("Workshops").document(fUser.getUid()).collection("Services").document(i.getID()).update(map1);
                        }
                        else
                        {
                            fStore.collection("Workshops").document(fUser.getUid()).collection("Services").document(i.getID()).update(map2);
                        }

                    }

                    newServices.clear();
                    for(RecVewService item:allServices)
                    {
                        if(!item.getDisabled() && item.getVisibility())
                            newServices.add(item);
                    }

                    Collections.sort(newServices, new Comparator<RecVewService>() {
                        @Override
                        public int compare(RecVewService o1, RecVewService o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    SerProvided_fab.setImageResource(R.drawable.ic_edit_list_24);

                }

                if(newServices.size() == 0)
                {

                    RV_SerProvided_Placeholder_TV.setText("Please Add New Services.");
                    RV_SerProvided_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_SerProvided.setVisibility(View.INVISIBLE);


//                        Snackbar.make(root, "Please Add New Services.", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    RV_SerProvided_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_SerProvided.setVisibility(View.VISIBLE);
                }



                adapterRecVewServices.notifyDataSetChanged();

            }
        });


        return root;
    }


    private void initDataForRecVew() {


        allServices = new ArrayList<>();
        newServices = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Workshops").document(fUser.getUid()).collection("Services");

        cReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    allServices.clear();
                    newServices.clear();

                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
                    allServices.addAll(snapshotList);

                    if(fabSelected)
                    {
                        newServices.addAll(allServices);

                        Collections.sort(newServices, new Comparator<RecVewService>() {
                            @Override
                            public int compare(RecVewService o1, RecVewService o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });


                        newServices.add(new RecVewService("new", null, null, "Add Service","", -1000, "", -1000, new ArrayList<Integer>(Arrays.asList())));

                    }
                    else
                    {
                        for(RecVewService item:allServices)
                        {
                            if(!item.getDisabled() && item.getVisibility())
                                newServices.add(item);
                        }

                        Collections.sort(newServices, new Comparator<RecVewService>() {
                            @Override
                            public int compare(RecVewService o1, RecVewService o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });

                    }

                    if(newServices.size() == 0)
                    {

                        RV_SerProvided_Placeholder_TV.setText("Please Add New Services.");
                        RV_SerProvided_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_SerProvided.setVisibility(View.INVISIBLE);


//                        Snackbar.make(root, "Please Add New Services.", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        RV_SerProvided_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_SerProvided.setVisibility(View.VISIBLE);
                    }

                    adapterRecVewServices.notifyDataSetChanged();

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });


//        allServices = new ArrayList<>();
//        allServices.add(new RecVewService(0, "Wheel Servicing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 Hour(s)", 1000, new ArrayList<Integer>(Arrays.asList(4,6))));
//        allServices.add(new RecVewService(1, "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "5 Day(s)", 4000, new ArrayList<Integer>(Arrays.asList(2,4))));
//        allServices.add(new RecVewService(2, "Puncture Removal",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "30 Minute(s)", 50, new ArrayList<Integer>(Arrays.asList(2,3,4,6))));
//        allServices.add(new RecVewService(3, "Petrol Pump",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "5 Minute(s)", 102, new ArrayList<Integer>(Arrays.asList(2,4,6))));
//        allServices.add(new RecVewService(4, "Towing Facility",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Variable", -1, new ArrayList<Integer>(Arrays.asList(4,6))));

//        newServices = new ArrayList<>();
//        for(RecVewService item:allServices)
//        {
//            if(item.getVisibility())
//                newServices.add(item);
//        }


//        newServices.add(new RecVewService(0, "Wheel Servicing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "1 Hour(s)", 1000, new ArrayList<Integer>(Arrays.asList(4,6))));
//        newServices.add(new RecVewService(1, "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "5 Day(s)", 4000, new ArrayList<Integer>(Arrays.asList(2,4))));
//        newServices.add(new RecVewService(2, "Puncture Removal",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "30 Minute(s)", 50, new ArrayList<Integer>(Arrays.asList(2,3,4,6))));
//        newServices.add(new RecVewService(3, "Petrol Pump",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 0, "5 Minute(s)", 102, new ArrayList<Integer>(Arrays.asList(2,4,6))));
//        newServices.add(new RecVewService(4, "Towing Facility",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green, 1, "Variable", -1, new ArrayList<Integer>(Arrays.asList(4,6))));

    }

    private void initRecVew() {

        RV_SerProvided = root.findViewById(R.id.RV_SerProvided);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_SerProvided.setLayoutManager(layoutManager);


        adapterRecVewServices = new AdapterRecVewServices(newServices,fabSelected,SerProvided_CV1.getBackground(),SerProvided_CV2.getBackground(), fUser.getUid());

        RV_SerProvided.setAdapter(adapterRecVewServices);
        adapterRecVewServices.notifyDataSetChanged();

    }



}