package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsinventiv.toolsbazzaradmin.Activities.Orders.OrdersFragment;


/**
 * Created by AliAh on 02/03/2018.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new OrdersFragment("Pending");
        } else if (position == 1) {
            return new OrdersFragment("Shipped");
        } else if (position == 2) {
            return new OrdersFragment("Delivered");
        } else if (position == 3) {
            return new OrdersFragment("Credit");
        } else if (position == 4) {
            return new OrdersFragment("Completed");
        } else if (position == 5) {
            return new OrdersFragment("Cancelled");
        } else {
            return null;
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 6;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Pending";
            case 1:
                return "Shipped";
            case 2:
                return "Delivered";
            case 3:
                return "Credit";
            case 4:
                return "Completed";
            case 5:
                return "Cancelled";

            default:
                return null;
        }
    }

}
