package com.jimo.mycost.func.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jimo.mycost.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * @author jimo
 * @date 19-8-3 上午9:55
 */
@ContentView(R.layout.activity_random)
public class RandomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }

    public void closeActivity(View view) {
        this.finish();
    }


}
