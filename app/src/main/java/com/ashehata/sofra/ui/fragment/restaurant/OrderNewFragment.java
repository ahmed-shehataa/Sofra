package com.ashehata.sofra.ui.fragment.restaurant;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.adapter.restaurant.OrderAdapter;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.order.Order;
import com.ashehata.sofra.data.model.reataurant.order.OrderData;
import com.ashehata.sofra.helper.HelperMethod;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.helper.OnEndLess;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.createToast;

public class OrderNewFragment extends BaseFragment {


    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerViewContainer;
    @BindView(R.id.order_new_fragment_rv_my_orders)
    RecyclerView orderNewFragmentRvMyOrders;
    @BindView(R.id.order_new_fragment_pb_indicator)
    ProgressBar orderNewFragmentPbIndicator;
    private GetDataService getDataService;
    private LinearLayoutManager linearLayoutManager;
    private String apiToken;
    private OnEndLess onEndLess;
    private List<OrderData> orderDataList;
    private OrderAdapter orderAdapter;
    private Call<Order> callOrders;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_new, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        apiToken = SharedPreferencesManger.LoadData(getActivity(), SharedPreferencesManger.USER_API_TOKEN);

        //set recycler view configuration
        setRecyclerConfig();
        orderNewFragmentPbIndicator.setVisibility(View.GONE);

        if (InternetState.isConnected(getContext())) {
            //getNextPage();
            loadMore(1);
        } else {
            createToast(getActivity(), getString(R.string.no_internet)
                    , Toast.LENGTH_SHORT);
        }

        return view;
    }



    private void loadMore(int current_page) {
         callOrders =getDataService.getOrders(apiToken, HelperMethod.ORDER_PENDING, current_page);

         callOrders.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                orderNewFragmentPbIndicator.setVisibility(View.GONE);

                try {
                    Order order = response.body();
                    if (order.getStatus() == 1) {
                        orderDataList = order.getData().getData();
                        if (current_page == 1) {
                            orderAdapter = new OrderAdapter(getActivity(), getContext(), orderDataList);
                            orderNewFragmentRvMyOrders.setAdapter(orderAdapter);
                            getNextPage();

                        } else {
                            if (orderDataList.size() != 0) {
                                for (int i = 0; i < orderDataList.size(); i++) {
                                    orderAdapter.addOrder(orderDataList.get(i));
                                }
                                orderAdapter.notifyDataSetChanged();
                            }
                        }

                    } else {
                        createToast(getContext(), order.getMsg()
                                , Toast.LENGTH_SHORT);
                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                // stop animating Shimmer and hide the layout
                shimmerViewContainer.stopShimmer();
                shimmerViewContainer.setVisibility(View.GONE);
                orderNewFragmentPbIndicator.setVisibility(View.GONE);
                try {
                    createToast(getActivity(), getString(R.string.error)
                            , Toast.LENGTH_SHORT);
                }catch (Exception e){
                    Log.v("catch error",e.getMessage());
                }

            }
        });
    }

    private void getNextPage() {
        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                orderNewFragmentPbIndicator.setVisibility(View.VISIBLE);
                loadMore(current_page);
            }
        };
        orderNewFragmentRvMyOrders.addOnScrollListener(onEndLess);
    }

    private void setRecyclerConfig() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        // Set items on linear manager
        orderNewFragmentRvMyOrders.setLayoutManager(linearLayoutManager);
        // Fixed size
        orderNewFragmentRvMyOrders.setHasFixedSize(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callOrders !=null){
            callOrders.cancel();
        }
    }
}