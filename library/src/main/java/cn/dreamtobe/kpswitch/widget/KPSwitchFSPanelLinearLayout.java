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
package cn.dreamtobe.kpswitch.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Window;
import android.widget.LinearLayout;

import cn.dreamtobe.kpswitch.IFSPanelConflictLayout;
import cn.dreamtobe.kpswitch.IPanelHeightTarget;
import cn.dreamtobe.kpswitch.handler.KPSwitchFSPanelLayoutHandler;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.util.ViewUtil;

/**
 * Created by Jacksgong on 3/27/16.
 * <p/>
 * The panel container linear layout for full-screen theme window, and this layout's height would
 * be always equal to the height of the keyboard.
 * <p/>
 * For non-full-screen theme window, please use {@link KPSwitchPanelLinearLayout} instead.
 *
 * @see KeyboardUtil#attach(android.app.Activity, IPanelHeightTarget)
 * @see #recordKeyboardStatus(Window)
 * @see KPSwitchFSPanelFrameLayout
 * @see KPSwitchFSPanelRelativeLayout
 */
public class KPSwitchFSPanelLinearLayout extends LinearLayout implements IPanelHeightTarget,
        IFSPanelConflictLayout {

    private KPSwitchFSPanelLayoutHandler panelHandler;

    public KPSwitchFSPanelLinearLayout(Context context) {
        super(context);
        init();
    }

    public KPSwitchFSPanelLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public KPSwitchFSPanelLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KPSwitchFSPanelLinearLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                       int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        panelHandler = new KPSwitchFSPanelLayoutHandler(this);
    }

    @Override
    public void refreshHeight(int panelHeight) {
        ViewUtil.refreshHeight(this, panelHeight);
    }

    @Override
    public void onKeyboardShowing(boolean showing) {
        panelHandler.onKeyboardShowing(showing);
    }


    @Override
    public void recordKeyboardStatus(final Window window) {
        panelHandler.recordKeyboardStatus(window);
    }
}
