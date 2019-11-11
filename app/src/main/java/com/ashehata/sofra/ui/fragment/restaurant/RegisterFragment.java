package com.ashehata.sofra.ui.fragment.restaurant;


import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.model.general.regions.RegionData;
import com.ashehata.sofra.data.model.general.regions.Regions;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.setEtTypeFace;
import static com.ashehata.sofra.helper.HelperMethod.setSpinner;

public class RegisterFragment extends BaseFragment {


    @BindView(R.id.register_fragment_et_restaurant_name)
    TextInputEditText registerFragmentEtRestaurantName;
    @BindView(R.id.register_fragment_et_email)
    TextInputEditText registerFragmentEtEmail;
    @BindView(R.id.register_fragment_et_deliver_time)
    TextInputEditText registerFragmentEtDeliverTime;
    @BindView(R.id.register_fragment_sp_city)
    Spinner registerFragmentSpCity;
    @BindView(R.id.register_fragment_sp_quarter)
    Spinner registerFragmentSpQuarter;
    @BindView(R.id.register_fragment_et_password)
    TextInputEditText registerFragmentEtPassword;
    @BindView(R.id.register_fragment_et_password_confirm)
    TextInputEditText registerFragmentEtPasswordConfirm;
    @BindView(R.id.register_fragment_et_min_order)
    TextInputEditText registerFragmentEtMinOrder;
    @BindView(R.id.register_fragment_et_delver_cost)
    TextInputEditText registerFragmentEtDelverCost;
    @BindView(R.id.register_fragment_btn_go_on)
    Button registerFragmentBtnGoOn;

    GetDataService getDataService;
    private ArrayList<String> citiesList = new ArrayList<>();
    private ArrayList<Integer> citiesId = new ArrayList<>();
    private ArrayList<String> regionList= new ArrayList<>();
    private ArrayList<Integer> regionId= new ArrayList<>();

    public String restaurantName ;
    public String email ;
    public String deliveryTime ;
    public String password ;
    public String passwordConfirm ;
    public String minOrder  ;
    public String cost  ;
    public String region ;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        getCities();
        registerFragmentSpQuarter.setVisibility(View.GONE);
        setEtTypeFace(registerFragmentEtPassword);
        setEtTypeFace(registerFragmentEtPasswordConfirm);



        return view;
    }


    private void getCities() {
        if(InternetState.isConnected(getContext())){
            getDataService.getCities().enqueue(new Callback<Regions>() {
                @Override
                public void onResponse(Call<Regions> call, Response<Regions> response) {
                   try {
                       Regions regions = response.body();
                       if (regions.getStatus() == 1){

                           citiesList.add(getString(R.string.city));
                           citiesId.add(0);
                           List<RegionData> citiesData = regions.getData().getData();
                           for(int i =0; i<citiesData.size() ;i++){
                               citiesList.add(citiesData.get(i).getName());
                               citiesId.add(citiesData.get(i).getId());
                           }
                           setSpinner(getActivity(),registerFragmentSpCity,citiesList);
                           setRegion();

                       }else {

                       }
                   }catch (Exception e){
                   }
                }
                @Override
                public void onFailure(Call<Regions> call, Throwable t) {

                }
            });
        }else {

        }
    }

    private void setRegion() {
        registerFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                TextView textView = (TextView)view;
                textView.setTextColor(getResources().getColor(R.color.colorWhite));
                int id = citiesId.get(i);
                regionList.clear();
                getRegion(id);
                registerFragmentSpQuarter.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getRegion(int id) {
        //Toast.makeText(getActivity(), id+"", Toast.LENGTH_SHORT).show();
        getDataService.getRegion(id).enqueue(new Callback<Regions>() {
            @Override
            public void onResponse(Call<Regions> call, Response<Regions> response) {
                Regions region = response.body();
                if (region.getStatus() == 1) {

                    regionList.add(getString(R.string.region));
                    regionId.add(0);

                    List<RegionData> citiesData = region.getData().getData();
                    for (int i = 0; i < citiesData.size(); i++) {
                        regionList.add(citiesData.get(i).getName());
                        regionId.add(citiesData.get(i).getId());
                    }
                    setSpinner(getActivity(), registerFragmentSpQuarter, regionList);
                }
            }

            @Override
            public void onFailure(Call<Regions> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @OnClick(R.id.register_fragment_btn_go_on)
    public void onViewClicked() {
        disappearKeypad(getActivity(),getView());
        goOn();
    }

    private void goOn() {
        RegisterSendFragment registerSendFragment= new RegisterSendFragment();
        restaurantName = registerFragmentEtRestaurantName.getText().toString().trim();
        email = registerFragmentEtEmail.getText().toString().trim();
        deliveryTime = registerFragmentEtDeliverTime.getText().toString().trim();
        password = registerFragmentEtPassword.getText().toString().trim();
        passwordConfirm = registerFragmentEtPasswordConfirm.getText().toString().trim();
        minOrder = registerFragmentEtMinOrder.getText().toString().trim();
        cost = registerFragmentEtDelverCost.getText().toString().trim();

        if(regionList.size()!=0){
            region = regionId.get(registerFragmentSpQuarter.getSelectedItemPosition())+"";

        }
        try {
            if(restaurantName.isEmpty()){
                createToast(getContext(),getString(R.string.insert_restaurant_name),Toast.LENGTH_SHORT);
            }else  if (email.isEmpty()){
                createToast(getContext(),getString(R.string.insert_email),Toast.LENGTH_SHORT);
            }else if (deliveryTime.isEmpty()){
                createToast(getContext(),getString(R.string.insert_delivery_time),Toast.LENGTH_SHORT);
            }else if (password.isEmpty() || !password.equals(passwordConfirm)){
                createToast(getContext(),getString(R.string.insert_password_does_not_match),Toast.LENGTH_SHORT);
            }else if (region.isEmpty()){
                createToast(getContext(),getString(R.string.choose_region),Toast.LENGTH_SHORT);
            }else if (cost.isEmpty()){
                createToast(getContext(),getString(R.string.insert_cost),Toast.LENGTH_SHORT);
            }else if (minOrder.isEmpty()){
                createToast(getContext(),getString(R.string.insert_min_order),Toast.LENGTH_SHORT);
            }else {
                registerSendFragment.restaurantName =  restaurantName ;
                registerSendFragment.email = email;
                registerSendFragment.deliveryTime = deliveryTime ;
                registerSendFragment.cost = cost;
                registerSendFragment.minOrder = minOrder;
                registerSendFragment.password = password;
                registerSendFragment.passwordConfirm = passwordConfirm;
                registerSendFragment.regionId = region;

                ReplaceFragment(getFragmentManager(), registerSendFragment
                        , R.id.login_activity_fl_login
                        , true);
            }
        }catch (Exception e){

        }
        //validation

    }
}
