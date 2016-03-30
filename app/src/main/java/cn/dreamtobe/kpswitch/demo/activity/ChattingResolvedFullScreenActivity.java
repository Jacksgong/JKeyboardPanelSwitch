package cn.dreamtobe.kpswitch.demo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import cn.dreamtobe.kpswitch.demo.R;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelLinearLayout;

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
        KPSwitchConflictUtil.attach(panelRoot, plusIv, sendEdt);

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
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (panelRoot.getVisibility() != View.GONE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(panelRoot);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
