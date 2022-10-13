package com.cmdrj.caremydriveadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentIndividualServices extends Fragment {

    public static FragmentIndividualServices newInstance() {
        FragmentIndividualServices fragment = new FragmentIndividualServices();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    GridView gridView;
    ArrayList<RecVewService> services;
    ArrayList<RecVewService> allServices;
    AdapterGridVewIndividualServices adapter;

    LinearLayout GV_IndSer_Placeholder_LL;
    TextView GV_IndSer_Placeholder_TV;

    CardView IndService_CV;
    FloatingActionButton IndService_fab;
    boolean fabSelected = false;

    private FirebaseAuth fAuth;
    private FirebaseUser fUser;
    private FirebaseFirestore fStore;

    View root;

    public FragmentIndividualServices() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_individual_services, container, false);

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        fStore = FirebaseFirestore.getInstance();

        GV_IndSer_Placeholder_LL = root.findViewById(R.id.GV_IndSer_Placeholder_LL);
        GV_IndSer_Placeholder_TV = root.findViewById(R.id.GV_IndSer_Placeholder_TV);


        IndService_fab = root.findViewById(R.id.IndService_fab);
        IndService_CV = root.findViewById(R.id.IndService_CV);

        initDataForGridVew();
        initGridVew();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),serviceName[position], Toast.LENGTH_SHORT).show();

                if(!fabSelected)
                {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("itemService", services.get(position));

                    DialogShowDetails dialogShowDetails = new DialogShowDetails();
                    dialogShowDetails.setTargetFragment(FragmentIndividualServices.this,1);
                    dialogShowDetails.setArguments(bundle);
                    dialogShowDetails.show(getFragmentManager(), "Details");
                }
                else
                {

                    if(services.get(position).getID().equals("new"))
                    {

                        // TODO : when firebase startActivityForResult wala + add to     2nd last     as last will be "new"

                        Intent intent = new Intent(getContext(), EditIndividualService.class);
                        intent.putExtra("itemService",services.get(position));
                        startActivity(intent);

                        //TODO : over
                    }
                    else
                    {
//                        if(services.get(position).getVisibility() ==1)
//                        {
//                            services.get(position).setVisibility(0);
//
//                        }
//                        else
//                        {
//                            services.get(position).setVisibility(1);
//
//                        }
                        services.get(position).setVisibility(!services.get(position).getVisibility());

                        adapter.notifyDataSetChanged();
                    }

                }

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if(!fabSelected)
                {
                    Intent intent = new Intent(getContext(), EditIndividualService.class);
                    intent.putExtra("itemService",services.get(position));
                    startActivity(intent);

//                Snackbar.make(view,""  + services.get(position).getName(), Snackbar.LENGTH_SHORT).show();

                }

                return true;
            }
        });

        IndService_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabSelected = !fabSelected;
                adapter.fabSelected = !adapter.fabSelected;

                if(fabSelected)
                {
                    services.clear();
                    services.addAll(allServices);
                    IndService_fab.setImageResource(R.drawable.ic_save_24);

                    Collections.sort(services, new Comparator<RecVewService>() {
                        @Override
                        public int compare(RecVewService o1, RecVewService o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    services.add(new RecVewService("new","Add Service",""));
                }
                else
                {

                    ArrayList<RecVewService> updatedServices = new ArrayList<>();
                    updatedServices.addAll(allServices);

                    Map<String, Object> map1 = new HashMap<>();
                    map1.put("visibility", true);

                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("visibility", false);

                    Map<String, Object> map3 = new HashMap<>();
                    map3.put("disabled", true);

                    Map<String, Object> map4 = new HashMap<>();
                    map4.put("disabled", false);

                    for(RecVewService i:updatedServices)
                    {
                        if(i.getVisibility())
                        {
                            fStore.collection("Services").document(i.getID()).update(map1)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            fStore.collection("Services").document(i.getID()).collection("Child Services").get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                            for(QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots)
                                                            {
                                                                queryDocumentSnapshot.getDocumentReference("ref").update(map4);
                                                            }
                                                        }
                                                    });

//                                            fStore.collection("Workshops").get()
//                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                                                            if(task.isSuccessful()){
//
//                                                                for(QueryDocumentSnapshot docWorkshop: task.getResult())
//                                                                {
//
////                                                                    Log.d("TAG" , "RishabhJain 1 " + docWorkshop.get("name"));
//                                                                    fStore.collection("Workshops").document(docWorkshop.getId()).collection("Services").whereEqualTo("parentRef","" + changeit i.getID()).whereEqualTo("disabled",true).get()
//                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                                @Override
//                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                                                                                    if(task.isSuccessful())
//                                                                                    {
//                                                                                        for(QueryDocumentSnapshot docService :task.getResult())
//                                                                                        {
////                                                                                            Log.d("TAG" , "RishabhJain 2 " + docService.get("name"));
//
//                                                                                            fStore.collection("Workshops").document(docWorkshop.getId()).collection("Services").document(docService.getId()).update(map4);
//                                                                                        }
//                                                                                    }
//                                                                                }
//                                                                            });
//                                                                }
//                                                            }
//                                                        }
//                                                    });
//
//
////                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
////                                                    @Override
////                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
////
////                                                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
////
////                                                        for(DocumentSnapshot snapshot :snapshotList)
////                                                        {
////                                                            DocumentReference dReference = snapshot.getDocumentReference();
////                                                            dReference.collection("Services").whereEqualTo("parentID",i.getID()).get()
////                                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                                                                        @Override
////                                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
////
////                                                                            if(task.isSuccessful()){
////
////                                                                                for (QueryDocumentSnapshot document : task.getResult()) {
////                                                                                    fStore.collection("Workshops").document().collection("Services").document(document.getId()).update();
//////                                                                                complaintsRef.document(document.getId()).set(map, SetOptions.merge());
////                                                                                }
////                                                                            }
////                                                                        }
////                                                                    });
////                                                        }
////                                                    }
////                                                });

                                        }
                                    });
                        }
                        else
                        {
                            fStore.collection("Services").document(i.getID()).update(map2)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                            fStore.collection("Services").document(i.getID()).collection("Child Services").get()
                                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                            for(QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots)
                                                            {
                                                                queryDocumentSnapshot.getDocumentReference("ref").update(map3);
                                                            }
                                                        }
                                                    });




//                                            fStore.collection("Workshops").get()
//                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                                                            if(task.isSuccessful()){
//
//                                                                for(QueryDocumentSnapshot docWorkshop: task.getResult())
//                                                                {
////                                                                    Log.d("TAG" , "RishabhJain 3 " + docWorkshop.get("name"));
//
//                                                                    fStore.collection("Workshops").document(docWorkshop.getId()).collection("Services").whereEqualTo("parentRef","" + changeit i.getID()).whereEqualTo("disabled",false).get()
//                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                                                                @Override
//                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                                                                                    if(task.isSuccessful())
//                                                                                    {
//                                                                                        for(QueryDocumentSnapshot docService :task.getResult())
//                                                                                        {
////                                                                                            Log.d("TAG" , "RishabhJain 4 " + docService.get("name"));
//
//                                                                                            fStore.collection("Workshops").document(docWorkshop.getId()).collection("Services").document(docService.getId()).update(map3);
//                                                                                        }
//                                                                                    }
//                                                                                }
//                                                                            });
//                                                                }
//                                                            }
//                                                        }
//                                                    });

                                        }
                                    });
                        }

                    }

//                    Log.d("TAG" , "RishabhJain -----------------------------------------------");
                    services.clear();
                    for(RecVewService item:allServices)
                    {
                        if(item.getVisibility())
                            services.add(item);
                    }

                    Collections.sort(services, new Comparator<RecVewService>() {
                        @Override
                        public int compare(RecVewService o1, RecVewService o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });


                    IndService_fab.setImageResource(R.drawable.ic_edit_list_24);


                }

                if(services.size() == 0)
                {

                    GV_IndSer_Placeholder_TV.setText("Please Add New Services.");
                    GV_IndSer_Placeholder_LL.setVisibility(View.VISIBLE);
//                    gridView.setVisibility(View.INVISIBLE);


//                        Snackbar.make(root, "Please Add New Services.", Snackbar.LENGTH_SHORT).show();
                }
                else
                {
                    GV_IndSer_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    gridView.setVisibility(View.VISIBLE);
                }



                adapter.notifyDataSetChanged();

            }
        });

        return root;
    }

    private void initDataForGridVew() {


        allServices = new ArrayList<>();
        services = new ArrayList<>();

        CollectionReference cReference = fStore.collection("Services");

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

                    allServices.clear();
                    services.clear();


                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
                    allServices.addAll(snapshotList);

                    if(fabSelected)
                    {
                        services.addAll(allServices);

                        Collections.sort(services, new Comparator<RecVewService>() {
                            @Override
                            public int compare(RecVewService o1, RecVewService o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });

                        services.add(new RecVewService("new","Add Service",""));

                    }
                    else
                    {
                        for(RecVewService item:allServices)
                        {
                            if(item.getVisibility())
                                services.add(item);
                        }

                        Collections.sort(services, new Comparator<RecVewService>() {
                            @Override
                            public int compare(RecVewService o1, RecVewService o2) {
                                return o1.getName().compareTo(o2.getName());
                            }
                        });

                    }

                    if(services.size() == 0)
                    {

                        GV_IndSer_Placeholder_TV.setText("Please Add New Services.");
                        GV_IndSer_Placeholder_LL.setVisibility(View.VISIBLE);
//                    gridView.setVisibility(View.INVISIBLE);


//                        Snackbar.make(root, "Please Add New Services.", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        GV_IndSer_Placeholder_LL.setVisibility(View.INVISIBLE);
//                    gridView.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();

                } else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });

//
//        allServices = new ArrayList<>();
//        allServices.add(new RecVewService(0, "Wheel Alignment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(1, "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(2, "Flat Tyre",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(3, "Fuel Support",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(4, "Crane Service",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(5, "4 Wheeler Service",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(6, "Accessory Fitment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(7, "Key Support",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        allServices.add(new RecVewService(8, "2 Wheeler Service",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//
//        services = new ArrayList<>();
//        for(RecVewService item:allServices)
//        {
//            if(item.getVisibility())
//                services.add(item);
//        }

    }



    private void initGridVew() {

        gridView = root.findViewById(R.id.GV_IndSer);
        adapter = new AdapterGridVewIndividualServices(getContext(),services,fabSelected,IndService_CV.getBackground());
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}