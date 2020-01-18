package com.ashehata.sofra.ui.fragment.client;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ashehata.sofra.R;
import com.ashehata.sofra.data.local.shared.SharedPreferencesManger;
import com.ashehata.sofra.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailsFragment extends BaseFragment {


    @BindView(R.id.order_details_fragment_et_notes)
    EditText orderDetailsFragmentEtNotes;
    @BindView(R.id.order_details_fragment_tv_address)
    TextView orderDetailsFragmentTvAddress;
    @BindView(R.id.order_details_fragment_rb_cash)
    RadioButton orderDetailsFragmentRbCash;
    @BindView(R.id.order_details_fragment_rb_online)
    RadioButton orderDetailsFragmentRbOnline;
    @BindView(R.id.order_details_fragment_rg_pay_method)
    RadioGroup orderDetailsFragmentRgPayMethod;
    @BindView(R.id.order_details_fragment_tv_total)
    TextView orderDetailsFragmentTvTotal;
    @BindView(R.id.order_details_fragment_tv_deliver_cost)
    TextView orderDetailsFragmentTvDeliverCost;
    @BindView(R.id.order_details_fragment_tv_total_price)
    TextView orderDetailsFragmentTvTotalPrice;
    @BindView(R.id.order_details_fragment_btn_confirm)
    Button orderDetailsFragmentBtnConfirm;

    public double total ;
    public int deliverCost ;
    public int totalPrice ;
    private String address;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        ButterKnife.bind(this,view);

        //set data
        displayTotalPrice();


        return view;
    }

    private void displayTotalPrice() {
        address = SharedPreferencesManger.loadClientData(getActivity()).getRegion().getCity().getName();
        orderDetailsFragmentTvAddress.setText(address);
        orderDetailsFragmentTvTotal.setText(total+"");

    }

    @OnClick(R.id.order_details_fragment_btn_confirm)
    public void onViewClicked() {
    }
}