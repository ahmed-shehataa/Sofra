package com.ashehata.sofra.ui.fragment.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItemData;
import com.ashehata.sofra.helper.HelperMethod;
import com.ashehata.sofra.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static java.lang.Integer.parseInt;

public class OrderFoodFragment extends BaseFragment {


    public FoodItemData foodItemData;
    @BindView(R.id.offer_food_fragment_iv_image)
    ImageView offerFoodFragmentIvImage;
    @BindView(R.id.offer_food_fragment_tv_name)
    TextView offerFoodFragmentTvName;
    @BindView(R.id.offer_food_fragment_tv_description)
    TextView offerFoodFragmentTvDescription;
    @BindView(R.id.offer_food_fragment_tv_price)
    TextView offerFoodFragmentTvPrice;
    @BindView(R.id.offer_food_fragment_v_offer_line)
    View offerFoodFragmentVOfferLine;
    @BindView(R.id.offer_food_fragment_tv_price_offer)
    TextView offerFoodFragmentTvPriceOffer;
    @BindView(R.id.offer_food_fragment_et_special_offer)
    EditText offerFoodFragmentEtSpecialOffer;
    @BindView(R.id.offer_food_fragment_btn_increase)
    Button offerFoodFragmentBtnIncrease;
    @BindView(R.id.offer_food_fragment_btn_display_quantity)
    Button offerFoodFragmentBtnDisplayQuantity;
    @BindView(R.id.offer_food_fragment_btn_decrease)
    Button offerFoodFragmentBtnDecrease;
    @BindView(R.id.offer_food_fragment_btn_confirm)
    ImageView offerFoodFragmentBtnConfirm;
    private int currentNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_food, container, false);
        ButterKnife.bind(this,view);

        displayData();

        return view;
    }

    private void displayData() {
        if (foodItemData != null) {
            onLoadImageFromUrl(offerFoodFragmentIvImage,foodItemData.getPhotoUrl(),getContext());
            offerFoodFragmentTvName.setText(foodItemData.getName());
            offerFoodFragmentTvDescription.setText(foodItemData.getDescription());
            offerFoodFragmentTvPrice.setText(foodItemData.getPrice());
            if(foodItemData.getHasOffer()){
                offerFoodFragmentVOfferLine.setVisibility(View.VISIBLE);
                offerFoodFragmentTvPriceOffer.setText(foodItemData.getOfferPrice());

            }else {
                offerFoodFragmentVOfferLine.setVisibility(View.GONE);
                offerFoodFragmentTvPriceOffer.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.offer_food_fragment_btn_increase, R.id.offer_food_fragment_btn_decrease, R.id.offer_food_fragment_btn_confirm})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(),getView());
        currentNum = parseInt(offerFoodFragmentBtnDisplayQuantity.getText().toString());
        switch (view.getId()) {
            case R.id.offer_food_fragment_btn_increase:
                currentNum ++;
                offerFoodFragmentBtnDisplayQuantity.setText(currentNum+"");

                break;
            case R.id.offer_food_fragment_btn_decrease:
                currentNum --;
                if(currentNum == 0){
                    currentNum = 1;
                    return;
                }
                offerFoodFragmentBtnDisplayQuantity.setText(currentNum+"");

                break;
            case R.id.offer_food_fragment_btn_confirm:
                break;
        }
    }
}
