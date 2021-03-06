package com.jimo.mycost.func.cost;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.CostInComeRecord;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jimo on 18-2-24.
 * 展示开销
 */
@ContentView(R.layout.fragment_cost_show)
public class CostShowFragment extends Fragment {

    @ViewInject(R.id.lv_cost)
    ListView listViewCost;

    private List<CostDayItem> dayCostItems;
    private CostDayItemAdapter costDayItemAdapter;

    final int SHOW_LIMIT = 50;//主页面显示的记录条数

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initViews();
        return view;
    }

    /**
     * 更新ListView
     */
    private void initViews() {
        dayCostItems = new ArrayList<>();
        costDayItemAdapter = new CostDayItemAdapter(dayCostItems, getContext());
        listViewCost.setAdapter(costDayItemAdapter);

        //当点击item时需跳转到新页面展示
        listViewCost.setOnItemClickListener((adapterView, view, i, l) -> {
//                CostDayItem dayCostTitle = dayCostItems.get(i);
            //TODO 显示item
        });

//        refresh();
    }

    private void queryData() {
        dayCostItems.clear();
        DbManager db = MyApp.dbManager;
        //查存每条记录
        try {
            List<CostInComeRecord> costInComeRecords =
                    db.selector(CostInComeRecord.class).orderBy("c_date", true).limit(SHOW_LIMIT).findAll();
            fillTitles(costInComeRecords, dayCostItems);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据构造double ListView的数据
     * //TODO 待优化
     *
     * @param costInComeRecords data
     */
    public static void fillTitles(List<CostInComeRecord> costInComeRecords, List<CostDayItem> dayCostItems) {
        if (costInComeRecords == null) {
            return;
        }
        Set<String> dates = new HashSet<>();
        for (CostInComeRecord c : costInComeRecords) {
            if (!dates.contains(c.getDate())) {
                dates.add(c.getDate());
                dayCostItems.add(new CostDayItem(c.getDate(), MyConst.ITEM_TYPE_TITLE));
                for (CostInComeRecord tc : costInComeRecords) {
                    String d = tc.getDate();
                    if (d != null && d.equals(c.getDate())) {

                        /*暫時不加載圖片*/
                        /*final List<RecyclerViewTempImgItem> imagePaths = tc.getImagePaths(db);
                        if (imagePaths != null && imagePaths.size() > 0) {
                            final RecyclerViewTempImgAdapter adapter = new RecyclerViewTempImgAdapter(getContext(), imagePaths);
                            dayCostItems.add(new CostDayItem(tc.getDate(), MyConst.ITEM_TYPE_WITH_PHOTO,
                                    tc.getTypeName(), String.valueOf(tc.getMoney()), tc.getRemark(), tc.getId(), adapter));
                        } else {
                            dayCostItems.add(new CostDayItem(tc.getDate(), MyConst.ITEM_TYPE_NO_PHOTO, tc.getTypeName(),
                                    String.valueOf(tc.getMoney()), tc.getRemark(), tc.getId()));
                        }*/

                        dayCostItems.add(new CostDayItem(tc.getDate(), MyConst.ITEM_TYPE_NO_PHOTO, tc.getTypeName(),
                                String.valueOf(tc.getMoney()), tc.getRemark(), tc.getId()));
                    }
                }
            }
        }
    }

    private void refresh() {
        queryData();
        costDayItemAdapter.notifyDataSetChanged();
    }

    @Event(R.id.btn_cost_refresh)
    private void clickToRefresh(View view) {
        refresh();
    }
}
