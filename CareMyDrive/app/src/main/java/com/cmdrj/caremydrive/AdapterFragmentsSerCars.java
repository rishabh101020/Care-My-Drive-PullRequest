package com.cmdrj.caremydrive;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdapterFragmentsSerCars extends FragmentStateAdapter {

    private RecVewCar item;
    public AdapterFragmentsSerCars(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, RecVewCar item) {
        super(fragmentManager, lifecycle);
        this.item = item;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position == 1)
            return FragmentSerCarsLive.newInstance(item);

        else if (position == 2)
            return FragmentSerCarsComp.newInstance(item);

        else
            return FragmentSerCarsInit.newInstance(item);
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}