package com.jimo.mycost.func.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jimo.mycost.R;
import com.jimo.mycost.func.body.BodyDataFragment;
import com.jimo.mycost.func.cost.add.CostAddFragment;
import com.jimo.mycost.func.income.InComeAddFragment;
import com.jimo.mycost.func.life.LifeAddFragment;
import com.jimo.mycost.func.common.MyFragmentAdapter;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_add_cost)
public class AddCostActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

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
        titles.add("支出");
        titles.add("身体数据");
        titles.add("人生啊");
        titles.add("收入");

        fragments.add(new CostAddFragment());
        fragments.add(new BodyDataFragment());
        fragments.add(new LifeAddFragment());
        fragments.add(new InComeAddFragment());

        adapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabsFromPagerAdapter(adapter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(this);
//        PictureFileUtils.deleteExternalCacheDirFile(this);
        Log.i("destory-activity", "已清除缓存");
    }
}
