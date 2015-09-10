package cn.dreamtobe.jkpswitch.activity.utils;

import android.app.Activity;
import android.os.Bundle;

import cn.dreamtobe.jkpswitch.R;

/**
 * Created by Jacksgong on 9/11/15.
 */
public class TranslucentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translucent);
    }
}
