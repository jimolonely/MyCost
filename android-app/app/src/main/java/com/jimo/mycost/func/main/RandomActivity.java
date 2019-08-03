package com.jimo.mycost.func.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.jimo.mycost.R;
import com.jimo.mycost.util.FuckUtil;
import com.jimo.mycost.util.JimoUtil;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author jimo
 * @date 19-8-3 上午9:55
 */
@ContentView(R.layout.activity_random)
public class RandomActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_random_result)
    private EditText edt_result;
    @ViewInject(R.id.tv_random_show)
    private TextView tv_show;
    @ViewInject(R.id.swt_random_fix_length)
    private Switch swt_fix_len;
    @ViewInject(R.id.sbk_len)
    private SeekBar skb_len;
    @ViewInject(R.id.sbk_num)
    private SeekBar skb_num;
    /**
     * 开始结束数字，由选择决定
     */
    private int startNum = 0;
    private int endNum = 10;
    /**
     * 随机数个数
     */
    private int num = 1;

    private boolean fixLen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        init();
    }

    private void init() {
        swt_fix_len.setOnCheckedChangeListener((compoundButton, b) -> {
            fixLen = b;
        });

        skb_len.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                startNum = fixLen ? (progress - 1) * 10 : 0;
                endNum = progress * 10;
                String info = "正在选择起始数字，start: " + startNum + ", end: " + endNum;
                tv_show.setText(info);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setInfo();
            }
        });

        skb_num.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                num = progress;
                String info = "正在选择个数: " + num;
                tv_show.setText(info);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setInfo();
            }
        });
    }

    private void setInfo() {
        String info = "起始数字【start: " + startNum + ", end: " + endNum + "】,个数：" + num;
        tv_show.setText(info);
    }

    public void closeActivity(View view) {
        this.finish();
    }

    /**
     * <p>
     * 生成随机数：
     * 1. 根据起始限制生成
     * 2. 填充结果
     * </p >
     *
     * @author jimo
     * @date 19-8-3 上午10:47
     */
    public void generate(View view) {
        if (!isValid()) {
            JimoUtil.myToast(this, "无法产生足够多的随机数");
            return;
        }
        Set<String> randoms = new HashSet<>(num);
        Random r = new Random();
        int diff = endNum - startNum;
        while (randoms.size() < num) {
            int i = r.nextInt(diff);
            randoms.add((startNum + i) + "");
        }
        // 构造结果
        String results = String.join(" ", randoms);
        edt_result.setText(results);
    }

    /**
     * <p>
     * 判断在起始数字范围能否产生那么多随机数
     * </p >
     *
     * @author jimo
     * @date 19-8-3 上午10:52
     */
    private boolean isValid() {
        return num > 0 && endNum - startNum >= num;
    }

    /**
     * <p>
     * 高级选项：弹框让用户输入起始数字
     * </p >
     *
     * @author jimo
     * @date 19-8-3 上午10:55
     */
    public void highOption(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_random_input_start_end_num, null);
        EditText edt_start = view.findViewById(R.id.edt_start_num);
        EditText edt_end = view.findViewById(R.id.edt_end_num);

        edt_start.setText(startNum + "");
        edt_end.setText(endNum + "");
        builder.setView(view);

        builder.setPositiveButton("确定", (dialogInterface, i) -> {
            if (!FuckUtil.checkInput(RandomActivity.this, edt_end, edt_start)) {
                return;
            }
            int start = Integer.parseInt(edt_start.getText().toString());
            int end = Integer.parseInt(edt_end.getText().toString());
            startNum = start;
            endNum = end;
            if (start >= end) {
                JimoUtil.mySnackbar(tv_show, "开始不能大于结束");
                return;
            }
            setInfo();
        });
        builder.create();
        builder.show();
    }
}
