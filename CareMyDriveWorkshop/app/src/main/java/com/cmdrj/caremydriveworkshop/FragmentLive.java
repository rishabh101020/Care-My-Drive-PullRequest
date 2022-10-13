package com.cmdrj.caremydriveworkshop;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FragmentLive#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentLive extends Fragment implements DialogUploadImage.onInputListenerUI {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public FragmentLive() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentLive.
//     */
//    // TODO: Rename and change types and number of parameters
    public static FragmentLive newInstance() {
        FragmentLive fragment = new FragmentLive();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    RecyclerView RV_LiveBook;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> liveBookings;
    AdapterRecVewBookings adapterRecVewBookings;

    LinearLayout RV_LiveBook_Placeholder_LL;
    TextView RV_LiveBook_Placeholder_TV;

    View v;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;

    public FragmentLive() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_live, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        RV_LiveBook_Placeholder_LL = v.findViewById(R.id.RV_LiveBook_Placeholder_LL);
        RV_LiveBook_Placeholder_TV = v.findViewById(R.id.RV_LiveBook_Placeholder_TV);

        initDataForRecVew();
        initRecVew();

        return v;    
    }

    private void initDataForRecVew() {
        liveBookings = new ArrayList<>();


        CollectionReference cReference = fStore.collection("Bookings");
        DocumentReference dReference = fStore.collection("Workshops").document(fUser.getUid());

        cReference.whereEqualTo("workShopRef",dReference).whereIn("status", Arrays.asList(1,2)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    liveBookings.clear();

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    liveBookings.addAll(snapshotList);

                    if(liveBookings.size() == 0)
                    {

                        RV_LiveBook_Placeholder_TV.setText("No Accepted or Started Bookings.");
                        RV_LiveBook_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_LiveBook.setVisibility(View.INVISIBLE);


//                        Snackbar.make(v, "No Accepted or Started Bookings.", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        RV_LiveBook_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_LiveBook.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(liveBookings, new Comparator<RecVewBookings>() {
                        @Override
                        public int compare(RecVewBookings o1, RecVewBookings o2) {
                            return o1.getServiceDate().compareTo(o2.getServiceDate());
                        }
                    });

                    adapterRecVewBookings.notifyDataSetChanged();

                } else {
//                    Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }

    private void initRecVew() {

        RV_LiveBook = v.findViewById(R.id.RV_LiveBook);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_LiveBook.setLayoutManager(layoutManager);

        adapterRecVewBookings = new AdapterRecVewBookings(liveBookings);

        RV_LiveBook.setAdapter(adapterRecVewBookings);
        adapterRecVewBookings.notifyDataSetChanged();
    }

    @Override
    public void sendInputUI(boolean valid, RecVewBookings booking) {

        if(valid)
        {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("status", 2);

//            Map<String, Object> map2 = new HashMap<>();
//            map2.put("id", booking.getID());
//            map2.put("ref", fStore.collection("Bookings").document(booking.getID()));

            fStore.collection("Bookings").document(booking.getID()).update(map1)        ;
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    booking.getClientRef().collection("acceptedBookings").document(booking.getID()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//
//                            booking.getWorkShopRef().collection("acceptedBookings").document(booking.getID()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                    booking.getServiceRef().collection("acceptedBookings").document(booking.getID()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//
//                                            booking.getParentServiceRef().collection("acceptedBookings").document(booking.getID()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                @Override
//                                                public void onSuccess(Void aVoid) {
//
//                                                    booking.getVehicleRef().collection("acceptedBookings").document(booking.getID()).update(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                        @Override
//                                                        public void onSuccess(Void aVoid) {
//
//
//                                                        }
//                                                    });
//
//                                                }
//                                            });
//
//                                        }
//                                    });
//
//                                }
//                            });
//
//                        }
//                    });
//                }
//            });

        }
        else
        {
//            Snackbar.make(RV_LiveBook, "Please Upload a job Card first." , Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void sendInputUI(boolean valid, RecVewBookings booking, double commissionablePrice, double commission, double nextKM){//, Date nextDate) {

        if(valid)
        {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("status", 3);

            Map<String, Object> map2 = new HashMap<>();
            map2.put("id", booking.getID());
            map2.put("ref", fStore.collection("Bookings").document(booking.getID()));

            fStore.collection("Bookings").document(booking.getID()).update(map1)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            booking.getClientRef().collection("acceptedBookings").document(booking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    booking.getClientRef().collection("completedBookings").document(booking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            booking.getWorkShopRef().collection("acceptedBookings").document(booking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    booking.getWorkShopRef().collection("completedBookings").document(booking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                            booking.getServiceRef().collection("acceptedBookings").document(booking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    booking.getServiceRef().collection("completedBookings").document(booking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            booking.getParentServiceRef().collection("acceptedBookings").document(booking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @Override
                                                                                public void onSuccess(Void aVoid) {

                                                                                    booking.getParentServiceRef().collection("completedBookings").document(booking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {

                                                                                            booking.getVehicleRef().collection("acceptedBookings").document(booking.getID()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                @Override
                                                                                                public void onSuccess(Void aVoid) {

                                                                                                    booking.getVehicleRef().collection("completedBookings").document(booking.getID()).set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                        @Override
                                                                                                        public void onSuccess(Void aVoid) {



                                                                                                        }
                                                                                                    });

                                                                                                }
                                                                                            });
                                                                                        }
                                                                                    });

                                                                                }
                                                                            });

                                                                        }
                                                                    });

                                                                }
                                                            });

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    });

                                }
                            });

                        }
                    });
        }
        else
        {
//            Snackbar.make(DetBooking_CarModel, "Please Fill All Details." , Snackbar.LENGTH_SHORT).show();

        }
    }
}