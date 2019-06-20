package com.jimo.mycost.func.cost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.CostInComeRecord;
import com.jimo.mycost.data.model.BigSmallType;
import com.jimo.mycost.data.model.MonthCost;
import com.jimo.mycost.func.common.SelectImgAdapter;
import com.jimo.mycost.util.CreateTypeList;
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
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.jimo.mycost.util.JimoUtil.getMonth;
import static com.jimo.mycost.util.JimoUtil.getYear;

/**
 * Created by root on 17-7-19.
 * 支出
 */
//TODO 重构Cost和InComeAdd这些重复代码
@ContentView(R.layout.fragment_cost_add)
public class CostAddFragment extends Fragment {

    @ViewInject(R.id.ll_cost_type)
    LinearLayout ll_types;

    @ViewInject(R.id.input_date)
    TextView tv_input_date;

    @ViewInject(R.id.input_type)
    TextView tv_input_type;

    @ViewInject(R.id.input_money)
    EditText edt_input_money;

    @ViewInject(R.id.input_remark)
    EditText edt_input_remark;//备注

    @ViewInject(R.id.rcv_images)
    RecyclerView rcv_imgs;

    private SelectImgAdapter adapterForSelectImg;

    private List<String> foodTitles = new ArrayList<>(Arrays.asList("早餐", "午餐", "晚餐", "零食", "其他"));
    private List<String> transportTitles = new ArrayList<>(Arrays.asList(
            "地铁", "公交", "共享单车", "滴滴", "的士", "火车", "飞机", "其他"));
    private List<String> studyTitles = new ArrayList<>(Arrays.asList("买书", "文具", "付费课程", "其他"));
    private List<String> lifeTitles = new ArrayList<>(Arrays.asList("健康",
            "服饰", "居家", "娱乐", "人情", "旅游", "通讯", "其他"));

    //同步类型。默认是插入
    private int modifyType = MyConst.SYNC_TYPE_INSERT;

    private CreateTypeList createTypeList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();

//        Log.i("root-path", Environment.getExternalStorageDirectory().getAbsolutePath());///storage/emulated/0
        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        createTypeList = new CreateTypeList(ll_types, getContext(), (
                (bigType, smallType) -> tv_input_type.setText(bigType + " " + smallType)),
                BigSmallType.TYPE_COST
        );
        createTypeList.setTypes();

        rcv_imgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapterForSelectImg = new SelectImgAdapter(getContext());
        rcv_imgs.setAdapter(adapterForSelectImg);
    }

    /**
     * 点击弹出框，添加类型
     *
     * @author jimo
     * @date 18-10-20 上午9:50
     */
    @Event(R.id.btn_cost_add_type)
    private void clickToAddTypes(View view) {
        AddBigSmallTypeDialog dialog = new AddBigSmallTypeDialog();
        String[] bigTypes = {"餐饮", "交通", "学习", "生活"};
        dialog.show(Objects.requireNonNull(getActivity()).getFragmentManager(),
                bigTypes, (b, s) -> createTypeList.saveSmallType(b, s));
    }

    /**
     * 提交
     *
     * @param view
     */
    @Event(R.id.btn_finish)
    private void finishClick(View view) {
        if (FuckUtil.checkInput(getContext(), tv_input_date, tv_input_type, edt_input_money)) {
            DbManager db = MyApp.dbManager;

            switch (modifyType) {
                case MyConst.SYNC_TYPE_INSERT:
                    String userName = MyConst.getUserName(getContext());
                    final String date = String.valueOf(tv_input_date.getText());
                    final float money = Float.parseFloat(String.valueOf(edt_input_money.getText()));
                    CostInComeRecord cost = new CostInComeRecord(MyConst.COST, money,
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
                        JimoUtil.storeImg(getContext(), adapterForSelectImg.getData(), db, parentId, MyConst.IMG_TYPE_COST, month, year);

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
                        FuckUtil.clearInput((obj) -> {
                            adapterForSelectImg.clear();
                        }, tv_input_date, tv_input_type, edt_input_money, edt_input_remark);
                        JimoUtil.mySnackbar(view, "保存成功");
                    } catch (DbException e) {
                        e.printStackTrace();
                        JimoUtil.mySnackbar(view, "error: " + e.getMessage());
                    }
            }
        }
    }

    @Event(R.id.input_date)
    private void dateClick(View view) {
        FuckUtil.showDateSelectDialog(getContext(), (date) -> tv_input_date.setText(String.valueOf(date)));
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
