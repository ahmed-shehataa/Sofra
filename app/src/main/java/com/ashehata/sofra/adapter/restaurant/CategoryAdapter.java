package com.ashehata.sofra.adapter.restaurant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.Categories.Categories;
import com.ashehata.sofra.data.model.reataurant.Categories.CategoriesData;
import com.ashehata.sofra.data.model.reataurant.GeneralResponse;
import com.ashehata.sofra.helper.CustomDialogCategory;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.activity.BaseActivity;
import com.ashehata.sofra.ui.fragment.restaurant.CategoriesFragment;
import com.ashehata.sofra.ui.fragment.restaurant.FoodItemFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.ashehata.sofra.helper.HelperMethod.convertToRequestBody;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private View.OnClickListener onClickListener;
    private Context context;
    private BaseActivity activity;
    private List<CategoriesData> categoryData;
    private FragmentManager fragmentManager;
    private String mToken;
    private GetDataService getDataService;
    public CustomDialogCategory customDialogCategory;

//    private List<RestaurantClientData> restaurantDataList = new ArrayList<>();


    public CategoryAdapter(Activity activity, Context context, List<CategoriesData> categoryData) {
        this.activity = (BaseActivity) activity;
        this.context = context;
        this.categoryData = categoryData;
        mToken = SharedPreferencesManger.LoadData(activity,SharedPreferencesManger.API_TOKEN_RESTAURANT);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_category,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);


    }


    private void setData(ViewHolder holder, int position) {
        holder.categoryName.setText(categoryData.get(position).getName());
        String imageUrl = categoryData.get(position).getPhotoUrl();
        //Log.v("photo", imageUrl);
        onLoadImageFromUrl(holder.categoryImage, imageUrl, context);

    }


    private void setAction(ViewHolder holder, int position) {

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(holder, position);
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCategory(holder, position);
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodItemFragment foodItemFragment = new FoodItemFragment();
                foodItemFragment.categoryId = categoryData.get(position).getId() + "";
                foodItemFragment.categoryName = categoryData.get(position).getName();
                ReplaceFragment(activity.getSupportFragmentManager()
                        , foodItemFragment, R.id.home_activity_fl_display
                        , true);
            }
        });
    }

    private void updateCategory(ViewHolder viewHolder, int position) {
        if (mToken != null) {
            if (InternetState.isConnected(context)) {
                CategoriesData category = categoryData.get(position);
                customDialogCategory = new CustomDialogCategory(activity,
                        (CategoriesFragment) activity.baseFragment);
                customDialogCategory.show();
                //set category's data
                customDialogCategory.dialogCategoryTitle.setText(activity.getString(R.string.update_category));
                customDialogCategory.dialogCategoryName.setText(category.getName());
                customDialogCategory.categoryAdd.setText(activity.getString(R.string.update));
                //load photo
                onLoadImageFromUrl(customDialogCategory.categoryImage
                        , category.getPhotoUrl(), context);

                customDialogCategory.categoryAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendData(position,customDialogCategory.imagePath);

                    }
                });
            } else {
                dismissProgressDialog();
                createToast(context, activity.getString(R.string.no_internet)
                        , Toast.LENGTH_SHORT);
            }
        }
    }


    private void sendData(int position,String imagePath) {
        // if user updated photo

        if (imagePath.isEmpty() || imagePath.equals("")){
            updateCategory(position,null);
        }else {
            MultipartBody.Part part = convertFileToMultipart(imagePath,"photo");
            updateCategory(position,part);

        }

    }



    private void updateCategory(int position, MultipartBody.Part part) {
        showProgressDialog(activity, activity.getString(R.string.wait_moment));
        disappearKeypad(activity, customDialogCategory.getCurrentFocus());
        String name = customDialogCategory.dialogCategoryName.getText().toString().trim();
        getDataService.updateCategory(part, convertToRequestBody(name)
                , convertToRequestBody(mToken)
                , convertToRequestBody(categoryData.get(position).getId() + "")).enqueue(
                new Callback<Categories>() {
                    @Override
                    public void onResponse(Call<Categories> call, Response<Categories> response) {
                        dismissProgressDialog();
                        try {
                            Categories category = response.body();
                            if (category.getStatus() == 1) {
                                createToast(context, category.getMsg(), Toast.LENGTH_SHORT);

                                //update category list from server
                                CategoriesFragment categoriesFragment = (CategoriesFragment) activity.baseFragment;
                                categoriesFragment.getCategories();
                                customDialogCategory.dismiss();


                            } else {
                                createToast(context, category.getMsg(), Toast.LENGTH_SHORT);
                            }

                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<Categories> call, Throwable t) {
                        dismissProgressDialog();
                        createToast(context, activity.getString(R.string.error)
                                , Toast.LENGTH_SHORT);
                    }
                });

    }

    private void deleteCategory(ViewHolder viewHolder, int position) {
        if (mToken != null) {
            if (InternetState.isConnected(context)) {
                showProgressDialog(activity, activity.getString(R.string.wait_moment));
                getDataService.deleteCategory(mToken, categoryData.get(position).getId().toString()).enqueue(
                        new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                dismissProgressDialog();

                                try {
                                    GeneralResponse deleteCategory = response.body();
                                    if (deleteCategory.getStatus() == 1) {

                                        categoryData.remove(position);
                                        notifyDataSetChanged();
                                        createToast(context, deleteCategory.getMsg()
                                                , Toast.LENGTH_SHORT);

                                    } else {
                                        createToast(context, deleteCategory.getMsg()
                                                , Toast.LENGTH_SHORT);
                                    }

                                } catch (Exception e) {

                                }
                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                dismissProgressDialog();
                            }
                        });

            } else {
                dismissProgressDialog();
                createToast(context, activity.getString(R.string.no_internet)
                        , Toast.LENGTH_SHORT);
            }
        }
    }

    /*
    public void addData(CategoryData data) {
        categoryData.add(data);

    }

     */

    private void showPostDetails(String title, String description, String imageUrl) {
        /*
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        postDetailsFragment.postTitle = title;
        postDetailsFragment.postDescription = description;
        postDetailsFragment.postImage = imageUrl;
        HelperMethod.ReplaceFragment(activity.getSupportFragmentManager(), postDetailsFragment, R.id.home_activity_fl_home, true);

         */
    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public TextView categoryName;
        public CircleImageView categoryImage;
        public ImageView delete;
        public ImageView update;
        public LinearLayout linearLayout;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            //ButterKnife.bind(this, view);
            linearLayout = itemView.findViewById(R.id.content);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryImage = itemView.findViewById(R.id.category_image);
            delete = itemView.findViewById(R.id.category_delete);
            update = itemView.findViewById(R.id.category_update);

        }
    }
}