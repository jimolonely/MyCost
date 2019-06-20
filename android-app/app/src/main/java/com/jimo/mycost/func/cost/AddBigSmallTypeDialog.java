package com.jimo.mycost.func.cost;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

/**
 * 添加开销或收入类型的对话框
 *
 * @author jimo
 * @date 18-10-20 上午10:08
 */
public class AddBigSmallTypeDialog extends DialogFragment {

    public interface Callback {
        void onOk(String bigType, String smallType);
    }

    private Callback callback;
    private String[] bigTypes;

    public void show(FragmentManager fragmentManager, String[] bigTypes, Callback callback) {
        this.callback = callback;
        this.bigTypes = bigTypes;
        show(fragmentManager, "AddTypeDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_cost_add_type, null);
        final Spinner sp_types = view.findViewById(R.id.sp_cost_big_type);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, bigTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_types.setAdapter(adapter);

        final String[] typeSelect = {bigTypes[0]};

        sp_types.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                typeSelect[0] = bigTypes[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        builder.setView(view).setPositiveButton("确定", (dialogInterface, i) -> {
            EditText edt_small_type = view.findViewById(R.id.edt_cost_small_type);
            String smallType = edt_small_type.getText().toString();
            if ("".equals(smallType)) {
                JimoUtil.mySnackbar(sp_types, "参数不正确");
                return;
            }
            callback.onOk(typeSelect[0], smallType);
        });
        builder.setTitle("SET TYPE");
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
