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
    public final static String KEY_IGNORE_RECOMMEND_PANEL_HEIGHT =
            "key.ignore.recommend.panel.height";
    public final static String KEY_MULTI_SUB_PANEL = "key.multi.sub.panel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(R.string.activity_main_title);

        assignViews();

        mThemeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.full_screen_rb) {
                    // full screen theme.
                    mHandleByPlaceholderResolvedBtn.
                            setText(R.string.activity_chatting_fullscreen_resolved_title);
                } else {
                    // translucent status with fitSystemWindows=false theme.
                    mHandleByPlaceholderResolvedBtn.
                            setText(R.string.activity_chatting_translucent_status_false_resolved_title);
                }
            }
        });

        mTranslucentStatusTrueCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // translucent status with fitSystemWindows=true theme.
                    mHandleByDelayResolvedBtn.
                            setText(R.string.activity_chatting_translucent_status_true_resolved_title);
                } else {
                    // normal theme.
                    mHandleByDelayResolvedBtn.setText(R.string.activity_chatting_resolved_title);
                }
            }
        });
    }

    public void onClickResolved(final View view) {
        Intent i = new Intent();
        i.putExtra(KEY_TRANSLUCENT_STATUS_FIT_SYSTEM_WINDOW_TRUE, mTranslucentStatusTrueCb.isChecked());
        i.putExtra(KEY_IGNORE_RECOMMEND_PANEL_HEIGHT, mIgnoreRecommendPanelHeightCb.isChecked());
        i.putExtra(KEY_MULTI_SUB_PANEL, mMultipleSubPanelCb.isChecked());

        final ComponentName componentName;
        if (mAppCompatActivityRb.isChecked()) {
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
        final boolean fullScreenTheme = mFullScreenRb.isChecked();

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

    private RadioButton mAppCompatActivityRb;
    private CheckBox mTranslucentStatusTrueCb;
    private CheckBox mIgnoreRecommendPanelHeightCb;
    private CheckBox mMultipleSubPanelCb;
    private Button mHandleByDelayResolvedBtn;
    private RadioGroup mThemeRg;
    private RadioButton mFullScreenRb;
    private Button mHandleByPlaceholderResolvedBtn;

    private void assignViews() {
        mAppCompatActivityRb = (RadioButton) findViewById(R.id.app_compat_activity_rb);
        mTranslucentStatusTrueCb = (CheckBox) findViewById(R.id.translucent_status_true_cb);
        mIgnoreRecommendPanelHeightCb = (CheckBox) findViewById(R.id.ignore_recommend_panel_height_cb);
        mMultipleSubPanelCb = (CheckBox) findViewById(R.id.multiple_sub_panel_cb);
        mHandleByDelayResolvedBtn = (Button) findViewById(R.id.handle_by_delay_resolved_btn);
        mThemeRg = (RadioGroup) findViewById(R.id.theme_rg);
        mFullScreenRb = (RadioButton) findViewById(R.id.full_screen_rb);
        mHandleByPlaceholderResolvedBtn = (Button) findViewById(R.id.handle_by_placeholder_resolved_btn);
    }


}
