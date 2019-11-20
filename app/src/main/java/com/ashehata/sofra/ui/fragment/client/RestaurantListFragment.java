package com.ashehata.sofra.ui.fragment.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.restaurant.FoodItemAdapter;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItem;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.createToast;

public class RestaurantListFragment extends BaseFragment {


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.restaurant_list_fragment_rv_my_food_list)
    RecyclerView restaurantListFragmentRvMyFoodList;
    private GetDataService getDataService;
    private LinearLayoutManager linearLayoutManager;
    private Call<FoodItem> call;
    public int restaurantID = 0 ;
    public int categoryID = -1;
    private FoodItemAdapter foodItemAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        //set recycler view configuration
        setRecyclerConfig();

        if (InternetState.isConnected(getContext())) {
            getFoodList();
        } else {
            createToast(getContext(), getString(R.string.no_internet)
                    , Toast.LENGTH_SHORT);
        }

        return view;
    }

    private void getFoodList() {
        call = getDataService.getRestaurantFoodList(restaurantID,categoryID);
        call.enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                try {
                    FoodItem foodItem = response.body();
                    if (foodItem.getStatus() == 1){

                        foodItemAdapter = new FoodItemAdapter(getActivity(), getContext()
                                ,foodItem.getData().getData(),R.layout.custom_restaurant_item);
                        restaurantListFragmentRvMyFoodList.setAdapter(foodItemAdapter);

                    }else {
                        createToast(getContext(), foodItem.getMsg()
                                , Toast.LENGTH_SHORT);

                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
            }
        });

    }

    private void setRecyclerConfig() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        // Set items on linear manager
        restaurantListFragmentRvMyFoodList.setLayoutManager(linearLayoutManager);

        // Fixed size
        restaurantListFragmentRvMyFoodList.setHasFixedSize(true);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call !=null){
            call.cancel();
        }
    }

}
