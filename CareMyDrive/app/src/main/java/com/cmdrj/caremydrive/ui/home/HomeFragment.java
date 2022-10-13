package com.cmdrj.caremydrive.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cmdrj.caremydrive.AdapterGridVewHomeServices;
import com.cmdrj.caremydrive.AdapterVewPgrHomeBanner;
import com.cmdrj.caremydrive.ChooseCar;
import com.cmdrj.caremydrive.ChooseWorkshop;
import com.cmdrj.caremydrive.R;
import com.cmdrj.caremydrive.RecVewCar;
import com.cmdrj.caremydrive.RecVewService;
import com.cmdrj.caremydrive.RecVewWorkshop;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    View root;


    ViewPager viewPager;
    AdapterVewPgrHomeBanner adapterVewPgrHomeBanner;
    ArrayList<Integer> banners = new ArrayList<>();

////        TODO : remove when editing View Pager
//    ImageView VP_home_banner_IV;
////        TODO : over


    GridView gridView;

    LinearLayout home_GV_services_Placeholder_LL;
    TextView home_GV_services_Placeholder_TV;

    ArrayList<RecVewService> services = new ArrayList<>();;
    AdapterGridVewHomeServices adapter;
    RecVewCar itemCar = null;
//    RecVewWorkshop itemWorkshop = null;

    private FirebaseFirestore fStore;


    public HomeFragment(){
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home,container,false);


////        TODO : remove when editing View Pager
//        VP_home_banner_IV = root.findViewById(R.id.VP_home_banner_IV);
////        TODO : over

        fStore = FirebaseFirestore.getInstance();

        ((AppCompatActivity) root.getContext()).getSupportActionBar().setTitle("Book Service");
//        ColorDrawable colorDrawable
//                = new ColorDrawable(getResources().getColor(R.color.green_200));
//
//        ((AppCompatActivity) root.getContext()).getSupportActionBar().setBackgroundDrawable(colorDrawable);


        home_GV_services_Placeholder_LL = root.findViewById(R.id.home_GV_services_Placeholder_LL);
        home_GV_services_Placeholder_TV = root.findViewById(R.id.home_GV_services_Placeholder_TV);


        initDataForVewPgr();
        initVewPgr();

        
        initDataForGridVew();
        initGridVew();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(getContext(),serviceName[position], Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(getContext(), ChooseWorkshop.class);
//                intent.putExtra("itemService",services.get(position));
//                intent.putExtra("itemCar",itemCar);
//                startActivity(intent);
//                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                Intent intent = new Intent(getContext(), ChooseCar.class);
//                intent.putExtra("itemWorkshop", itemWorkshop);
                intent.putExtra("itemService", services.get(position));
                intent.putExtra("parentActivity",  1);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(),services.get(position).getName(), Toast.LENGTH_SHORT).show();
                Snackbar.make(view,""  + services.get(position).getName(), Snackbar.LENGTH_SHORT).show();

                return true;
            }
        });

////        TODO : remove when editing View Pager
//
//        VP_home_banner_IV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Log.d("TAG", "Rishabh Jain");
//                Snackbar.make(v, "Wait for the next version for exciting offers.", Snackbar.LENGTH_SHORT).show();
//            }
//        });
////        TODO : over



        return root;
    }

    private void initVewPgr() {

        viewPager = root.findViewById(R.id.home_VP);
        adapterVewPgrHomeBanner = new AdapterVewPgrHomeBanner(banners);
        viewPager.setAdapter(adapterVewPgrHomeBanner);
        adapterVewPgrHomeBanner.notifyDataSetChanged();

    }


    private void initDataForVewPgr() {

//        banners = new ArrayList<>();
        banners.add(R.drawable.banner1);
        banners.add(R.drawable.banner2);
        banners.add(R.drawable.banner3);
        banners.add(R.drawable.banner4);
        banners.add(R.drawable.banner5);
    }

    private void initDataForGridVew() {

        services = new ArrayList<>();


        CollectionReference cReference = fStore.collection("Services");

        cReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

//                    Log.d("TAG", "RishabhJain + " +  error);
//                    Toast.makeText(getContext(),"" + error,Toast.LENGTH_SHORT).show();
//                    Snackbar.make(root.findViewById(android.R.id.content), "" + error, Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (value != null) {
                    services.clear();

                    List<RecVewService> snapshotList = value.toObjects(RecVewService.class);
//                    services.addAll(snapshotList);

                    for(RecVewService item : snapshotList)
                    {
                        if(item.getVisibility())
                            services.add(item);
                    }

                    if(services.size() == 0)
                    {

                        home_GV_services_Placeholder_TV.setText("All Services Slots Booked.");
                        home_GV_services_Placeholder_LL.setVisibility(View.VISIBLE);
                        gridView.setVisibility(View.GONE);


//                        Snackbar.make(root, "All Services Slots Booked.", Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        home_GV_services_Placeholder_LL.setVisibility(View.GONE);
                        gridView.setVisibility(View.VISIBLE);
                    }

                    Collections.sort(services, new Comparator<RecVewService>() {
                        @Override
                        public int compare(RecVewService o1, RecVewService o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });

                    adapter.notifyDataSetChanged();

                }
                else {
//                    Snackbar.make(root, " ", Snackbar.LENGTH_SHORT).show();
                    return;
                }
            }
        });


//        services.add(new RecVewService("0", "Wheel Alignment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("1", "Vehicle Detailing",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("2", "Flat Tyre",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("3", "Fuel Support",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("4", "Crane Service",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("5", "4 Wheeler Service",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("6", "Accessory Fitment",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("7", "Key Support",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));
//        services.add(new RecVewService("8", "2 Wheeler Service",getString(R.string.large_text), R.drawable.ic_cars_repair_48_green));

    }



    private void initGridVew() {

        gridView = root.findViewById(R.id.home_GV_services);
        adapter = new AdapterGridVewHomeServices(getContext(),services);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}