package com.jimo.mycost.func.record;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.jimo.mycost.MyConst;
import com.jimo.mycost.R;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 */
@ContentView(R.layout.fragment_record_list)
public class RecordListFragment extends Fragment {

    @ViewInject(R.id.sp_record_path)
    private Spinner sp_path;
    @ViewInject(R.id.btn_record_add_path)
    private Button btn_add_path;

    Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * 拿回音频目录的code
     */
    private final int CODE_PATH = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        initData();
        return view;
    }

    /**
     * 初始化程序
     *
     * @author jimo
     * @date 18-10-7 下午4:39
     */
    private void initData() {
        // 长按弹出路径列表，让我们可以删除
        btn_add_path.setOnLongClickListener(view -> {
        String[] paths = getPathArray();
        String selectPath;
            DeletePathDialog dialog = new DeletePathDialog();
            dialog.show("delete path", paths,
                    (dialogInterface, i) -> {},
                    (dialogInterface, i) -> {
                        // TODO delete
                    },
                    (dialogInterface, i) -> {
                        //do nothing
                    },
                    Objects.requireNonNull(RecordListFragment.this.getActivity()).getFragmentManager()
            );
            return true;
        });
    }

    /**
     * @author jimo
     * @date 18-10-14 上午11:42
     */
    private String[] getPathArray() {
        Set<String> set = getAudioRootDirPath();
        if (set == null) {
            return new String[0];
        }
        String[] paths = new String[set.size()];
        return set.toArray(paths);
    }

    /**
     * 搜索
     *
     * @author jimo
     * @date 18-10-7 下午4:18
     */
    @Event(R.id.btn_record_search)
    private void clickToSearchRecord(View view) {
        Set<String> dirRoots = getAudioRootDirPath();
        if (dirRoots == null) {
            JimoUtil.mySnackbar(view, "请先添加检索路径，你懂的");
            return;
        }
        //TODO search
    }

    /**
     * 添加搜索路径
     *
     * @author jimo
     * @date 18-10-14 上午10:58
     */
    @Event(R.id.btn_record_add_path)
    private void clickToAddPath(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, CODE_PATH);
    }

    /**
     * 获取用户定义的存放录音的目录，可能不止一个
     *
     * @author jimo
     * @date 18-10-7 下午4:23
     */
    private Set<String> getAudioRootDirPath() {
        SharedPreferences preferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        return preferences.getStringSet(MyConst.RECORD_PATH_KEY, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        // 加入选择的路径存起来
        if (requestCode == CODE_PATH) {
            Uri uri = data.getData();
            if (uri != null) {
                String path = uri.getPath() == null ? "" : uri.getPath();
                //取出前面的路径
                path = path.substring(0, path.lastIndexOf("/"));
                SharedPreferences preferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
                Set<String> paths = preferences.getStringSet(MyConst.RECORD_PATH_KEY, null);
                if (paths == null) {
                    paths = new HashSet<>();
                }
                paths.add(path);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putStringSet(MyConst.RECORD_PATH_KEY, paths);
                editor.apply();
                JimoUtil.mySnackbar(sp_path, "添加成功");
            }
        }
    }
}
