package com.ashehata.sofra.ui.activity;

import android.os.Bundle;
import android.widget.FrameLayout;
import com.ashehata.sofra.R;
import com.ashehata.sofra.ui.fragment.restaurant.LoginFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import static com.ashehata.sofra.helper.HelperMethod.ReplaceFragment;

public class UserActivity extends BaseActivity {

    @BindView(R.id.login_activity_fl_login)
    FrameLayout loginActivityFlLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // open login fragment in the activity
        ReplaceFragment(getSupportFragmentManager(), new LoginFragment(), R.id.login_activity_fl_login,false);


    }
}
