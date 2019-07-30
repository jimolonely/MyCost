package com.jimo.mycost.func.cloud;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.data.dto.CloudFileEntry;
import com.jimo.mycost.util.JimoUtil;
import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 云文件展示
 *
 * @author jimo
 * @date 19-7-29 下午7:49
 */
public class CloudFileListDialog extends DialogFragment {


    private Context context;
    private FileEntryAdapter adapter;
    private List<CloudFileEntry> entries;

    public void show(FragmentManager fragmentManager, Context context) {
        this.context = context;
        entries = new ArrayList<>();
        adapter = new FileEntryAdapter(entries, context);
        show(fragmentManager, "AddUserInfoDialog");
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View view = layoutInflater.inflate(R.layout.dialog_cloud_file_entry_show, null);
        Button btn = view.findViewById(R.id.btn_sync);
        btn.setOnClickListener((view1) -> {
            JimoUtil.myToast(context, "开始同步");
            // 同步
            new SyncDbToCloudTask().execute(MyConst.getCloudUserName(context),
                    MyConst.getCloudUserPass(context));
        });
        ListView lv_file = view.findViewById(R.id.lv_cloud_files);
        lv_file.setAdapter(adapter);

        builder.setView(view);
        builder.setTitle("同步数据库");
        JimoUtil.myToast(context, "开始获取列表");
        new ListCloudFileTask().execute(MyConst.getCloudUserName(context),
                MyConst.getCloudUserPass(context));
        return builder.create();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class ListCloudFileTask extends AsyncTask<String, Integer, List<CloudFileEntry>> {

        @Override
        protected List<CloudFileEntry> doInBackground(String... s) {
            Sardine sardine = new OkHttpSardine();
            sardine.setCredentials(s[0], s[1]);

            try {
                if (!sardine.exists(MyConst.CLOUD_DB_PATH)) {
                    sardine.createDirectory(MyConst.CLOUD_DB_PATH);
                }
                List<DavResource> resources = sardine.list(MyConst.CLOUD_DB_PATH);
                List<CloudFileEntry> list = new ArrayList<>();
                for (DavResource r : resources) {
                    String name = r.getName();
                    if (name.startsWith("mycost.db")) {
                        long size = r.getContentLength() / 1024;
                        String time = JimoUtil.formatDate(r.getModified(), "yyyy-MM-dd HH:mm:ss");
                        list.add(new CloudFileEntry(name, size, time));
                    }
                }
                return list;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<CloudFileEntry> entries) {
            super.onPostExecute(entries);
            System.out.println(entries.size());
            CloudFileListDialog.this.entries.clear();
            CloudFileListDialog.this.entries.addAll(entries);
            adapter.notifyDataSetChanged();
        }
    }

    private class SyncDbToCloudTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... s) {
            Sardine sardine = new OkHttpSardine();
            sardine.setCredentials(s[0], s[1]);

            String[] files = {"mycost.db", "mycost.db-shm", "mycost.db-wal"};
            try {
                for (String file : files) {
                    byte[] data = Files.readAllBytes(Paths.get("/mnt/sdcard/" + file));
                    sardine.put(MyConst.CLOUD_DB_PATH + "/" + file, data);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
//                JimoUtil.myToast(context, "文件不存在：" + e.getMessage());
                return null;
            } catch (IOException e) {
                e.printStackTrace();
//                JimoUtil.myToast(context, "IO异常：" + e.getMessage());
                return null;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            JimoUtil.myToast(context, "同步成功");
            new ListCloudFileTask().execute(MyConst.getCloudUserName(context),
                    MyConst.getCloudUserPass(context));
        }
    }
}
