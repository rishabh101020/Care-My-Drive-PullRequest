package com.cmdrj.caremydrive.ui.cars;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmdrj.caremydrive.AdapterRecVewCars;
import com.cmdrj.caremydrive.R;
import com.cmdrj.caremydrive.RecVewBookings;
import com.cmdrj.caremydrive.RecVewCar;
import com.cmdrj.caremydrive.RecVewService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    //    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    static int parentActivity;
    //    static RecVewWorkshop newWorkshop;
    static RecVewService newService;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param parent      Parameter 1.
     * @param itemService Parameter 3.
     * @return A new instance of fragment CarsFragment.
     */

//    public static CarsFragment newInstance(int parent) {
//        CarsFragment fragment = new CarsFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM1, parent);
//        fragment.setArguments(args);
//        return fragment;
//    }
    public static CarsFragment newInstance(int parent, RecVewService itemService) {
        CarsFragment fragment = new CarsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, parent);
//        args.putParcelable(ARG_PARAM2, itemWorkshop);
        args.putParcelable(ARG_PARAM3, itemService);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            parentActivity = getArguments().getInt(ARG_PARAM1);
//            newWorkshop = getArguments().getParcelable(ARG_PARAM2);
            newService = getArguments().getParcelable(ARG_PARAM3);
        } else {
            parentActivity = 0;
//            newWorkshop = null;
            newService = null;
        }
    }


//    TabLayout tabLayout;
//    ViewPager2 viewPager2;
//    FragmentManager fragmentManager;
//    AdapterFragmentsCars adapterFragmentsCars;

    RecyclerView RV_Cars;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewCar> newCars;
    AdapterRecVewCars adapterRecVewCars;

    TextView Cars_TV_All, Cars_TV_Bikes, Cars_TV_Cars, Cars_TV_Heavy, Cars_TV_Others, RV_Cars_Placeholder_TV;
    LinearLayout RV_Cars_Placeholder_LL;
    //    HorizontalScrollView Cars_HSV;
    ArrayList<RecVewCar> allCars;


    CardView Cars_CV1, Cars_CV2;
    FloatingActionButton Cars_fab;
    boolean fabSelected = false;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;


    View root;

    public CarsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cars, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();











//        GeoPoint geoPoint = new GeoPoint(24.254,73.65);
//
//        Map<String,Object> map = new HashMap<>();
//        map.put("loc",geoPoint);
//        fStore.collection("Clients").document(fUser.getUid()).update(map);










        Cars_fab = root.findViewById(R.id.Cars_fab);
        Cars_CV1 = root.findViewById(R.id.Cars_CV1);
        Cars_CV2 = root.findViewById(R.id.Cars_CV2);

        if (parentActivity == 0) {
            ((AppCompatActivity) root.getContext()).getSupportActionBar().setTitle("My Vehicles");
            Cars_fab.setVisibility(View.VISIBLE);
        }
//        ColorDrawable colorDrawable
//                = new ColorDrawable(getResources().getColor(R.color.green_200));
//
//        ((AppCompatActivity) root.getContext()).getSupportActionBar().setBackgroundDrawable(colorDrawable);

//        tabLayout = root.findViewById(R.id.cars_TL);
//        viewPager2 = root.findViewById(R.id.cars_VP2);
//        tabLayout.addTab(tabLayout.newTab().setText("4 Wheelers"));
//        tabLayout.addTab(tabLayout.newTab().setText("2 Wheelers"));

//        fragmentManager = getChildFragmentManager();
//        adapterFragmentsCars = new AdapterFragmentsCars(fragmentManager,getLifecycle());
//        viewPager2.setAdapter(adapterFragmentsCars);

//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager2.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                tabLayout.selectTab(tabLayout.getTabAt(position));
//            }
//        });


//        Cars_HSV = root.findViewById(R.id.Cars_HSV);
        Cars_TV_All = root.findViewById(R.id.Cars_TV_All);
        Cars_TV_Bikes = root.findViewById(R.id.Cars_TV_Bikes);
        Cars_TV_Cars = root.findViewById(R.id.Cars_TV_Cars);
        Cars_TV_Heavy = root.findViewById(R.id.Cars_TV_Heavy);
        Cars_TV_Others = root.findViewById(R.id.Cars_TV_Others);

        RV_Cars_Placeholder_TV = root.findViewById(R.id.RV_Cars_Placeholder_TV);
        RV_Cars_Placeholder_LL = root.findViewById(R.id.RV_Cars_Placeholder_LL);

        initDataForRecVew();
        initRecVew();

//        for(RecVewCar item:newCars)
//        {
//            allCars.add(item);
//
////            if(item.getWheelType() == 2)
////                bikes.add(item);
////
////            else if(item.getWheelType() == 4)
////                cars.add(item);
////
////            else if(item.getWheelType() == 6)
////                heavy.add(item);
////
////            else if(item.getWheelType() == 3)
////                Others.add(item);
////
//        }

        Cars_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabSelected = !fabSelected;
                adapterRecVewCars.fabSelected = !adapterRecVewCars.fabSelected;


//                newCars.clear();

                if (fabSelected) {
//                    Cars_HSV.setVisibility(View.GONE);

                    Cars_TV_All.performClick();
//                    newCars.addAll(allCars);
                    Cars_fab.setImageResource(R.drawable.ic_save_24);

//                    newCars.add(new RecVewCar(-1, "Add Vehicle","", "","",-1,R.drawable.ic_add_48,-1));

                } else {

                    ArrayList<RecVewCar> updatedCars = new ArrayList<>();
                    updatedCars.addAll(allCars);

//                    for(RecVewCar i:allCars)
//                    {
//                        Log.d("TAG" , "RishabhJain + " + i.getID() + " Visible : " + i.getVisibility());
//                    }

                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("visibility", true);

                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("visibility", false);

                    for(RecVewCar i:updatedCars)
                    {
                        if(i.getVisibility())
                        {
                            fStore.collection("Vehicles").document(i.getID()).update(map1);
                        }
                        else
                        {
                            fStore.collection("Vehicles").document(i.getID()).update(map2);
                        }

                    }

//                    Cars_HSV.setVisibility(View.VISIBLE);
                    Cars_TV_All.performClick();
//                    for(RecVewCar item:allCars)
//                    {
//                        if(item.getVisibility())
//                            newCars.add(item);
//                    }
                    Cars_fab.setImageResource(R.drawable.ic_edit_list_24);

                }
                adapterRecVewCars.notifyDataSetChanged();

            }
        });


        Cars_TV_All.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_All.setTextColor(root.getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(root.getResources().getColor(R.color.black));

                newCars.clear();

                if (fabSelected) {
                    //                        if (item.getVisibility())
                    newCars.addAll(allCars);

                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    newCars.add(new RecVewCar("new",          null ,                "Add Vehicle", "", "", "", -1, -1, ""));

                } else {
                    for (RecVewCar item : allCars) {
                        if (item.getVisibility())
                            newCars.add(item);
                    }

                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }

                if(newCars.size() == 0)
                {
                    RV_Cars_Placeholder_TV.setText("Please Add New Vehicles.");
                    RV_Cars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_Cars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_Cars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_Cars.setVisibility(View.VISIBLE);
                }

                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Bikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(getResources().getColor(R.color.black));

                newCars.clear();

                if (fabSelected) {
                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 2)
                            newCars.add(item);
                    }
                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    newCars.add(new RecVewCar("new",          null ,                 "Add Vehicle", "", "", "", -1, -1, ""));

                } else {
                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 2 && item.getVisibility())
                            newCars.add(item);
                    }

                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }

                if(newCars.size() == 0)
                {
                    RV_Cars_Placeholder_TV.setText("Please Add New Bikes.");
                    RV_Cars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_Cars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_Cars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_Cars.setVisibility(View.VISIBLE);
                }



                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Cars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(getResources().getColor(R.color.black));

//                newCars = cars;

                newCars.clear();
                if (fabSelected) {
                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 4)
                            newCars.add(item);
                    }
                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    newCars.add(new RecVewCar("new",          null ,                 "Add Vehicle", "", "", "", -1, -1, ""));


                } else {
                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 4 && item.getVisibility())
                            newCars.add(item);
                    }
                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                }

                if(newCars.size() == 0)
                {
                    RV_Cars_Placeholder_TV.setText("Please Add New Cars.");
                    RV_Cars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_Cars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_Cars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_Cars.setVisibility(View.VISIBLE);
                }



                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Heavy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(getResources().getColor(R.color.black));

//                newCars = heavy;

                newCars.clear();
                if (fabSelected) {
                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 6)
                            newCars.add(item);
                    }
                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    newCars.add(new RecVewCar("new",          null ,                 "Add Vehicle", "", "", "", -1, -1, ""));


                } else {
                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 6 && item.getVisibility())
                            newCars.add(item);
                    }
                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                }

                if(newCars.size() == 0)
                {
                    RV_Cars_Placeholder_TV.setText("Please Add New Heavy Vehicles.");
                    RV_Cars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_Cars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_Cars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_Cars.setVisibility(View.VISIBLE);
                }



                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        Cars_TV_Others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cars_TV_Others.setBackgroundResource(R.drawable.txtvew_indicator_selected);
                Cars_TV_Bikes.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Cars.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_Heavy.setBackgroundResource(R.drawable.txtvew_indicator_unselected);
                Cars_TV_All.setBackgroundResource(R.drawable.txtvew_indicator_unselected);

                Cars_TV_Others.setTextColor(getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(getResources().getColor(R.color.black));

//                newCars = Others;
                newCars.clear();
                if (fabSelected) {

                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 3)
                            newCars.add(item);
                    }
                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    newCars.add(new RecVewCar("new",          null ,                 "Add Vehicle", "", "", "", -1, -1, ""));


                } else {

                    for (RecVewCar item : allCars) {
                        if (item.getWheelType() == 3 && item.getVisibility())
                            newCars.add(item);
                    }

                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                }


                if(newCars.size() == 0)
                {
                    RV_Cars_Placeholder_TV.setText("Please Add New Vehicles.");
                    RV_Cars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_Cars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_Cars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_Cars.setVisibility(View.VISIBLE);
                }



                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        return root;
    }

    private void initDataForRecVew() {

        allCars = new ArrayList<>();
        newCars = new ArrayList<>();


        CollectionReference cReference = fStore.collection("Vehicles");
        DocumentReference dReference = fStore.collection("Clients").document(fUser.getUid());

        cReference.whereEqualTo("clientRef",dReference).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    allCars.clear();

                    List<RecVewCar> snapshotList = value.toObjects(RecVewCar.class);
                    allCars.addAll(snapshotList);

                    Cars_TV_All.performClick();

//                    for (RecVewCar item : allCars) {
//                        if (item.getVisibility())
//                            newCars.add(item);
//                    }
//
//                    adapterRecVewCars.notifyDataSetChanged();

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });


//        allCars.add(new RecVewCar(0, "Main Car", "PN 02 CA 1234", "Santro Xing", "Hyundai", 0, R.drawable.ic_cars_logo_48, 4));
//        allCars.add(new RecVewCar(1, "Bike", "UP 48 PA 7654", "WagonR", "Maruti Suzuki", 1, R.drawable.ic_cars_logo_48, 2));
//        allCars.add(new RecVewCar(2, "Office Car", "TN 36 ZQ 1658", "Mercedes Maybach S-Class", "Mercedes-Benz", 0, R.drawable.ic_cars_logo_48, 4));
//        allCars.add(new RecVewCar(3, "Scooty", "MH 78 HG 7563", "WagonR", "Maruti Suzuki", 0, R.drawable.ic_cars_logo_48, 2));
//        allCars.add(new RecVewCar(4, "SUV", "DL 05 CQ 9635", "Nexon", "Tata", 0, R.drawable.ic_cars_logo_48, 4));
//        allCars.add(new RecVewCar(5, "Electric Car", "DL 05 CL 9753", "I-20", "Hyundai", 1, R.drawable.ic_cars_logo_48, 4));
//        allCars.add(new RecVewCar(6, "Sports Car", "CN 58 CN 4563", "Santro Xing", "Hyundai", 0, R.drawable.ic_cars_logo_48, 4));
//        allCars.add(new RecVewCar(7, "Electric Bike", "AP 41 DA 3731", "Verna", "Hyundai", 0, R.drawable.ic_cars_logo_48, 2));
//        allCars.add(new RecVewCar(8, "Sports Bike", "MH 02 CL 0555", "Indica", "Tata", 1, R.drawable.ic_cars_logo_48, 2));
//        allCars.add(new RecVewCar(9, "Rented Scooty", "RJ 12 CG 2345", "Honda City", "Honda", 0, R.drawable.ic_cars_logo_48, 2));


//        newCars.add(new RecVewCar(0, "Main Car","PN 02 CA 1234","Santro Xing","Hyundai", 0, R.drawable.ic_cars_logo_48, 4));
//        newCars.add(new RecVewCar(1, "Bike","UP 48 PA 7654","WagonR","Maruti Suzuki", 1, R.drawable.ic_cars_logo_48,2));
//        newCars.add(new RecVewCar(2, "Office Car","TN 36 ZQ 1658","Mercedes Maybach S-Class","Mercedes-Benz", 0, R.drawable.ic_cars_logo_48, 4));
//        newCars.add(new RecVewCar(3, "Scooty","MH 78 HG 7563","WagonR","Maruti Suzuki", 0, R.drawable.ic_cars_logo_48, 2));
//        newCars.add(new RecVewCar(4, "SUV","DL 05 CQ 9635","Nexon","Tata", 0, R.drawable.ic_cars_logo_48, 4));
//        newCars.add(new RecVewCar(5, "Electric Car","DL 05 CL 9753","I-20","Hyundai", 1, R.drawable.ic_cars_logo_48, 4));
//        newCars.add(new RecVewCar(6, "Sports Car","CN 58 CN 4563","Santro Xing","Hyundai", 0, R.drawable.ic_cars_logo_48, 4));
//        newCars.add(new RecVewCar(7, "Electric Bike","AP 41 DA 3731","Verna","Hyundai", 0, R.drawable.ic_cars_logo_48, 2));
//        newCars.add(new RecVewCar(8, "Sports Bike","MH 02 CL 0555","Indica","Tata", 1, R.drawable.ic_cars_logo_48, 2));
//        newCars.add(new RecVewCar(9, "Rented Scooty","RJ 12 CG 2345","Honda City","Honda", 0, R.drawable.ic_cars_logo_48, 2));
    }

    private void initRecVew() {

        RV_Cars = root.findViewById(R.id.RV_Cars);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_Cars.setLayoutManager(layoutManager);


        adapterRecVewCars = new AdapterRecVewCars(getActivity(), newCars, parentActivity, newService, fabSelected, Cars_CV1.getBackground(), Cars_CV2.getBackground());

        RV_Cars.setAdapter(adapterRecVewCars);
        adapterRecVewCars.notifyDataSetChanged();
    }


}