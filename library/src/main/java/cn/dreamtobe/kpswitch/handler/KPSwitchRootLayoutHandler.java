/*
 * Copyright (C) 2015-2017 Jacksgong(blog.dreamtobe.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.dreamtobe.kpswitch.handler;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import cn.dreamtobe.kpswitch.IPanelConflictLayout;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.util.StatusBarHeightUtil;
import cn.dreamtobe.kpswitch.util.ViewUtil;

/**
 * Created by Jacksgong on 3/30/16.
 *
 * @see cn.dreamtobe.kpswitch.widget.KPSwitchRootFrameLayout
 * @see cn.dreamtobe.kpswitch.widget.KPSwitchRootLinearLayout
 * @see cn.dreamtobe.kpswitch.widget.KPSwitchRootRelativeLayout
 */
public class KPSwitchRootLayoutHandler {
    private static final String TAG = "KPSRootLayoutHandler";

    private int mOldHeight = -1;
    private final View mTargetRootView;

    private final int mStatusBarHeight;
    private final boolean mIsTranslucentStatus;

    public KPSwitchRootLayoutHandler(final View rootView) {
        this.mTargetRootView = rootView;
        this.mStatusBarHeight = StatusBarHeightUtil.getStatusBarHeight(rootView.getContext());
        final Activity activity = (Activity) rootView.getContext();
        this.mIsTranslucentStatus = ViewUtil.isTranslucentStatus(activity);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void handleBeforeMeasure(final int width, int height) {
        // 由当前布局被键盘挤压，获知，由于键盘的活动，导致布局将要发生变化。

        if (mIsTranslucentStatus && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && mTargetRootView.getFitsSystemWindows()) {
            // In this case, the height is always the same one, so, we have to calculate below.
            final Rect rect = new Rect();
            mTargetRootView.getWindowVisibleDisplayFrame(rect);
            height = rect.bottom - rect.top;
        }

        Log.d(TAG, "onMeasure, width: " + width + " height: " + height);
        if (height < 0) {
            return;
        }

        if (mOldHeight < 0) {
            mOldHeight = height;
            return;
        }

        final int offset = mOldHeight - height;

        if (offset == 0) {
            Log.d(TAG, "" + offset + " == 0 break;");
            return;
        }

        if (Math.abs(offset) == mStatusBarHeight) {
            Log.w(TAG, String.format("offset just equal statusBar height %d", offset));
            // 极有可能是 相对本页面的二级页面的主题是全屏&是透明，但是本页面不是全屏，因此会有status bar的布局变化差异，进行调过
            // 极有可能是 该布局采用了透明的背景(windowIsTranslucent=true)，而背后的布局`full screen`为false，
            // 因此有可能第一次绘制时没有attach上status bar，而第二次status bar attach上去，导致了这个变化。
            return;
        }

        mOldHeight = height;
        final IPanelConflictLayout panel = getPanelLayout(mTargetRootView);

        if (panel == null) {
            Log.w(TAG, "can't find the valid panel conflict layout, give up!");
            return;
        }

        // 检测到布局变化非键盘引起
        if (Math.abs(offset) < KeyboardUtil.getMinKeyboardHeight(mTargetRootView.getContext())) {
            Log.w(TAG, "system bottom-menu-bar(such as HuaWei Mate7) causes layout changed");
            return;
        }

        if (offset > 0) {
            //键盘弹起 (offset > 0，高度变小)
            panel.handleHide();
        } else if (panel.isKeyboardShowing() && panel.isVisible()) {
            // 1. 总得来说，在监听到键盘已经显示的前提下，键盘收回才是有效有意义的。
            // 2. 修复在Android L下使用V7.Theme.AppCompat主题，进入Activity，默认弹起面板bug，
            // 第2点的bug出现原因:在Android L下使用V7.Theme.AppCompat主题，并且
            // 不使用系统的ActionBar/ToolBar，V7.Theme.AppCompat主题,还是会先默认绘制一帧默认ActionBar，
            // 然后再将他去掉（略无语）
            //键盘收回 (offset < 0，高度变大)

            // the panel is showing/will showing
            panel.handleShow();
        }
    }

    private IPanelConflictLayout mPanelLayout;

    private IPanelConflictLayout getPanelLayout(final View view) {
        if (mPanelLayout != null) {
            return mPanelLayout;
        }

        if (view instanceof IPanelConflictLayout) {
            mPanelLayout = (IPanelConflictLayout) view;
            return mPanelLayout;
        }

        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                IPanelConflictLayout v = getPanelLayout(((ViewGroup) view).getChildAt(i));
                if (v != null) {
                    mPanelLayout = v;
                    return mPanelLayout;
                }
            }
        }

        return null;
    }
}
