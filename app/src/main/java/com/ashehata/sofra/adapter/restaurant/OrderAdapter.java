package com.ashehata.sofra.adapter.restaurant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.order.OrderData;
import com.ashehata.sofra.ui.activity.BaseActivity;

import java.util.List;

import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>  {

    private View.OnClickListener onClickListener;
    private Context context;
    private BaseActivity activity;
    private List<OrderData> orderData;
    private FragmentManager fragmentManager;
    private String mToken;
    private GetDataService getDataService;


    public OrderAdapter(Activity activity, Context context, List<OrderData> orderData) {
        this.activity = (BaseActivity) activity;
        this.context = context;
        this.orderData = orderData;
        mToken =  SharedPreferencesManger.LoadData(activity,SharedPreferencesManger.API_TOKEN_RESTAURANT);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_order,
                parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setAction(ViewHolder holder, int position) {

    }

    public void addOrder(OrderData data){
        orderData.add(data);
    }


    private void setData(OrderAdapter.ViewHolder holder, int position) {
        holder.orderClient.setText(orderData.get(position).getClient().getName());
        holder.orderNumber.setText(orderData.get(position).getId()+"");
        holder.orderTotal.setText(orderData.get(position).getTotal());
        holder.orderAddress.setText(orderData.get(position).getAddress());

        String imageUrl = orderData.get(position).getClient().getPhotoUrl();
        onLoadImageFromUrl(holder.image, imageUrl, context);

    }


    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public TextView orderClient;
        public TextView orderNumber;
        public TextView orderTotal;
        public TextView orderAddress;

        public ImageView image;
        public Button call ;
        public Button accept ;
        public Button cancel ;



        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            //ButterKnife.bind(this, view);
            image= itemView.findViewById(R.id.order_image);
            orderClient= itemView.findViewById(R.id.order_client_name);
            orderNumber= itemView.findViewById(R.id.order_number);
            orderTotal= itemView.findViewById(R.id.order_total);
            orderAddress= itemView.findViewById(R.id.order_address);
            call = itemView.findViewById(R.id.order_btn_call);
            accept = itemView.findViewById(R.id.order_btn_accept);
            cancel = itemView.findViewById(R.id.order_btn_cancel);
        }
    }
}
