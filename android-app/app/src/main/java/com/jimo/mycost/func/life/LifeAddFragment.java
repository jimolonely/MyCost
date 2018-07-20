package com.jimo.mycost.func.life;

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

import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.func.common.SelectImgAdapter;
import com.jimo.mycost.data.model.LifeRecord;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.jimo.mycost.MyConst.themeData;

@ContentView(R.layout.fragment_life_add)
public class LifeAddFragment extends Fragment {

    public static final String THEME_MOVIE = "电影";
    public static final String THEME_BOOK = "书籍";

    @ViewInject(R.id.sp_theme_life)
    private Spinner sp_theme;
    @ViewInject(R.id.input_name_life)
    private EditText edt_name;
    @ViewInject(R.id.input_type_life)
    private EditText edt_type;
    @ViewInject(R.id.input_creator_life)
    private EditText edt_author;
    @ViewInject(R.id.input_remark_life)
    private EditText edt_remark;
    @ViewInject(R.id.input_pubdate_life)
    private EditText edt_pubdate;
    @ViewInject(R.id.input_rating_life)
    private EditText edt_rating;
    @ViewInject(R.id.rb_score_life)
    private RatingBar rb_score;
    @ViewInject(R.id.input_comment_life)
    private EditText edt_comment;
    @ViewInject(R.id.input_spend_life)
    private EditText edt_spend;
    @ViewInject(R.id.input_mood_life)
    private EditText edt_mood;
    @ViewInject(R.id.input_time_life)
    private TextView tv_date;

    private ArrayAdapter<String> adapter;
    private String theme = THEME_MOVIE;

    @ViewInject(R.id.rcv_images)
    RecyclerView rcv_imgs;

    private SelectImgAdapter adapterForSelectImg;

    private float myScore = 6;

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
        adapterForSelectImg = new SelectImgAdapter(getContext());
        rcv_imgs.setAdapter(adapterForSelectImg);
    }


    /**
     * https://api.douban.com/v2/book/search?q=寂寞&start=20
     * https://api.douban.com/v2/movie/search?q=西游记
     */

    @Event(R.id.btn_search_life)
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
                //把封面加入图片列表
                x.image().loadFile(result.getImgUrl(), null, new Callback.CacheCallback<File>() {
                    @Override
                    public boolean onCache(File result) {
                        Log.i("cache-img-path", result.getAbsolutePath() + "/" + result.getName());
                        adapterForSelectImg.getData().add(result.getAbsolutePath());
                        adapterForSelectImg.notifyDataSetChanged();
                        return true;
                    }

                    @Override
                    public void onSuccess(File result) {
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }
                });
            });
            dialog.show();
        }
    }

    public interface SearchDataHandle {
        void getData(LifeItemSearchResult result);
    }

    @Event(R.id.btn_submit_life)
    private void submit(View view) {
        if (FuckUtil.checkInput(getContext(), edt_author, edt_comment, edt_name, edt_pubdate, edt_remark, edt_type, edt_rating, edt_spend, edt_mood, tv_date)) {
            final LifeRecord lifeRecord = new LifeRecord(String.valueOf(edt_name.getText()), theme, String.valueOf(edt_type.getText()),
                    String.valueOf(edt_author.getText()), String.valueOf(edt_pubdate.getText()), String.valueOf(edt_remark.getText()),
                    String.valueOf(edt_rating.getText()), myScore, String.valueOf(edt_comment.getText()), String.valueOf(edt_mood.getText()),
                    String.valueOf(edt_spend.getText()), String.valueOf(tv_date.getText()));
            //image store
            final DbManager db = MyApp.dbManager;
            try {
                db.save(lifeRecord);
                final long parentId = db.selector(LifeRecord.class).orderBy("id", true).findFirst().getId();
                JimoUtil.storeImg(getContext(), adapterForSelectImg.getData(), db, parentId, MyConst.IMG_TYPE_LIFE);
                FuckUtil.clearInput((obj) -> {
                    tv_date.setText("点击选择时间");
                    adapterForSelectImg.clear();
                }, edt_author, edt_comment, edt_name, edt_pubdate, edt_remark, edt_type, edt_rating, edt_spend, edt_mood);
                JimoUtil.mySnackbar(view, "保存成功");
            } catch (DbException e) {
                e.printStackTrace();
                JimoUtil.myToast(getContext(), "error: " + e.getMessage());
            }
        }
    }

    @Event(R.id.input_time_life)
    private void chooseTime(View view) {
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
        PictureFileUtils.deleteExternalCacheDirFile(getContext());
        Log.i("destory", "已清除缓存");
    }

}
