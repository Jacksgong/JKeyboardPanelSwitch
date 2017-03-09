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

import android.view.View;
import android.view.Window;

import cn.dreamtobe.kpswitch.IFSPanelConflictLayout;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;

/**
 * Created by Jacksgong on 3/31/16.
 * <p/>
 * The handler for handling the layout-conflict in the full-screen panel-layout.
 */
public class KPSwitchFSPanelLayoutHandler implements IFSPanelConflictLayout {

    private final View panelLayout;
    private boolean isKeyboardShowing;

    public KPSwitchFSPanelLayoutHandler(final View panelLayout) {
        this.panelLayout = panelLayout;
    }

    public void onKeyboardShowing(boolean showing) {
        isKeyboardShowing = showing;
        if (!showing && panelLayout.getVisibility() == View.INVISIBLE) {
            panelLayout.setVisibility(View.GONE);
        }

        if (!showing && recordedFocusView != null) {
            restoreFocusView();
            recordedFocusView = null;
        }
    }

    @Override
    public void recordKeyboardStatus(Window window) {
        final View focusView = window.getCurrentFocus();
        if (focusView == null) {
            return;
        }

        if (isKeyboardShowing) {
            saveFocusView(focusView);
        } else {
            focusView.clearFocus();
        }
    }

    private View recordedFocusView;

    private void saveFocusView(final View focusView) {
        recordedFocusView = focusView;
        focusView.clearFocus();
        panelLayout.setVisibility(View.GONE);
    }

    private void restoreFocusView() {
        panelLayout.setVisibility(View.INVISIBLE);
        KeyboardUtil.showKeyboard(recordedFocusView);

    }
}
