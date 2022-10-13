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
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link FragmentInitiated#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentInitiated extends Fragment {
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
//    public FragmentInitiated() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentInitiated.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static FragmentInitiated newInstance(String param1, String param2) {
//        FragmentInitiated fragment = new FragmentInitiated();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    RecyclerView RV_InitBook;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> newAppli;


    LinearLayout RV_InitBook_Placeholder_LL;
    TextView RV_InitBook_Placeholder_TV;


    AdapterRecVewLiveBookings adapterRecVewLiveBookings;
    View v;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;


    public FragmentInitiated() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_initiated, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        RV_InitBook_Placeholder_LL = v.findViewById(R.id.RV_InitBook_Placeholder_LL);
        RV_InitBook_Placeholder_TV = v.findViewById(R.id.RV_InitBook_Placeholder_TV);

        initDataForRecVew();
        initRecVew();

        return v;
    }

    private void initDataForRecVew() {

        newAppli = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Bookings");
        DocumentReference dReference = fStore.collection("Clients").document(fUser.getUid());

        cReference.whereEqualTo("clientRef",dReference).whereIn("status",Arrays.asList(0,-1,-2,-3)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    newAppli.clear();

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    newAppli.addAll(snapshotList);

                    if(newAppli.size() == 0)
                    {

                        RV_InitBook_Placeholder_TV.setText("No Requested Bookings.");
                        RV_InitBook_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_InitBook.setVisibility(View.INVISIBLE);


//                        Snackbar.make(v, "No Requested Bookings.", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        RV_InitBook_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_InitBook.setVisibility(View.VISIBLE);
                    }

//                    Log.d("TAG", "RishabhJain + bookings: " + newAppli.get(0).getVehicleRef());


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

        RV_InitBook = v.findViewById(R.id.RV_InitBook);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_InitBook.setLayoutManager(layoutManager);

        adapterRecVewLiveBookings = new AdapterRecVewLiveBookings(newAppli,getActivity());

        RV_InitBook.setAdapter(adapterRecVewLiveBookings);
        adapterRecVewLiveBookings.notifyDataSetChanged();
    }

}