package studio.jhd.miuiweather;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private dynamicBgView bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bg= (dynamicBgView) findViewById(R.id.bg);

        //SensorManager sensorManager=getSystemService();
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
