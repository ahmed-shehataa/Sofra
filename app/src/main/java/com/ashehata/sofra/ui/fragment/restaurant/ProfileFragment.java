package com.ashehata.sofra.ui.fragment.restaurant;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.api.GetDataService;
import com.ashehata.sofra.data.api.RetrofitClient;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.data.model.general.regions.RegionData;
import com.ashehata.sofra.data.model.general.regions.Regions;
import com.ashehata.sofra.data.model.reataurant.restaurantCycle.Profile.User;
import com.ashehata.sofra.helper.InternetState;
import com.ashehata.sofra.ui.activity.UserActivity;
import com.ashehata.sofra.ui.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.REMEMBER_CLIENT;
import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.TYPE_CLIENT;
import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.TYPE_RESTAURANT;
import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.createToast;
import static com.ashehata.sofra.helper.HelperMethod.disappearKeypad;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUri;
import static com.ashehata.sofra.helper.HelperMethod.onLoadImageFromUrl;
import static com.ashehata.sofra.helper.HelperMethod.openAlbum;
import static com.ashehata.sofra.helper.HelperMethod.setSpinner;
import static java.lang.Integer.parseInt;

public class ProfileFragment extends BaseFragment {


    @BindView(R.id.profile_fragment_iv_logo)
    CircleImageView profileFragmentIvLogo;
    @BindView(R.id.profile_fragment_et_restaurant_name)
    EditText profileFragmentEtRestaurantName;
    @BindView(R.id.profile_fragment_et_restaurant_email)
    EditText profileFragmentEtRestaurantEmail;
    @BindView(R.id.profile_fragment_sp_city)
    Spinner profileFragmentSpCity;
    @BindView(R.id.profile_fragment_sp_region)
    Spinner profileFragmentSpRegion;
    @BindView(R.id.profile_fragment_et_delivery_time)
    EditText profileFragmentEtMinCharger;
    @BindView(R.id.profile_fragment_btn_go_on)
    Button profileFragmentBtnGoOn;
    @BindView(R.id.profile_fragment_ll_parent)
    LinearLayout profileFragmentLlParent;
    @BindView(R.id.profile_fragment_et_number)
    EditText profileFragmentEtNumber;
    private User user;
    private List<String> cityNames;
    private List<String> regionNames;
    private String imagePath = "";
    private GetDataService getDataService;
    private List<String> citiesList;
    private List<Integer> citiesIds;
    private List<String> regionList;
    private List<Integer> regionIds;
    private String restaurantName;
    private String restaurantEmail;
    private int regionId;
    private String minOrder;
    private String userType ;
    private boolean loginBefore;
    private Integer mRegion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        getDataService = RetrofitClient.getClient().create(GetDataService.class);
        profileFragmentSpRegion.setVisibility(View.GONE);
        userType = SharedPreferencesManger.LoadUserType(getActivity());
        //Log.v("user_type", userType);
        setLists();
        getCities();
        if ( userType.equals(TYPE_CLIENT)){
            displayClientData();
        }else if(userType.equals(TYPE_RESTAURANT)) {
            displayRestaurantData();
        }
        return view;
    }

    private void setLists() {
        cityNames = new ArrayList<>();
        regionNames = new ArrayList<>();
        citiesList = new ArrayList<>();
        citiesIds = new ArrayList<>();
        regionIds = new ArrayList<>();
        regionList = new ArrayList<>();
    }

    private void displayClientData() {

        loginBefore = false;
        loginBefore = SharedPreferencesManger.LoadBoolean(getActivity(),REMEMBER_CLIENT);
        if(loginBefore == true){
            user = SharedPreferencesManger.loadClientData(getActivity());
            onLoadImageFromUrl(profileFragmentIvLogo, user.getPhotoUrl(), getContext());
            profileFragmentEtRestaurantName.setText(user.getName());
            profileFragmentEtRestaurantEmail.setText(user.getEmail());
            profileFragmentEtMinCharger.setVisibility(View.GONE);
            profileFragmentEtNumber.setVisibility(View.VISIBLE);
            profileFragmentEtNumber.setText(user.getPhone());
            profileFragmentBtnGoOn.setText(getString(R.string.update));
        }else {
            loginFragment();
            profileFragmentLlParent.setVisibility(View.GONE);
        }
    }
    private void loginFragment() {
        getActivity().startActivity(new Intent(getActivity(), UserActivity.class).setType(TYPE_CLIENT));
    }

    private void displayRestaurantData() {
        user = SharedPreferencesManger.loadRestaurantData(getActivity());
        onLoadImageFromUrl(profileFragmentIvLogo, user.getPhotoUrl(), getContext());
        profileFragmentEtRestaurantName.setText(user.getName());
        profileFragmentEtRestaurantEmail.setText(user.getEmail());
        profileFragmentEtMinCharger.setText(user.getMinimumCharger());

    }

    @OnClick({R.id.profile_fragment_btn_go_on, R.id.profile_fragment_ll_parent, R.id.profile_fragment_iv_logo})
    public void onViewClicked(View view) {
        disappearKeypad(getActivity(), getView());
        switch (view.getId()) {
            case R.id.profile_fragment_btn_go_on:
                goOnSend();
                break;

            case R.id.profile_fragment_ll_parent:
                disappearKeypad(getActivity(), getView());
                break;

            case R.id.profile_fragment_iv_logo:
                editLogo();
                break;
        }
    }

    private void goOnSend() {
        restaurantName = profileFragmentEtRestaurantName.getText().toString().trim();
        restaurantEmail = profileFragmentEtRestaurantEmail.getText().toString().trim();
        if (regionList.size() != 0) {
            regionId = regionIds.get(profileFragmentSpRegion.getSelectedItemPosition());

        }
        //Toast.makeText(getActivity(), regionId+"", Toast.LENGTH_SHORT).show();
        minOrder = profileFragmentEtMinCharger.getText().toString().trim();

        if (restaurantName.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_restaurant_name), Toast.LENGTH_SHORT);
        } else if (restaurantEmail.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_email), Toast.LENGTH_SHORT);
        } else if (regionId == 0) {
            createToast(getContext(), getString(R.string.insert_region), Toast.LENGTH_SHORT);

        } else if (minOrder.isEmpty()) {
            createToast(getContext(), getString(R.string.insert_min_order), Toast.LENGTH_SHORT);
        } else {
            if (InternetState.isConnected(getContext())) {
                ProfileSendFragment profileSendFragment = new ProfileSendFragment();
                profileSendFragment.restaurantName = restaurantName;
                profileSendFragment.restaurantEmail = restaurantEmail;
                profileSendFragment.regionId = regionId;
                profileSendFragment.minOrder = minOrder;
                profileSendFragment.imagePath = imagePath;
                ReplaceFragment(getFragmentManager(), profileSendFragment
                        , R.id.home_activity_fl_display
                        , true);


            } else {
                createToast(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT);

            }
        }
    }

    private void editLogo() {
        openAlbum(1, getContext(), new ArrayList<>(), new Action<ArrayList<AlbumFile>>() {
            @Override
            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                imagePath = result.get(0).getPath();
                onLoadImageFromUri(profileFragmentIvLogo, imagePath, getContext());
            }
        });
    }

    private void getCities() {
        if (InternetState.isConnected(getContext())) {
            getDataService.getCities().enqueue(new Callback<Regions>() {
                @Override
                public void onResponse(Call<Regions> call, Response<Regions> response) {
                    try {
                        Regions regions = response.body();
                        if (regions.getStatus() == 1) {

                            citiesList.add(getString(R.string.city));
                            citiesIds.add(0);
                            List<RegionData> citiesData = regions.getData().getData();
                            for (int i = 0; i < citiesData.size(); i++) {
                                citiesList.add(citiesData.get(i).getName());
                                citiesIds.add(citiesData.get(i).getId());
                            }
                            setSpinner(getActivity(), profileFragmentSpCity, citiesList);
                            profileFragmentSpCity.setSelection(citiesIds.get(parseInt(user.getRegion().getCityId())));
                            setRegion();

                        } else {

                        }
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onFailure(Call<Regions> call, Throwable t) {

                }
            });
        } else {

        }
    }

    private void setRegion() {
        profileFragmentSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                int id = citiesIds.get(i);
                regionList.clear();
                getRegion(id);
                profileFragmentSpRegion.setVisibility(View.VISIBLE);
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
                    regionIds.add(0);

                    List<RegionData> citiesData = region.getData().getData();
                    for (int i = 0; i < citiesData.size(); i++) {
                        regionList.add(citiesData.get(i).getName());
                        regionIds.add(citiesData.get(i).getId());
                    }

                    setSpinner(getActivity(), profileFragmentSpRegion, regionList);
                    for(int i =0 ; i< regionIds.size()  ;i++){

                        if (regionIds.get(i) == parseInt(user.getRegionId())){
                            //code here
                            mRegion = i;
                            profileFragmentSpRegion.setSelection(mRegion);
                            break;
                        }
                    }

                }
            }
            @Override
            public void onFailure(Call<Regions> call, Throwable t) {

            }
        });
    }
}