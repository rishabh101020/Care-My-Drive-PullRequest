package com.cmdrj.caremydriveworkshop;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdapterFragmentsBookings extends FragmentStateAdapter {


    public AdapterFragmentsBookings(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if(position == 1)
            return new FragmentLive();

        else if(position == 2)
            return new FragmentCompleted();

        else
            return new FragmentInitiated();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
