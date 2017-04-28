package com.yu.pay;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu on 2017/4/28.
 */

public class PayPasswordView extends View {
    private List<String> result;//保存输入数据
    private int count;//密码位数
    private int size;//默认每格大小
    private Paint mBorderPaint;//边界画笔
    private Paint mDotPaint;//小黑点画笔
    private int mBorderColor;//边界颜色
    private int mDotColor;//小黑点颜色
    private RectF mRoundRect;//圆角背景
    private int mRoundRadius;//圆角半径

    private PayKeyboardView mPayKeyboardView;//数字键盘
    private InputCallBack mInputCallBack;//完全输入的回调

    public interface InputCallBack {
        void onInputFinsh(String result);
    }

    public PayPasswordView(Context context) {
        super(context);
        init(null);
    }

    public PayPasswordView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PayPasswordView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        float dp = getResources().getDisplayMetrics().density;
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        result = new ArrayList<>();
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PayPasswordView);
            mBorderColor = typedArray.getColor(R.styleable.PayPasswordView_border_color, Color.LTGRAY);
            mDotColor = typedArray.getColor(R.styleable.PayPasswordView_dot_color, Color.BLACK);
            count = typedArray.getInt(R.styleable.PayPasswordView_count, 6);
            typedArray.recycle();
        } else {
            mBorderColor = Color.LTGRAY;
            mDotColor = Color.GRAY;
            count = 6;
        }
        size = (int) (dp * 30);//默认每格30dp
        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStrokeWidth(3);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(mBorderColor);

        mDotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDotPaint.setStrokeWidth(3);
        mDotPaint.setStyle(Paint.Style.FILL);
        mDotPaint.setColor(mDotColor);
        mRoundRect = new RectF();
        mRoundRadius = (int) (5 * dp);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = measureWidth(widthMeasureSpec);
        int h = measureHeight(heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (w == -1) {
            if (h != -1) {
                w = h * count;
            } else {
                w = size * count;
                h = size;
            }
        } else {
            if (h == -1) {
                h = w / count;
                size = h;
            }
        }
        setMeasuredDimension(Math.min(w, wSize), Math.min(h, hSize));
    }

    private int measureWidth(int withMeasureSpec) {
        //宽度
        int wMode = MeasureSpec.getMode(withMeasureSpec);
        int wSize = MeasureSpec.getSize(withMeasureSpec);
        if (wMode == MeasureSpec.AT_MOST) {//wrap_content
            return -1;
        }
        return wSize;
    }

    private int measureHeight(int heightMeasureSpec) {
        //高度
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        if (hMode == MeasureSpec.AT_MOST) {
            return -1;
        }
        return hSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            requestFocus();
            mPayKeyboardView.setVisibility(VISIBLE);
            return true;
        }
        return true;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            mPayKeyboardView.setVisibility(VISIBLE);
        } else {
            mPayKeyboardView.setVisibility(GONE);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int width = getWidth() - 1;
        final int height = getHeight() - 1;
        mRoundRect.set(0, 0, width, height);
        canvas.drawRoundRect(mRoundRect, 0, 0, mBorderPaint);
        //分割线
        for (int i = 1; i < count; i++) {
            final int x = i * size;
            canvas.drawLine(x, 0, x, height, mBorderPaint);
        }
        //小黑点
        int dotRadius = size / 8;
        for (int i = 0; i < result.size(); i++) {
            final float x = (float) (size * (i + 0.5));
            final float y = size / 2;
            canvas.drawCircle(x, y, dotRadius, mDotPaint);
        }
    }

    @Override
    public boolean onCheckIsTextEditor() {
        return true;
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;//输入类型为数字
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return new MInputConnection(this, false);
    }

    public void setInputCallBack(InputCallBack inputCallBack) {
        this.mInputCallBack = inputCallBack;
    }

    public void clearResult() {
        result.clear();
        invalidate();
    }

    private class MInputConnection extends BaseInputConnection {

        public MInputConnection(View targetView, boolean fullEditor) {
            super(targetView, fullEditor);
        }

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            //这里是接受输入法的文本的，我们只处理数字，所以什么操作都不做
            return super.commitText(text, newCursorPosition);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //软键盘删除键
            if (beforeLength == 1 && afterLength == 0) {
                return super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && super.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }

    public void setInputMethodView(PayKeyboardView inputMethodView) {
        this.mPayKeyboardView = inputMethodView;
        this.mPayKeyboardView.setKeyboardReceiver(new PayKeyboardView.KeyboardReceiver() {
            @Override
            public void onReveive(String number) {
                if (number.equals("-1")) {
                    if (!result.isEmpty()) {
                        result.remove(result.size() - 1);
                        invalidate();
                    }
                } else {
                    if (result.size() < count) {
                        result.add(number);
                        invalidate();
                        ensureFinishInput();
                    }
                }
            }
        });
    }

    /**
     * 判断是否输入完成
     */
    public void ensureFinishInput() {
        if (result.size() == count && mInputCallBack != null) {
            StringBuilder sb = new StringBuilder();
            for (String i : result) {
                sb.append(i);
            }
            mInputCallBack.onInputFinsh(sb.toString());
        }
    }

    /**
     * 获取输入文字
     */
    public String getInputText() {
        if (result.size() == count) {
            StringBuilder sb = new StringBuilder();
            for (String i : result) {
                sb.append(i);
            }
            return sb.toString();
        }
        return null;
    }
}
