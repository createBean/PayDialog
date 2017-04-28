package com.yu.pay;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by yu on 2017/4/28.
 */

public class PayFragment extends DialogFragment implements View.OnClickListener {
    public static final String EXTRA_CONTENT = "extra_content";    //提示框内容
    private PayPasswordView mPasswordView;
    private PayPasswordView.InputCallBack mInputCallBack;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_pay);
        dialog.setCanceledOnTouchOutside(false);

        final Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.AinmBottom);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.TOP;
        window.setAttributes(lp);

        initView(dialog);
        return dialog;
    }

    private void initView(Dialog dialog) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            TextView tvContent = (TextView) dialog.findViewById(R.id.tv_content);
            tvContent.setText("￥"+bundle.getString(EXTRA_CONTENT));
        }
        mPasswordView = (PayPasswordView) dialog.findViewById(R.id.payPwdView);
        PayKeyboardView payKeyboardView = (PayKeyboardView) dialog.findViewById(R.id.inputMethodView);
        mPasswordView.setInputMethodView(payKeyboardView);
        mPasswordView.setInputCallBack(mInputCallBack);
        dialog.findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
        }
    }

    /**
     * 设置输入回调
     */
    public void setPaySucessCallBack(PayPasswordView.InputCallBack inputCallBack) {
        this.mInputCallBack = inputCallBack;
    }
}
