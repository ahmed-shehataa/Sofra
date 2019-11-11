package com.ashehata.sofra.ui.fragment.restaurant;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.Profile;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.activity.HomeActivity;
import com.ashehata.sofra.ui.activity.SplashActivity;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.setEtTypeFace;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class LoginFragment extends BaseFragment {

    @BindView(R.id.login_fragment_btn_sign_in)
    Button loginFragmentBtnSignIn;
    @BindView(R.id.login_fragment_tv_register)
    LinearLayout loginFragmentTvRegister;
    @BindView(R.id.login_fragment_et_email)
    TextInputEditText loginFragmentEtEmail;
    @BindView(R.id.login_fragment_et_password)
    TextInputEditText loginFragmentEtPassword;
    @BindView(R.id.login_fragment_tv_reset_password)
    TextView loginFragmentTvResetPassword;

    GetDataService getDataService;
    @BindView(R.id.login_fragment_rl_parent)
    RelativeLayout loginFragmentRlParent;
    private String email;
    private String password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        //loginFragmentEtEmail.clearFocus();
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        setEtTypeFace(loginFragmentEtPassword);


        return view;
    }

    @Override
    public void onBack() {
        startActivity(new Intent(getContext(), SplashActivity.class));
        getActivity().finish();
    }

    @OnClick({R.id.login_fragment_btn_sign_in, R.id.login_fragment_tv_register, R.id.login_fragment_tv_reset_password, R.id.login_fragment_rl_parent})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.login_fragment_btn_sign_in:
                signIn();
                break;
            case R.id.login_fragment_tv_register:
                register();
                break;
            case R.id.login_fragment_tv_reset_password:
                resetPassword();
                break;
            case R.id.login_fragment_rl_parent:
                disappearKeypad(getActivity(), getView());
                break;
        }
    }

    private void resetPassword() {

    }

    private void register() {
        ReplaceFragment(getFragmentManager(), new RegisterFragment(), R.id.login_activity_fl_login
                , true);


    }

    private void signIn() {
        //validation
        email = loginFragmentEtEmail.getText().toString().trim();
        password = loginFragmentEtPassword.getText().toString().trim();

        if (email.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_email)
                    , Toast.LENGTH_SHORT);

        } else if (password.isEmpty()) {

            createToast(getContext(), getString(R.string.insert_password)
                    , Toast.LENGTH_SHORT);
        } else {
            if (InternetState.isConnected(getContext())) {
                sendData();

            } else {
                createToast(getContext(), getString(R.string.no_internet)
                        , Toast.LENGTH_SHORT);
            }
        }


    }

    private void sendData() {
        //show progress indicator
        showProgressDialog(getActivity(), getString(R.string.wait_moment));
        disappearKeypad(getActivity(), getView());
        //make an api request
        getDataService.restaurantLogin(email, password).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                //hide progress indicator
                dismissProgressDialog();
                Profile restaurantLogin = response.body();
                if (restaurantLogin.getStatus() == 1) {

                    try {
                        //save user data to shared preference
                        SharedPreferencesManger.saveUserData(getActivity(),restaurantLogin.getData().getUser());

                        Log.v("myApiToken", restaurantLogin.getData().getApiToken());
                        SharedPreferencesManger.SaveData(getActivity(),SharedPreferencesManger.USER_API_TOKEN,
                                restaurantLogin.getData().getApiToken());

                        SharedPreferencesManger.SaveData(getActivity(), SharedPreferencesManger.REMEMBER_RESTAURANT
                                , true);
                        //sent user to home cycle
                        startActivity(new Intent(getActivity(), HomeActivity.class));
                        getActivity().finish();

                    } catch (Exception e) {

                    }

                } else {
                    createToast(getContext(), restaurantLogin.getMsg()
                            , Toast.LENGTH_SHORT);
                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dismissProgressDialog();
                createToast(getContext(), getString(R.string.error)
                        , Toast.LENGTH_SHORT);
            }
        });


    }

}
