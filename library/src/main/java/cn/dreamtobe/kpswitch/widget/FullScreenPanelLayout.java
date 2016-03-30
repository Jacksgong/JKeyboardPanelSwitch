/*
 * Copyright (C) 2015-2016 Jacksgong(blog.dreamtobe.cn)
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
package cn.dreamtobe.kpswitch.widget;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import cn.dreamtobe.kpswitch.IPanelHeightTarget;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.util.ViewUtil;

/**
 * Created by Jacksgong on 3/27/16.
 * <p/>
 * The root layout of Panel, and this layout's height would be always equal to the height of
 * the keyboard.
 *
 * @see KeyboardUtil#attach(Activity, IPanelHeightTarget)
 * @see #recordKeyboardStatus(Window)
 */
public class FullScreenPanelLayout extends LinearLayout implements IPanelHeightTarget {
    public FullScreenPanelLayout(Context context) {
        super(context);
    }

    public FullScreenPanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FullScreenPanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FullScreenPanelLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void refreshHeight(int panelHeight) {
        ViewUtil.refreshHeight(this, panelHeight);
    }

    private boolean isKeyboardShowing;

    @Override
    public void onKeyboardShowing(boolean showing) {
        this.isKeyboardShowing = showing;
        if (!showing && getVisibility() == View.INVISIBLE) {
            setVisibility(View.GONE);
        }

        if (!showing && recordedFocusView != null) {
            restoreFocusView();
            recordedFocusView = null;
        }
    }

    /**
     * Record the current keyboard status on {@link Activity#onPause()} and will be restore
     * the keyboard status automatically {@link Activity#onResume()}
     * <p/>
     * Recommend invoke this method on the {@link Activity#onPause()}, to record the keyboard
     * status for the right keyboard status and non-layout-conflict when the activity on resume.
     *
     * @param window The current window of the current visual activity.
     * @see #onKeyboardShowing(boolean)
     * <p/>
     * For fix issue#12 Bug1&Bug2.
     */
    public void recordKeyboardStatus(final Window window) {
        final View focusView = window.getCurrentFocus();
        if (focusView == null) {
            return;
        }

        if (isKeyboardShowing()) {
            saveFocusView(focusView);
        } else {
            focusView.clearFocus();
        }
    }

    private View recordedFocusView;
    private void saveFocusView(final View focusView) {
        recordedFocusView = focusView;
        focusView.clearFocus();
        setVisibility(View.GONE);
    }

    private void restoreFocusView() {
        setVisibility(View.INVISIBLE);
        KeyboardUtil.showKeyboard(recordedFocusView);

    }

    public boolean isKeyboardShowing() {
        return isKeyboardShowing;
    }
}
