package com.cmdrj.caremydriveworkshop.ui.bookings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.cmdrj.caremydriveworkshop.AdapterFragmentsBookings;
import com.cmdrj.caremydriveworkshop.R;
import com.google.android.material.tabs.TabLayout;

public class BookingsFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentManager fragmentManager;
    AdapterFragmentsBookings adapterFragmentsBookings;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_bookings, container, false);
//        ((AppCompatActivity) root.getContext()).getSupportActionBar().setTitle("My Bookings");

        tabLayout = root.findViewById(R.id.bookings_TL);
        viewPager2 = root.findViewById(R.id.bookings_VP2);
        tabLayout.addTab(tabLayout.newTab().setText("Requested"));
        tabLayout.addTab(tabLayout.newTab().setText("Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed"));

        fragmentManager = getChildFragmentManager();
        adapterFragmentsBookings = new AdapterFragmentsBookings(fragmentManager,getLifecycle());
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
}