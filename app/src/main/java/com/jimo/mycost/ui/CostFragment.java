package com.jimo.mycost.ui;

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
 * Created by root on 17-7-19.
 * 支出
 */
@ContentView(R.layout.cost_fragment)
public class CostFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        return inflater.inflate(R.layout.cost_fragment,container,false);
        return x.view().inject(this, inflater, container);
    }
}
