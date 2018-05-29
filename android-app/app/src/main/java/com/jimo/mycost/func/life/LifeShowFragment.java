package com.jimo.mycost.func.life;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimo.mycost.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

@ContentView(R.layout.fragment_life_show)
public class LifeShowFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        return view;
    }


}
