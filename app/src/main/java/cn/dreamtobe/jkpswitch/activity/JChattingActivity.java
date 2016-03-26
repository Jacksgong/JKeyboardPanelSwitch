package cn.dreamtobe.jkpswitch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import cn.dreamtobe.jkpswitch.R;
import cn.dreamtobe.jkpswitch.activity.utils.TranslucentActivity;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.CustomRootLayout;
import cn.dreamtobe.kpswitch.widget.PanelLayout;

/**
 * Created by Jacksgong on 15/7/1.
 * <p/>
 * Desc: 适配了 Panel<->Keyboard 切换冲突
 */
public class JChattingActivity extends AppCompatActivity {

    private static final String TAG = "JChattingActivity";
    private CustomRootLayout mRootView;
    private RecyclerView mContentRyv;
    private EditText mSendEdt;
    private PanelLayout mPanelRoot;

    private void assignViews() {
        mRootView = (CustomRootLayout) findViewById(R.id.rootView);
        mContentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        mSendEdt = (EditText) findViewById(R.id.send_edt);
        mPanelRoot = (PanelLayout) findViewById(R.id.panel_root);
    }


    public void onClickPlusIv(final View view) {
        if (mPanelRoot.getVisibility() == View.VISIBLE) {
            KeyboardUtil.showKeyboard(mSendEdt);
        } else {
            KeyboardUtil.hideKeyboard(mSendEdt);
            mPanelRoot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        assignViews();
        KeyboardUtil.attach(this, mPanelRoot);

        // Add keyboard showing state callback, do like this when you want to listen in the keyboard's show/hide change.
        mRootView.setOnKeyboardShowingListener(new CustomRootLayout.OnKeyboardShowingListener() {
            @Override
            public void onKeyboardShowing(boolean isShowing) {
                Log.d(TAG, String.format("Keyboard is %s", isShowing ? "showing" : "hiding"));
            }
        });

        mContentRyv.setLayoutManager(new LinearLayoutManager(this));

        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    KeyboardUtil.hideKeyboard(mSendEdt);
                    mPanelRoot.setVisibility(View.GONE);
                }

                return false;
            }
        });
    }



    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelRoot.getVisibility() == View.VISIBLE) {
                mPanelRoot.setVisibility(View.GONE);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void onClickPhoto(final View view) {
        startActivity(new Intent(this, TranslucentActivity.class));
    }
}