package com.jimo.mycost.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jimo.mycost.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.activity_data_statistic)
public class DataStatisticActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    /**
     * 返回
     *
     * @param view
     */
    @Event(R.id.ib_back)
    private void back(View view) {
        this.finish();
    }
}
