package com.jimo.mycost.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.model.Subject;
import com.jimo.mycost.model.TimeRecord;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.view.AddSubjectDialog;
import com.jimo.mycost.view.DayTimeItemAdapter;
import com.jimo.mycost.view.ItemDayTime;
import com.jimo.mycost.view.SelectSubjectDialog;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_time_cost)
public class TimeCostActivity extends AppCompatActivity {

    @ViewInject(R.id.timer)
    private Chronometer timer;
    @ViewInject(R.id.tv_choose_subject)
    private TextView tv_select_subject;
    @ViewInject(R.id.lv_time_day)
    private ListView lv_day_cost;

    private boolean isStop = true;//控制开始结束
    private String[] subjects;
    private String startTime;
    private DayTimeItemAdapter dayTimeItemAdapter;
    private List<ItemDayTime> itemDayTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        init();
    }

    private void init() {
        itemDayTimes = new ArrayList<>();
        dayTimeItemAdapter = new DayTimeItemAdapter(itemDayTimes, this);
        lv_day_cost.setAdapter(dayTimeItemAdapter);
        loadSubject();
        loadTimeDayRecord();
    }

    /**
     * 加载每天花的时间列表
     */
    private void loadTimeDayRecord() {
        DbManager db = MyApp.dbManager;
        try {
            List<TimeRecord> timeRecords = db.selector(TimeRecord.class).where("start_time", ">",
                    JimoUtil.getDateBefore(0)).findAll();
            if (timeRecords != null) {
                getTimeDayItems(timeRecords);
                dayTimeItemAdapter.notifyDataSetChanged();
            }
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_select_subject, "加载每日时间出错");
        }
    }

    /**
     * 重构数据为listview显示的数据,本来可以用sql聚类完成，但这里框架限制了
     *
     * @param timeRecords
     * @return
     */
    private void getTimeDayItems(List<TimeRecord> timeRecords) {
        itemDayTimes.clear();
        Map<String, String> map = new HashMap<>();
        for (TimeRecord timeRecord : timeRecords) {
            String subjectName = timeRecord.getSubjectName();
            if (map.containsKey(subjectName)) {
                map.put(subjectName, JimoUtil.addTwoTime(map.get(subjectName), timeRecord.getTimeLen()));
            } else {
                map.put(subjectName, timeRecord.getTimeLen());
            }
        }
        for (String key : map.keySet()) {
            itemDayTimes.add(new ItemDayTime(key, map.get(key)));
        }
    }

    private void loadSubject() {
        subjects = null;
        DbManager db = MyApp.dbManager;
        try {
            List<Subject> sbs = db.selector(Subject.class).findAll();
            if (sbs == null) {
                sbs = new ArrayList<>();
            }
            subjects = new String[sbs.size()];
            int i = 0;
            for (Subject sb : sbs) {
                subjects[i++] = sb.getSubjectName();
            }
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_select_subject, "加载主题出错");
        }
    }

    public void recordTime(View view) {
        Button btn = (Button) view;
        if (!isStop) {
            String subjectName = tv_select_subject.getText().toString();
            if ("".equals(subjectName)) {
                JimoUtil.mySnackbar(tv_select_subject, "请先选择主题");
            } else {
                timer.stop();
                btn.setText("开始计时");
                isStop = true;
                saveTheTime(subjectName);
            }
        } else {
            startTime = JimoUtil.getDateTimeNow();
            timer.setBase(SystemClock.elapsedRealtime());
            timer.setFormat("%s");
            timer.start();
            isStop = false;
            btn.setText("停止计时");
        }
    }

    /**
     * 保存时间到数据库
     *
     * @param subjectName
     */
    private void saveTheTime(String subjectName) {
        String timeLen = timer.getText().toString();
        JimoUtil.mySnackbar(tv_select_subject, timeLen);
        DbManager db = MyApp.dbManager;
        TimeRecord timeRecord = new TimeRecord(subjectName, timeLen, startTime, JimoUtil.getDateTimeNow(), MyConst.SYNC_TYPE_INSERT);
        try {
            db.save(timeRecord);
            timer.setBase(SystemClock.elapsedRealtime());
            JimoUtil.mySnackbar(tv_select_subject, "保存成功");
            loadTimeDayRecord();//重新加载时间
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_select_subject, "保存时间出错");
        }
    }

    /**
     * 选择主题
     *
     * @param view
     */
    private int indexOfSubject;

    public void selectSubject(View view) {
        SelectSubjectDialog selectSubjectDialog = new SelectSubjectDialog();
        String title = "选择主题";
        final String[] items = subjects;
        selectSubjectDialog.show(title, items,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        indexOfSubject = i;
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tv_select_subject.setText(items[indexOfSubject]);
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        doDeleteSubject(items[indexOfSubject]);
                    }
                }, getFragmentManager());
    }

    private void doDeleteSubject(final String subjectName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确定删除吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DbManager db = MyApp.dbManager;
                WhereBuilder whereBuilder = WhereBuilder.b();
                whereBuilder.and("subject_name", "=", subjectName);
                try {
                    db.delete(Subject.class, whereBuilder);
                    JimoUtil.mySnackbar(tv_select_subject, "删除[" + subjectName + "]成功");
                    loadSubject();
                } catch (DbException e) {
                    JimoUtil.mySnackbar(tv_select_subject, "删除[" + subjectName + "]失败");
                }
            }
        }).create().show();
    }

    /**
     * 添加主题
     *
     * @param view
     */
    public void addSubject(View view) {
        //添加完成后重新加载主题
        AddSubjectDialog addSubjectDialog = new AddSubjectDialog();
        addSubjectDialog.show(getFragmentManager(), new AddSubjectDialog.Callback() {
            @Override
            public void onOk(String subjectName, String endDate) {
                saveOneSubject(subjectName, endDate);
            }
        });
    }

    private void saveOneSubject(String subjectName, String endDate) {
        DbManager dbManager = MyApp.dbManager;
        Subject subject = new Subject(subjectName, endDate, MyConst.SYNC_TYPE_INSERT);
        try {
            dbManager.save(subject);
            loadSubject();
            JimoUtil.mySnackbar(tv_select_subject, "保存成功");
        } catch (DbException e) {
            e.printStackTrace();
            JimoUtil.mySnackbar(tv_select_subject, "存储主题错误");
        }
    }

    public void closeActivity(View view) {
        handleReturn();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            handleReturn();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在退出前判断是否处于计时状态
     */
    private void handleReturn() {
        if (isStop) {
            this.finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("警告");
            builder.setMessage("确定放弃这次计时吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    TimeCostActivity.this.finish();
                }
            });
            builder.create().show();
//            JimoUtil.mySnackbar(tv_select_subject, "请先停止计时才能退出");
        }
    }
}
