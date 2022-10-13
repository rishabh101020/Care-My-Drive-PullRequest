package com.cmdrj.caremydriveadmin.ui.entities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmdrj.caremydriveadmin.AdapterRecVewCars;
import com.cmdrj.caremydriveadmin.R;
import com.cmdrj.caremydriveadmin.RecVewCar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link CarsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class CarsFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public CarsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment CarsFragment.
//     */
//    // TODO: Rename and change types and number of parameters

    public static CarsFragment newInstance() {
        CarsFragment fragment = new CarsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView RV_ListCars;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewCar> newCars;
    AdapterRecVewCars adapterRecVewCars;

    CardView Cars_CV1, Cars_CV2;
    TextView Cars_TV_All, Cars_TV_Bikes, Cars_TV_Cars, Cars_TV_Heavy, Cars_TV_Others, RV_ListCars_Placeholder_TV;

    LinearLayout RV_ListCars_Placeholder_LL;

    ArrayList<RecVewCar> allCars;

    //    FloatingActionButton ListCars_fab;


    View root;

    private FirebaseFirestore fStore;


    public CarsFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_cars, container, false);

        fStore = FirebaseFirestore.getInstance();

        Cars_CV1 = root.findViewById(R.id.Cars_CV1);
        Cars_CV2 = root.findViewById(R.id.Cars_CV2);

        //        ListCars_fab = root.findViewById(R.id.ListCars_fab);

        Cars_TV_All = root.findViewById(R.id.Cars_TV_All);
        Cars_TV_Bikes = root.findViewById(R.id.Cars_TV_Bikes);
        Cars_TV_Cars = root.findViewById(R.id.Cars_TV_Cars);
        Cars_TV_Heavy = root.findViewById(R.id.Cars_TV_Heavy);
        Cars_TV_Others = root.findViewById(R.id.Cars_TV_Others);

        RV_ListCars_Placeholder_TV = root.findViewById(R.id.RV_ListCars_Placeholder_TV);
        RV_ListCars_Placeholder_LL = root.findViewById(R.id.RV_ListCars_Placeholder_LL);


        initDataForRecVew();
        initRecVew();

//        ListCars_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


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
                for(RecVewCar item:allCars)
                {
                    newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_ListCars_Placeholder_TV.setText("No Vehicles Enrolled.");
                    RV_ListCars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListCars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_ListCars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListCars.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

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

                Cars_TV_Bikes.setTextColor(root.getResources().getColor(R.color.white));
                Cars_TV_All.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(root.getResources().getColor(R.color.black));

                newCars.clear();
                for(RecVewCar item:allCars)
                {
                    if(item.getWheelType() == 2)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_ListCars_Placeholder_TV.setText("No Bikes Enrolled.");
                    RV_ListCars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListCars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_ListCars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListCars.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

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

                Cars_TV_Cars.setTextColor(root.getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(root.getResources().getColor(R.color.black));

//                newCars = cars;

                newCars.clear();
                for(RecVewCar item:allCars)
                {
                    if(item.getWheelType() == 4)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_ListCars_Placeholder_TV.setText("No Cars Enrolled.");
                    RV_ListCars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListCars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_ListCars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListCars.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

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

                Cars_TV_Heavy.setTextColor(root.getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Others.setTextColor(root.getResources().getColor(R.color.black));

//                newCars = heavy;

                newCars.clear();
                for(RecVewCar item:allCars)
                {
                    if(item.getWheelType() == 6)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_ListCars_Placeholder_TV.setText("No Heavy Vehicles Enrolled.");
                    RV_ListCars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListCars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_ListCars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListCars.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

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

                Cars_TV_Others.setTextColor(root.getResources().getColor(R.color.white));
                Cars_TV_Bikes.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Cars.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_Heavy.setTextColor(root.getResources().getColor(R.color.black));
                Cars_TV_All.setTextColor(root.getResources().getColor(R.color.black));

//                newCars = Others;
                newCars.clear();
                for(RecVewCar item:allCars)
                {
                    if(item.getWheelType() == 3)
                        newCars.add(item);
                }

                if(newCars.size() == 0)
                {
                    RV_ListCars_Placeholder_TV.setText("No Vehicles Enrolled.");
                    RV_ListCars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListCars.setVisibility(View.INVISIBLE);
                }
                else
                {
                    RV_ListCars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListCars.setVisibility(View.VISIBLE);
                }

                Collections.sort(newCars, new Comparator<RecVewCar>() {
                    @Override
                    public int compare(RecVewCar o1, RecVewCar o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                adapterRecVewCars.notifyDataSetChanged();
            }
        });

        return root;
    }

    private void initDataForRecVew() {

        allCars = new ArrayList<>();
        newCars = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Vehicles");

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

                    allCars.clear();
                    newCars.clear();

                    List<RecVewCar> snapshotList = value.toObjects(RecVewCar.class);
                    allCars.addAll(snapshotList);
                    newCars.addAll(snapshotList);

                    if(newCars.size() == 0)
                    {
                        RV_ListCars_Placeholder_TV.setText("No Vehicles Enrolled.");
                        RV_ListCars_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListCars.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        RV_ListCars_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListCars.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(newCars, new Comparator<RecVewCar>() {
                        @Override
                        public int compare(RecVewCar o1, RecVewCar o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    adapterRecVewCars.notifyDataSetChanged();

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initRecVew() {

        RV_ListCars = root.findViewById(R.id.RV_ListCars);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_ListCars.setLayoutManager(layoutManager);

        adapterRecVewCars = new AdapterRecVewCars(newCars, Cars_CV1.getBackground(), Cars_CV2.getBackground());

        RV_ListCars.setAdapter(adapterRecVewCars);
        adapterRecVewCars.notifyDataSetChanged();
    }
}