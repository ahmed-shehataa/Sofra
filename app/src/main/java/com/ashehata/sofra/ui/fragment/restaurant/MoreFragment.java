package com.ashehata.sofra.ui.fragment.restaurant;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.local.room.AppDatabase;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.ui.activity.SplashActivity;
import com.ashehata.sofra.ui.fragment.BaseFragment;

import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.TYPE_CLIENT;
import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.TYPE_RESTAURANT;
import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;

public class MoreFragment extends BaseFragment {


    @BindView(R.id.more_fragment_tv_change_password)
    TextView moreFragmentTvChangePassword;
    @BindView(R.id.more_fragment_tv_sign_out)
    TextView moreFragmentTvSignOut;
    @BindView(R.id.more_fragment_tv_offers)
    TextView moreFragmentTvOffers;
    @BindView(R.id.more_fragment_tv_comments)
    TextView moreFragmentTvComments;
    @BindView(R.id.more_fragment_v_comments_line)
    View moreFragmentVCommentsLine;

    private String userType;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_more_restaurant, container, false);
        ButterKnife.bind(this, view);
        userType = SharedPreferencesManger.LoadUserType(getActivity());

        setUserClientConfig();


        return view;
    }

    private void setUserClientConfig() {
        if (userType.equals(TYPE_CLIENT)) {
            moreFragmentTvComments.setVisibility(View.GONE);
            moreFragmentVCommentsLine.setVisibility(View.GONE);
            moreFragmentTvOffers.setText(R.string.the_offers);
        }
    }

    @OnClick({R.id.more_fragment_tv_change_password, R.id.more_fragment_tv_sign_out, R.id.more_fragment_tv_offers})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_fragment_tv_change_password:
                ReplaceFragment(getFragmentManager(), new ChangePasswordFragment()
                        , R.id.home_activity_fl_display, true);
                break;
            case R.id.more_fragment_tv_sign_out:
                signOut();
                break;
            case R.id.more_fragment_tv_offers:
                ReplaceFragment(getFragmentManager(), new OfferFragment()
                        , R.id.home_activity_fl_display, true);
                break;
        }
    }

    private void signOut() {
        if ( userType.equals(TYPE_CLIENT)){
            //clear shared preference
            SharedPreferencesManger.saveClientData(getActivity(),null);
            SharedPreferencesManger.SaveData(getActivity(), SharedPreferencesManger.REMEMBER_CLIENT
                    , false);
            //clear room Db
            clearRoomDB();

        }else if(userType.equals(TYPE_RESTAURANT)) {
            //clear shared preference
            SharedPreferencesManger.saveRestaurantData(getActivity(),null);
            SharedPreferencesManger.SaveData(getActivity(), SharedPreferencesManger.REMEMBER_RESTAURANT
                    , false);

        }
        //start splash activity
        startActivity(new Intent(getContext(), SplashActivity.class));
        getActivity().finish();
    }

    private void clearRoomDB() {
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase.getInstance(getContext()).userDao().deleteAllFoodItem();
            }
        });
    }
}