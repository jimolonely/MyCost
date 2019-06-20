package com.jimo.mycost.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.jimo.mycost.MyApp;
import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.BigSmallType;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 提取的公共代码：创建大小类型列表
 */
public class CreateTypeList {

    private LinearLayout ll_types;
    private Context context;
    private Callback callback;
    private int type;

    public CreateTypeList(LinearLayout ll_types, Context context, Callback callback, int type) {
        this.ll_types = ll_types;
        this.context = context;
        this.callback = callback;
        this.type = type;
    }

    public interface Callback {
        void callback(String bigType, String smallType);
    }

    /**
     * 存储小类别
     */
    public void saveSmallType(String bigType, String smallType) {
        DbManager db = MyApp.dbManager;
        BigSmallType bigSmallType =
                new BigSmallType(bigType, smallType, type, JimoUtil.getDateTimeNow(), MyConst.SYNC_TYPE_INSERT);
        try {
            db.save(bigSmallType);
            setTypes();
        } catch (DbException e) {
            JimoUtil.mySnackbar(ll_types, "保存error:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 从数据库读出开销类型
     *
     * @author jimo
     * @date 18-10-20 上午8:31
     */
    private Map<String, Set<String>> getType() {
        Map<String, Set<String>> types = new HashMap<>();
        DbManager db = MyApp.dbManager;
        try {
            List<BigSmallType> costTypes = db.selector(BigSmallType.class)
                    .where("type", "=", type).findAll();
            if (costTypes == null) {
                return types;
            }
            for (BigSmallType type : costTypes) {
                String bigType = type.getBigType();
                if (types.containsKey(bigType)) {
                    types.get(bigType).add(type.getSmallType());
                } else {
                    Set<String> s = new HashSet<>();
                    s.add(type.getSmallType());
                    types.put(type.getBigType(), s);
                }
            }
        } catch (DbException e) {
            JimoUtil.mySnackbar(ll_types, "load types error");
            e.printStackTrace();
        }
        return types;
    }

    public void setTypes() {
        Map<String, Set<String>> types = getType();
        ll_types.removeAllViews();
        for (Map.Entry<String, Set<String>> entry : types.entrySet()) {
            TextView bigTypeTextView = createBigTypeTextView(entry.getKey());
            FlexboxLayout flexboxLayout = createFlexboxLayout();
            TextView divider = createDivider();
            ll_types.addView(bigTypeTextView);

            for (String s : entry.getValue()) {
                TextView tvv = createSmallTypeTextView(s, (view) -> {
                    if (view instanceof TextView) {
                        TextView tv = (TextView) view;
//                        final String text = entry.getKey() + " " + String.valueOf(tv.getText());
//                        tv_input_type.setText(text);
                        callback.callback(entry.getKey(), String.valueOf(tv.getText()));
                    }
                });
                flexboxLayout.addView(tvv);
            }
            ll_types.addView(flexboxLayout);
            ll_types.addView(divider);
        }
    }

    /**
     * 创建大类的TextView
     *
     * @author jimo
     * @date 18-10-20 上午9:23
     */
    private TextView createBigTypeTextView(String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setGravity(Gravity.START);
        tv.setTextSize(18);
        tv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 60
        ));
        tv.setTextColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.primary_text));
        return tv;
    }

    /**
     * 创建分割线
     *
     * @author jimo
     * @date 18-10-20 上午9:31
     */
    private TextView createDivider() {
        TextView tv = new TextView(context);
        tv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 1
        ));
        tv.setBackgroundColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.divider));
        return tv;
    }

    /**
     * 创建小类的TextView
     *
     * @author jimo
     * @date 18-10-20 上午9:19
     */
    @NonNull
    private TextView createSmallTypeTextView(String s, View.OnClickListener listener) {
        TextView tv = new TextView(context);
        tv.setText(s);
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 5, 10, 5);
        tv.setOnClickListener(listener);
        tv.setTextColor(ContextCompat.getColor(Objects.requireNonNull(context), R.color.secondary_text));
        return tv;
    }

    /**
     * 创建类型的流式布局，里面装TextView
     *
     * @author jimo
     * @date 18-10-20 上午9:15
     */
    private FlexboxLayout createFlexboxLayout() {
        FlexboxLayout flex = new FlexboxLayout(Objects.requireNonNull(context));
        flex.setAlignContent(AlignContent.STRETCH);
        flex.setAlignItems(AlignItems.STRETCH);
        flex.setFlexWrap(FlexWrap.WRAP);
        flex.setJustifyContent(JustifyContent.FLEX_START);
        flex.setLayoutParams(new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return flex;
    }
}
