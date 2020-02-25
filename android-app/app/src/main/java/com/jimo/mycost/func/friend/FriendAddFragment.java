package com.jimo.mycost.func.friend;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.Friend;
import com.jimo.mycost.data.model.FriendThing;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
@ContentView(R.layout.fragment_friend_add)
public class FriendAddFragment extends Fragment {

    @ViewInject(R.id.fbl_friend_show)
    FlexboxLayout fl_names;
    @ViewInject(R.id.act_friend_search)
    AutoCompleteTextView act_search;
    @ViewInject(R.id.tv_add_date)
    TextView tv_date;
    @ViewInject(R.id.tv_add_name)
    TextView tv_name;
    @ViewInject(R.id.edt_add_thing)
    EditText edt_thing;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = x.view().inject(this, inflater, container);

        initViews();

        return view;
    }

    private List<String> loadFriendNames() {
        List<String> names = new ArrayList<>();
        final DbManager db = MyApp.dbManager;
        try {
            final List<Friend> friends = db.selector(Friend.class).findAll();
            if (friends != null) {
                friends.forEach((f) -> names.add(f.getName()));
            }
        } catch (DbException e) {
            e.printStackTrace();
            JimoUtil.myToast(getContext(), "find friend error: " + e.getMessage());
        }
        return names;
    }

    private void initViews() {
        reloadNames();
    }

    /**
     * 更新名字
     */
    private void reloadNames() {
        final List<String> names = loadFriendNames();
        ArrayAdapter<String> adapterForSearch = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_dropdown_item_1line, names);
        act_search.setAdapter(adapterForSearch);
        fl_names.removeAllViewsInLayout();
        registClickListener(names, fl_names);
    }

    private void registClickListener(List<String> strs, FlexboxLayout flex) {
        for (String s : strs) {
            TextView tvv = FuckUtil.getTextView(getContext(), s, (view) -> {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    final String text = String.valueOf(tv.getText());
                    tv_name.setText(text);
                }
            });
            flex.addView(tvv);
        }
    }

    @Event(R.id.btn_add_thing)
    private void submit(View view) {
        if (FuckUtil.checkInput(getContext(), tv_date, tv_name, edt_thing)) {
            DbManager db = MyApp.dbManager;
            final String date = String.valueOf(tv_date.getText());
            final FriendThing thing = new FriendThing(String.valueOf(tv_name.getText()), date, String.valueOf(edt_thing.getText()));

            try {
                db.save(thing);
                final Long id = db.selector(FriendThing.class).orderBy("id", true).findFirst().getId();
                FuckUtil.clearInput((obj) -> {
                }, tv_name, tv_date, edt_thing);
                JimoUtil.myToast(getContext(), "保存成功");
            } catch (DbException e) {
                e.printStackTrace();
                JimoUtil.myToast(getContext(), "error: " + e.getMessage());
            }
        }
    }

    @Event(R.id.tv_add_date)
    private void dateClick(View view) {
        FuckUtil.showDateSelectDialog(getContext(), (date) -> tv_date.setText(String.valueOf(date)));
    }

    /**
     * 弹出对话框添加一个朋友
     */
    @Event(R.id.ib_add_friend)
    private void addFriend(View view) {
        new FriendAddDialog().show(Objects.requireNonNull(getActivity()).getFragmentManager(), (obj) -> reloadNames());
    }

}
