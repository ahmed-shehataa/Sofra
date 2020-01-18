package com.ashehata.sofra.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.TYPE_CLIENT;
import static com.ashehata.sofra.data.local.shared.SharedPreferencesManger.TYPE_RESTAURANT;
import static com.ashehata.sofra.helper.HelperMethod.changeLang;

public class SplashActivity extends BaseActivity {

    private static final long SPLASH_DISPLAY_LENGTH = 2000;

    @BindView(R.id.splash_activity_iv_logo)
    ImageView splashActivityIvLogo;
    @BindView(R.id.splash_activity_btn_order)
    Button splashActivityBtnOrder;
    @BindView(R.id.splash_activity_btn_sale)
    Button splashActivityBtnSale;
    private boolean b;
    public String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //change language to arabic
        changeLang(this,"ar");
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        //this is for hiding status bar
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set animation for logo
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        splashActivityIvLogo.setAnimation(bounce);

        //hide buttons
        splashActivityBtnOrder.setVisibility(View.INVISIBLE);
        splashActivityBtnSale.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //show buttons
                splashActivityBtnOrder.setVisibility(View.VISIBLE);
                splashActivityBtnSale.setVisibility(View.VISIBLE);
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @OnClick({R.id.splash_activity_btn_order, R.id.splash_activity_btn_sale})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_activity_btn_order:
                clientCycle();
                break;
            case R.id.splash_activity_btn_sale:
                restaurantCycle();
                break;
        }
    }

    private void clientCycle() {
        SharedPreferencesManger.SaveUserType(this,TYPE_CLIENT);
        userType = TYPE_CLIENT ;
        startNewActivity(HomeActivity.class);
    }

    private void restaurantCycle() {
        SharedPreferencesManger.SaveUserType(this,TYPE_RESTAURANT);
        userType = TYPE_RESTAURANT ;
        b = SharedPreferencesManger.LoadBoolean(this,SharedPreferencesManger.REMEMBER_RESTAURANT);
       if(b){
           startNewActivity(HomeActivity.class);

       }else {
          startNewActivity(UserActivity.class);
       }
    }
    private void startNewActivity(Class<?> activity){
        startActivity(new Intent(this, activity));
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
