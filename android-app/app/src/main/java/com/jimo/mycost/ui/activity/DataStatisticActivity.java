package com.jimo.mycost.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jimo.mycost.R;
import com.jimo.mycost.ui.fragment.BodyDataShowFragment;
import com.jimo.mycost.ui.fragment.CostShowFragment;
import com.jimo.mycost.ui.fragment.InComeShowFragment;
import com.jimo.mycost.util.MyFragmentAdapter;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_data_statistic)
public class DataStatisticActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @ViewInject(R.id.vp_content)
    ViewPager viewPager;

    @ViewInject(R.id.tab_layout)
    TabLayout tabLayout;

    private List<Fragment> fragments = new ArrayList<>();

    private List<String> titles = new ArrayList<>();//tab页的标题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        initViews();
    }

    private void initViews() {
        titles.add("支出");
        titles.add("身体数据");
        titles.add("收入");

        fragments.add(new CostShowFragment());
        fragments.add(new BodyDataShowFragment());
        fragments.add(new InComeShowFragment());

        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabsFromPagerAdapter(adapter);
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


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
