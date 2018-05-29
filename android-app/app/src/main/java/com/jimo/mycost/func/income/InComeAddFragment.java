package com.jimo.mycost.func.income;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.CostInComeRecord;
import com.jimo.mycost.data.model.MonthCost;
import com.jimo.mycost.func.common.SelectImgAdapter;
import com.jimo.mycost.util.FuckUtil;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.jimo.mycost.util.JimoUtil.getMonth;
import static com.jimo.mycost.util.JimoUtil.getYear;

/**
 * Created by root on 17-7-19.
 * 收入
 */
@ContentView(R.layout.fragment_income_add)
public class InComeAddFragment extends Fragment {

    @ViewInject(R.id.fbl_normal_income)
    FlexboxLayout fl_normal;

    @ViewInject(R.id.input_date)
    TextView tv_input_date;

    @ViewInject(R.id.input_type)
    TextView tv_input_type;

    @ViewInject(R.id.input_money)
    EditText edt_input_money;

    @ViewInject(R.id.input_remark)
    EditText edt_input_remark;

    private List<String> normalInComes = new ArrayList<>(Arrays.asList("工资", "兼职", "红包", "投资", "奖金", "补贴", "礼金", "其他"));

    //同步类型。默认是插入
    private int modifyType = MyConst.SYNC_TYPE_INSERT;

    @ViewInject(R.id.rcv_images)
    RecyclerView rcv_imgs;

    private SelectImgAdapter adapterForSelectImg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();

        return view;
    }

    private void initData() {
        registClickListener(normalInComes, "正常", fl_normal);

        rcv_imgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapterForSelectImg = new SelectImgAdapter(getContext());
        rcv_imgs.setAdapter(adapterForSelectImg);
    }

    private void registClickListener(List<String> title, String type, FlexboxLayout flex) {
        for (String s : title) {
            TextView tvv = getTextView(s, (view) -> {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    final String text = type + " " + String.valueOf(tv.getText());
                    tv_input_type.setText(text);
                }
            });
            flex.addView(tvv);
        }
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
        if (FuckUtil.checkInput(getContext(), tv_input_date, edt_input_money, tv_input_type)) {
            DbManager db = MyApp.dbManager;

            switch (modifyType) {
                case MyConst.SYNC_TYPE_INSERT:
                    String userName = MyConst.getUserName(getContext());
                    final String date = String.valueOf(tv_input_date.getText());
                    final float money = Float.parseFloat(String.valueOf(edt_input_money.getText()));
                    CostInComeRecord cost = new CostInComeRecord(MyConst.IN_COME, money,
                            String.valueOf(edt_input_remark.getText()), date,
                            String.valueOf(tv_input_type.getText()), userName, MyConst.SYNC_TYPE_INSERT);
                    //存储图片,目录结构按 type/year/month/file
                    // /storage/emulated/0/Android/data/com.jimo.mycost/cache/luban_disk_cache/1526471417309704.jpg
                    //TODO 事务
                    try {
                        int month = getMonth(date);
                        int year = getYear(date);

                        //存储后获得id,用于关联图片
                        db.save(cost);
                        final long parentId = db.selector(CostInComeRecord.class).orderBy("id", true).findFirst().getId();
                        JimoUtil.storeImg(getContext(), adapterForSelectImg.getData(), db, parentId, MyConst.IMG_TYPE_INCOME, month, year);

                        //更新月记录
                        MonthCost monthCost = db.selector(MonthCost.class).
                                where("month", "=", month).and("year", "=", year).
                                and("user_name", "=", userName).and("in_out", "=", MyConst.IN_COME).findFirst();
                        if (monthCost == null) {
                            monthCost = new MonthCost(year, month, money, MyConst.IN_COME, MyConst.SYNC_TYPE_INSERT, userName);
                            db.save(monthCost);
                        } else {
                            monthCost.setSyncType(MyConst.SYNC_TYPE_UPDATE);
                            monthCost.setMoney(monthCost.getMoney() + money);
                            db.update(monthCost, "money", "sync_type");
                        }
                        FuckUtil.clearInput((obj) -> {
                            adapterForSelectImg.clear();
                        }, tv_input_date, tv_input_type, edt_input_money, edt_input_remark);
                        JimoUtil.mySnackbar(view, "保存成功");
                    } catch (DbException e) {
                        e.printStackTrace();
                        JimoUtil.mySnackbar(view, "error: " + e.getMessage());
                    }
            }
//            final float money = Float.parseFloat(String.valueOf(edt_input_money.getText()));
//            final String remark = String.valueOf(edt_input_remark.getText());
//            final String date = String.valueOf(tv_input_date.getText());
//            DbManager db = MyApp.dbManager;
//
//            switch (modifyType) {
//                case MyConst.SYNC_TYPE_INSERT:
//                    String userName = MyConst.getUserName(getContext());
//                    CostInComeRecord cost = new CostInComeRecord(MyConst.IN_COME, money,
//                            remark, date, String.valueOf(tv_input_type.getText()), userName, MyConst.SYNC_TYPE_INSERT);
//                    //TODO 事务
//                    try {
//                        db.save(cost);
//
//                        int month = getMonth(date);
//                        int year = getYear(date);
//                        MonthCost monthCost = db.selector(MonthCost.class).
//                                where("month", "=", month).and("year", "=", year).
//                                and("user_name", "=", userName).and("in_out", "=", MyConst.IN_COME).findFirst();
//                        if (monthCost == null) {
//                            monthCost = new MonthCost(year, month, money, MyConst.IN_COME, MyConst.SYNC_TYPE_INSERT, userName);
//                            db.save(monthCost);
//                        } else {
//                            monthCost.setSyncType(MyConst.SYNC_TYPE_UPDATE);
//                            monthCost.setMoney(monthCost.getMoney() + money);
//                            db.update(monthCost, "money", "sync_type");
//                        }
//                        FuckUtil.clearInput((obj) -> {
//                        }, tv_input_type, edt_input_money, tv_input_date, edt_input_remark);
//                        JimoUtil.mySnackbar(view, "保存成功");
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                        JimoUtil.mySnackbar(view, "error: " + e.getMessage());
//                    }
//            }
        }
    }

    @Event(R.id.input_date)
    private void dateClick(View view) {
        FuckUtil.showDateSelectDialog(getContext(), (date) -> tv_input_date.setText(String.valueOf(date)));
    }

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
                            adapterForSelectImg.getData().add(m.getCompressPath());
                        } else {
                            Log.i("path", m.getPath());
                            adapterForSelectImg.getData().add(m.getPath());
                        }
                    }
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
