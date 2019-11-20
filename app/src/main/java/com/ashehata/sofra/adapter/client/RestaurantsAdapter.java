package com.ashehata.sofra.adapter.client;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.ashehata.sofra.R;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.ui.activity.BaseActivity;
import com.ashehata.sofra.ui.fragment.client.RestaurantDetailsFragment;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.ViewHolder> {

    private View.OnClickListener onClickListener;
    private BaseActivity activity;
    private Context context;
    private List<User> user;

    public RestaurantsAdapter(Activity activity, Context context, List<User> User) {
        this.activity = (BaseActivity) activity;
        this.context = context;
        this.user = User;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_restaurant,
                parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setData(ViewHolder holder, int position) {
        holder.restaurantName.setText(user.get(position).getName());
        String imageUrl = user.get(position).getPhotoUrl();
        onLoadImageFromUrl(holder.restaurantImage, imageUrl, context);
        holder.restaurantMinOrder.setText(user.get(position).getMinimumCharger());
        holder.restaurantCost.setText(user.get(position).getDeliveryCost());
        holder.restaurantOpened.setText(user.get(position).getAvailability());
        if(Integer.parseInt(user.get(position).getActivated()) == 1){
            changeCircleImageColor(holder,R.color.colorAccent);
        }else{
            changeCircleImageColor(holder,R.color.colorRedLight);
        }
        holder.ratingBar.setRating(user.get(position).getRate());
    }

    private void changeCircleImageColor(ViewHolder holder , int resColor){
        ImageViewCompat.setImageTintList(holder.restaurantCircle,
                ColorStateList.valueOf(context.getResources().getColor(resColor)));
    }

    private void setAction(ViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestaurantDetailsFragment restaurantDetailsFragment = new RestaurantDetailsFragment();

                restaurantDetailsFragment.restaurantID = user.get(position).getId();
                restaurantDetailsFragment.restaurantInfo = user.get(position);

                ReplaceFragment(activity.getSupportFragmentManager()
                        , restaurantDetailsFragment, R.id.home_activity_fl_display
                        , true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView restaurantName;
        public CircleImageView restaurantImage;
        public TextView restaurantMinOrder;
        public TextView restaurantCost;
        public TextView restaurantOpened;
        public ImageView restaurantCircle;
        public RatingBar ratingBar;



        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            //ButterKnife.bind(this, view);
            restaurantName= itemView.findViewById(R.id.offer_name);
            restaurantImage= itemView.findViewById(R.id.offer_image);
            restaurantCircle= itemView.findViewById(R.id.restaurant_opened_circle);
            restaurantMinOrder= itemView.findViewById(R.id.restaurant_min_order);
            restaurantCost= itemView.findViewById(R.id.restaurant_cost);
            restaurantOpened= itemView.findViewById(R.id.restaurant_opened);
            ratingBar = itemView.findViewById(R.id.restaurant_rating);
        }
    }
}