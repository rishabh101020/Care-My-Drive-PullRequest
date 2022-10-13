package com.cmdrj.caremydriveadmin.ui.bookings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cmdrj.caremydriveadmin.AdapterFragmentsBookings;
import com.cmdrj.caremydriveadmin.AdapterRecVewUsers;
import com.cmdrj.caremydriveadmin.R;
import com.cmdrj.caremydriveadmin.RecVewBookings;
import com.cmdrj.caremydriveadmin.RecVewClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingsFragment extends Fragment {



//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

//    private static final String ARG_PARAM4 = "param4";
//    private static final String ARG_PARAM5 = "param5";
//    private static final String ARG_PARAM6 = "param6";
//
//    // TODO: Rename and change types of parameters

    String initBookings;
    String liveBookings;
    String  compBookings;
//    String  rejBookings;

//    boolean initBookingsDocument;
//    boolean liveBookingsDocument;
//    boolean  compBookingDocuments;
////    boolean  rejBookingDocuments;

    public BookingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param initBookings Parameter 1.
     * @param liveBookings Parameter 2.
     * @param compBookings Parameter 3.
//     * @param initBookingsDocument Parameter 4.
//     * @param liveBookingsDocument Parameter 5.
//     * @param compBookingDocuments Parameter 6.
     * @return A new instance of fragment BookingsFragment.
     */
//    // TODO: Rename and change types and number of parameters
//    public static BookingsFragment newInstance(String initBookings, String liveBookings, String compBookings, boolean initBookingsDocument, boolean liveBookingsDocument, boolean compBookingDocuments) {
//
//        BookingsFragment fragment = new BookingsFragment();
//
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, initBookings);
//        args.putString(ARG_PARAM2, liveBookings);
//        args.putString(ARG_PARAM3, compBookings);
//        args.putBoolean(ARG_PARAM4, initBookingsDocument);
//        args.putBoolean(ARG_PARAM5, liveBookingsDocument);
//        args.putBoolean(ARG_PARAM6, compBookingDocuments);
//        fragment.setArguments(args);
//
//        return fragment;
//    }

    public static BookingsFragment newInstance(String initBookings, String liveBookings, String compBookings) {

        BookingsFragment fragment = new BookingsFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, initBookings);
        args.putString(ARG_PARAM2, liveBookings);
        args.putString(ARG_PARAM3, compBookings);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            initBookings = getArguments().getString(ARG_PARAM1);
            liveBookings = getArguments().getString(ARG_PARAM2);
            compBookings = getArguments().getString(ARG_PARAM3);

//            initBookingsDocument = getArguments().getBoolean(ARG_PARAM4);
//            liveBookingsDocument = getArguments().getBoolean(ARG_PARAM5);
//            compBookingDocuments = getArguments().getBoolean(ARG_PARAM6);
        }
        else
        {
            intiData();
        }
    }

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentManager fragmentManager;
    AdapterFragmentsBookings adapterFragmentsBookings;



//    ArrayList<RecVewBookings> initBookingsArrayList;
//    ArrayList<RecVewBookings> liveBookingsArrayList;
//    ArrayList<RecVewBookings>  compBookingsArrayList;
////    ArrayList<RecVewBookings>  rejBookingsArrayList;


    View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_bookings, container, false);

        tabLayout = root.findViewById(R.id.bookings_TL);
        viewPager2 = root.findViewById(R.id.bookings_VP2);

        tabLayout.addTab(tabLayout.newTab().setText("Requested"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));


        // TODO : why get"Child"FragmentManager() and not getFragmentManager()
        fragmentManager = getChildFragmentManager();
        adapterFragmentsBookings = new AdapterFragmentsBookings(fragmentManager,getLifecycle(),initBookings, liveBookings, compBookings);
        viewPager2.setAdapter(adapterFragmentsBookings);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });


        return root;
    }



    private void intiData() {

        initBookings = null;
        liveBookings = null;
        compBookings = null;

//        initBookingsDocument = null;
//        liveBookingsDocument = null;
//        compBookingDocuments = null;



    }
}