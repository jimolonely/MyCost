package com.jimo.mycost.func.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * 删除path的对话框
 *
 * @author jimo
 * @date 18-10-14 上午11:29
 */
public class DeletePathDialog extends DialogFragment {

    private String title;

    private String[] items;

    private DialogInterface.OnClickListener onDeleteClickListener;
    private DialogInterface.OnClickListener onCancelClickListener;
    private DialogInterface.OnClickListener onSelectListener;

    public void show(String title, String[] items,
                     DialogInterface.OnClickListener onSelectListener,
                     DialogInterface.OnClickListener onDeleteClickListener,
                     DialogInterface.OnClickListener onCancelClickListener,
                     FragmentManager fragmentManager) {
        this.title = title;
        this.items = items;
        this.onSelectListener = onSelectListener;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onCancelClickListener = onCancelClickListener;
        show(fragmentManager, "Jimo Dialog Tag2");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setSingleChoiceItems(items, 0, onSelectListener).
                setPositiveButton("delete", onDeleteClickListener).setNegativeButton("cancel", onCancelClickListener);
        return builder.create();
    }
}
