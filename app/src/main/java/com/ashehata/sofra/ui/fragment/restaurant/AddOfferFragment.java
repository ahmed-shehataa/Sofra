package com.ashehata.sofra.ui.fragment.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.DateModel;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItem;
import com.ashehata.sofra.data.model.reataurant.offer.Offer;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.convertImageToPart;
import static com.ashehata.sofra.helper.HelperMethod.convertTextToPart;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUri;
import static com.ashehata.sofra.helper.HelperMethod.openAlbum;
import static com.ashehata.sofra.helper.HelperMethod.showCalender;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class AddOfferFragment extends BaseFragment {

    @BindView(R.id.txt_from)
    TextView txtFrom;
    @BindView(R.id.txt_to)
    TextView txtTo;
    @BindView(R.id.add_offer_fragment_tv_title)
    TextView addOfferFragmentTvTitle;
    @BindView(R.id.add_offer_fragment_iv_photo)
    ImageView addOfferFragmentIvPhoto;
    @BindView(R.id.add_offer_fragment_et_offer_name)
    EditText addOfferFragmentEtOfferName;
    @BindView(R.id.add_offer_fragment_et_price)
    EditText addOfferFragmentEtPrice;
    @BindView(R.id.add_offer_fragment_et_offer_price)
    EditText addOfferFragmentEtOfferPrice;
    @BindView(R.id.add_offer_fragment_et_offer_description)
    EditText addOfferFragmentEtOfferDescription;
    @BindView(R.id.add_offer_fragment_ll_from)
    LinearLayout addOfferFragmentLlFrom;
    @BindView(R.id.add_offer_fragment_ll_to)
    LinearLayout addOfferFragmentLlTo;
    @BindView(R.id.add_offer_fragment_btn_add)
    Button addOfferFragmentBtnAdd;
    private String imagePath = "";
    private boolean isPhotoUpdated = false;
    private String apiToken;
    private String orderName;
    private String description;
    DateModel dateFrom = new DateModel();
    DateModel dateTo = new DateModel();
    private GetDataService getDataService;
    private String price;
    private String offerPrice;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_offer, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);


        return view;


    }

    @OnClick({R.id.add_offer_fragment_iv_photo, R.id.add_offer_fragment_ll_from, R.id.add_offer_fragment_ll_to, R.id.add_offer_fragment_btn_add})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.add_offer_fragment_iv_photo:
                addPhoto();
                break;
            case R.id.add_offer_fragment_ll_from:
                openCalender(dateFrom, txtFrom);
                break;
            case R.id.add_offer_fragment_ll_to:
                openCalender(dateTo, txtTo);
                break;
            case R.id.add_offer_fragment_btn_add:
                validation();
                break;
        }
    }

    private void openCalender(DateModel dateModel, TextView textView) {
        showCalender(getContext(), getString(R.string.choose_time), textView, dateModel);
    }

    private void validation() {
        orderName = addOfferFragmentEtOfferName.getText().toString().trim();
        description = addOfferFragmentEtOfferDescription.getText().toString().trim();
        price = addOfferFragmentEtPrice.getText().toString().trim();
        offerPrice = addOfferFragmentEtOfferPrice.getText().toString().trim();

        if (orderName.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_order_name), Toast.LENGTH_SHORT);
        } else if (description.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_description), Toast.LENGTH_SHORT);
        } else if (imagePath.isEmpty()) {
            createToast(getContext(), getString(R.string.choose_photo), Toast.LENGTH_SHORT);
        } else if (dateFrom.getDateTxt().isEmpty()) {
            createToast(getContext(), getString(R.string.insert_date_from), Toast.LENGTH_SHORT);
        } else if (dateTo.getDateTxt().isEmpty()) {
            createToast(getContext(), getString(R.string.insert_date_to), Toast.LENGTH_SHORT);
        } else {
            if (InternetState.isConnected(getContext())) {
                addOrder();

            } else {
                createToast(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT);
            }
        }

    }

    private void addOrder() {
        apiToken = SharedPreferencesManger.LoadData(getActivity(), SharedPreferencesManger.USER_API_TOKEN);
        disappearKeypad(getActivity(), getView());
        showProgressDialog(getActivity(), getString(R.string.wait_moment));
        getDataService.addOffer(convertImageToPart(imagePath),
                convertTextToPart(orderName),
                convertTextToPart(description),
                convertTextToPart(price),
                convertTextToPart(offerPrice),
                convertTextToPart(apiToken),
                convertTextToPart(dateFrom.getDateTxt()),
                convertTextToPart(dateTo.getDateTxt())).enqueue(new Callback<Offer>() {
            @Override
            public void onResponse(Call<Offer> call, Response<Offer> response) {
                dismissProgressDialog();
                try {
                    Offer offer = response.body();
                    if (offer.getStatus() == 1) {
                        createToast(getContext(), offer.getMsg(), Toast.LENGTH_SHORT);
                        onBack();

                    } else {
                        createToast(getContext(), offer.getMsg(), Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<Offer> call, Throwable t) {
                dismissProgressDialog();
                //createToast(getContext(), getString(R.string.error), Toast.LENGTH_SHORT);
            }
        });


    }


    private void addPhoto() {
        openAlbum(1, getContext(), new ArrayList<>(), new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                imagePath = result.get(0).getPath();
                isPhotoUpdated = true;
                onLoadImageFromUri(addOfferFragmentIvPhoto, imagePath, getContext());
            }
        });
    }
}
