package cn.dreamtobe.jkpswitch.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.dreamtobe.jkpswitch.R;
import cn.dreamtobe.jkpswitch.widget.PanelRootLayout;

/**
 * Created by Jacksgong on 15/7/1.
 * <p/>
 * Desc: 适配了 Panel<->Keybord 切换冲突
 */
public class JChattingActivity extends FragmentActivity {

    private RecyclerView mContentRyv;
    private LinearLayout mSendMsgLayout;
    private ImageView mVoiceTextSwitchIv;
    private EditText mSendEdt;
    private TextView mSendVoiceBtn;
    private ImageView mPlusIv;
    private TextView mSendBtn;
    private PanelRootLayout mPanelRoot;
    private ImageView mSendImgIv;
    private ImageView mSendSiteIv;

    private void assignViews() {
        mContentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        mSendMsgLayout = (LinearLayout) findViewById(R.id.sendMsgLayout);
        mVoiceTextSwitchIv = (ImageView) findViewById(R.id.voice_text_switch_iv);
        mSendEdt = (EditText) findViewById(R.id.send_edt);
        mSendVoiceBtn = (TextView) findViewById(R.id.send_voice_btn);
        mPlusIv = (ImageView) findViewById(R.id.plus_iv);
        mSendBtn = (TextView) findViewById(R.id.send_btn);
        mPanelRoot = (PanelRootLayout) findViewById(R.id.panel_root);
        mSendImgIv = (ImageView) findViewById(R.id.send_img_iv);
        mSendSiteIv = (ImageView) findViewById(R.id.send_site_iv);
    }


    public void onClickPlusIv(final View view) {
        if (mPanelRoot.getVisibility() == View.VISIBLE) {
            showKeybord();
        } else {
            hideKeybord();
            mPanelRoot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        setTitle(R.string.activity_jchatting_title);

        assignViews();

        mContentRyv.setLayoutManager(new LinearLayoutManager(this));

        mSendEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mPanelRoot.setIsHide(true);
                }
            }
        });

        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    hideKeybord();
                    mPanelRoot.setVisibility(View.GONE);
                }

                return false;
            }
        });
    }

    private void showKeybord() {
        mSendEdt.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) mSendEdt.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mSendEdt, 0);
    }

    public void hideKeybord() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mSendEdt.clearFocus();
        imm.hideSoftInputFromWindow(mSendEdt.getWindowToken(), 0);
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
