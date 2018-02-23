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
 * Created by jimo on 18-2-24.
 */
@ContentView(R.layout.fragment_body_data)
public class BodyDataFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        
    }
}
