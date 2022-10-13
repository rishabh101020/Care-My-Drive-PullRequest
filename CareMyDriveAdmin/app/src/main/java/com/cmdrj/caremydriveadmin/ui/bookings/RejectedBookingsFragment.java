package com.cmdrj.caremydriveadmin.ui.bookings;

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

import com.cmdrj.caremydriveadmin.AdapterRecVewBookings;
import com.cmdrj.caremydriveadmin.AdapterRecVewUsers;
import com.cmdrj.caremydriveadmin.R;
import com.cmdrj.caremydriveadmin.RecVewBookings;
import com.cmdrj.caremydriveadmin.RecVewWorkshop;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link RejectedBookingsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class RejectedBookingsFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public RejectedBookingsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment RejectedBookingsFragment.
//     */
//    // TODO: Rename and change types and number of parameters

    public static RejectedBookingsFragment newInstance() {
        RejectedBookingsFragment fragment = new RejectedBookingsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RecyclerView RV_RejBookings;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> bookings;
    AdapterRecVewBookings adapterRecVewBookings;

    LinearLayout RV_RejBookings_Placeholder_LL;
    TextView RV_RejBookings_Placeholder_TV;

    View root;

    private FirebaseFirestore fStore;


    public RejectedBookingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_rejected_bookings, container, false);

        fStore = FirebaseFirestore.getInstance();

        RV_RejBookings_Placeholder_LL = root.findViewById(R.id.RV_RejBookings_Placeholder_LL);
        RV_RejBookings_Placeholder_TV = root.findViewById(R.id.RV_RejBookings_Placeholder_TV);

        initDataForRecVew();
        initRecVew();

        return root;
    }

    private void initDataForRecVew() {


        bookings = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Bookings");

        cReference.whereEqualTo("status",-1).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        RV_RejBookings_Placeholder_TV.setText("No Rejected Bookings.");
                        RV_RejBookings_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_RejBookings.setVisibility(View.INVISIBLE);

//                        Snackbar.make(RV_RejBookings, "No Rejected Bookings.", Snackbar.LENGTH_SHORT).show();

                    }
                    else
                    {
                        RV_RejBookings_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_RejBookings.setVisibility(View.VISIBLE);
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

    private void initRecVew() {

        RV_RejBookings = root.findViewById(R.id.RV_RejBookings);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_RejBookings.setLayoutManager(layoutManager);

        adapterRecVewBookings = new AdapterRecVewBookings(bookings,getActivity());

        RV_RejBookings.setAdapter(adapterRecVewBookings);
        adapterRecVewBookings.notifyDataSetChanged();
    }
}