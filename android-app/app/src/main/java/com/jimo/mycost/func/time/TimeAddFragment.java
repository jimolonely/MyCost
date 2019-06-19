package com.jimo.mycost.func.time;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.BigSmallType;
import com.jimo.mycost.util.CreateTypeList;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;
import java.util.Set;

/**
 * time add
 */
@ContentView(R.layout.fragment_time_add)
public class TimeAddFragment extends Fragment {

    @ViewInject(R.id.ll_time_type)
    private LinearLayout ll_types;
    @ViewInject(R.id.tv_time_type)
    private TextView tv_time_type;
    @ViewInject(R.id.tv_time_begin)
    private TextView tv_time_begin;

    private CreateTypeList createTypeList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = x.view().inject(this, inflater, container);

        initViews();

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void initViews() {
        createTypeList = new CreateTypeList(ll_types, getContext(), (
                (bigType, smallType) -> tv_time_type.setText(bigType + " " + smallType)),
                BigSmallType.TYPE_TIME
        );
        createTypeList.setTypes();
    }

    /**
     * 开始一个项目
     */
    @Event(R.id.tv_time_begin)
    public void startTime(View view) {
        String type = tv_time_type.getText().toString();
        if (TextUtils.isEmpty(type)) {
            return;
        }
        // begin
    }
}
