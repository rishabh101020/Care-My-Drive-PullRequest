package com.cmdrj.caremydriveadmin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class AdapterFragmentsBookings extends FragmentStateAdapter {

    String initBookings;
    String liveBookings;
    String compBookings;

    public AdapterFragmentsBookings(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String initBookings, String liveBookings, String  compBookings) {
        super(fragmentManager, lifecycle);
        this.initBookings = initBookings;
        this.liveBookings = liveBookings;
        this.compBookings = compBookings;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 1)
            return FragmentLive.newInstance(liveBookings);

        else if(position == 2)
            return FragmentCompleted.newInstance(compBookings);

        else
            return FragmentInitiated.newInstance(initBookings);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
