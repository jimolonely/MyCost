package com.jimo.mycost.ui;

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
import com.jimo.mycost.R;
import com.jimo.mycost.model.Subject;
import com.jimo.mycost.util.JimoUtil;
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
            timer.stop();
            btn.setText("开始计时");
            isStop = true;
            saveTheTime(view);
        } else {
            timer.setBase(SystemClock.elapsedRealtime());
            timer.setFormat("%s");
            timer.start();
            isStop = false;
            btn.setText("停止计时");
        }
    }

    private void saveTheTime(View view) {
        String s = timer.getText().toString();
        JimoUtil.mySnackbar(view, s);

        getFragmentManager();
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
            JimoUtil.mySnackbar(tv_select_subject, "请先停止计时才能退出");
        }
    }
}
