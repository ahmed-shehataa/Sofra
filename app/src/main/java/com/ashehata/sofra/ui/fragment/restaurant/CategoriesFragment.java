package com.ashehata.sofra.ui.fragment.restaurant;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.restaurant.CategoryAdapter;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.Categories.Categories;
import com.ashehata.sofra.data.model.reataurant.Categories.CategoriesData;
import com.ashehata.sofra.helper.CustomDialogCategory;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.activity.SplashActivity;
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

import static com.ashehata.sofra.helper.HelperMethod.createToast;

public class CategoriesFragment extends BaseFragment {


    @BindView(R.id.categories_fragment_rv_my_category)
    RecyclerView categoriesFragmentRvMyCategory;
    @BindView(R.id.categories_fragment_btn_add_category)
    FloatingActionButton categoriesFragmentBtnAddCategory;

    GetDataService getDataService;
    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    private List<CategoriesData> categoryDataList;
    private String apiToken;
    private CustomDialogCategory customDialogCategory;
    public CategoryAdapter categoryAdapter;
    private Call<Categories> call;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_categories, container, false);
        ButterKnife.bind(this, view);

        getDataService = RetrofitClient.getClient().create(GetDataService.class);

        //set recycler view configuration
        setRecyclerConfig();

        if (InternetState.isConnected(getContext())) {
            getCategories();
        } else {
            createToast(getContext(), getString(R.string.no_internet)
                    , Toast.LENGTH_SHORT);
        }

        return view;
    }

    public void getCategories() {
        apiToken = SharedPreferencesManger.LoadData(getActivity(),SharedPreferencesManger.USER_API_TOKEN);
        call = getDataService.getCategories(apiToken) ;
        call.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                try {
                    Categories restaurantCategory = response.body();

                    if (restaurantCategory.getStatus() == 1) {

                        categoryDataList = restaurantCategory.getData().getData();
                        categoryAdapter = new CategoryAdapter(getActivity(), getContext()
                                , categoryDataList);

                        categoriesFragmentRvMyCategory.setAdapter(categoryAdapter);

                    } else {
                        createToast(getContext(), restaurantCategory.getMsg()
                                , Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                try {
                    // stop animating Shimmer and hide the layout
                    shimmerViewContainer.stopShimmer();
                    shimmerViewContainer.setVisibility(View.GONE);
//                    createToast(getContext(), getString(R.string.error)
//                            , Toast.LENGTH_SHORT);
                }catch (Exception e){}
            }
        });
    }

    private void setRecyclerConfig() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        // Set items on linear manager
        categoriesFragmentRvMyCategory.setLayoutManager(linearLayoutManager);

        // Fixed size
        categoriesFragmentRvMyCategory.setHasFixedSize(true);
    }

    @Override
    public void onBack() {
        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();
    }

    @OnClick(R.id.categories_fragment_btn_add_category)
    public void onViewClicked() {

        customDialogCategory = new CustomDialogCategory(getActivity(),this);
        customDialogCategory.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(call !=null){
            call.cancel();
        }
    }
}
