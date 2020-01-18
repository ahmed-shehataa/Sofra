package com.ashehata.sofra.ui.fragment.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.Profile;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.changeState.ChangeState;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.ui.fragment.BaseFragment;

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
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class ProfileSendFragment extends BaseFragment implements Switch.OnClickListener {


    @BindView(R.id.profile_send_fragment_et_min_order)
    EditText profileSendFragmentEtCost;
    @BindView(R.id.profile_send_fragment_et_deliver_time)
    EditText profileSendFragmentEtDeliverTime;
    @BindView(R.id.profile_send_fragment_sw_status)
    Switch profileSendFragmentSwStatus;
    @BindView(R.id.profile_send_fragment_et_phone)
    EditText profileSendFragmentEtPhone;
    @BindView(R.id.profile_send_fragment_et_whatsapp)
    EditText profileSendFragmentEtWhatsapp;
    @BindView(R.id.profile_send_fragment_btn_update)
    Button profileSendFragmentBtnUpdate;
    @BindView(R.id.profile_send_fragment_ll_parent)
    LinearLayout profileSendFragmentLlParent;
    private User user;
    private String status;
    private boolean isActivated=false;
    private GetDataService getDataService;
    private String mToken;
    private boolean b;
    public int regionId=0 ;
    public String restaurantName="" ;
    public String restaurantEmail="" ;
    public String minOrder="" ;
    public String imagePath = "";
    private String deliverTime;
    private String deliverCost;
    private String phone;
    private String whatsApp;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_send, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        mToken = SharedPreferencesManger.LoadData(getActivity(),SharedPreferencesManger.API_TOKEN_RESTAURANT);

        displayUserData();

        return view;

    }

    private void displayUserData() {
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        profileSendFragmentEtCost.setText(user.getDeliveryCost());
        profileSendFragmentEtDeliverTime.setText(user.getDeliveryTime());
        profileSendFragmentEtPhone.setText(user.getPhone());
        profileSendFragmentEtWhatsapp.setText(user.getWhatsapp());
        status =user.getAvailability();
        if(status.equals("open")){
            isActivated = true;
        }else {
            isActivated = false;
        }
        profileSendFragmentSwStatus.setChecked(isActivated);
        profileSendFragmentSwStatus.setOnClickListener(this);

    }

    @OnClick({R.id.profile_send_fragment_btn_update, R.id.profile_send_fragment_ll_parent})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(),getView());
        switch (view.getId()) {
            case R.id.profile_send_fragment_btn_update:
                updateProfile();
                break;
            case R.id.profile_send_fragment_ll_parent:
                disappearKeypad(getActivity(),getView());
                break;
        }
    }

    private void updateProfile() {
        if(imagePath.equals("")){
            sendData(null);
        }else {
            sendData(convertImageToPart(imagePath));
        }
    }

    private void sendData(MultipartBody.Part part) {
        disappearKeypad(getActivity(),getView());
        showProgressDialog(getActivity(),getString(R.string.wait_moment));
        deliverTime = profileSendFragmentEtDeliverTime.getText().toString().trim();
        deliverCost = profileSendFragmentEtCost.getText().toString().trim();
        phone = profileSendFragmentEtPhone.getText().toString().trim();
        whatsApp = profileSendFragmentEtWhatsapp.getText().toString().trim();

        Call<Profile> profileCall =getDataService.editProfile(part,
                                    convertTextToPart(restaurantName),
                                    convertTextToPart(restaurantEmail),
                                    convertTextToPart(deliverTime),
                                    convertTextToPart(phone),
                                    convertTextToPart(whatsApp),
                                    convertTextToPart(regionId+""),
                                    convertTextToPart(deliverCost),
                                    convertTextToPart(minOrder),
                                    convertTextToPart(mToken),
                                    convertTextToPart(user.getAvailability()));

        profileCall.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                dismissProgressDialog();
                Profile profile= response.body();
               try {
                   if (profile.getStatus() ==1){
                       createToast(getContext(),profile.getMsg(),Toast.LENGTH_SHORT);
                       SharedPreferencesManger.saveRestaurantData(getActivity(),profile.getData().getUser());
                        ReplaceFragment(getFragmentManager(),
                                        new CategoriesFragment(),
                                        R.id.home_activity_fl_display,
                                        false);

                   }else {
                       createToast(getContext(),profile.getMsg(),Toast.LENGTH_SHORT);

                   }
               }catch (Exception e){}
            }
            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dismissProgressDialog();
                createToast(getContext(),getString(R.string.error),Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onClick(View view) {
        showProgressDialog(getActivity(),getString(R.string.wait_moment));
        disappearKeypad(getActivity(),getView());
        if(!isActivated){
            status = "open";
        }else {
            status = "closed";
        }
        getDataService.changeState(mToken,status).enqueue(new Callback<ChangeState>() {
            @Override
            public void onResponse(Call<ChangeState> call, Response<ChangeState> response) {
                dismissProgressDialog();
                ChangeState data= response.body();
                try {
                    if (data.getStatus() ==1) {
                        //createToast(getContext(),data.getMsg(),Toast.LENGTH_SHORT);
                        user.setAvailability(status);
                        SharedPreferencesManger.saveRestaurantData(getActivity(),user);

                    }else{
                        createToast(getContext(),data.getMsg(),Toast.LENGTH_SHORT);


                    }

                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<ChangeState> call, Throwable t) {
                dismissProgressDialog();
                //createToast(getContext(),getString(R.string.error),Toast.LENGTH_SHORT);
                //set switch to default
                profileSendFragmentSwStatus.setChecked(isActivated);

            }
        });

    }
}
