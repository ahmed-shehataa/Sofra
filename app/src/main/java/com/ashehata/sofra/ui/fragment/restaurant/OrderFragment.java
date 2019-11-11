package com.ashehata.sofra.ui.fragment.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.ViewPagerAdapter;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderFragment extends BaseFragment {


    @BindView(R.id.order_fragment_tl_titles)
    TabLayout orderFragmentTlTitles;
    @BindView(R.id.order_fragment_vp_orders)
    ViewPager orderFragmentVpOrders;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);

        setViewPagerAndTab();
        setUpActivity();

        return view;
    }

    private void setViewPagerAndTab() {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        //adding 3 orders fragments
        viewPagerAdapter.addPager(new OrderNewFragment(), getString(R.string.order_new));
        //OrderCurrentFragment()
        viewPagerAdapter.addPager(new OrderNewFragment(), getString(R.string.order_current));
        //OrderPreviousFragment()
        viewPagerAdapter.addPager(new OrderNewFragment(), getString(R.string.order_previous));
        orderFragmentVpOrders.setAdapter(viewPagerAdapter);

        //set tab layout with view pager
        orderFragmentTlTitles.setupWithViewPager(orderFragmentVpOrders);
    }
}
