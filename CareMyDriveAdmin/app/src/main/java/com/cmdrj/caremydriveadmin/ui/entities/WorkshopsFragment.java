package com.cmdrj.caremydriveadmin.ui.entities;

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

import com.cmdrj.caremydriveadmin.AdapterRecVewUsers;
import com.cmdrj.caremydriveadmin.ListWorkshops;
import com.cmdrj.caremydriveadmin.R;
import com.cmdrj.caremydriveadmin.RecVewClient;
import com.cmdrj.caremydriveadmin.RecVewService;
import com.cmdrj.caremydriveadmin.RecVewWorkshop;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link WorkshopsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class WorkshopsFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public WorkshopsFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment WorkshopsFragment.
//     */
//    // TODO: Rename and change types and number of parameters

    public static WorkshopsFragment newInstance() {
        WorkshopsFragment fragment = new WorkshopsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    RecyclerView RV_ListWorkshops;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewWorkshop> newWorkshops;
    AdapterRecVewUsers adapterRecVewUsers;

    LinearLayout RV_ListWorkshops_Placeholder_LL;
    TextView RV_ListWorkshops_Placeholder_TV;

    //    FloatingActionButton ListWorkshops_fab;


    View root;

    private FirebaseFirestore fStore;


    public WorkshopsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_workshops, container, false);

        //        ListWorkshops_fab = root.findViewById(R.id.ListWorkshops_fab);

        fStore = FirebaseFirestore.getInstance();

        RV_ListWorkshops_Placeholder_LL = root.findViewById(R.id.RV_ListWorkshops_Placeholder_LL);
        RV_ListWorkshops_Placeholder_TV = root.findViewById(R.id.RV_ListWorkshops_Placeholder_TV);


        initDataForRecVew();
        initRecVew();

//        ListWorkshops_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        return root;
    }

    private void initDataForRecVew() {

        newWorkshops = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Workshops");

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

                    newWorkshops.clear();

                    List<RecVewWorkshop> snapshotList = value.toObjects(RecVewWorkshop.class);
                    newWorkshops.addAll(snapshotList);

                    if(newWorkshops.size() == 0)
                    {

                        RV_ListWorkshops_Placeholder_TV.setText("No Workshops Enrolled.");
                        RV_ListWorkshops_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListWorkshops.setVisibility(View.INVISIBLE);


                    }
                    else
                    {
                        RV_ListWorkshops_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListWorkshops.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(newWorkshops, new Comparator<RecVewWorkshop>() {
                        @Override
                        public int compare(RecVewWorkshop o1, RecVewWorkshop o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    adapterRecVewUsers.notifyDataSetChanged();

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void initRecVew() {

        RV_ListWorkshops = root.findViewById(R.id.RV_ListWorkshops);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_ListWorkshops.setLayoutManager(layoutManager);

        adapterRecVewUsers = new AdapterRecVewUsers(newWorkshops);

        RV_ListWorkshops.setAdapter(adapterRecVewUsers);
        adapterRecVewUsers.notifyDataSetChanged();
    }
}