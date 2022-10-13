package com.cmdrj.caremydrive;

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
// * Use the {@link FragmentLive#newInstance} factory method to
// * create an instance of this fragment.
// */
public class FragmentLive extends Fragment {

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
//    public static FragmentLive newInstance(String param1, String param2) {
//        FragmentLive fragment = new FragmentLive();
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

    RecyclerView RV_LiveBook;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewBookings> liveAppli;

    LinearLayout RV_LiveBook_Placeholder_LL;
    TextView RV_LiveBook_Placeholder_TV;

    AdapterRecVewLiveBookings adapterRecVewLiveBookings;
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

        liveAppli = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Bookings");
        DocumentReference dReference = fStore.collection("Clients").document(fUser.getUid());

        cReference.whereEqualTo("clientRef",dReference).whereIn("status",Arrays.asList(1,2)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(v, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    liveAppli.clear();

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    liveAppli.addAll(snapshotList);


                    if(liveAppli.size() == 0)
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

                    Collections.sort(liveAppli, new Comparator<RecVewBookings>() {
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

        RV_LiveBook = v.findViewById(R.id.RV_LiveBook);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_LiveBook.setLayoutManager(layoutManager);

        adapterRecVewLiveBookings = new AdapterRecVewLiveBookings(liveAppli);

        RV_LiveBook.setAdapter(adapterRecVewLiveBookings);
        adapterRecVewLiveBookings.notifyDataSetChanged();
    }
}