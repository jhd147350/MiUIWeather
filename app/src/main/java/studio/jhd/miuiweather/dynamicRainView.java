package studio.jhd.miuiweather;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jiahaodong on 2016/10/18-18:52.
 * 935410469@qq.com
 * https://github.com/jhd147350
 */

public class dynamicRainView extends View {

    //自定义view的宽和高
    private int viewWidth;
    private int viewHeight;

    private Context context;

    private Paint mPaint = new Paint();

    //雨滴数量
    private int numOfRaindrop = 0;

    //雨滴应该是视觉上越远的，看起来越小，越半透明
    //随机产生长短、粗细、透明度不同的雨滴，表示远近 近的雨滴下落速度也快

    //雨滴粗细范围
    private float rangeOfRaindropThick[] = {0, 0};

    //雨滴长短范围
    private float rangeOfRaindropLength[] = {0, 0};

    //动画是否正在运行
    private boolean isRunning = false;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(viewWidth, viewHeight);
    }

    //雨滴类，需要随机产生
    private class RainDrop {
        //位置
        float x, y;
        //
        RainDrop(){

        }
        public void startAnim(){

        }
    }
}
