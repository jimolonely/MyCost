package com.jimo.mycost.func.life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.LifeRecord;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import net.alhazmy13.wordcloud.WordCloud;
import net.alhazmy13.wordcloud.WordCloudView;

import org.xutils.DbManager;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jimo.mycost.MyConst.themeData;
import static com.jimo.mycost.func.life.Constant.THEME_MOVIE;

@ContentView(R.layout.fragment_life_show)
public class LifeShowFragment extends Fragment {

    @ViewInject(R.id.sp_theme_life)
    private Spinner sp_theme;
    private ArrayAdapter<String> adapter;
    private String theme = THEME_MOVIE;

    @ViewInject(R.id.tv_common_start_date)
    private TextView tv_date_from;
    @ViewInject(R.id.tv_common_end_date)
    private TextView tv_date_to;

    @ViewInject(R.id.wc_life_show_type)
    private WordCloudView wordCloudType;

    private List<LifeRecord> lifeRecords;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, themeData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_theme.setAdapter(adapter);
        sp_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                theme = adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //初始日期为本月
        tv_date_from.setText(JimoUtil.getFirstDayOfMonth(JimoUtil.getCurrentMonth()));
        tv_date_to.setText(JimoUtil.getDateBefore(0));
    }

    /**
     * <p>
     * 展示类型词云
     * </p >maven { url "http://maven.aliyun.com/nexus/content/groups/public/" }
     *
     * @author jimo
     * @date 19-11-23 下午4:01
     */
    private List<DbModel> getLifeData() {
        DbManager db = MyApp.dbManager;
        try {
            return db.selector(LifeRecord.class)
                    .select("type", "creators", "pubdate", "score", "mood", "spend_time")
                    .where("theme", "=", theme)
                    .and("time", ">=", tv_date_from.getText())
                    .and("time", "<=", tv_date_to.getText())
                    .findAll();
        } catch (DbException e) {
            e.printStackTrace();
            JimoUtil.mySnackbar(tv_date_from, "load life data error: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    @Event(R.id.btn_common_ok)
    private void clickToSearch(View view) {
        List<DbModel> lifeData = getLifeData();

        showWordCloud(wordCloudType, getWordCloudData(lifeData, "type"));
    }

    /**
     * <p>
     * 按照key进行统计出词频数据
     * </p >
     *
     * @author jimo
     * @date 19-11-23 下午4:46
     */
    private List<WordCloud> getWordCloudData(List<DbModel> lifeData, String key) {
        Map<String, Integer> map = new HashMap<>();
        for (DbModel model : lifeData) {
            String d = model.getString(key);
            map.put(d, map.getOrDefault(d, 0) + 1);
        }
        List<WordCloud> wordClouds = new ArrayList<>(map.size());
        map.entrySet().forEach(m -> wordClouds.add(new WordCloud(m.getKey(), m.getValue())));
        return wordClouds;
    }

    private void showWordCloud(WordCloudView wordCloudView, List<WordCloud> data) {
        wordCloudView.setDataSet(data);
        wordCloudView.notifyDataSetChanged();
    }

    @Event(R.id.tv_common_start_date)
    private void clickToSetFromDate(View view) {
        FuckUtil.showDateSelectDialog(getContext(), obj -> tv_date_from.setText((String) obj));
    }

    @Event(R.id.tv_common_end_date)
    private void clickToSetEndDate(View view) {
        FuckUtil.showDateSelectDialog(getContext(), obj -> tv_date_to.setText((String) obj));
    }
}
