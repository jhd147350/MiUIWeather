package studio.jhd.miuiweather;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by jiahaodong on 2016/10/11-0:20.
 * 935410469@qq.com
 * https://github.com/jhd147350
 * 仿miui8天气
 */

public class dynamicSkyView extends View {
    Bitmap bitmap;
    Context mContext;
    private Paint mPaint;
    private ValueAnimator translateSkyAni;
    private float canvasTranslateX=0;
    public dynamicSkyView(Context context) {
        super(context);
        init(context);
    }

    public dynamicSkyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public dynamicSkyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context){
        mContext=context;
        bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.bg_cloudy_first_cloud);
        mPaint=new Paint();
        startAni();
    }

    private void startAni() {
        final String nameCanvasTranslateX="canvasTranslateX";
        PropertyValuesHolder holderCanvasTranslateX=PropertyValuesHolder.ofFloat(nameCanvasTranslateX,canvasTranslateX,500);
        translateSkyAni=ValueAnimator.ofPropertyValuesHolder(holderCanvasTranslateX);
        translateSkyAni.setDuration(1000);
        translateSkyAni.setInterpolator(new LinearInterpolator());
        translateSkyAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                canvasTranslateX= (float) animation.getAnimatedValue(nameCanvasTranslateX);
                System.out.println(":"+canvasTranslateX);
                invalidate();
            }
        });
        System.out.println("111111111111111111111");
        translateSkyAni.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,canvasTranslateX,0,mPaint);
    }

}
