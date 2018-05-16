package com.jimo.mycost.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.adapter.SelectImgAdapter;
import com.jimo.mycost.model.CostInComeRecord;
import com.jimo.mycost.model.ImageRecord;
import com.jimo.mycost.model.MonthCost;
import com.jimo.mycost.util.JimoUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.jimo.mycost.util.JimoUtil.getMonth;
import static com.jimo.mycost.util.JimoUtil.getYear;

/**
 * Created by root on 17-7-19.
 * 支出
 */
@ContentView(R.layout.fragment_cost)
public class CostFragment extends Fragment {

    @ViewInject(R.id.fbl_food)
    FlexboxLayout fl_food;

    @ViewInject(R.id.fbl_transport)
    FlexboxLayout fl_transport;

    @ViewInject(R.id.fbl_study)
    FlexboxLayout fl_study;

    @ViewInject(R.id.fbl_life)
    FlexboxLayout fl_life;

    @ViewInject(R.id.input_date)
    TextView input_date;

    @ViewInject(R.id.input_type)
    TextView input_type;

    @ViewInject(R.id.input_money)
    EditText input_money;

    @ViewInject(R.id.input_remark)
    EditText input_remark;//备注

    @ViewInject(R.id.iv_select_img)
    ImageView iv_select_img;

    @ViewInject(R.id.rcv_images)
    RecyclerView rcv_imgs;

    private SelectImgAdapter adapterForSelectImg;
    private List<String> imgPath = new ArrayList<>();

    private List<String> foodTitles = new ArrayList<>(Arrays.asList("早餐", "午餐", "晚餐", "零食", "其他"));
    private List<String> transportTitles = new ArrayList<>(Arrays.asList(
            "地铁", "公交", "共享单车", "滴滴", "的士", "火车", "飞机", "其他"));
    private List<String> studyTitles = new ArrayList<>(Arrays.asList("买书", "文具", "付费课程", "其他"));
    private List<String> lifeTitles = new ArrayList<>(Arrays.asList("健康",
            "服饰", "居家", "娱乐", "人情", "旅游", "通讯", "其他"));
    //存储输入的值
    private String date;
    private String type;
    private float money;
    private String remark;

    //同步类型。默认是插入
    private int modifyType = MyConst.SYNC_TYPE_INSERT;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();

//        Log.i("root-path", Environment.getExternalStorageDirectory().getAbsolutePath());///storage/emulated/0
        return view;

    }

    private void initData() {
        //food
        for (String s : foodTitles) {
            TextView tv = getTextView(s, new FoodOnClickListener());
            fl_food.addView(tv);
        }

        //transport
        for (String s : transportTitles) {
            fl_transport.addView(getTextView(s, new TransportClickListener()));
        }

        //study
        for (String s : studyTitles) {
            fl_study.addView(getTextView(s, new StudyClickListener()));
        }

        //life
        for (String s : lifeTitles) {
            fl_life.addView(getTextView(s, new LifeClickListener()));
        }

        rcv_imgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapterForSelectImg = new SelectImgAdapter(getContext(), imgPath);
        rcv_imgs.setAdapter(adapterForSelectImg);
    }

    @NonNull
    private TextView getTextView(String s, View.OnClickListener listener) {
        TextView tv = new TextView(getContext());
        tv.setText(s);
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 5, 10, 5);
        tv.setOnClickListener(listener);
        tv.setTextColor(getResources().getColor(R.color.secondary_text));
        return tv;
    }

    /**
     * 提交
     *
     * @param view
     */
    @Event(R.id.btn_finish)
    private void finishClick(View view) {
        if (checkInput(view)) {
            try {
                money = Float.parseFloat(String.valueOf(input_money.getText()));
                remark = String.valueOf(input_remark.getText());
            } catch (Exception e) {
                Snackbar.make(view, "error", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (localStore(view)) {
                clearInput();
                JimoUtil.mySnackbar(view, "保存成功");
            }
        }
    }

    //清空输入框
    private void clearInput() {
        input_date.setText("");
        input_type.setText("");
        input_money.setText("");
        input_remark.setText("");
        imgPath.clear();
        adapterForSelectImg.setData(imgPath);
        adapterForSelectImg.notifyDataSetChanged();
    }


    /**
     * 存本地数据库
     */
    private boolean localStore(View view) {

        DbManager db = MyApp.dbManager;

        switch (modifyType) {
            case MyConst.SYNC_TYPE_INSERT:
                String userName = MyConst.getUserName(getContext());
                CostInComeRecord cost = new CostInComeRecord(MyConst.COST, money,
                        remark, date, type, userName, MyConst.SYNC_TYPE_INSERT);
                //TODO 存储图片,目录结构按 type/year/month/file
                // /storage/emulated/0/Android/data/com.jimo.mycost/cache/luban_disk_cache/1526471417309704.jpg

                //TODO 事务
                try {
                    int month = getMonth(date);
                    int year = getYear(date);

                    //存储后获得id,用于关联图片
                    db.save(cost);
                    final CostInComeRecord newest = db.selector(CostInComeRecord.class).orderBy("id", true).findFirst();
                    final long parentId = newest.getId();
                    final String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                    final String dir = Paths.get(MyConst.IMG_SAVE_PATH, MyConst.IMG_TYPE_COST, year + "", month + "").toString();
                    for (String p : imgPath) {
                        //构造图片地址
                        final String fileName = p.substring(p.lastIndexOf('/') + 1);
                        String path = dir + "/" + fileName;
                        //需要绝对路径来复制图片
                        String absPath = Paths.get(rootPath, dir).toString();
                        final boolean ok = JimoUtil.fileCopy(p, absPath, fileName);
                        Log.i("path", absPath);
                        if (ok) {
                            final ImageRecord imageRecord = new ImageRecord(parentId, MyConst.IMG_TYPE_COST, path);
                            db.save(imageRecord);
                        } else {
                            JimoUtil.mySnackbar(view, "存储图片失败");
                        }
                    }

                    //更新月记录
                    MonthCost monthCost = db.selector(MonthCost.class).
                            where("month", "=", month).and("year", "=", year).
                            and("user_name", "=", userName).and("in_out", "=", MyConst.COST).findFirst();
                    if (monthCost == null) {
                        monthCost = new MonthCost(year, month, money, MyConst.COST, MyConst.SYNC_TYPE_INSERT, userName);
                        db.save(monthCost);
                    } else {
                        monthCost.setSyncType(MyConst.SYNC_TYPE_UPDATE);
                        monthCost.setMoney(monthCost.getMoney() + money);
                        db.update(monthCost, "money", "sync_type");
                    }
                    return true;
                } catch (DbException e) {
                    e.printStackTrace();
                    JimoUtil.mySnackbar(view, "error local store");
                    return false;
                }
        }

        return false;
    }


    /**
     * 检查输入
     */
    private boolean checkInput(View view) {
        if (TextUtils.isEmpty(date)) {
            Snackbar.make(view, "选择日期", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(type)) {
            Snackbar.make(view, "选择用途", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(String.valueOf(money))) {
            Snackbar.make(view, "输入金额", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Event(R.id.input_date)
    private void dateClick(View view) {
        //TODO 重构
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (datePicker, year1, month1, dayOfMonth) -> {
            //为了按日期排序，当9月9号时应该写成09-09,而不是9-9
            if (month1 < 9) {
                date = year1 + "-0" + (month1 + 1);
            } else {
                date = year1 + "-" + (month1 + 1);
            }
            if (dayOfMonth < 10) {
                date += "-0" + dayOfMonth;
            } else {
                date += "-" + dayOfMonth;
            }
            input_date.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }

    private class FoodOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "餐饮 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    private class TransportClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "交通 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    private class StudyClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "学习 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    private class LifeClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                type = "生活 " + String.valueOf(tv.getText());
                input_type.setText(type);
            }
        }
    }

    //在从主页面点击一条数据进来时确定是修改
    //TODO


    @Event(R.id.iv_select_img)
    private void onSelectImgClick(View view) {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
                .compress(true).isCamera(true).forResult(PictureConfig.CHOOSE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    final List<LocalMedia> media = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia m : media) {
                        if (m.isCompressed()) {
                            Log.i("path-compress", m.getCompressPath());
                            imgPath.add(m.getCompressPath());
                        } else {
                            Log.i("path", m.getPath());
                            imgPath.add(m.getPath());
                        }
                    }
                    adapterForSelectImg.setData(imgPath);
                    adapterForSelectImg.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(getContext());
        PictureFileUtils.deleteExternalCacheDirFile(getContext());
        Log.i("destory", "已清除缓存");
    }
}
