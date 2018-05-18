package com.jimo.mycost.ui.fragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jimo.mycost.R;
import com.jimo.mycost.adapter.ItemLifeSearchResult;
import com.jimo.mycost.adapter.SelectImgAdapter;
import com.jimo.mycost.model.LifeRecord;
import com.jimo.mycost.ui.dialog.LifeSearchDialog;
import com.jimo.mycost.util.JimoUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.jimo.mycost.MyConst.themeData;

@ContentView(R.layout.fragment_life)
public class LifeFragment extends Fragment {

    public static final String THEME_MOVIE = "电影";
    public static final String THEME_BOOK = "书籍";

    @ViewInject(R.id.sp_life_theme)
    private Spinner sp_theme;
    @ViewInject(R.id.input_life_name)
    private EditText edt_name;
    @ViewInject(R.id.input_life_type)
    private EditText edt_type;
    @ViewInject(R.id.input_life_creator)
    private EditText edt_author;
    @ViewInject(R.id.input_life_remark)
    private EditText edt_remark;
    @ViewInject(R.id.input_life_pubdate)
    private EditText edt_pubdate;
    @ViewInject(R.id.input_life_rating)
    private EditText edt_rating;
    @ViewInject(R.id.rb_life_score)
    private RatingBar rb_score;
    @ViewInject(R.id.input_life_comment)
    private EditText edt_comment;
    @ViewInject(R.id.input_life_spend)
    private EditText edt_spend;
    @ViewInject(R.id.input_life_mood)
    private EditText edt_mood;

    private ArrayAdapter<String> adapter;
    private String theme = THEME_MOVIE;

    @ViewInject(R.id.rcv_images)
    RecyclerView rcv_imgs;

    private SelectImgAdapter adapterForSelectImg;
    private List<String> imgPath = new ArrayList<>();

    private float myScore = 6;
    private int hour, minute;
    private String watchTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, themeData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_theme.setAdapter(adapter);
        sp_theme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                theme = adapter.getItem(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        rb_score.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            myScore = rating;
        });

        rcv_imgs.setLayoutManager(new GridLayoutManager(getContext(), 3));
        adapterForSelectImg = new SelectImgAdapter(getContext(), imgPath);
        rcv_imgs.setAdapter(adapterForSelectImg);
    }


    /**
     * https://api.douban.com/v2/book/search?q=寂寞&start=20
     * https://api.douban.com/v2/movie/search?q=西游记
     */

    @Event(R.id.btn_life_search)
    private void openSearch(View view) {
        String keyword = String.valueOf(edt_name.getText());
        if (!TextUtils.isEmpty(keyword)) {
            final LifeSearchDialog dialog = new LifeSearchDialog(getContext(), keyword, theme, result -> {
                edt_author.setText(result.getCreators());
                edt_name.setText(result.getTitle());
                edt_pubdate.setText(result.getPubdate());
                edt_rating.setText(result.getRating());
                edt_remark.setText(result.getRemark());
                edt_type.setText(result.getType());
            });
            dialog.show();
        }
    }

    public interface SearchDataHandle {
        void getData(ItemLifeSearchResult result);
    }

    @Event(R.id.btn_life_finish)
    private void submit(View view) {
        if (checkInput(edt_author, edt_comment, edt_name, edt_pubdate, edt_remark, edt_type, edt_rating, edt_spend, edt_mood)) {
            //
            final LifeRecord lifeRecord = new LifeRecord(String.valueOf(edt_name.getText()), theme, String.valueOf(edt_type.getText()),
                    String.valueOf(edt_author.getText()), String.valueOf(edt_pubdate.getText()), String.valueOf(edt_remark.getText()),
                    String.valueOf(edt_rating.getText()), myScore, String.valueOf(edt_comment.getText()), String.valueOf(edt_mood.getText()),
                    String.valueOf(edt_spend.getText()), watchTime);
            //image store

            //store to db
        }
    }

    @Event(R.id.input_life_date)
    private void chooseTime(View view) {
        TextView tv_date = (TextView) view;
        final Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        new TimePickerDialog(getContext(), (view1, hourOfDay, minute) -> {
            watchTime = hourOfDay + ":" + minute;
            tv_date.setText(watchTime);
        }, hour, minute, true).show();
    }

    private boolean checkInput(EditText... edts) {
        for (EditText edt : edts) {
            if (TextUtils.isEmpty(edt.getText())) {
                JimoUtil.myToast(getContext(), "不能为空");
                return false;
            }
        }
        return true;
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
                            imgPath.add(m.getCompressPath());
                        } else {
                            Log.i("path", m.getPath());
                            imgPath.add(m.getPath());
                        }
                    }
                    adapterForSelectImg.setData(imgPath);
                    adapterForSelectImg.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PictureFileUtils.deleteExternalCacheDirFile(getContext());
        Log.i("destory", "已清除缓存");
    }

}
