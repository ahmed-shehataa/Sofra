package com.ashehata.sofra.ui.fragment.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.restaurant.CategoryAdapter;
import com.ashehata.sofra.adapter.restaurant.FoodItemAdapter;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItem;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItemData;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.createToast;

public class FoodItemFragment extends BaseFragment {


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.food_item_fragment_rv_my_food_item)
    RecyclerView foodItemFragmentRvMyFoodItem;
    @BindView(R.id.food_item_fragment_btn_add_food_item)
    FloatingActionButton foodItemFragmentBtnAddFoodItem;

    GetDataService getDataService;
    @BindView(R.id.food_item_fragment_tv_title)
    TextView foodItemFragmentTvTitle;
    private LinearLayoutManager linearLayoutManager;
    private String apiToken;
    public String categoryId = "";
    public String categoryName = "";
    private List<FoodItemData> foodItemList;
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
        View view = inflater.inflate(R.layout.fragment_food_item, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        foodItemFragmentTvTitle.setText(categoryName);

        //set recycler view configuration
        setRecyclerConfig();

        if (InternetState.isConnected(getContext())) {
            getFoodItem();
        } else {
            createToast(getContext(), getString(R.string.no_internet)
                    , Toast.LENGTH_SHORT);
        }

        return view;
    }

    private void getFoodItem() {

        apiToken =  SharedPreferencesManger.LoadData(getActivity(),SharedPreferencesManger.USER_API_TOKEN);
        getDataService.getFoodItem(apiToken, categoryId).enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                try {
                    FoodItem foodItem = response.body();

                    if (foodItem.getStatus() == 1) {

                        foodItemList = foodItem.getData().getData();
                        foodItemAdapter = new FoodItemAdapter(getActivity(), getContext()
                                , foodItemList);

                        foodItemFragmentRvMyFoodItem.setAdapter(foodItemAdapter);

                    } else {
                        createToast(getContext(), foodItem.getMsg()
                                , Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                createToast(getContext(), getString(R.string.error)
                        , Toast.LENGTH_SHORT);
            }
        });

    }

    private void setRecyclerConfig() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        // Set items on linear manager
        foodItemFragmentRvMyFoodItem.setLayoutManager(linearLayoutManager);

        // Fixed size
        foodItemFragmentRvMyFoodItem.setHasFixedSize(true);

    }

    @OnClick(R.id.food_item_fragment_btn_add_food_item)
    public void onViewClicked() {
        AddFoodFragment addFoodFragment= new AddFoodFragment();
        addFoodFragment.categoryId = categoryId;
        ReplaceFragment(getFragmentManager(),addFoodFragment
                , R.id.home_activity_fl_display, true);

    }
}
