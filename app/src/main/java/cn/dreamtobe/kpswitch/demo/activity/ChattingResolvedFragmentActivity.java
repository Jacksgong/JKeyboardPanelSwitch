package cn.dreamtobe.kpswitch.demo.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.dreamtobe.kpswitch.demo.R;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

/**
 * Created by Jacksgong on 15/7/1.
 * <p/>
 * For FragmentActivity/Activity. (Just for test(because has different view hierarchy, but has
 * already handled internal), all calls are identical to the {@link ChattingResolvedActivity}).
 * <p/>
 * For resolving the conflict by delay the visible or gone of panel.
 * <p/>
 * In case of Normal(not fullscreen) Theme.
 * In case of Translucent Status Theme with the {@code getFitSystemWindow()} is true in root view.
 */
public class ChattingResolvedFragmentActivity extends FragmentActivity {

    private static final String TAG = "ResolvedActivity";
    private RecyclerView mContentRyv;
    private EditText mSendEdt;
    private KPSwitchPanelLinearLayout mPanelRoot;
    private TextView mSendImgTv;
    private ImageView mPlusIv;

    private void assignViews() {
        mContentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        mSendEdt = (EditText) findViewById(R.id.send_edt);
        mPanelRoot = (KPSwitchPanelLinearLayout) findViewById(R.id.panel_root);
        mSendImgTv = (TextView) findViewById(R.id.send_img_tv);
        mPlusIv = (ImageView) findViewById(R.id.plus_iv);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void adaptTheme(final boolean isTranslucentStatusFitSystemWindowTrue) {
        if (isTranslucentStatusFitSystemWindowTrue
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void adaptTitle(final boolean isTranslucentStatusFitSystemWindowTrue) {
        if (isTranslucentStatusFitSystemWindowTrue) {
            setTitle(R.string.activity_chatting_translucent_status_true_resolved_title);
        } else {
            setTitle(R.string.activity_chatting_resolved_title);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void adaptFitsSystemWindows(final boolean isTranslucentStatusFitSystemWindowTrue) {
        if (isTranslucentStatusFitSystemWindowTrue
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            findViewById(R.id.rootView).setFitsSystemWindows(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ********* Below code Just for Demo Test, do not need to adapt in your code. ************
        final boolean isTranslucentStatusFitSystemWindowTrue = getIntent().
                getBooleanExtra(MainActivity.KEY_TRANSLUCENT_STATUS_FIT_SYSTEM_WINDOW_TRUE, false);
        adaptTheme(isTranslucentStatusFitSystemWindowTrue);

        setContentView(R.layout.activity_chatting_resolved);

        adaptFitsSystemWindows(isTranslucentStatusFitSystemWindowTrue);

        adaptTitle(isTranslucentStatusFitSystemWindowTrue);

        assignViews();

        if (getIntent().getBooleanExtra(MainActivity.KEY_IGNORE_RECOMMEND_PANEL_HEIGHT, false)) {
            mPanelRoot.setIgnoreRecommendHeight(true);
        }
        // ********* Above code Just for Demo Test, do not need to adapt in your code. ************

        KeyboardUtil.attach(this, mPanelRoot,
                // Add keyboard showing state callback, do like this when you want to listen in the
                // keyboard's show/hide change.
                new KeyboardUtil.OnKeyboardShowingListener() {
                    @Override
                    public void onKeyboardShowing(boolean isShowing) {
                        Log.d(TAG, String.format("Keyboard is %s", isShowing
                                ? "showing" : "hiding"));
                    }
                });

        KPSwitchConflictUtil.attach(mPanelRoot, mPlusIv, mSendEdt,
                new KPSwitchConflictUtil.SwitchClickListener() {
                    @Override
                    public void onClickSwitch(boolean switchToPanel) {
                        if (switchToPanel) {
                            mSendEdt.clearFocus();
                        } else {
                            mSendEdt.requestFocus();
                        }
                    }
                });

        mSendImgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mock start the translucent full screen activity.
                startActivity(new Intent(ChattingResolvedFragmentActivity.this,
                        TranslucentActivity.class));
            }
        });

        mContentRyv.setLayoutManager(new LinearLayoutManager(this));

        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
                }

                return false;
            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && mPanelRoot.getVisibility() == View.VISIBLE) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelRoot);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}