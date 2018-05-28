package com.jimo.mycost.func.body;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.BodyData;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import static com.jimo.mycost.MyConst.bodyData;
import static com.jimo.mycost.MyConst.bodyDataUnit;

/**
 * Created by jimo on 18-2-24.
 * 记录身体数据
 */
@ContentView(R.layout.fragment_body_data)
public class BodyDataFragment extends Fragment {

    @ViewInject(R.id.sp_body)
    Spinner sp_body;
    @ViewInject(R.id.input_date)
    TextView tv_date;
    @ViewInject(R.id.input_body)
    EditText edt_data;
    @ViewInject(R.id.tv_unit)
    TextView tv_unit;

    private ArrayAdapter<String> adapter;
    private String bodyPart;
    private String date;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    private void initData() {
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, bodyData);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_body.setAdapter(adapter);
        sp_body.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bodyPart = adapter.getItem(i);
                tv_unit.setText(bodyDataUnit[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Event(R.id.input_date)
    private void dateClick(View view) {
        FuckUtil.showDateSelectDialog(getContext(), (date) -> tv_date.setText(String.valueOf(date)));
    }

    @Event(R.id.btn_finish)
    private void finishClick(View view) {
        if (checkInput(view)) {
            try {
                float data = Float.parseFloat(String.valueOf(edt_data.getText()));
                String unit = String.valueOf(tv_unit.getText());
                storeData(data, unit);
                JimoUtil.mySnackbar(view, "保存成功");
                clearInput();
            } catch (DbException e1) {
                JimoUtil.mySnackbar(view, "存储异常");
            } catch (Exception e) {
                Snackbar.make(view, "error", Snackbar.LENGTH_SHORT).show();
            }
//            if (localStore(view)) {
//            }
        }
    }

    private void clearInput() {
        edt_data.setText("");
        tv_unit.setText("");
        tv_date.setText("");
    }

    /**
     * 存储数据
     *
     * @param data
     * @param unit
     */
    private void storeData(float data, String unit) throws DbException {
        final DbManager dbManager = MyApp.dbManager;
        final BodyData bodyData = new BodyData(bodyPart, data, date, unit);
        dbManager.save(bodyData);
    }

    private boolean checkInput(View view) {
        return !(TextUtils.isEmpty(edt_data.getText())
                || TextUtils.isEmpty(tv_unit.getText())
                || TextUtils.isEmpty(tv_date.getText()));
    }
}
