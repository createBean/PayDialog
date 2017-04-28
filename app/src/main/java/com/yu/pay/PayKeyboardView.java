package com.yu.pay;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by yu on 2017/4/28.
 */

public class PayKeyboardView extends LinearLayout implements View.OnClickListener {
    private KeyboardReceiver mKeyboardReceiver;

    public PayKeyboardView(Context context) {
        super(context);
        initView();
    }

    public PayKeyboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_keyboard_pay, this);
        initView();
    }

    public PayKeyboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_1).setOnClickListener(this);
        findViewById(R.id.btn_2).setOnClickListener(this);
        findViewById(R.id.btn_3).setOnClickListener(this);
        findViewById(R.id.btn_4).setOnClickListener(this);
        findViewById(R.id.btn_5).setOnClickListener(this);
        findViewById(R.id.btn_6).setOnClickListener(this);
        findViewById(R.id.btn_7).setOnClickListener(this);
        findViewById(R.id.btn_8).setOnClickListener(this);
        findViewById(R.id.btn_9).setOnClickListener(this);
        findViewById(R.id.btn_0).setOnClickListener(this);
        findViewById(R.id.btn_del).setOnClickListener(this);
        findViewById(R.id.btn_space).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.layout_hide).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibility(GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        String nmuber = (String) v.getTag();
        this.mKeyboardReceiver.onReveive(nmuber);
    }

    public void setKeyboardReceiver(KeyboardReceiver keyboardReceiver){
        this.mKeyboardReceiver = keyboardReceiver;
    }

    public interface KeyboardReceiver {
        void onReveive(String number);
    }
}
