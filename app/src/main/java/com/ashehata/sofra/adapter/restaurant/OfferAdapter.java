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
import com.ashehata.sofra.data.model.reataurant.offer.Offer;
import com.ashehata.sofra.data.model.reataurant.offer.OfferData;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.activity.BaseActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder>  {

    private View.OnClickListener onClickListener;
    private Context context;
    private BaseActivity activity;
    private List<OfferData> offerData;
    private FragmentManager fragmentManager;
    private String mToken;
    private GetDataService getDataService;


    public OfferAdapter(Activity activity, Context context, List<OfferData> offerDataList) {
        this.activity = (BaseActivity) activity;
        this.context = context;
        this.offerData = offerDataList;
        mToken =  SharedPreferencesManger.LoadData(activity,SharedPreferencesManger.API_TOKEN_RESTAURANT);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_offer,
                parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull OfferAdapter.ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);

    }

    private void setAction(ViewHolder holder, int position) {
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOfferItem(holder, position);
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }


    private void deleteOfferItem(ViewHolder holder, int position) {

        if(mToken!=null){
            if(InternetState.isConnected(context)){
                showProgressDialog(activity, activity.getString(R.string.wait_moment));
                getDataService.deleteOffer(mToken,offerData.get(position).getId()).enqueue(new Callback<Offer>() {
                    @Override
                    public void onResponse(Call<Offer> call, Response<Offer> response) {
                        dismissProgressDialog();
                        try {
                            Offer offer = response.body();
                            if (offer.getStatus() == 1) {

                                offerData.remove(position);
                                notifyDataSetChanged();
                                createToast(context, offer.getMsg()
                                        , Toast.LENGTH_SHORT);

                            } else {
                                createToast(context, offer.getMsg()
                                        , Toast.LENGTH_SHORT);
                            }

                        } catch (Exception e) {

                        }
                    }
                    @Override
                    public void onFailure(Call<Offer> call, Throwable t) {
                        dismissProgressDialog();

                    }
                });
            }
        }

    }



    private void setData(OfferAdapter.ViewHolder holder, int position) {
        holder.offerName.setText(offerData.get(position).getName());

        String imageUrl = offerData.get(position).getPhotoUrl();
        //Log.v("photo", imageUrl);
        onLoadImageFromUrl(holder.offerImage, imageUrl, context);

    }


    @Override
    public int getItemCount() {
        return offerData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        /*
        public TextView foodItemTitle;
        public TextView foodItemDetails;
        public TextView foodItemPrice;

         */
        public TextView offerName;
        public CircleImageView offerImage;
        public ImageView delete;
        public ImageView update;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            //ButterKnife.bind(this, view);
            offerName= itemView.findViewById(R.id.offer_name);
            offerImage= itemView.findViewById(R.id.offer_image);
            delete = itemView.findViewById(R.id.offer_delete);
            update = itemView.findViewById(R.id.offer_update);

        }
    }

}
