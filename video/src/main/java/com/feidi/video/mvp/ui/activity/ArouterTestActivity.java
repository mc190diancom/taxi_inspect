package com.feidi.video.mvp.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.feidi.video.R;
import com.miu30.common.config.Constance;

public class ArouterTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter_test);
        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ArouterTestActivity.this,"点击了",Toast.LENGTH_SHORT).show();
                ARouter.getInstance().build(Constance.ACTIVITY_URL_TEST).navigation();
            }
        });
    }
}
