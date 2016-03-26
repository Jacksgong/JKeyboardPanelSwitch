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
package cn.dreamtobe.kpswitch.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import cn.dreamtobe.kpswitch.IPanelHeightTarget;
import cn.dreamtobe.kpswitch.R;

/**
 * Created by Jacksgong on 15/7/6.
 */
public class KeyboardUtil {

    public static void showKeyboard(final View view) {
        view.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) view.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public static void hideKeyboard(final View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        view.clearFocus();
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private static int LAST_SAVE_KEYBOARD_HEIGHT = 0;

    public static boolean saveKeyboardHeight(final Context context, int keyboardHeight) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == keyboardHeight) {
            return false;
        }

        if (keyboardHeight < 0) {
            return false;
        }

        LAST_SAVE_KEYBOARD_HEIGHT = keyboardHeight;
        Log.d("KeyBordUtil", String.format("save keyboard: %d", keyboardHeight));

        return KeyBoardSharedPreferences.save(context, keyboardHeight);
    }

    public static int getKeyboardHeight(final Context context) {
        if (LAST_SAVE_KEYBOARD_HEIGHT == 0) {
            LAST_SAVE_KEYBOARD_HEIGHT = KeyBoardSharedPreferences.get(context, getMinPanelHeight(context.getResources()));
        }

        return LAST_SAVE_KEYBOARD_HEIGHT;
    }

    public static int getValidPanelHeight(final Context context) {
        final int maxPanelHeight = getMaxPanelHeight(context.getResources());
        final int minPanelHeight = getMinPanelHeight(context.getResources());

        int validPanelHeight = getKeyboardHeight(context);

        validPanelHeight = Math.max(minPanelHeight, validPanelHeight);
        validPanelHeight = Math.min(maxPanelHeight, validPanelHeight);
        return validPanelHeight;


    }


    private static int MAX_PANEL_HEIGHT = 0;
    private static int MIN_PANEL_HEIGHT = 0;

    public static int getMaxPanelHeight(final Resources res) {
        if (MAX_PANEL_HEIGHT == 0) {
            MAX_PANEL_HEIGHT = res.getDimensionPixelSize(R.dimen.max_panel_height);
        }

        return MAX_PANEL_HEIGHT;
    }

    public static int getMinPanelHeight(final Resources res) {
        if (MIN_PANEL_HEIGHT == 0) {
            MIN_PANEL_HEIGHT = res.getDimensionPixelSize(R.dimen.min_panel_height);
        }

        return MIN_PANEL_HEIGHT;
    }


    /**
     * Recommend invoked by {@link Activity#onCreate(Bundle)}
     * For align the height of the keyboard to {@code target} as much as possible.
     *
     * @param activity contain the view
     * @param target whose height will be align to the keyboard height.
     */
    public static void attach(final Activity activity, IPanelHeightTarget target) {
        final ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().
                addOnGlobalLayoutListener(new KeyboardSizeListener(contentView, target));
    }

    private static class KeyboardSizeListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final static String TAG = "KeyboardSizeListener";

        private int previousHeight = 0;
        private ViewGroup contentView;
        IPanelHeightTarget panelHeightTarget;

        KeyboardSizeListener(ViewGroup contentView, IPanelHeightTarget panelHeightTarget) {
            this.contentView = contentView;
            this.panelHeightTarget = panelHeightTarget;
        }

        @Override
        public void onGlobalLayout() {
            final View userRootView = this.contentView.getChildAt(0);

            // Step 1. calculate the current display frame's height.
            Rect r = new Rect();
            userRootView.getWindowVisibleDisplayFrame(r);
            final int nowHeight = (r.bottom - r.top);

            // first result.
            if (previousHeight == 0) {
                previousHeight = nowHeight;

                // init the panel height for target.
                panelHeightTarget.refreshHeight(KeyboardUtil.getValidPanelHeight(getContext()));
                return;
            }

            // no change.
            if (nowHeight == previousHeight) {
                return;
            }



            final int keyboardHeight = Math.abs(nowHeight - previousHeight);

            // influence from the layout of the Status-bar.
            if (keyboardHeight == StatusBarHeightUtil.getStatusBarHeight(getContext())) {
                Log.w(TAG, String.format("On global layout change get keyboard height just equal" +
                        " statusBar height %d", keyboardHeight));
                return;
            }

            // Step 2. save the keyboardHeight
            boolean changed = KeyboardUtil.saveKeyboardHeight(getContext(), keyboardHeight);
            if (changed) {
                final int validPanelHeight = KeyboardUtil.getValidPanelHeight(getContext());
                if (this.panelHeightTarget.getHeight() != validPanelHeight) {
                    // Step3. refresh the panel's height with valid-panel-height which refer to
                    // the last keyboard height
                    this.panelHeightTarget.refreshHeight(validPanelHeight);
                }
            }


            previousHeight = nowHeight;
            Log.d(TAG, String.format("height: %d rootHeight: %d", nowHeight,
                    userRootView.getRootView().getHeight()));
        }

        private Context getContext() {
            return contentView.getContext();
        }
    }

}
