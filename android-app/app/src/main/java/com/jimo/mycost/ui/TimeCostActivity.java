package com.jimo.mycost.ui;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import com.jimo.mycost.R;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_time_cost)
public class TimeCostActivity extends AppCompatActivity {

    @ViewInject(R.id.timer)
    private Chronometer timer;

    private boolean isStop;//控制开始结束

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }


    public void recordTime(View view) {
        Button btn = (Button) view;
        if (isStop) {
            timer.stop();
            btn.setText("开始计时");
            isStop = false;
            saveTheTime(view);
        } else {
            timer.setBase(SystemClock.elapsedRealtime());
            timer.setFormat("%s");
            timer.start();
            isStop = true;
            btn.setText("停止计时");
        }
    }

    private void saveTheTime(View view) {
        String s = timer.getText().toString();
        JimoUtil.mySnackbar(view, s);
    }
}
