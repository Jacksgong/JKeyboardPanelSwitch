package cn.dreamtobe.kpswitch.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.dreamtobe.kpswitch.demo.R;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.FullScreenPanelLayout;

/**
 * Created by Jacksgong on 3/26/16.
 */
public class ChattingResolvedFullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_fullscreen_resolved);
        assignViews();

        KeyboardUtil.attach(this, panelRoot);

        plusIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (panelRoot.getVisibility() == View.VISIBLE) {
                    KeyboardUtil.showKeyboard(sendEdt);
                    panelRoot.setVisibility(View.INVISIBLE);
                } else {
                    KeyboardUtil.hideKeyboard(sendEdt);
                    panelRoot.setVisibility(View.VISIBLE);
                }
            }
        });

        sendEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    panelRoot.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });

        contentRyv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    KeyboardUtil.hideKeyboard(sendEdt);
                    panelRoot.setVisibility(View.GONE);
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

    private FullScreenPanelLayout panelRoot;
    private LinearLayout sendMsgLayout;
    private ImageView voiceTextSwitchIv;
    private EditText sendEdt;
    private TextView sendVoiceBtn;
    private ImageView plusIv;
    private TextView sendBtn;
    private RecyclerView contentRyv;


    private void assignViews() {
        contentRyv = (RecyclerView) findViewById(R.id.content_ryv);
        panelRoot = (FullScreenPanelLayout) findViewById(R.id.panel_root);
        sendMsgLayout = (LinearLayout) findViewById(R.id.sendMsgLayout);
        voiceTextSwitchIv = (ImageView) findViewById(R.id.voice_text_switch_iv);
        sendEdt = (EditText) findViewById(R.id.send_edt);
        sendVoiceBtn = (TextView) findViewById(R.id.send_voice_btn);
        plusIv = (ImageView) findViewById(R.id.plus_iv);
        sendBtn = (TextView) findViewById(R.id.send_btn);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (panelRoot.getVisibility() != View.GONE) {
                panelRoot.setVisibility(View.GONE);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
