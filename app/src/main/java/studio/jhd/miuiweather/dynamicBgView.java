package studio.jhd.miuiweather;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by jiahaodong on 2016/10/15-0:18.
 * 935410469@qq.com
 * https://github.com/jhd147350
 * 动态的背景(山和风车)，可以随重力左右滚动一小段距离
 */

public class dynamicBgView extends BaseView implements SensorEventListener {

    private Context context;

    private Paint mPaint = new Paint();

    //动画是否正在运行
    private boolean isRunning = false;
    //10张绘制所需的图片
    private Bitmap bitmaps[] = new Bitmap[10];
    //10张图的id
    private int IDs[] = {R.drawable.bg_cloudy_left,
            R.drawable.bg_cloudy_right,
            R.drawable.bg_cloudy_windmill_first_rod,//风车只绘制了一个，其他风车只是位置不同，转速不同
            R.drawable.bg_cloudy_windmill_first_head,
            R.drawable.bg_cloudy_windmill_second_rod,
            R.drawable.bg_cloudy_windmill_second_head,
            R.drawable.bg_cloudy_windmill_third_rod,
            R.drawable.bg_cloudy_windmill_third_head,
            R.drawable.bg_cloudy_windmill_fourth_rod,
            R.drawable.bg_cloudy_windmill_fourth_head,
    };
    //每张图的宽和高
    private int widths[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private int heights[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    //每张图的绘制位置
    private float positionXs[] = new float[10];
    private float positionYs[] = new float[10];
    //自定义view的宽和高
    private int viewWidth = 0;
    private int viewHeight = 0;

    //风车的旋转角度
    private float rotateD = 0;

    //随重力变换时，背景左右上下的平移量
    private float translateX = 0;
    private float translateY = 0;
    //最大平移距离，最终的平移距离 按最大平移距离的百分比计算
    private float translateDistance = 0;

    private SensorManager sensorManager;
    private Sensor gyroSensor;

    //属性动画
    private ValueAnimator animator;

    public dynamicBgView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public dynamicBgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public dynamicBgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //需要初始化的参数
    private void init() {

        for (int i = 0; i < 10; i++) {//初始化 云 的图片资源，并获取宽高
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), IDs[i]);
            widths[i] = bitmaps[i].getWidth();
            heights[i] = bitmaps[i].getHeight();
        }
        mPaint = new Paint();

        //解决编辑器下不能预览问题
        if (isInEditMode()) { return; }

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
        translateDistance = widths[0] - viewWidth;
        calculateXYs();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //左边的图和右边的图 上下移动的距离是相反的
        canvas.translate(translateX, -translateY + translateDistance / 2);
        canvas.drawBitmap(bitmaps[0], positionXs[0], positionYs[0], mPaint);
        canvas.restore();
        canvas.save();
        canvas.translate(translateX, translateY + translateDistance / 2);

        for (int i = 1; i < 4; i++) {

            if (i != 3) {
                canvas.drawBitmap(bitmaps[i], positionXs[i], positionYs[i], mPaint);
            } else {
                canvas.save();
                canvas.translate(positionXs[i] + widths[i] / 2, positionYs[i] + heights[i] / 2);
                canvas.rotate(rotateD);
                //bitmaps[i].
                canvas.drawBitmap(bitmaps[i], -widths[i] / 2, -heights[i] / 2, mPaint);
                canvas.restore();
            }
        }
        canvas.restore();

        if (!isRunning) {
            //设置每一个动画的持续时间都不同
            startAni(3000);
        }

    }

    //计算每个bitmap的位置
    private void calculateXYs() {
        //前2张是山，需要居中显示
        positionXs[0] = -(widths[0] - viewWidth) / 2;
        positionYs[0] = viewWidth - heights[0] - dp2px(100/3);
        positionXs[1] = -(widths[1] - viewWidth) / 2;
        positionYs[1] = viewHeight - heights[1];

        //后面都是风车，位置自己决定 只写了一个风车的位置
        positionXs[2] = widths[3] / 2 - dp2px(13/3) + viewWidth - dp2px(400/3);
        positionYs[2] = widths[3] / 2 + dp2px(6/3) + viewHeight - dp2px(700/3);
        positionXs[3] = viewWidth - dp2px(400/3);
        positionYs[3] = viewHeight - dp2px(700/3);
    }

    //开始动画
    private void startAni(int duration) {

        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("name", 0f, 360f);

        animator = ValueAnimator.ofPropertyValuesHolder(holder);
        animator.setDuration(duration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateD = (float) animation.getAnimatedValue("name");

                invalidate();
            }
        });
        animator.start();

        isRunning = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            //int de = (int) event.values[0];
            float y = event.values[1];
            float x = event.values[2];

            //x的范围从90到-90
            translateX = -x / 90 * translateDistance;

            //y的范围从180到-180
            translateY = -y / 180 * translateDistance / 2;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //暂停动画和传感器
    public void stopRefresh() {
        //需在activity 的 onPause中调用
        if (animator != null) {
            animator.cancel();
        }
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }

    }

    //再次开启动画和传感器
    public void reStartRefresh() {
        //需在activity 的 onResume中调用
        if (animator != null) {
            animator.start();
        }
        if (sensorManager != null) {
            sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }



}
