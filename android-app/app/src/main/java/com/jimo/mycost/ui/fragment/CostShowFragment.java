package com.jimo.mycost.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimo.mycost.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by jimo on 18-2-24.
 * 展示开销
 */
@ContentView(R.layout.fragment_cost_show)
public class CostShowFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        return view;
    }

}
