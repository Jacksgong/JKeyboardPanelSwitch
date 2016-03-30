/*
 * Copyright (C) 2015-2016 Jacksgong(blog.dreamtobe.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.dreamtobe.kpswitch.util;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Jacksgong on 3/30/16.
 * <p/>
 * This util will help you control your panel and keyboard easily and exactly with
 * non-layout-conflict.
 * <p/>
 * This util just support the application layer encapsulation, more detail for how to resolve
 * the layout-conflict please Ref  {@link cn.dreamtobe.kpswitch.handler.KPSwitchRootLayoutHandler}
 * {@link cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout} {@link cn.dreamtobe.kpswitch.widget.FullScreenPanelLayout}
 * <p/>
 * Any problems: https://github.com/Jacksgong/JKeyboardPanelSwitch
 *
 * @see cn.dreamtobe.kpswitch.handler.KPSwitchRootLayoutHandler
 * @see cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout
 * @see cn.dreamtobe.kpswitch.widget.FullScreenPanelLayout
 */
public class KPSwitchConflictUtil {

    /**
     * Attach the action of {@code switchPanelKeyboardBtn} and the {@code focusView} to
     * non-layout-conflict.
     * <p/>
     * You do not have to use this method to attach non-layout-conflict, in other words, you can
     * attach the action by yourself with invoke methods manually: {@link #showPanel(View)}、
     * {@link #showKeyboard(View, View)}、{@link #hidePanelAndKeyboard(View)}, and in the case of don't
     * invoke this method to attach, and if your activity with the fullscreen-theme, please ensure your
     * panel layout is {@link View#INVISIBLE} before the keyboard is going to show.
     *
     * @param panelLayout            the layout of panel.
     * @param switchPanelKeyboardBtn the view will be used to trigger switching between the panel and
     *                               the keyboard.
     * @param focusView              the view will be focused or lose the focus.
     */
    public static void attach(final View panelLayout,
                              /** Nullable **/final View switchPanelKeyboardBtn,
                              /** Nullable **/final View focusView) {
        final Activity activity = (Activity) panelLayout.getContext();

        if (switchPanelKeyboardBtn != null) {
            switchPanelKeyboardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switchPanelAndKeyboard(panelLayout, focusView);
                }
            });
        }

        if (ViewUtil.isFullScreen(activity)) {
            focusView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        /**
                         * Show the fake empty keyboard-same-height panel to fix the conflict when
                         * keyboard going to show.
                         * @see KPSwitchConflictUtil#showKeyboard(View, View)
                         */
                        panelLayout.setVisibility(View.INVISIBLE);
                    }
                    return false;
                }
            });
        }
    }

    /**
     * To show the panel(hide the keyboard automatically if the keyboard is showing) with
     * non-layout-conflict.
     *
     * @param panelLayout the layout of panel.
     * @see cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout
     * @see cn.dreamtobe.kpswitch.widget.FullScreenPanelLayout
     */
    public static void showPanel(final View panelLayout) {
        final Activity activity = (Activity) panelLayout.getContext();
        panelLayout.setVisibility(View.VISIBLE);
        if (activity.getCurrentFocus() != null) {
            KeyboardUtil.hideKeyboard(activity.getCurrentFocus());
        }
    }

    /**
     * To show the keyboard(hide the panel automatically if the panel is showing) with
     * non-layout-conflict.
     *
     * @param panelLayout the layout of panel.
     * @param focusView   the view will be focused.
     */
    public static void showKeyboard(final View panelLayout, final View focusView) {
        final Activity activity = (Activity) panelLayout.getContext();

        KeyboardUtil.showKeyboard(focusView);
        if (ViewUtil.isFullScreen(activity)) {
            panelLayout.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * If the keyboard is showing, then going to show the {@code panelLayout},
     * and hide the keyboard with non-layout-conflict.
     * <p/>
     * If the panel is showing, then going to show the keyboard,
     * and hide the {@code panelLayout} with non-layout-conflict.
     * <p/>
     * If the panel and the keyboard are both hiding. then going to show the {@code panelLayout}
     * with non-layout-conflict.
     *
     * @param panelLayout the layout of panel.
     * @param focusView   the view will be focused or lose the focus.
     */
    public static void switchPanelAndKeyboard(final View panelLayout, final View focusView) {
        if (panelLayout.getVisibility() == View.VISIBLE) {
            showKeyboard(panelLayout, focusView);
        } else {
            showPanel(panelLayout);
        }
    }

    /**
     * Hide the panel and the keyboard.
     *
     * @param panelLayout the layout of panel.
     */
    public static void hidePanelAndKeyboard(final View panelLayout) {
        final Activity activity = (Activity) panelLayout.getContext();

        if (activity.getCurrentFocus() != null) {
            KeyboardUtil.hideKeyboard(activity.getCurrentFocus());
        }

        panelLayout.setVisibility(View.GONE);
    }
}
