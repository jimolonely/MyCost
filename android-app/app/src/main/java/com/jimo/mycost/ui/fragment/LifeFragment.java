package com.jimo.mycost.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.jimo.mycost.R;
import com.jimo.mycost.adapter.ItemLifeSearchResult;
import com.jimo.mycost.ui.dialog.LifeSearchDialog;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

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
    @ViewInject(R.id.rb_score)
    private RatingBar rb_score;
    @ViewInject(R.id.input_life_comment)
    private EditText edt_comment;

    private ArrayAdapter<String> adapter;
    private String theme = THEME_MOVIE;

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
            //rating
        });
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


}
