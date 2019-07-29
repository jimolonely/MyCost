package com.jimo.mycost.func.cloud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.jimo.mycost.R;
import com.jimo.mycost.data.dto.CloudFileEntry;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import java.util.List;

/**
 * 云文件展示
 *
 * @author jimo
 * @date 19-7-29 下午7:49
 */
public class CloudFileListDialog extends DialogFragment {

    public interface Callback {
        void onOk();
    }

    private Context context;
    private Callback callback;
    private FileEntryAdapter adapter;

    public void show(FragmentManager fragmentManager, Context context, List<CloudFileEntry> entries) {
        this.context = context;
        adapter = new FileEntryAdapter(entries, context);
        show(fragmentManager, "AddUserInfoDialog");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_cloud_file_entry_show, null);
        Button btn = view.findViewById(R.id.btn_sync);
        btn.setOnClickListener((view1) -> {
            // 同步

            // 刷新列表
        });
        ListView lv_file = view.findViewById(R.id.lv_cloud_files);
        lv_file.setAdapter(adapter);

        builder.setView(view);
        builder.setTitle("同步数据库");
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callback = null;
    }
}
