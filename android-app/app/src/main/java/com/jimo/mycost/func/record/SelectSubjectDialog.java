package com.jimo.mycost.func.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by jimo on 17-11-21.
 * 一个列表选择对话框
 */
public class SelectSubjectDialog extends DialogFragment {

    private String title;

    private String[] items;

    private DialogInterface.OnClickListener onOkClickListener;
    private DialogInterface.OnClickListener onDeleteClickListener;
    private DialogInterface.OnClickListener onClickListener;

    public void show(String title, String[] items,
                     DialogInterface.OnClickListener onClickListener,
                     DialogInterface.OnClickListener onOkClickListener,
                     DialogInterface.OnClickListener onDeleteClickListener,
                     FragmentManager fragmentManager) {
        this.title = title;
        this.items = items;
        this.onClickListener = onClickListener;
        this.onOkClickListener = onOkClickListener;
        this.onDeleteClickListener = onDeleteClickListener;
        show(fragmentManager, "Jimo Dialog Tag");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setSingleChoiceItems(items, 0, onClickListener).
                setPositiveButton("OK", onOkClickListener).setNegativeButton("删除", onDeleteClickListener);
        return builder.create();
    }
}
