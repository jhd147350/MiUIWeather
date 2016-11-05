package studio.jhd.miuiweather;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by jiahaodong on 2016/11/5-18:31.
 * 935410469@qq.com
 * https://github.com/jhd147350
 */

public class BaseView extends View {
    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //dp转像素
    public float dp2px(float dpValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    //sp转像素
    public float sp2px(float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }
}
