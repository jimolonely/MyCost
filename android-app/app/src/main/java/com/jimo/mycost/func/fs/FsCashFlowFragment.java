package com.jimo.mycost.func.fs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimo.mycost.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

/**
 * 现金流量表
 *
 * @author jimo
 * @date 20-2-24 上午8:27
 */
@ContentView(R.layout.fragment_cash_flow)
public class FsCashFlowFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);

        initData();

        return view;
    }

    private void initData() {
    }

    /**
     * 提交
     */
    @Event(R.id.btn_finish)
    private void finishClick(View view) {

    }

}
