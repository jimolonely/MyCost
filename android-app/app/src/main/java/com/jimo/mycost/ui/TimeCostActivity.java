package com.jimo.mycost.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.model.Subject;
import com.jimo.mycost.model.TimeRecord;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.view.AddSubjectDialog;
import com.jimo.mycost.view.SelectSubjectDialog;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_time_cost)
public class TimeCostActivity extends AppCompatActivity {

    @ViewInject(R.id.timer)
    private Chronometer timer;
    @ViewInject(R.id.tv_choose_subject)
    private TextView tv_select_subject;

    private boolean isStop = true;//控制开始结束
    private String[] subjects;
    private String startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        init();
    }

    private void init() {
        loadSubject();
    }

    private void loadSubject() {
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
        } catch (DbException e) {
            JimoUtil.mySnackbar(tv_select_subject, "保存时间出错");
        }
    }

    /**
     * 选择主题
     *
     * @param view
     */
    public void selectSubject(View view) {
        SelectSubjectDialog selectSubjectDialog = new SelectSubjectDialog();
        String title = "选择主题";
        final String[] items = subjects;
        selectSubjectDialog.show(title, items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tv_select_subject.setText(items[i]);
            }
        }, getFragmentManager());
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
