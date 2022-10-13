package com.cmdrj.caremydriveadmin.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cmdrj.caremydriveadmin.CompServices;
import com.cmdrj.caremydriveadmin.IndividualServices;
import com.cmdrj.caremydriveadmin.InitServices;
import com.cmdrj.caremydriveadmin.ListCars;
import com.cmdrj.caremydriveadmin.ListClients;
import com.cmdrj.caremydriveadmin.ListWorkshops;
import com.cmdrj.caremydriveadmin.LiveServices;
import com.cmdrj.caremydriveadmin.MainActivity;
import com.cmdrj.caremydriveadmin.R;
import com.cmdrj.caremydriveadmin.RecVewBookings;
import com.cmdrj.caremydriveadmin.RecVewCar;
import com.cmdrj.caremydriveadmin.RecVewClient;
import com.cmdrj.caremydriveadmin.RecVewService;
import com.cmdrj.caremydriveadmin.RecVewWorkshop;
import com.cmdrj.caremydriveadmin.RejServices;
import com.cmdrj.caremydriveadmin.ui.entities.WorkshopsFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment {

    CardView home_SV_item1,home_SV_item2,home_SV_item3,home_SV_item4,home_SV_item5,home_SV_item6,home_SV_item7,home_SV_item8, home_SV_item9, home_SV_item10;
    TextView home_SV_item1_No,home_SV_item2_No,home_SV_item3_No,home_SV_item4_No,home_SV_item5_No,home_SV_item6_No,home_SV_item7_No,home_SV_item8_No, home_SV_item9_No, home_SV_item10_No, home_SV_item11_No, home_SV_item12_No, home_SV_item13_No, home_SV_item14_No;

    View root;

    private FirebaseFirestore fStore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        fStore = FirebaseFirestore.getInstance();


        home_SV_item1 = root.findViewById(R.id.home_SV_item1);
        home_SV_item2 = root.findViewById(R.id.home_SV_item2);
        home_SV_item3 = root.findViewById(R.id.home_SV_item3);
        home_SV_item4 = root.findViewById(R.id.home_SV_item4);
        home_SV_item5 = root.findViewById(R.id.home_SV_item5);
        home_SV_item6 = root.findViewById(R.id.home_SV_item6);
        home_SV_item7 = root.findViewById(R.id.home_SV_item7);
        home_SV_item8 = root.findViewById(R.id.home_SV_item8);
        home_SV_item9 = root.findViewById(R.id.home_SV_item9);
        home_SV_item10 = root.findViewById(R.id.home_SV_item10);

        home_SV_item1_No = root.findViewById(R.id.home_SV_item1_No);
        home_SV_item2_No = root.findViewById(R.id.home_SV_item2_No);
        home_SV_item3_No = root.findViewById(R.id.home_SV_item3_No);
        home_SV_item4_No = root.findViewById(R.id.home_SV_item4_No);
        home_SV_item5_No = root.findViewById(R.id.home_SV_item5_No);
        home_SV_item6_No = root.findViewById(R.id.home_SV_item6_No);
        home_SV_item7_No = root.findViewById(R.id.home_SV_item7_No);
        home_SV_item8_No = root.findViewById(R.id.home_SV_item8_No);
        home_SV_item9_No = root.findViewById(R.id.home_SV_item9_No);
        home_SV_item10_No = root.findViewById(R.id.home_SV_item10_No);
        home_SV_item11_No = root.findViewById(R.id.home_SV_item11_No);
        home_SV_item12_No = root.findViewById(R.id.home_SV_item12_No);
        home_SV_item13_No = root.findViewById(R.id.home_SV_item13_No);
        home_SV_item14_No = root.findViewById(R.id.home_SV_item14_No);

//        home_SV_item12_No.setSelected(true);



        fStore.collection("Services").whereEqualTo("visibility",true).whereEqualTo("disabled",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
                    home_SV_item1_No.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Clients").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewClient> snapshotList = value.toObjects(RecVewClient.class);
                    home_SV_item3_No.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Workshops").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewWorkshop> snapshotList = value.toObjects(RecVewWorkshop.class);
                    home_SV_item4_No.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Vehicles").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewCar> snapshotList = value.toObjects(RecVewCar.class);
                    home_SV_item5_No.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Bookings").whereIn("status", Arrays.asList(0,-2,-3)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    home_SV_item7_No.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Bookings").whereEqualTo("status",-1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    home_SV_item8_No.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Bookings").whereIn("status", Arrays.asList(1,2)).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    home_SV_item9_No.setText(Integer.toString(snapshotList.size()));

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        fStore.collection("Bookings").whereEqualTo("status",3).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root, "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {

                    List<RecVewBookings> snapshotList = value.toObjects(RecVewBookings.class);
                    home_SV_item10_No.setText(Integer.toString(snapshotList.size()));









                    float totalInvoiceAmount = 0, totalCommission = 0, totalCommissionableAmount = 0;
                    for(RecVewBookings item: snapshotList)
                    {
                        totalInvoiceAmount += item.getPrice();
                        totalCommission += item.getCommission();
                        totalCommissionableAmount += item.getCommissionablePrice();
                    }

                    home_SV_item11_No.setText(Float.toString(totalInvoiceAmount));
                    home_SV_item12_No.setText(Float.toString(totalCommission));
                    home_SV_item13_No.setText(Float.toString(totalCommissionableAmount));





                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });







        home_SV_item1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), IndividualServices.class);
                startActivity(intent);

            }
        });

        home_SV_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getContext(), InitServices.class);
//                startActivity(intent);

            }
        });

        home_SV_item3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ListClients.class);
                startActivity(intent);

            }
        });

        home_SV_item4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                NavigationView nav_view = getActivity().findViewById(R.id.nav_view);
//                nav_view.getMenu().getItem(3).setChecked(true);
//                onNavigationItemSelected(navigationView.getMenu().getItem(0));
//                nav_view.setCheckedItem(R.id.nav_workshops);

//                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new WorkshopsFragment()).commit();

//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                // Replace the contents of the container with the new fragment
//                fragmentTransaction.replace(R.id.ListWorkshops_frameLayout, WorkshopsFragment.newInstance());
//                // or ft.add(R.id.your_placeholder, new FooFragment());
//                // Complete the changes added above
//                fragmentTransaction.commit();

//
                Intent intent = new Intent(getContext(), ListWorkshops.class);
                startActivity(intent);

            }
        });

        home_SV_item5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ListCars.class);
                startActivity(intent);

            }
        });

        home_SV_item6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getContext(), RejServices.class);
//                startActivity(intent);

            }
        });

        home_SV_item7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), InitServices.class);
                startActivity(intent);

            }
        });

        home_SV_item8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), RejServices.class);
                startActivity(intent);

            }
        });

        home_SV_item9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), LiveServices.class);
                startActivity(intent);

            }
        });

        home_SV_item10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), CompServices.class);
                startActivity(intent);

            }
        });

        return root;
    }
}