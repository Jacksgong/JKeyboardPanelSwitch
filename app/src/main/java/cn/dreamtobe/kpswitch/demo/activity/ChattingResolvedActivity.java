package cn.dreamtobe.kpswitch.demo.activity;

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
import android.widget.TextView;

import cn.dreamtobe.kpswitch.demo.R;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.CustomRootLayout;
import cn.dreamtobe.kpswitch.widget.PanelLayout;

/**
 * Created by Jacksgong on 15/7/1.
 * <p/>
 * Desc: 适配了 Panel<->Keyboard 切换冲突
 */
public class ChattingResolvedActivity extends AppCompatActivity {

    private static final String TAG = "JChattingActivity";
    private CustomRootLayout mRootView;
    private RecyclerView mContentRyv;
    private EditText mSendEdt;
    private PanelLayout mPanelRoot;
    private TextView sendImgTv;

    private void assignViews() {
        mRootView = (CustomRootLayout) findViewById(R.id.rootView);
        mContentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        mSendEdt = (EditText) findViewById(R.id.send_edt);
        mPanelRoot = (PanelLayout) findViewById(R.id.panel_root);
        sendImgTv = (TextView) findViewById(R.id.send_img_tv);
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
        setContentView(R.layout.activity_chatting_resolved);

        assignViews();
        KeyboardUtil.attach(this, mPanelRoot);

        sendImgTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mock start the translucent full screen activity.
                startActivity(new Intent(ChattingResolvedActivity.this, TranslucentActivity.class));
            }
        });

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

}