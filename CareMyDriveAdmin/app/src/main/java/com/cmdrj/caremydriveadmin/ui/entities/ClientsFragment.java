package com.cmdrj.caremydriveadmin.ui.entities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmdrj.caremydriveadmin.AdapterRecVewUsers;
import com.cmdrj.caremydriveadmin.ListClients;
import com.cmdrj.caremydriveadmin.R;
import com.cmdrj.caremydriveadmin.RecVewBookings;
import com.cmdrj.caremydriveadmin.RecVewCar;
import com.cmdrj.caremydriveadmin.RecVewClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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

public class ClientsFragment extends Fragment {

    public static ClientsFragment newInstance() {
        ClientsFragment fragment = new ClientsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    RecyclerView RV_ListClients;
    LinearLayoutManager layoutManager;
    ArrayList<RecVewClient> newClients;
    AdapterRecVewUsers adapterRecVewUsers;

    LinearLayout RV_ListClients_Placeholder_LL;
    TextView RV_ListClients_Placeholder_TV;

//    FloatingActionButton ListClients_fab;


    View root;

    private FirebaseFirestore fStore;


    public ClientsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_clients, container, false);

        fStore = FirebaseFirestore.getInstance();

        RV_ListClients_Placeholder_LL = root.findViewById(R.id.RV_ListClients_Placeholder_LL);
        RV_ListClients_Placeholder_TV = root.findViewById(R.id.RV_ListClients_Placeholder_TV);

//        ListClients_fab = root.findViewById(R.id.ListClients_fab);


        initDataForRecVew();
        initRecVew();

//        ListClients_fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        return root;
    }

    private void initDataForRecVew() {

        newClients = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Clients");

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

                    newClients.clear();

                    List<RecVewClient> snapshotList = value.toObjects(RecVewClient.class);
                    newClients.addAll(snapshotList);

                    if(newClients.size() == 0)
                    {

                        RV_ListClients_Placeholder_TV.setText("No Clients Enrolled.");
                        RV_ListClients_Placeholder_LL.setVisibility(View.VISIBLE);
//                    RV_ListClients.setVisibility(View.INVISIBLE);


                    }
                    else
                    {
                        RV_ListClients_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    RV_ListClients.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(newClients, new Comparator<RecVewClient>() {
                        @Override
                        public int compare(RecVewClient o1, RecVewClient o2) {
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

        RV_ListClients = root.findViewById(R.id.RV_ListClients);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        RV_ListClients.setLayoutManager(layoutManager);

        adapterRecVewUsers = new AdapterRecVewUsers(newClients,-1);

        RV_ListClients.setAdapter(adapterRecVewUsers);
        adapterRecVewUsers.notifyDataSetChanged();
    }
}