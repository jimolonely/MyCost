package com.jimo.mycost.func.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.func.common.MyFragmentAdapter;
import com.jimo.mycost.func.common.MyOnPageChangeListener;
import com.jimo.mycost.func.record.RecordListFragment;
import com.jimo.mycost.func.record.RecordShowFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_func_frag)
public class RecordActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_title)
    TextView tv_title;

    @ViewInject(R.id.vp_content)
    ViewPager viewPager;

    @ViewInject(R.id.tab_layout)
    TabLayout tabLayout;

    private List<Fragment> fragments = new ArrayList<>();

    private List<String> titles = new ArrayList<>();//tab页的标题

    MyFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);

        initViews();
    }

    @Event(R.id.ib_back)
    private void back(View view) {
        this.finish();
    }

    private void initViews() {
        tv_title.setText("Record");

        titles.add("List");
        titles.add("Show");

        fragments.add(new RecordListFragment());
        fragments.add(new RecordShowFragment());

        adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }

}
