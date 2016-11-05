package studio.jhd.miuiweather;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jiahaodong on 2016/10/18-18:52.
 * 935410469@qq.com
 * https://github.com/jhd147350
 */

public class dynamicRainView extends BaseView implements SensorEventListener {
    //自定义view的宽和高
    private int viewWidth;
    private int viewHeight;

    private Context context;

    private Paint mPaint = new Paint();

    private List<RainDrop> rainDropList = new ArrayList<>();

    //雨滴随重力倾斜角度
    private float rotateDegree = 0;


    //雨滴应该是视觉上越远的，看起来越小，越半透明
    //随机产生长短、粗细、透明度不同的雨滴，表示远近 近的雨滴下落速度也快

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rainDropList.add(new RainDrop());
            Iterator<RainDrop> iterator = rainDropList.iterator();
            while (iterator.hasNext()) {//如果动画播放完成，就移除该对象
                RainDrop rainDrop = iterator.next();
                if (rainDrop.isFinished)
                    iterator.remove();   //注意这个地方
            }

            // TODO 以后研究吧，先照着百度解决就ok了
            /*
            这种执行会抛异常java.util.ConcurrentModificationException
            for (RainDrop temp : rainDropList) {//如果动画播放完成，就移除该对象
                if (temp.isFinished) {
                    rainDropList.remove(temp);
                }
            }*/
        }
    };


    public dynamicRainView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public dynamicRainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public dynamicRainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(0xff000000);
        mPaint.setAntiAlias(true);

        //解决编辑器下不能预览问题
        if (isInEditMode()) {
            return;
        }
        //传感器相关
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private void needInitOnMeasure() {
        //初始化所有雨滴,里面有用到view的高宽做参数，只能在拿到高宽之后才能初始化
      /*  for (int i = 0; i < numOfRaindrop; i++) {
            drops[i] = new RainDrop();
        }
*/
        createRainDrops();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
/*
        for (int i = 0; i < numOfRaindrop; i++) {
            //drops[i]=new RainDrop();

        }*/
        canvas.translate(viewWidth / 2, 0);
        canvas.rotate(rotateDegree);
        for (RainDrop temp : rainDropList) {
            mPaint.setStrokeWidth(temp.thickness);
            canvas.drawLine(temp.x, temp.y, temp.x, temp.y + temp.length, mPaint);
        }
        //保证60帧
        postInvalidate();
    }

    private void createRainDrops() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                long sleepRandomTime;
                while (true) {
                    sleepRandomTime = (long) (200 * Math.random());
                    try {
                        sleep(sleepRandomTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //子线程不能运行animator，要在handler中处理
                    handler.sendEmptyMessage(0);//去主线程操作
                }
            }
        };
        thread.start();
        //rainDropList.add(new RainDrop());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
        needInitOnMeasure();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == null) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

            //int de = (int) event.values[0];
            //float y = event.values[1];
            float x = event.values[2];

            //x的范围从90到-90
            rotateDegree = x / 90 * 40;

            //y的范围从180到-180
            //translateY = -y / 180 * 40;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //雨滴类，需要随机产生
    private class RainDrop {
        //位置
        float x, y;
        float initx, inity;
        float length;
        float thickness;
        private boolean isFinished = false;

        //
        RainDrop() {
            createRandomData();
            startAnim();
        }

        private void startAnim() {
            ValueAnimator animator;
            animator = ValueAnimator.ofFloat(0.0f, viewHeight);
            animator.setDuration(1500);
            //float temp=0;
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float temp = (float) animation.getAnimatedValue();
                    y = inity + temp;
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isFinished = true;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            //延迟200ms播放
            //animator.setStartDelay(200);
            animator.setInterpolator(null);
            animator.start();
        }

        private void createRandomData() {
            x = (float) Math.random() * viewWidth;
            x = x - viewWidth / 2;
            // y = (float) Math.random() * (viewHeight / 2);
            y = 0;
            initx = x;
            inity = y;
            float temp = (float) Math.random();
            //长度3-9
            length = temp * dp2px(6) + dp2px(3);
            //厚度0.5-1.25
            thickness = temp * dp2px(0.75f) + dp2px(0.5f);
        }

        @Override
        public String toString() {
            return "RainDrop{" +
                    "x=" + x +
                    ", y=" + y +
                    ", length=" + length +
                    ", thickness=" + thickness +
                    '}';
        }
    }
}
