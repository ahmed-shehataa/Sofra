package com.ashehata.sofra.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.ashehata.sofra.R;
import com.ashehata.sofra.ui.fragment.restaurant.MoreFragment;
import com.ashehata.sofra.ui.fragment.restaurant.CategoriesFragment;
import com.ashehata.sofra.ui.fragment.restaurant.OrderFragment;
import com.ashehata.sofra.ui.fragment.restaurant.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;
import static com.ashehata.sofra.helper.HelperMethod.onPermission;

public class HomeActivity extends BaseActivity {

    MoreFragment moreFragment ;
    CategoriesFragment categoriesFragment ;
    ProfileFragment profileFragment;
    OrderFragment orderFragment;

    BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            switch (itemId) {
                case R.id.navigation_home:
                    ReplaceFragment(getSupportFragmentManager(),categoriesFragment
                    ,R.id.home_activity_fl_display,false);

                    break;

                case R.id.navigation_clipboard:
                    ReplaceFragment(getSupportFragmentManager(),orderFragment
                            ,R.id.home_activity_fl_display,false);

                    break;

                case R.id.navigation_user:
                    ReplaceFragment(getSupportFragmentManager(),profileFragment
                            ,R.id.home_activity_fl_display,false);

                    break;

                case R.id.navigation_dots:
                    ReplaceFragment(getSupportFragmentManager(),moreFragment
                        ,R.id.home_activity_fl_display,false);
                    break;
            }
            return true;
        }
    };
    @BindView(R.id.home_activity_fl_notification)
    FrameLayout homeActivityFlNotification;
    @BindView(R.id.home_activity_fl_calc)
    FrameLayout homeActivityFlCalc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setHomeFragments();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_home);

        //grant permission
        onPermission(this);

    }

    private void setHomeFragments() {
        moreFragment = new MoreFragment();
        categoriesFragment = new CategoriesFragment();
        profileFragment = new ProfileFragment();
        orderFragment =new OrderFragment();
    }

    @OnClick({R.id.home_activity_fl_notification, R.id.home_activity_fl_calc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_activity_fl_notification:
                break;
            case R.id.home_activity_fl_calc:
                break;
        }
    }


}
