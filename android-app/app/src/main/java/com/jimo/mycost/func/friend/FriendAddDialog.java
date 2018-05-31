package com.jimo.mycost.func.friend;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jimo.mycost.MyApp;
import com.jimo.mycost.R;
import com.jimo.mycost.data.model.Friend;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;
import com.jimo.mycost.util.MyCallback;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.concurrent.atomic.AtomicReference;

public class FriendAddDialog extends DialogFragment {

    private MyCallback.CommonCallback callback;

    public void show(FragmentManager manager, MyCallback.CommonCallback callback) {
        this.callback = callback;
        setCancelable(false);
        show(manager, "FriendAddDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_friend_add, null);

        final EditText edt_name = view.findViewById(R.id.edt_name);
        final EditText edt_id_num = view.findViewById(R.id.edt_id_num);
        final EditText edt_contact = view.findViewById(R.id.edt_contact);
        final EditText edt_address = view.findViewById(R.id.edt_address);
        final EditText edt_speciality = view.findViewById(R.id.edt_speciality);
        final EditText edt_school = view.findViewById(R.id.edt_school);
        final EditText edt_height = view.findViewById(R.id.edt_height);
        final EditText edt_weight = view.findViewById(R.id.edt_weight);
        final EditText edt_hobby = view.findViewById(R.id.edt_hobby);
        final EditText edt_relation = view.findViewById(R.id.edt_relation);
        final EditText edt_score = view.findViewById(R.id.edt_score);
        final EditText edt_remark = view.findViewById(R.id.edt_remark);
        final RadioGroup rg_sex = view.findViewById(R.id.rg_sex);
        final TextView tv_birth = view.findViewById(R.id.tv_birthday);
        final Button btn_add = view.findViewById(R.id.btn_friend_add);

        tv_birth.setOnClickListener(v -> FuckUtil.showDateSelectDialog(getContext(), (date) -> tv_birth.setText((CharSequence) date)));

        AtomicReference<String> sex = new AtomicReference<>("Male");
        rg_sex.setOnCheckedChangeListener((group, checkedId) -> {
            final RadioButton rb = view.findViewById(checkedId);
            sex.set(String.valueOf(rb.getText()));
        });

        btn_add.setOnClickListener((v) -> {
            if (FuckUtil.checkInput(getContext(), edt_address, edt_contact, edt_height, edt_hobby,
                    edt_id_num, edt_name, edt_relation, edt_remark,
                    edt_school, edt_score, edt_speciality, edt_weight, tv_birth)) {

                final Friend friend = new Friend(String.valueOf(tv_birth.getText()), String.valueOf(edt_name.getText()),
                        String.valueOf(edt_id_num.getText()), sex.get(), String.valueOf(edt_speciality.getText()),
                        String.valueOf(edt_school.getText()), String.valueOf(edt_address.getText()), String.valueOf(edt_contact.getText()),
                        Float.parseFloat(String.valueOf(edt_height.getText())), Float.parseFloat(String.valueOf(edt_weight.getText())),
                        String.valueOf(edt_hobby.getText()), String.valueOf(edt_relation.getText()), Float.parseFloat(String.valueOf(edt_score.getText())),
                        String.valueOf(edt_remark.getText()));
                final DbManager db = MyApp.dbManager;
                try {
                    db.save(friend);
                    JimoUtil.myToast(getContext(), "save successfully!");
                    FuckUtil.clearInput((obj) -> {
                            }, edt_address, edt_contact, edt_height, edt_hobby,
                            edt_id_num, edt_name, edt_relation, edt_remark,
                            edt_school, edt_score, edt_speciality, edt_weight, tv_birth);
                    callback.doSomething(null);
                } catch (DbException e) {
                    e.printStackTrace();
                    JimoUtil.myToast(getContext(), "save friend error: " + e.getMessage());
                }
            }
        });
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view).setPositiveButton("Close", (dialog, which) -> {
            dismiss();
        });
        builder.setTitle("Friend Add");
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.callback = null;
    }
}
