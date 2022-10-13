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
// * Use the {@link FragmentCompleted#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentCompleted extends Fragment {
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
//    public FragmentCompleted() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment FragmentCompleted.
//     */
//    // TODO: Rename and change types and number of parameters
    public static FragmentCompleted newInstance() {
        FragmentCompleted fragment = new FragmentCompleted();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView RV_CompBook;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> compBookings;
    AdapterRecVewBookings adapterRecVewBookings;

    LinearLayout RV_CompBook_Placeholder_LL;
    TextView RV_CompBook_Placeholder_TV;

    View v;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;

    public FragmentCompleted() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_completed, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        RV_CompBook_Placeholder_LL = v.findViewById(R.id.RV_CompBook_Placeholder_LL);
        RV_CompBook_Placeholder_TV = v.findViewById(R.id.RV_CompBook_Placeholder_TV);


        initDataForRecVew();
        initRecVew();

        return v;
    }

    private void initDataForRecVew() {

        compBookings = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Bookings");
        DocumentReference dReference = fStore.collection("Workshops").document(fUser.getUid());

        cReference.whereEqualTo("workShopRef",dReference).whereEqualTo("status",3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    compBookings.clear();

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    compBookings.addAll(snapshotList);


                    if(compBookings.size() == 0)
                    {

                        RV_CompBook_Placeholder_TV.setText("No Completed Bookings");
                        RV_CompBook_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_CompBook.setVisibility(View.INVISIBLE);


//                        Snackbar.make(v, "No Completed Bookings.", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        RV_CompBook_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_CompBook.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(compBookings, new Comparator<RecVewBookings>() {
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

        RV_CompBook = v.findViewById(R.id.RV_CompBook);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_CompBook.setLayoutManager(layoutManager);

        adapterRecVewBookings = new AdapterRecVewBookings(compBookings);

        RV_CompBook.setAdapter(adapterRecVewBookings);
        adapterRecVewBookings.notifyDataSetChanged();
    }
}