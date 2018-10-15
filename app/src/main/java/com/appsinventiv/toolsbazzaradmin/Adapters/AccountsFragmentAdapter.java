package com.appsinventiv.toolsbazzaradmin.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.PendingPOAccounts;
import com.appsinventiv.toolsbazzaradmin.Activities.Accounts.PurchaseOrdersAccountsFragment;
import com.appsinventiv.toolsbazzaradmin.Activities.Orders.OrdersFragment;

import java.util.ArrayList;


/**
 * Created by AliAh on 02/03/2018.
 */

public class AccountsFragmentAdapter extends FragmentPagerAdapter {

    Context mContext;
    ArrayList<String> arrayList;

    public AccountsFragmentAdapter(Context context, ArrayList<String> arrayList, FragmentManager fm) {
        super(fm);
        this.mContext = context;
        this.arrayList = arrayList;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new PendingPOAccounts();

        } else if (position == 1) {
            return new PendingPOAccounts();

        } else if (position == 2) {
            return new PendingPOAccounts();

        } else if (position == 3) {
            return new PurchaseOrdersAccountsFragment();

        } else if (position == 4) {
            return new PendingPOAccounts();

        } else {
            return null;
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return arrayList.size();
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position

        return arrayList.get(position);
    }


}