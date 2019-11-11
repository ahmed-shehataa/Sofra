package com.ashehata.sofra.ui.fragment.restaurant;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.reataurant.GeneralResponse;
import com.ashehata.sofra.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.dismissProgressDialog;
import static com.ashehata.sofra.helper.HelperMethod.showProgressDialog;

public class ChangePasswordFragment extends BaseFragment {


    @BindView(R.id.change_password_fragment_et_old)
    EditText changePasswordFragmentEtOld;
    @BindView(R.id.change_password_fragment_et_new)
    EditText changePasswordFragmentEtNew;
    @BindView(R.id.change_password_fragment_et_new_confirm)
    EditText changePasswordFragmentEtNewConfirm;
    @BindView(R.id.change_password_fragment_btn_chnage)
    Button changePasswordFragmentBtnChnage;

    GetDataService getDataService;
    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;
    private String apiToken;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        apiToken = SharedPreferencesManger.LoadData(getActivity(),SharedPreferencesManger.USER_API_TOKEN);


        return view;
    }

    @OnClick(R.id.change_password_fragment_btn_chnage)
    public void onViewClicked() {
        oldPassword = changePasswordFragmentEtOld.getText().toString().trim();
        newPassword = changePasswordFragmentEtNew.getText().toString().trim();
        newPasswordConfirm = changePasswordFragmentEtNewConfirm.getText().toString().trim();

        //validation
        if (oldPassword.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_old_password)
                    , Toast.LENGTH_SHORT);
        }else if (newPassword.isEmpty() || !newPassword.equals(newPasswordConfirm) || newPasswordConfirm.isEmpty()){
            createToast(getContext(), getString(R.string.insert_password_does_not_match)
                    , Toast.LENGTH_SHORT);

        }else {
            changePassword();
        }


    }

    private void changePassword() {
        showProgressDialog(getActivity(),getString(R.string.wait_moment));
        getDataService.changePassword(apiToken,oldPassword,newPassword, newPasswordConfirm).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                dismissProgressDialog();
               try {
                   GeneralResponse generalResponse = response.body();
                   if (generalResponse.getStatus() == 1){
                       createToast(getContext(),generalResponse.getMsg(), Toast.LENGTH_SHORT);
                       onBack();

                   }else {
                       createToast(getContext(),generalResponse.getMsg(), Toast.LENGTH_SHORT);
                   }
               }catch (Exception e){

               }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                dismissProgressDialog();
                createToast(getContext(),getString(R.string.error), Toast.LENGTH_SHORT);
            }
        });
    }
}
