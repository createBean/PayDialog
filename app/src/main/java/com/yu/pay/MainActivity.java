package com.yu.pay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements PayPasswordView.InputCallBack {
    private PayFragment mPayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_wechat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(PayFragment.EXTRA_CONTENT, "" + 100.00);
                mPayFragment = new PayFragment();
                mPayFragment.setArguments(bundle);
                mPayFragment.setPaySucessCallBack(MainActivity.this);
                mPayFragment.show(getSupportFragmentManager(), "pay");
            }
        });
    }

    @Override
    public void onInputFinsh(String result) {
        mPayFragment.dismiss();
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }
}
