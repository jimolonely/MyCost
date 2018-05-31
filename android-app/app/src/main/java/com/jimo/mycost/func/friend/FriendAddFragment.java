package com.jimo.mycost.func.friend;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.FriendThing;
import com.jimo.mycost.func.common.SelectImgAdapter;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
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
    @ViewInject(R.id.rcv_images)
    RecyclerView rcv_imgs;

    private SelectImgAdapter adapterForSelectImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_friend_add, container, false);

        initViews();

        return view;
    }

    private List<String> loadFriendNames() {
        List<String> names = new ArrayList<>();
        //TODO
        return names;
    }

    private void initViews() {
        final List<String> names = loadFriendNames();
        ArrayAdapter<String> adapterForSearch = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_dropdown_item_1line, names);
        act_search.setAdapter(adapterForSearch);
        registClickListener(names, fl_names);

        rcv_imgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapterForSelectImg = new SelectImgAdapter(getContext());
        rcv_imgs.setAdapter(adapterForSelectImg);
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

    public void submit(View view) {
        if (FuckUtil.checkInput(getContext(), tv_date, tv_name, edt_thing)) {
            DbManager db = MyApp.dbManager;
            final String date = String.valueOf(tv_date.getText());
            final FriendThing thing = new FriendThing(String.valueOf(tv_name.getText()), date, String.valueOf(edt_thing.getText()));

            try {
                db.save(thing);
                final Long id = db.selector(FriendThing.class).orderBy("id", true).findFirst().getId();
                JimoUtil.storeImg(getContext(), adapterForSelectImg.getData(), db, id, MyConst.IMG_TYPE_FRIEND_THING);
                FuckUtil.clearInput((obj) -> adapterForSelectImg.clear(), tv_name, tv_date, edt_thing);
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

    @Event(R.id.iv_select_img)
    private void onSelectImgClick(View view) {
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
                .compress(true).isCamera(true).forResult(PictureConfig.CHOOSE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:

                    final List<LocalMedia> media = PictureSelector.obtainMultipleResult(data);
                    for (LocalMedia m : media) {
                        if (m.isCompressed()) {
                            Log.i("path-compress", m.getCompressPath());
                            adapterForSelectImg.getData().add(m.getCompressPath());
                        } else {
                            Log.i("path", m.getPath());
                            adapterForSelectImg.getData().add(m.getPath());
                        }
                    }
                    adapterForSelectImg.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteCacheDirFile(getContext());
        PictureFileUtils.deleteExternalCacheDirFile(getContext());
        Log.i("destory", "已清除缓存");
    }
}
