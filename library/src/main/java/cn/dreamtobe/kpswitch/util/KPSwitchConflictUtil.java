/*
 * Copyright (C) 2015-2017 Jacksgong(blog.dreamtobe.cn)
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

import cn.dreamtobe.kpswitch.handler.KPSwitchFSPanelLayoutHandler;
import cn.dreamtobe.kpswitch.handler.KPSwitchPanelLayoutHandler;
import cn.dreamtobe.kpswitch.handler.KPSwitchRootLayoutHandler;

/**
 * Created by Jacksgong on 3/30/16.
 * <p/>
 * This util will help you control your panel and keyboard easily and exactly with
 * non-layout-conflict.
 * <p/>
 * This util just support the application layer encapsulation, more detail for how to resolve
 * the layout-conflict please Ref  {@link KPSwitchRootLayoutHandler}、
 * {@link KPSwitchPanelLayoutHandler}、{@link KPSwitchFSPanelLayoutHandler}
 * <p/>
 * Any problems: https://github.com/Jacksgong/JKeyboardPanelSwitch
 *
 * @see KPSwitchRootLayoutHandler
 * @see KPSwitchPanelLayoutHandler
 * @see KPSwitchFSPanelLayoutHandler
 */
public class KPSwitchConflictUtil {

    /**
     * @see #attach(View, View, View, SwitchClickListener)
     */
    public static void attach(final View panelLayout,
                              /* Nullable **/final View switchPanelKeyboardBtn,
                              /* Nullable **/final View focusView) {
        attach(panelLayout, switchPanelKeyboardBtn, focusView, null);
    }

    /**
     * Attach the action of {@code switchPanelKeyboardBtn} and the {@code focusView} to
     * non-layout-conflict.
     * <p/>
     * You do not have to use this method to attach non-layout-conflict, in other words, you can
     * attach the action by yourself with invoke methods manually: {@link #showPanel(View)}、
     * {@link #showKeyboard(View, View)}、{@link #hidePanelAndKeyboard(View)}, and in the case of
     * don't invoke this method to attach, and if your activity with the fullscreen-theme, please
     * ensure your panel layout is {@link View#INVISIBLE} before the keyboard is going to show.
     *
     * @param panelLayout            the layout of panel.
     * @param switchPanelKeyboardBtn the view will be used to trigger switching between the panel
     *                               and the keyboard.
     * @param focusView              the view will be focused or lose the focus.
     * @param switchClickListener    the click listener is used to listening the click event for
     *                               {@code switchPanelKeyboardBtn}.
     * @see #attach(View, View, SwitchClickListener, SubPanelAndTrigger...)
     */
    public static void attach(final View panelLayout,
                              /* Nullable **/final View switchPanelKeyboardBtn,
                              /* Nullable **/final View focusView,
                              /* Nullable **/final SwitchClickListener switchClickListener) {
        final Activity activity = (Activity) panelLayout.getContext();

        if (switchPanelKeyboardBtn != null) {
            switchPanelKeyboardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final boolean switchToPanel = switchPanelAndKeyboard(panelLayout, focusView);
                    if (switchClickListener != null) {
                        switchClickListener.onClickSwitch(v, switchToPanel);
                    }
                }
            });
        }

        if (isHandleByPlaceholder(activity)) {
            focusView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        /*
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
     * The same to {@link #attach(View, View, SwitchClickListener, SubPanelAndTrigger...)}.
     */
    public static void attach(final View panelLayout,
                              final View focusView,
                              SubPanelAndTrigger... subPanelAndTriggers) {
        attach(panelLayout, focusView, null, subPanelAndTriggers);
    }

    /**
     * If you have multiple sub-panels in the {@code panelLayout}, you can use this method to simply
     * attach them to non-layout-conflict. otherwise you can use {@link #attach(View, View, View)}
     * or {@link #attach(View, View, View, SwitchClickListener)}.
     *
     * @param panelLayout         the layout of panel.
     * @param focusView           the view will be focused or lose the focus.
     * @param switchClickListener the listener is used to listening whether the panel is showing or
     *                            keyboard is showing with toggle the panel/keyboard state.
     * @param subPanelAndTriggers the array of the trigger-toggle-view and
     *                            the sub-panel which bound trigger-toggle-view.
     */
    public static void attach(final View panelLayout,
                              final View focusView,
                              /** Nullable **/final SwitchClickListener switchClickListener,
                              SubPanelAndTrigger... subPanelAndTriggers) {
        final Activity activity = (Activity) panelLayout.getContext();

        for (SubPanelAndTrigger subPanelAndTrigger : subPanelAndTriggers) {

            bindSubPanel(subPanelAndTrigger, subPanelAndTriggers,
                    focusView, panelLayout, switchClickListener);
        }

        if (KPSwitchConflictUtil.isHandleByPlaceholder(activity)) {
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
     * @see #attach(View, View, SwitchClickListener, SubPanelAndTrigger...)
     */
    public static class SubPanelAndTrigger {
        /**
         * The sub-panel view is the child of panel-layout.
         */
        final View subPanelView;
        /**
         * The trigger view is used for triggering the {@code subPanelView} VISIBLE state.
         */
        final View triggerView;

        public SubPanelAndTrigger(View subPanelView, View triggerView) {
            this.subPanelView = subPanelView;
            this.triggerView = triggerView;
        }
    }

    /**
     * To show the panel(hide the keyboard automatically if the keyboard is showing) with
     * non-layout-conflict.
     *
     * @param panelLayout the layout of panel.
     * @see KPSwitchPanelLayoutHandler
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
        if (isHandleByPlaceholder(activity)) {
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
     * @return If true, switch to showing {@code panelLayout}; If false, switch to showing Keyboard.
     */
    public static boolean switchPanelAndKeyboard(final View panelLayout, final View focusView) {
        boolean switchToPanel = panelLayout.getVisibility() != View.VISIBLE;
        if (!switchToPanel) {
            showKeyboard(panelLayout, focusView);
        } else {
            showPanel(panelLayout);
        }

        return switchToPanel;
    }

    /**
     * Hide the panel and the keyboard.
     *
     * @param panelLayout the layout of panel.
     */
    public static void hidePanelAndKeyboard(final View panelLayout) {
        final Activity activity = (Activity) panelLayout.getContext();

        final View focusView = activity.getCurrentFocus();
        if (focusView != null) {
            KeyboardUtil.hideKeyboard(activity.getCurrentFocus());
            focusView.clearFocus();
        }

        panelLayout.setVisibility(View.GONE);
    }

    /**
     * This listener is used to listening the click event for a view which is received the click
     * event to switch between Panel and Keyboard.
     *
     * @see #attach(View, View, View, SwitchClickListener)
     */
    public interface SwitchClickListener {
        /**
         * @param v The view that was clicked.
         * @param switchToPanel If true, switch to showing Panel; If false, switch to showing
         *                      Keyboard.
         */
        void onClickSwitch(View v, boolean switchToPanel);
    }

    /**
     * @param isFullScreen        Whether in fullscreen theme.
     * @param isTranslucentStatus Whether in translucent status theme.
     * @param isFitsSystem        Whether the root view(the child of the content view) is in
     *                            {@code getFitSystemWindow()} equal true.
     * @return Whether handle the conflict by show panel placeholder, otherwise, handle by delay the
     * visible or gone of panel.
     */
    public static boolean isHandleByPlaceholder(boolean isFullScreen, boolean isTranslucentStatus,
                                                boolean isFitsSystem) {
        return isFullScreen || (isTranslucentStatus && !isFitsSystem);
    }

    static boolean isHandleByPlaceholder(final Activity activity) {
        return isHandleByPlaceholder(ViewUtil.isFullScreen(activity),
                ViewUtil.isTranslucentStatus(activity), ViewUtil.isFitsSystemWindows(activity));
    }

    private static void bindSubPanel(final SubPanelAndTrigger subPanelAndTrigger,
                                     final SubPanelAndTrigger[] subPanelAndTriggers,
                                     final View focusView, final View panelLayout,
                                     /* Nullable */final SwitchClickListener switchClickListener) {

        final View triggerView = subPanelAndTrigger.triggerView;
        final View boundTriggerSubPanelView = subPanelAndTrigger.subPanelView;

        triggerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean switchToPanel = null;
                if (panelLayout.getVisibility() == View.VISIBLE) {
                    // panel is visible.

                    if (boundTriggerSubPanelView.getVisibility() == View.VISIBLE) {

                        // bound-trigger panel is visible.
                        // to show keyboard.
                        KPSwitchConflictUtil.showKeyboard(panelLayout, focusView);
                        switchToPanel = false;

                    } else {
                        // bound-trigger panel is invisible.
                        // to show bound-trigger panel.
                        showBoundTriggerSubPanel(boundTriggerSubPanelView, subPanelAndTriggers);
                    }
                } else {
                    // panel is gone.
                    // to show panel.
                    KPSwitchConflictUtil.showPanel(panelLayout);
                    switchToPanel = true;

                    // to show bound-trigger panel.
                    showBoundTriggerSubPanel(boundTriggerSubPanelView, subPanelAndTriggers);
                }

                if (switchClickListener != null && switchToPanel != null) {
                    switchClickListener.onClickSwitch(v, switchToPanel);
                }
            }
        });
    }

    private static void showBoundTriggerSubPanel(final View boundTriggerSubPanelView,
                                                 final SubPanelAndTrigger[] subPanelAndTriggers) {
        // to show bound-trigger panel.
        for (SubPanelAndTrigger panelAndTrigger : subPanelAndTriggers) {
            if (panelAndTrigger.subPanelView != boundTriggerSubPanelView) {
                // other sub panel.
                panelAndTrigger.subPanelView.setVisibility(View.GONE);
            }
        }
        boundTriggerSubPanelView.setVisibility(View.VISIBLE);
    }
}
