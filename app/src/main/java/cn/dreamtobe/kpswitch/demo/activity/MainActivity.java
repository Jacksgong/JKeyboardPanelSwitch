package cn.dreamtobe.kpswitch.demo.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.dreamtobe.kpswitch.demo.R;

/**
 * Created by Jacksgong on 15/7/1.
 */
public class MainActivity extends AppCompatActivity {

    public final static String KEY_FULL_SCREEN_THEME = "key.theme.fullscreen";
    public final static String KEY_TRANSLUCENT_STATUS_FIT_SYSTEM_WINDOW_TRUE =
            "key.theme.translucent.status.fitSystemWindow.true";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.activity_main_title);

        assignViews();

        themeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.full_screen_rb) {
                    // full screen theme.
                    handleByPlaceholderResolvedBtn.
                            setText(R.string.activity_chatting_fullscreen_resolved_title);
                } else {
                    // translucent status with fitSystemWindows=false theme.
                    handleByPlaceholderResolvedBtn.
                            setText(R.string.activity_chatting_translucent_status_false_resolved_title);
                }
            }
        });

        translucentStatusTrueCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // translucent status with fitSystemWindows=true theme.
                    handleByDelayResolvedBtn.
                            setText(R.string.activity_chatting_translucent_status_true_resolved_title);
                } else {
                    // normal theme.
                    handleByDelayResolvedBtn.setText(R.string.activity_chatting_resolved_title);
                }
            }
        });
    }

    public void onClickResolved(final View view) {
        Intent i = new Intent();
        i.putExtra(KEY_TRANSLUCENT_STATUS_FIT_SYSTEM_WINDOW_TRUE, translucentStatusTrueCb.isChecked());

        final ComponentName componentName;
        if (appCompatActivityRb.isChecked()) {
             componentName = new ComponentName(this, ChattingResolvedActivity.class);
        } else {
            componentName = new ComponentName(this, ChattingResolvedFragmentActivity.class);
        }
        i.setComponent(componentName);

        startActivity(i);
    }

    /**
     * Resolved for Full Screen Theme or Translucent Status Theme.
     */
    public void onClickExtraThemeResolved(final View view) {
        final boolean fullScreenTheme = fullScreenRb.isChecked();

        Intent i = new Intent(this, ChattingResolvedHandleByPlaceholderActivity.class);
        i.putExtra(KEY_FULL_SCREEN_THEME, fullScreenTheme);
        startActivity(i);
    }


    public void onClickUnResolved(final View view) {
        startActivity(new Intent(this, ChattingUnresolvedActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_github:
                openGitHub();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openGitHub() {
        Uri uri = Uri.parse(getString(R.string.app_github_url));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private RadioButton appCompatActivityRb;
    private CheckBox translucentStatusTrueCb;
    private Button handleByDelayResolvedBtn;
    private RadioGroup themeRg;
    private RadioButton fullScreenRb;
    private Button handleByPlaceholderResolvedBtn;

    private void assignViews() {
        appCompatActivityRb = (RadioButton) findViewById(R.id.app_compat_activity_rb);
        translucentStatusTrueCb = (CheckBox) findViewById(R.id.translucent_status_true_cb);
        handleByDelayResolvedBtn = (Button) findViewById(R.id.handle_by_delay_resolved_btn);
        themeRg = (RadioGroup) findViewById(R.id.theme_rg);
        fullScreenRb = (RadioButton) findViewById(R.id.full_screen_rb);
        handleByPlaceholderResolvedBtn = (Button) findViewById(R.id.handle_by_placeholder_resolved_btn);
    }


}
