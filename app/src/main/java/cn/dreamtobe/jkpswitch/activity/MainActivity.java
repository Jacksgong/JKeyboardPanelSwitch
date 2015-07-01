package cn.dreamtobe.jkpswitch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.dreamtobe.jkpswitch.R;

/**
 * Created by Jacksgong on 15/7/1.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.activity_main_title);

    }

    public void onClickResolved(final View view) {
        startActivity(new Intent(this, JChattingActivity.class));
    }

    public void onClickUnResolved(final View view) {
        // 使用差别只是未使用CustomContentRootLayout与PanelRotLayout 并且在切换的时候未使用PanelRootLayout#setIsHide
        startActivity(new Intent(this, ChattingActivity.class));
    }
}
