package com.ashehata.sofra.adapter.client;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.ui.activity.BaseActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        View view = LayoutInflater.from(context).inflate(R.layout.custom_offer,
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
        //Log.v("photo", imageUrl);
        onLoadImageFromUrl(holder.restaurantImage, imageUrl, context);
    }

    private void setAction(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return user.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView restaurantName;
        public CircleImageView restaurantImage;
        public ImageView delete;
        public ImageView update;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            //ButterKnife.bind(this, view);
            restaurantName= itemView.findViewById(R.id.offer_name);
            restaurantImage= itemView.findViewById(R.id.offer_image);
            delete = itemView.findViewById(R.id.offer_delete);
            update = itemView.findViewById(R.id.offer_update);

        }
    }
}