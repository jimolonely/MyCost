package com.jimo.mycost.func.friend;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimo.mycost.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 */
@ContentView(R.layout.fragment_friend_show)
public class FriendShowFragment extends Fragment {
    @ViewInject(R.id.srl_friend_show)
    private SwipeRefreshLayout srl_show;
    private boolean isRefresh = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        srl_show.setColorSchemeColors(Color.BLACK, Color.BLUE, Color.GREEN, Color.YELLOW);
        srl_show.setOnRefreshListener(() -> {
            if (!isRefresh) {
                isRefresh = true;
                refresh();
            }
        });
    }

    private void refresh() {
        //TODO
        isRefresh = false;
        srl_show.setRefreshing(false);
    }

}
