package com.ashehata.sofra.adapter.restaurant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.GeneralResponse;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItem;
import com.ashehata.sofra.data.model.reataurant.foodItem.FoodItemData;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.activity.BaseActivity;
import com.ashehata.sofra.ui.fragment.restaurant.AddFoodFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder>  {

    private View.OnClickListener onClickListener;
    private Context context;
    private BaseActivity activity;
    private List<FoodItemData> foodItemData;
    private FragmentManager fragmentManager;
    private String mToken;
    private GetDataService getDataService;


    public FoodItemAdapter(Activity activity, Context context, List<FoodItemData> foodItemData) {
        this.activity = (BaseActivity) activity;
        this.context = context;
        this.foodItemData = foodItemData;
        mToken =  SharedPreferencesManger.LoadData(activity,SharedPreferencesManger.USER_API_TOKEN);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_food_item,
                parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setAction(ViewHolder holder, int position) {
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFoodItem(holder, position);
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFoodFragment addFoodFragment= new AddFoodFragment();
                addFoodFragment.isUpdate = true;
                addFoodFragment.foodItemData = foodItemData.get(position);
                ReplaceFragment(activity.getSupportFragmentManager(),addFoodFragment
                        , R.id.home_activity_fl_display, true);

            }
        });

    }

    private void deleteFoodItem(ViewHolder holder, int position) {

        if(mToken!=null){
            if(InternetState.isConnected(context)){
                showProgressDialog(activity, activity.getString(R.string.wait_moment));
                getDataService.deleteFoodItem(mToken,foodItemData.get(position).getId()+"").enqueue(new Callback<FoodItem>() {
                    @Override
                    public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                        dismissProgressDialog();
                        try {
                            FoodItem foodItem = response.body();
                            if (foodItem.getStatus() == 1) {

                                foodItemData.remove(position);
                                notifyDataSetChanged();
                                createToast(context, foodItem.getMsg()
                                        , Toast.LENGTH_SHORT);

                            } else {
                                createToast(context, foodItem.getMsg()
                                        , Toast.LENGTH_SHORT);
                            }

                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onFailure(Call<FoodItem> call, Throwable t) {
                        dismissProgressDialog();

                    }
                });
            }
        }

    }

    private void setData(FoodItemAdapter.ViewHolder holder, int position) {
        holder.foodItemTitle.setText(foodItemData.get(position).getName());
        holder.foodItemDetails.setText(foodItemData.get(position).getDescription());
        holder.foodItemPrice.setText(foodItemData.get(position).getPrice());


        String imageUrl = foodItemData.get(position).getPhotoUrl();
        //Log.v("photo", imageUrl);
        onLoadImageFromUrl(holder.foodItemImage, imageUrl, context);

    }


    @Override
    public int getItemCount() {
        return foodItemData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public TextView foodItemTitle;
        public TextView foodItemDetails;
        public TextView foodItemPrice;

        public CircleImageView foodItemImage;
        public ImageView delete;
        public ImageView update;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            //ButterKnife.bind(this, view);
            foodItemTitle = itemView.findViewById(R.id.food_item_title);
            foodItemDetails= itemView.findViewById(R.id.food_item_details);
            foodItemPrice = itemView.findViewById(R.id.food_item_price);

            foodItemImage = itemView.findViewById(R.id.food_item_image);
            delete = itemView.findViewById(R.id.food_item_delete);
            update = itemView.findViewById(R.id.food_item_update);

        }
    }

}
