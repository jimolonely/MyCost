package com.jimo.mycost.func.friend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimo.mycost.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendAddFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_friend_add, container, false);

        initViews();

        return view;
    }

    private void initViews() {

    }

}
