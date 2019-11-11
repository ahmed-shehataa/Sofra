package com.ashehata.sofra.helper;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.Categories.Categories;
import com.ashehata.sofra.ui.fragment.restaurant.CategoriesFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.convertFileToMultipart;
import static com.ashehata.sofra.helper.HelperMethod.convertToRequestBody;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUri;
import static com.ashehata.sofra.helper.HelperMethod.openAlbum;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class CustomDialogCategory extends Dialog {

    Activity activity;
    CategoriesFragment categoriesFragment;
    @BindView(R.id.dialog_category_image)
    public CircleImageView categoryImage;
    @BindView(R.id.dialog_category_add)
    public Button categoryAdd;
    @BindView(R.id.dialog_category_name)
    public EditText dialogCategoryName;
    @BindView(R.id.dialog_category_title)
    public TextView dialogCategoryTitle;

    GetDataService getDataService;
    public String imagePath = "";

    public String categoryName;
    private String apiToken;

    public CustomDialogCategory(Activity activity, CategoriesFragment categoriesFragment) {
        super(activity);
        this.activity = activity;
        this.categoriesFragment = categoriesFragment;
        onCreate();
    }

    protected void onCreate() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_category);
        ButterKnife.bind(this);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @OnClick({R.id.dialog_category_image, R.id.dialog_category_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dialog_category_image:
                getImage();
                break;
            case R.id.dialog_category_add:
                addCategory();
                break;
        }
    }

    private void getImage() {
        openAlbum(1, activity, new ArrayList<>(), new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                imagePath = result.get(0).getPath();
                onLoadImageFromUri(categoryImage, imagePath, activity);
            }
        });
    }

    private void addCategory() {
        categoryName = dialogCategoryName.getText().toString().trim();
        //validation
        if (categoryName.isEmpty()) {
            createToast(getContext(), activity.getString(R.string.insert_category_name)
                    , Toast.LENGTH_SHORT);
        } else if (imagePath.isEmpty()) {
            createToast(getContext(), activity.getString(R.string.choose_photo)
                    , Toast.LENGTH_SHORT);
        } else {
            if (InternetState.isConnected(getContext())) {
                sendData();

            } else {
                createToast(getContext(), activity.getString(R.string.no_internet)
                        , Toast.LENGTH_SHORT);
            }
        }
    }

    private void sendData() {
        //createToast(activity,imageUri.getPath(),Toast.LENGTH_SHORT);
        disappearKeypad(activity, getCurrentFocus());
        showProgressDialog(activity, activity.getString(R.string.wait_moment));
        //get image path
        MultipartBody.Part part = convertFileToMultipart(imagePath, "photo");
        apiToken = SharedPreferencesManger.LoadData(activity,SharedPreferencesManger.USER_API_TOKEN);
        //"Zc3CHfa2Slem4zXMzsP33HRN4k4bq0KAQROROhDUhLFiSbtWRVBcNLZcJJpo";
        getDataService.addCategory(part, convertToRequestBody(categoryName), convertToRequestBody(apiToken)).enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                dismissProgressDialog();
                try {
                    Categories addCategory = response.body();
                    if (addCategory.getStatus() == 1) {
                        createToast(activity, addCategory.getMsg(), Toast.LENGTH_SHORT);
                        //hide dialog
                        dismiss();
                        //update recycler view of category fragment
                        categoriesFragment.getCategories();
                    } else {
                        createToast(activity, addCategory.getMsg(), Toast.LENGTH_SHORT);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                dismissProgressDialog();
                createToast(activity, activity.getString(R.string.error), Toast.LENGTH_SHORT);

            }
        });
    }
}
