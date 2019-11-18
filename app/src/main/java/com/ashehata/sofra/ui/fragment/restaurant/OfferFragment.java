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
import com.ashehata.sofra.adapter.restaurant.OfferAdapter;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.offer.Offer;
import com.ashehata.sofra.data.model.reataurant.offer.OfferData;
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

public class OfferFragment extends BaseFragment {

    @BindView(R.id.offer_fragment_tv_title)
    TextView offerFragmentTvTitle;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.offer_fragment_rv_my_offer)
    RecyclerView offerFragmentRvMyOffer;
    @BindView(R.id.offer_fragment_btn_add_offer)
    FloatingActionButton offerFragmentBtnAddOffer;
    private GetDataService getDataService;
    private LinearLayoutManager linearLayoutManager;
    private String apiToken;
    private Call<Offer> offerCall;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);
        ButterKnife.bind(this,view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);

        //set recycler view configuration
        setRecyclerConfig();

        if (InternetState.isConnected(getContext())) {
            getOrder();
        } else {
            createToast(getContext(), getString(R.string.no_internet)
                    , Toast.LENGTH_SHORT);
        }

        return view;
    }


    private void setRecyclerConfig() {
        linearLayoutManager = new LinearLayoutManager(getContext());
        // Set items on linear manager
        offerFragmentRvMyOffer.setLayoutManager(linearLayoutManager);
        // Fixed size
        offerFragmentRvMyOffer.setHasFixedSize(true);
    }

    private void getOrder() {
        apiToken = SharedPreferencesManger.LoadData(getActivity(), SharedPreferencesManger.USER_API_TOKEN);
        offerCall =getDataService.getOffers(apiToken,1);

        offerCall.enqueue(new Callback<Offer>() {
            @Override
            public void onResponse(Call<Offer> call, Response<Offer> response) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                try {
                    Offer offer= response.body();
                    if (offer.getStatus()==1){
                        List<OfferData> offerDataList = offer.getData().getData();
                        OfferAdapter offerAdapter = new OfferAdapter(getActivity(),getContext(),offerDataList);
                        offerFragmentRvMyOffer.setAdapter(offerAdapter);

                    }else {
                        createToast(getContext(), offer.getMsg()
                                , Toast.LENGTH_SHORT);
                    }

                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Offer> call, Throwable t) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);

                try {
                    createToast(getContext(), getString(R.string.error)
                            , Toast.LENGTH_SHORT);

                }catch (Exception e){

                }

            }
        });
    }

    @OnClick(R.id.offer_fragment_btn_add_offer)
    public void onViewClicked() {
        ReplaceFragment(getFragmentManager(),
                        new AddOfferFragment(),
                        R.id.home_activity_fl_display,
                        true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (offerCall !=null) {
            offerCall.cancel();
        }
    }
}