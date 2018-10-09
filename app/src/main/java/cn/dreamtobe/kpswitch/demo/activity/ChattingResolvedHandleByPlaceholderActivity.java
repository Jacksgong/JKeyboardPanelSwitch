package cn.dreamtobe.kpswitch.demo.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import cn.dreamtobe.kpswitch.demo.R;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelLinearLayout;

/**
 * Created by Jacksgong on 3/26/16.
 * <p/>
 * For resolving the conflict by showing the panel placeholder.
 * <p/>
 * In case of FullScreen Theme.
 * In case of Translucent Status Theme with the {@code getFitSystemWindow()} is false in root view.
 */
public class ChattingResolvedHandleByPlaceholderActivity extends AppCompatActivity {

    private void adaptTitle(final boolean isFullScreenTheme) {
        if (isFullScreenTheme) {
            setTitle(R.string.activity_chatting_fullscreen_resolved_title);
        } else {
            setTitle(R.string.activity_chatting_translucent_status_false_resolved_title);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void adaptTheme(final boolean isFullScreenTheme) {
        if (isFullScreenTheme) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ********* Below code Just for Demo Test, do not need to adapt in your code. ************
        final boolean isFullScreenTheme = getIntent().
                getBooleanExtra(MainActivity.KEY_FULL_SCREEN_THEME, false);

        adaptTheme(isFullScreenTheme);

        setContentView(R.layout.activity_chatting_fullscreen_resolved);
        assignViews();

        adaptTitle(isFullScreenTheme);

        if (!isFullScreenTheme) {
            // For present the theme: Translucent Status and FitSystemWindow is True.
            contentRyv.setBackgroundColor(getResources().
                    getColor(R.color.abc_search_url_text_normal));
        }
        // ********* Above code Just for Demo Test, do not need to adapt in your code. ************

        KeyboardUtil.attach(this, panelRoot);
        KPSwitchConflictUtil.attach(panelRoot, plusIv, sendEdt,
                new KPSwitchConflictUtil.SwitchClickListener() {
                    @Override
                    public void onClickSwitch(View v, boolean switchToPanel) {
                        if (switchToPanel) {
                            sendEdt.clearFocus();
                        } else {
                            sendEdt.requestFocus();
                        }
                    }
                });

        contentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(panelRoot);
                }
                return false;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        panelRoot.recordKeyboardStatus(getWindow());
    }

    private KPSwitchFSPanelLinearLayout panelRoot;
    private EditText sendEdt;
    private ImageView plusIv;
    private RecyclerView contentRyv;


    private void assignViews() {
        contentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        panelRoot = (KPSwitchFSPanelLinearLayout) findViewById(R.id.panel_root);
        sendEdt = (EditText) findViewById(R.id.send_edt);
        plusIv = (ImageView) findViewById(R.id.plus_iv);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && panelRoot.getVisibility() != View.GONE) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(panelRoot);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    // 当屏幕分屏/多窗口变化时回调
    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        KPSwitchConflictUtil.onMultiWindowModeChanged(isInMultiWindowMode);
    }
}
