package com.jimo.mycost.func.fs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 资产负债表
 *
 * @author jimo
 * @date 20-2-24 上午8:27
 */
@ContentView(R.layout.fragment_balance_sheet)
public class FsBalanceSheetFragment extends Fragment {

    private TextView tv_date_from;
    private TextView tv_date_to;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {

        tv_date_from = getActivity().findViewById(R.id.tv_common_start_date);
        tv_date_to = getActivity().findViewById(R.id.tv_common_end_date);

        //初始日期为本月
        tv_date_from.setText(JimoUtil.getFirstDayOfMonth(JimoUtil.getCurrentMonth()));
        tv_date_to.setText(JimoUtil.getDateBefore(0));
    }

}
