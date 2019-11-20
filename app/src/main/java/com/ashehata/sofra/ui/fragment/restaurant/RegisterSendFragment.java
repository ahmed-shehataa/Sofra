package com.ashehata.sofra.ui.fragment.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.model.reataurant.GeneralResponse;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.convertImageToPart;
import static com.ashehata.sofra.helper.HelperMethod.convertTextToPart;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUri;
import static com.ashehata.sofra.helper.HelperMethod.openAlbum;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class RegisterSendFragment extends BaseFragment {


    @BindView(R.id.register_data_fragment_et_phone)
    TextInputEditText registerDataFragmentEtPhone;
    @BindView(R.id.register_data_fragment_et_whats_app)
    TextInputEditText registerDataFragmentEtWhatsApp;
    @BindView(R.id.register_data_fragment_fl_pic)
    FrameLayout registerDataFragmentFlPic;
    @BindView(R.id.register_data_fragment_btn_register)
    Button registerDataFragmentBtnRegister;

    public String restaurantName;
    public String email;
    public String deliveryTime;
    public String password;
    public String passwordConfirm;
    public String minOrder;
    public String cost;
    public String phone;
    public String whatsApp;
    public String regionId;
    GetDataService getDataService;
    @BindView(R.id.register_data_fragment_iv_photo)
    ImageView registerDataFragmentIvPhoto;
    private String imagePath="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_data, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);


        return view;

    }

    @OnClick({R.id.register_data_fragment_fl_pic, R.id.register_data_fragment_btn_register})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(),getView());
        switch (view.getId()) {
            case R.id.register_data_fragment_fl_pic:

                openAlbum(1, getContext(), new ArrayList<AlbumFile>(), new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagePath = result.get(0).getPath();
                        onLoadImageFromUri(registerDataFragmentIvPhoto, imagePath, getContext());
                    }
                });


                break;
            case R.id.register_data_fragment_btn_register:
                register();
                break;
        }
    }

    private void register() {
        phone = registerDataFragmentEtPhone.getText().toString().trim();
        whatsApp = registerDataFragmentEtWhatsApp.getText().toString().trim();

        //validation
        if (phone.isEmpty() || phone.length() != 11) {
            createToast(getContext(), getString(R.string.insert_correct_phone), Toast.LENGTH_SHORT);

        } else if (whatsApp.isEmpty() || whatsApp.length() != 11) {
            createToast(getContext(), getString(R.string.insert_correct_whats), Toast.LENGTH_SHORT);

        } else if (imagePath.isEmpty()) {
            createToast(getContext(), getString(R.string.choose_photo), Toast.LENGTH_SHORT);

        } else if (InternetState.isConnected(getContext())) {
            sendData();

        } else {
            createToast(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT);

        }
    }

    private void sendData() {
        showProgressDialog(getActivity(),getString(R.string.wait_moment));
        disappearKeypad(getActivity(),getView());
        MultipartBody.Part part = convertImageToPart(imagePath);
        getDataService.register(part, convertTextToPart(restaurantName)
                , convertTextToPart(email)
                , convertTextToPart(deliveryTime)
                , convertTextToPart(password)
                , convertTextToPart(passwordConfirm)
                , convertTextToPart(phone)
                , convertTextToPart(whatsApp)
                , convertTextToPart(regionId)
                , convertTextToPart(cost)
                , convertTextToPart(minOrder)).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
               try {
                   GeneralResponse generalResponse = response.body();
                   if(generalResponse.getStatus() ==1){
                       createToast(getContext(), generalResponse.getMsg(), Toast.LENGTH_SHORT);
                       ReplaceFragment(getFragmentManager(), new LoginFragment(), R.id.login_activity_fl_login,false);

                   }else {
                       createToast(getContext(),generalResponse.getMsg(), Toast.LENGTH_SHORT);
                       onBack();
                   }

               }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                dismissProgressDialog();
                //createToast(getContext(),getString(R.string.error), Toast.LENGTH_SHORT);

            }
        });
    }
}
