package com.ashehata.sofra.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.ashehata.sofra.ui.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {

    public BaseFragment baseFragment;

    public void superBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }
}
