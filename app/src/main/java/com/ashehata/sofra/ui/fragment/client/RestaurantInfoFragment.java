package com.ashehata.sofra.ui.fragment.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantInfoFragment extends BaseFragment {


    @BindView(R.id.restautant_info_fragment_tv_status)
    TextView restautantInfoFragmentTvStatus;
    @BindView(R.id.restautant_info_fragment_tv_city)
    TextView restautantInfoFragmentTvCity;
    @BindView(R.id.restautant_info_fragment_tv_region)
    TextView restautantInfoFragmentTvRegion;
    @BindView(R.id.restautant_info_fragment_tv_min_order)
    TextView restautantInfoFragmentTvMinOrder;
    @BindView(R.id.restautant_info_fragment_tv_cost)
    TextView restautantInfoFragmentTvCost;

    public User restaurantInfo ;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_info, container, false);
        ButterKnife.bind(this,view);

        displayRestaurantInfo();

        return view;
    }

    private void displayRestaurantInfo() {
        if(restaurantInfo != null){
            restautantInfoFragmentTvStatus.setText(restaurantInfo.getAvailability());
//            restautantInfoFragmentTvCity.setText(restaurantInfo.getRegion().getCity().getName());
//            restautantInfoFragmentTvRegion.setText(restaurantInfo.getRegion().getName());
            restautantInfoFragmentTvMinOrder.setText(restaurantInfo.getMinimumCharger());
            restautantInfoFragmentTvCost.setText(restaurantInfo.getDeliveryCost());
        }
    }
}
