package com.jimo.mycost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.jimo.mycost.ui.AddCostActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.ib_add)
    ImageButton ib_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

    }

    /**
     * 点击跳转页面
     */
    @Event(R.id.ib_add)
    private void ibAddClick(View v) {
        Intent intent = new Intent(this, AddCostActivity.class);
        startActivity(intent);
    }
}
