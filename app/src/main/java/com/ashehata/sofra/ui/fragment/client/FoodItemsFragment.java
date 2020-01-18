package com.ashehata.sofra.ui.fragment.client;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.restaurant.FoodItemAdapter;
import com.ashehata.sofra.data.local.room.AppDatabase;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItemData;
import com.ashehata.sofra.helper.HelperMethod;
import com.ashehata.sofra.ui.activity.HomeActivity;
import com.ashehata.sofra.ui.activity.UserActivity;
import com.ashehata.sofra.ui.fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.REMEMBER_CLIENT;
import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.TYPE_CLIENT;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class FoodItemsFragment extends BaseFragment {


    @BindView(R.id.food_items_fragment_rv_my_food_list)
    RecyclerView foodItemsFragmentRvMyFoodList;
    @BindView(R.id.food_items_fragment_pb_indicator)
    ProgressBar foodItemsFragmentPbIndicator;
    @BindView(R.id.food_items_fragment_tv_total)
    TextView foodItemsFragmentTvTotal;
    @BindView(R.id.food_items_fragment_btn_confirm)
    Button foodItemsFragmentBtnConfirm;
    @BindView(R.id.food_items_fragment_btn_add_more)
    Button foodItemsFragmentBtnAddMore;

    List<FoodItemData> foodItemDataList;
    public FoodItemData foodItemData;
    double totalPrice = 0;
    @BindView(R.id.food_items_fragment_iv_empty)
    ImageView foodItemsFragmentIvEmpty;
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
        View view = inflater.inflate(R.layout.fragment_food_items, container, false);
        ButterKnife.bind(this, view);

        setRecyclerConfig();
        // get food items from Db and display it in recyclerView
        getFoodItems();

        return view;
    }

    private void setRecyclerConfig() {
        HelperMethod.setRecyclerConfig(foodItemsFragmentRvMyFoodList, getContext());
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        foodItemsFragmentRvMyFoodList.addItemDecoration(itemDecorator);
        foodItemsFragmentRvMyFoodList.setNestedScrollingEnabled(false);
    }

    private void getFoodItems() {
        AppDatabase.getInstance(getContext()).userDao().getAllFoodItems().observe(getViewLifecycleOwner(), new Observer<List<FoodItemData>>() {
            @Override
            public void onChanged(List<FoodItemData> foodItemData) {
                foodItemDataList = foodItemData;

                displayStoredItems(foodItemData);
                //set items number
                getItemsNumbers(foodItemData);
                // set Items summation
                getTotalPrice(foodItemData);
            }
        });
    }

    private void displayStoredItems(List<FoodItemData> foodItemData) {
        foodItemsFragmentRvMyFoodList.setAdapter(null);
        foodItemAdapter = null ;
        foodItemAdapter = new FoodItemAdapter(getActivity(), getContext()
                , foodItemData, R.layout.custom_food_item_room_db);
        foodItemsFragmentRvMyFoodList.setAdapter(foodItemAdapter);
    }

    private void getItemsNumbers(List<FoodItemData> foodItemData) {
        HomeActivity homeActivity = (HomeActivity) getActivity();
        homeActivity.homeActivityTvFoodItemsNum.setText(foodItemData.size()+"");
        if (foodItemData.size() ==0){
            foodItemsFragmentIvEmpty.setVisibility(View.VISIBLE);
        }else {
            foodItemsFragmentIvEmpty.setVisibility(View.GONE);
        }
        foodItemsFragmentPbIndicator.setVisibility(View.GONE);
    }

    private void getTotalPrice(List<FoodItemData> foodItemData) {
        totalPrice = 0;
        int itemsNum = foodItemData.size();
        if (itemsNum != 0 && foodItemData != null) {
            for (int i = 0; i < itemsNum; i++) {
                if (foodItemData.get(i).getHasOffer()) {
                    totalPrice += parseDouble(foodItemData.get(i).getOfferPrice()) *
                                                foodItemData.get(i).getQuantity();

                } else {
                    totalPrice += parseDouble(foodItemData.get(i).getPrice()) *
                                                foodItemData.get(i).getQuantity();
                }
            }
        }
        foodItemsFragmentTvTotal.setText(totalPrice + "");
    }

    @OnClick({R.id.food_items_fragment_btn_confirm, R.id.food_items_fragment_btn_add_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.food_items_fragment_btn_confirm:
               confirmAction();
                break;
            case R.id.food_items_fragment_btn_add_more:
                onBack();
                break;
        }
    }

    private void confirmAction() {
        if (foodItemDataList != null && foodItemDataList.size() == 0) {
            HelperMethod.createToast(getContext(), getString(R.string.no_items_count), Toast.LENGTH_LONG);

        } else {
            if(SharedPreferencesManger.LoadBoolean(getActivity(),REMEMBER_CLIENT)){
                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                orderDetailsFragment.total = totalPrice ;
                //orderDetailsFragment.deliverCost = foodItemData. ;
                orderDetailsFragment.totalPrice = 0 ;

                HelperMethod.ReplaceFragment(getFragmentManager(),
                        orderDetailsFragment,
                        R.id.home_activity_fl_display,
                        true);
            }else {
                loginFragment();
            }
        }
    }

    private void loginFragment() {
        getActivity().startActivity(new Intent(getActivity(), UserActivity.class).setType(TYPE_CLIENT));
    }
}