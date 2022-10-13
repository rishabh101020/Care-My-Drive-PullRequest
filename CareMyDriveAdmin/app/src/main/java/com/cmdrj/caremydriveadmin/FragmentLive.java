package com.cmdrj.caremydriveadmin;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FragmentLive#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentLive extends Fragment {

    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";

    private String bookingsRef = null;
//    private String mParam2;

    public FragmentLive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param listBookingRef Parameter 1.
     * @return A new instance of fragment FragmentLive.
     */

    public static FragmentLive newInstance(String listBookingRef) {

        FragmentLive fragment = new FragmentLive();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, listBookingRef);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bookingsRef = getArguments().getString(ARG_PARAM1);
        }
    }


    RecyclerView RV_LiveBook;
    LinearLayoutManager layoutManager;
    AdapterRecVewBookings adapterRecVewBookings;
    ArrayList<RecVewBookings> bookings;

    LinearLayout RV_LiveBook_Placeholder_LL;
    TextView RV_LiveBook_Placeholder_TV;

    View v;

    FirebaseFirestore fStore;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_live, container, false);

        fStore = FirebaseFirestore.getInstance();

        RV_LiveBook_Placeholder_LL = v.findViewById(R.id.RV_LiveBook_Placeholder_LL);
        RV_LiveBook_Placeholder_TV = v.findViewById(R.id.RV_LiveBook_Placeholder_TV);

        initDataForRecVew();
        initRecVew();

        return v;
    }

    private void initDataForRecVew() {


        bookings = new ArrayList<>();

        if(bookingsRef != null)
        {
            fStore.collection(bookingsRef)
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {

//                          Log.d("TAG", "RishabhJain + " +  error);
//                          Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                          Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                                return;
                            }

                            if (value != null) {


                                bookings.clear();

//                                Log.d("TAG" , "RishabhJain + " + "Start");

                                for(QueryDocumentSnapshot document : value)
                                {

//                                    Log.d("TAG" , "RishabhJain + " + document.toString());
                                    DocumentReference dReference = document.getDocumentReference("ref");

                                    dReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                                            if (error != null) {
                                                Snackbar.make(RV_LiveBook, "" + error, Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (value != null) {


                                                RecVewBookings addBooking = value.toObject(RecVewBookings.class);

                                                bookings.add(addBooking);

                                                if(bookings.size() == 0)
                                                {
                                                    RV_LiveBook_Placeholder_TV.setText("No Accepted or Started Bookings.");
                                                    RV_LiveBook_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_LiveBook.setVisibility(View.INVISIBLE);


//                        Snackbar.make(v, ""No Accepted or Started Bookings.", Snackbar.LENGTH_SHORT).show();
                                                }
                                                else if(bookings.size() == 1)
                                                {
                                                    RV_LiveBook_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_LiveBook.setVisibility(View.VISIBLE);
                                                }

                                                Collections.sort(bookings, new Comparator<RecVewBookings>() {
                                                    @Override
                                                    public int compare(RecVewBookings o1, RecVewBookings o2) {
                                                        return o1.getServiceDate().compareTo(o2.getServiceDate());
                                                    }
                                                });

                                                adapterRecVewBookings.notifyDataSetChanged();

//                                                Log.d("TAG" , "RishabhJain + " + bookings.toString());

                                            }
                                            else
                                            {
//                                  Snackbar.make(v, " ", Snackbar.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                    });
                                }


                                if(value.size() == 0)
                                {
                                    RV_LiveBook_Placeholder_TV.setText("No Accepted or Started Bookings.");
                                    RV_LiveBook_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_LiveBook.setVisibility(View.INVISIBLE);


//                        Snackbar.make(findViewById(android.R.id.content), "No Accepted or Started Bookings.", Snackbar.LENGTH_SHORT).show();
                                }







//                            bookings.clear();
//
//                            List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
//                            bookings.addAll(snapshotList);
//
//                            adapterRecVewBookings.notifyDataSetChanged();

                            } else {
//                          Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });


            // TODO : uncommnt following lines and rectify the problem
//        TODO : useful Link: https://stackoverflow.com/questions/49390196/firebase-cloud-firestore-getting-a-document-to-custom-object-and-passing-it-to

//        if(bookings.size() == 0)
//        {
//            Log.d("TAG" , "RishabhJain + here" );
//            Snackbar.make(findViewById(android.R.id.content), "No Bookings available for " + Categ +" Client.", Snackbar.LENGTH_SHORT).show();
//        }

            // TODO : over
            // TODO : over
        }
        else
        {
            CollectionReference cReference = fStore.collection("Bookings");

            cReference.whereIn("status",Arrays.asList(1,2)).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "" + error, Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {

                        bookings.clear();

                        List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                        bookings.addAll(snapshotList);

                        if(bookings.size() == 0)
                        {
                            RV_LiveBook_Placeholder_TV.setText("No Accepted or Started Bookings.");
                            RV_LiveBook_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_LiveBook.setVisibility(View.INVISIBLE);

//                            Snackbar.make(RV_LiveBook, "No Accepted or Started Bookings.", Snackbar.LENGTH_SHORT).show();

                        }
                        else
                        {
                            RV_LiveBook_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_LiveBook.setVisibility(View.VISIBLE);
                        }

//                    Log.d("TAG", "RishabhJain + bookings: " + newAppli.get(0).getVehicleRef());


                        Collections.sort(bookings, new Comparator<RecVewBookings>() {
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

    }

    private void initRecVew() {

        RV_LiveBook = v.findViewById(R.id.RV_LiveBook);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_LiveBook.setLayoutManager(layoutManager);

        adapterRecVewBookings = new AdapterRecVewBookings(bookings,getActivity());

        RV_LiveBook.setAdapter(adapterRecVewBookings);
        adapterRecVewBookings.notifyDataSetChanged();
    }
}