package cn.dreamtobe.jkpswitch.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Jacksgong on 15/6/29.
 * <p/>
 * Detail: http://blog.dreamtobe.cn/2015/06/29/keybord-panel-switch/
 */
public class CustomContentRootLayout extends LinearLayout {

    private final static String TAG = "JFrame.CustomContentRootLayout";

    public CustomContentRootLayout(Context context) {
        super(context);
    }

    public CustomContentRootLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomContentRootLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomContentRootLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int mOldHeight = -1;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 由当前布局被键盘挤压，获知，由于键盘的活动，导致布局将要发生变化。
        do {
            final int width = MeasureSpec.getSize(widthMeasureSpec);
            final int height = MeasureSpec.getSize(heightMeasureSpec);

            Log.d(TAG, "onMeasure, width: " + width + " height: " + height);
            if (height < 0) {
                break;
            }

            if (mOldHeight < 0) {
                mOldHeight = height;
                break;
            }

            final int offset = mOldHeight - height;
            mOldHeight = height;

            if (offset >= 0) {
                //键盘弹起 (offset > 0，高度变小)
                Log.d(TAG, "" + offset + " >= 0 break;");
                break;
            }

            final PanelRootLayout bottom = getPanelView(this);

            if (bottom == null) {
                Log.d(TAG, "bottom == null break;");
                break;
            }

            // 检测到真正的 由于键盘收起触发了本次的布局变化
            bottom.setIsNeedHeight(true);

        } while (false);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private PanelRootLayout mPanelRootLayout;

    private PanelRootLayout getPanelView(final View view) {
        if (mPanelRootLayout != null) {
            return mPanelRootLayout;
        }

        if (view instanceof PanelRootLayout) {
            mPanelRootLayout = (PanelRootLayout) view;
            return mPanelRootLayout;
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                PanelRootLayout v = getPanelView(((ViewGroup) view).getChildAt(i));
                if (v != null) {
                    mPanelRootLayout = v;
                    return mPanelRootLayout;
                }
            }
        }

        return null;

    }

    private int maxBottom = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (b >= maxBottom && maxBottom != 0) {
            // 在底部，键盘隐藏状态
            Log.d(TAG, "keybor hiding");
            getPanelView(this).setIsKeybordShowing(false);
        } else if(maxBottom != 0){
            Log.d(TAG, "keybor showing");
            getPanelView(this).setIsKeybordShowing(true);
        }

        if (maxBottom < b) {
            maxBottom = b;
        }

    }
}

