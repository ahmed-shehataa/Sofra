package com.ashehata.sofra.ui.fragment.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItem;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItemData;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.convertImageToPart;
import static com.ashehata.sofra.helper.HelperMethod.convertTextToPart;
import static com.ashehata.sofra.helper.HelperMethod.convertToRequestBody;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUri;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.ashehata.sofra.helper.HelperMethod.openAlbum;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class AddFoodFragment extends BaseFragment {


    @BindView(R.id.add_food_fragment_iv_photo)
    public ImageView addFoodFragmentIvPhoto;
    @BindView(R.id.add_food_fragment_et_product_name)
    public EditText addFoodFragmentEtProductName;
    @BindView(R.id.add_food_fragment_et_description)
    public EditText addFoodFragmentEtDescription;
    @BindView(R.id.add_food_fragment_et_price)
    public EditText addFoodFragmentEtPrice;
    @BindView(R.id.add_food_fragment_et_price_in_sale)
    public EditText addFoodFragmentEtPriceInSale;
    @BindView(R.id.add_food_fragment_btn_add)
    public Button addFoodFragmentBtnAdd;
    @BindView(R.id.add_food_fragment_tv_title)
    TextView addFoodFragmentTvTitle;
    private String imagePath = "";
    private String productName;
    private String description;
    private String price;
    private String priceInSale;
    private GetDataService getDataService;
    private String apiToken;
    public String categoryId;
    public boolean isUpdate = false;
    boolean isPhotoUpdated =false;
    public FoodItemData foodItemData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_food, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);

        if (isUpdate) {

            onLoadImageFromUrl(addFoodFragmentIvPhoto, foodItemData.getPhotoUrl(), getContext());
            addFoodFragmentEtProductName.setText(foodItemData.getName());
            addFoodFragmentEtDescription.setText(foodItemData.getDescription());
            addFoodFragmentEtPrice.setText(foodItemData.getPrice());
            addFoodFragmentEtPriceInSale.setText(foodItemData.getOfferPrice());
            addFoodFragmentBtnAdd.setText(getString(R.string.update));
            addFoodFragmentTvTitle.setText(getString(R.string.product_photo));
             categoryId = foodItemData.getCategoryId();
            imagePath = foodItemData.getPhoto();
        }
        return view;
    }

    @OnClick({R.id.add_food_fragment_iv_photo, R.id.add_food_fragment_btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_food_fragment_iv_photo:
                openAlbum(1, getContext(), new ArrayList<>(), new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagePath = result.get(0).getPath();
                        isPhotoUpdated = true;
                        onLoadImageFromUri(addFoodFragmentIvPhoto, imagePath, getContext());
                    }
                });
                break;
            case R.id.add_food_fragment_btn_add:
                validation();

                break;
        }
    }

    private void validation() {
        //validation
        apiToken =  SharedPreferencesManger.LoadData(getActivity(),SharedPreferencesManger.API_TOKEN_RESTAURANT);
        productName = addFoodFragmentEtProductName.getText().toString().trim();
        description = addFoodFragmentEtDescription.getText().toString().trim();
        price = addFoodFragmentEtPrice.getText().toString().trim();
        priceInSale = addFoodFragmentEtPriceInSale.getText().toString().trim();

        if (imagePath.isEmpty() || imagePath.equals("")) {
            createToast(getContext(), getString(R.string.choose_photo), Toast.LENGTH_SHORT);
        } else if (productName.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_product_name), Toast.LENGTH_SHORT);
        } else if (description.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_description), Toast.LENGTH_SHORT);
        } else if (price.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_price), Toast.LENGTH_SHORT);

        } else {
            if (InternetState.isConnected(getContext())) {
                if(isUpdate){
                    if(isPhotoUpdated){
                        //if user updated photo
                        MultipartBody.Part part = convertImageToPart(imagePath);
                        updateItem(part);
                    }else {
                        updateItem(null);
                    }
                }else {
                    addItem();
                }
            } else {
                createToast(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT);

            }
        }
    }

    private void updateItem(MultipartBody.Part part) {
        onCall(getDataService.updateFoodItem(part,convertToRequestBody(foodItemData.getId()+""),
                convertTextToPart(productName),
                convertTextToPart(description),
                convertTextToPart(price),
                convertTextToPart(apiToken),
                convertTextToPart(priceInSale),
                convertTextToPart(categoryId)));
    }

    private void onCall(Call<FoodItem> foodItemCall){
        disappearKeypad(getActivity(), getView());
        showProgressDialog(getActivity(), getString(R.string.wait_moment));
        foodItemCall.enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                dismissProgressDialog();
                try {
                    FoodItem foodItem = response.body();
                    if (foodItem.getStatus() == 1) {
                        createToast(getContext(), foodItem.getMsg(), Toast.LENGTH_SHORT);
                        onBack();

                    } else {
                        createToast(getContext(), foodItem.getMsg(), Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {
                dismissProgressDialog();
                //createToast(getContext(), getString(R.string.error), Toast.LENGTH_SHORT);
            }
        });
    }

    private void addItem() {
        MultipartBody.Part part = convertImageToPart(imagePath);
        onCall(getDataService.addFoodItem(part, convertTextToPart(productName),
                convertTextToPart(description),
                convertTextToPart(price),
                convertTextToPart(apiToken), convertTextToPart(priceInSale),
                convertTextToPart(categoryId)));

    }
}
