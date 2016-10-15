package studio.jhd.miuiweather;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by jiahaodong on 2016/10/11-0:20.
 * 935410469@qq.com
 * https://github.com/jhd147350
 * 仿miui8天气 循环飘动的云
 */

public class dynamicSkyView extends View {
   // Context mContext;
    private Bitmap bitmap_clouds[] = new Bitmap[4];
   // private Bitmap bitmap_cloud;
    private int cloudsID[] = {R.drawable.bg_cloudy_first_cloud,
            R.drawable.bg_cloudy_second_cloud,
            R.drawable.bg_cloudy_third_cloud,
            R.drawable.bg_cloudy_fourth_cloud};
    private int cloud_widths[] = {0, 0, 0, 0};
    private int cloud_heights[] = {0, 0, 0, 0};
    private float canvasTranslateXs[] = {0, 0, 0, 0};
    private ValueAnimator animators[] = new ValueAnimator[4];
    private PropertyValuesHolder holders[] = new PropertyValuesHolder[4];
    private String names[] = {"name1", "name2", "name3", "name4"};
    private Paint mPaint;
    //private ValueAnimator translateSkyAni;

    private int viewWidth = 0;
    private int viewHeight = 0;
    private boolean isRunning = false;


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

    private void init(Context context) {
       // mContext = context;

        for (int i = 0; i < 4; i++) {//初始化 云 的图片资源，并获取宽高
            bitmap_clouds[i] = BitmapFactory.decodeResource(getResources(), cloudsID[i]);
            cloud_widths[i] = bitmap_clouds[i].getWidth();
            cloud_heights[i] = bitmap_clouds[i].getHeight();
            System.out.println(":" + cloud_widths[i]);
        }
       // bitmap_cloud = BitmapFactory.decodeResource(getResources(), cloudsID[0]);

        mPaint = new Paint();
    }

    //开始动画
    private void startAni(int duration[]) {
        for (int i = 0; i < 4; i++) {
            holders[i] = PropertyValuesHolder.ofFloat(names[i], -cloud_widths[i], viewWidth);

            animators[i] = ValueAnimator.ofPropertyValuesHolder(holders[i]);
            animators[i].setDuration(duration[i]);
            animators[i].setRepeatCount(ValueAnimator.INFINITE);
            animators[i].setRepeatMode(ValueAnimator.RESTART);
            animators[i].setInterpolator(new LinearInterpolator());
            final int finalI = i;
            animators[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    canvasTranslateXs[finalI] = (float) animation.getAnimatedValue(names[finalI]);
                    if (finalI == 3) {
                        invalidate();
                    }
                }
            });
            animators[i].reverse();
        }

        isRunning = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap_clouds[0], canvasTranslateXs[0], viewHeight - cloud_heights[0] - 300, mPaint);
        canvas.drawBitmap(bitmap_clouds[1], canvasTranslateXs[1], viewHeight - cloud_heights[1] - 550, mPaint);
        canvas.drawBitmap(bitmap_clouds[2], canvasTranslateXs[2], viewHeight - cloud_heights[2] - 700, mPaint);
        canvas.drawBitmap(bitmap_clouds[3], canvasTranslateXs[3], viewHeight - cloud_heights[3] - 800, mPaint);
        // canvas.drawColor(0xffff0000);
        //因为屏幕宽度 只有在测量之后才能正确拿到，所以将动画启动放在这里
        if (!isRunning) {
            //设置每一个动画的持续时间都不同
            int durations[] = {15000, 25000, 20000, 30000};
            startAni(durations);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);

    }
}
