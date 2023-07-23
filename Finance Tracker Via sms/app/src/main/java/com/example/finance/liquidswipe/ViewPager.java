package com.example.finance.liquidswipe;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPager extends FragmentStatePagerAdapter {

    public ViewPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: return new liquidswipe1();
            case 1: return new liquidswipe2();
            case 2: return new liquidswipe3();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
