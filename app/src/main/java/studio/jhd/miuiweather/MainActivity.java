package studio.jhd.miuiweather;

import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private dynamicBgView bg;
    private ImageView imageView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bg= (dynamicBgView) findViewById(R.id.bg);
        imageView = (ImageView) findViewById(R.id.image01);
        //开启帧动画
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();
        animationDrawable.start();

        // TODO: 2016/11/14
        frameLayout = (FrameLayout) findViewById(R.id.my_frame);
        frameLayout.addView(new dynamicRainView(this));
        Log.d(TAG, "main: ");
        android.util.Log.d(TAG, "onCreate: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("Pause");
        //关掉动画和注销传感器监听，节省电量
        bg.stopRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("Resume");
        bg.reStartRefresh();
    }
}
