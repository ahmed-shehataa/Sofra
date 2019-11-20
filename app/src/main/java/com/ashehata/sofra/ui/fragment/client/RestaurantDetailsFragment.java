package com.ashehata.sofra.ui.fragment.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.ViewPagerAdapter;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.ashehata.sofra.ui.fragment.restaurant.OrderNewFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantDetailsFragment extends BaseFragment {


    @BindView(R.id.restaurant_details_fragment_tl_titles)
    TabLayout restaurantDetailsFragmentTlTitles;
    @BindView(R.id.restaurant_details_fragment_vp_orders)
    ViewPager restaurantDetailsFragmentVpOrders;
    private ViewPagerAdapter viewPagerAdapter;
    public int restaurantID ;
    public User restaurantInfo ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);
        ButterKnife.bind(this, view);

        setViewPagerAndTab();
        setUpActivity();

        return view;
    }

    private void setViewPagerAndTab() {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        //adding 3 orders fragments
        //RestaurantListFragment
        RestaurantListFragment restaurantListFragment = new RestaurantListFragment();
        restaurantListFragment.restaurantID = restaurantID;
        viewPagerAdapter.addPager(restaurantListFragment, getString(R.string.food_list));

        //RestaurantListFragment
        viewPagerAdapter.addPager( new RestaurantListFragment(), getString(R.string.comments_ratings));

        //RestaurantInfoFragment
        RestaurantInfoFragment restaurantInfoFragment = new RestaurantInfoFragment();
        restaurantInfoFragment.restaurantInfo = restaurantInfo;
        viewPagerAdapter.addPager( restaurantInfoFragment, getString(R.string.restaurant_info));

        restaurantDetailsFragmentVpOrders.setAdapter(viewPagerAdapter);

        //set tab layout with view pager
        restaurantDetailsFragmentTlTitles.setupWithViewPager(restaurantDetailsFragmentVpOrders);
    }
}
