package cn.dreamtobe.jkpswitch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cn.dreamtobe.jkpswitch.util.KeyboardUtil;


/**
 * Copyright 2015 Jacks Blog(blog.dreamtobe.cn).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * Created by Jacksgong on 15/6/29.
 * <p/>
 * Detail: http://blog.dreamtobe.cn/2015/06/29/keybord-panel-switch/
 *
 * @see CustomRootLayout
 */
public class PanelLayout extends LinearLayout {

    private boolean mIsHide = false;
    private boolean mIsShow = true;

    private final static String TAG = "JFrame.InputPanelLayout";

    public PanelLayout(Context context) {
        super(context);
        init();
    }

    public PanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        refreshHeight();
    }

    public void refreshHeight() {
        if (isInEditMode()) {
            return;
        }

        if (getHeight() == KeyboardUtil.getValidPanelHeight()) {
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();
                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, KeyboardUtil.getValidPanelHeight());
                } else {
                    layoutParams.height = KeyboardUtil.getValidPanelHeight();
                }

                setLayoutParams(layoutParams);
            }
        });
    }


    /**
     * @param visibility {@value View#VISIBLE: 这里有两种情况，1. 键盘没有弹起(需要适配)、2. 键盘没有弹起（不用适配）}
     */
    @Override
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE) {
            this.mIsHide = false;
        }

        if (visibility == getVisibility()) {
            return;
        }


        if (mIsKeybordShowing && visibility == VISIBLE) {
            return;
        }

        super.setVisibility(visibility);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsHide) {
            setVisibility(View.GONE);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 这里只是一个状态，是在{@link #onMeasure}之前{@link CustomRootLayout#onLayout(boolean, int, int, int, int)}中获知
     */
    private boolean mIsKeybordShowing = false;

    public void setIsKeybordShowing(final boolean isKeybordShowing) {
        this.mIsKeybordShowing = isKeybordShowing;
    }


    public void setIsShow(final boolean isShow) {
        this.mIsShow = isShow;
        if (mIsShow) {
            super.setVisibility(View.VISIBLE);
        }
    }

    public void setIsHide(final boolean isHide) {
        this.mIsHide = isHide;
    }

}
