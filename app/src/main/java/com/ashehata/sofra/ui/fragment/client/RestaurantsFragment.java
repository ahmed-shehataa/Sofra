package com.ashehata.sofra.ui.fragment.client;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.client.RestaurantsAdapter;
import com.ashehata.sofra.adapter.restaurant.OfferAdapter;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.model.general.restaurants.Restaurants;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.activity.SplashActivity;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.createToast;

public class RestaurantsFragment extends BaseFragment {


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.restaurants_fragment_rv_my_restaurants)
    RecyclerView restaurantsFragmentRvMyRestaurants;
    private GetDataService getDataService;
    private LinearLayoutManager linearLayoutManager;
    private Call<Restaurants> userCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurants, container, false);
        ButterKnife.bind(this,view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        //set recycler view configuration
        setRecyclerConfig();

        if (InternetState.isConnected(getContext())) {
            getRestaurants();
        } else {
            createToast(getContext(), getString(R.string.no_internet)
                    , Toast.LENGTH_SHORT);
        }

        return view;
    }

    private void getRestaurants() {
        userCall = getDataService.getRestaurants();
        userCall.enqueue(new Callback<Restaurants>() {
            @Override
            public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                try {
                    Restaurants restaurants= response.body();
                    if (restaurants.getStatus()==1) {
                        List<User> restaurantsList = restaurants.getData().getData();
                        RestaurantsAdapter restaurantsAdapter = new RestaurantsAdapter(getActivity(),getContext(),restaurantsList);
                        restaurantsFragmentRvMyRestaurants.setAdapter(restaurantsAdapter);
                    }else {
                        createToast(getContext(), restaurants.getMsg()
                                , Toast.LENGTH_SHORT);

                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<Restaurants> call, Throwable t) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);

            }
        });
    }

    private void setRecyclerConfig() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        // Set items on linear manager
        restaurantsFragmentRvMyRestaurants.setLayoutManager(linearLayoutManager);
        // Fixed size
        restaurantsFragmentRvMyRestaurants.setHasFixedSize(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(userCall !=null){
            userCall.cancel();
        }
    }
    @Override
    public void onBack() {
        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();
    }

}
