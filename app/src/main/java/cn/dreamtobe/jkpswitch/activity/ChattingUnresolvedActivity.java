package cn.dreamtobe.jkpswitch.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import cn.dreamtobe.jkpswitch.R;

/**
 * Created by Jacksgong on 15/7/1.
 * <p/>
 * Desc: 未适配 Panel<->Keyboard 切换冲突
 */
public class ChattingUnresolvedActivity extends AppCompatActivity {

    private RecyclerView mContentRyv;
    private EditText mSendEdt;
    private LinearLayout mPanelRoot;

    private void assignViews() {
        mContentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        mSendEdt = (EditText) findViewById(R.id.send_edt);
        mPanelRoot = (LinearLayout) findViewById(R.id.panel_root);
    }


    public void onClickPlusIv(final View view) {
        if (mPanelRoot.getVisibility() == View.VISIBLE) {
            showKeyboard();
        } else {
            hideKeyboard();
            mPanelRoot.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_unresolved);

        assignViews();

        mContentRyv.setLayoutManager(new LinearLayoutManager(this));

        mSendEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mPanelRoot.setVisibility(View.GONE);
                }
            }
        });

        mContentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    hideKeyboard();
                    mPanelRoot.setVisibility(View.GONE);
                }

                return false;
            }
        });
    }

    private void showKeyboard() {
        mSendEdt.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) mSendEdt.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mSendEdt, 0);
    }

    private void hideKeyboard() {
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
