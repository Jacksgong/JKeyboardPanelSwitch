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
package cn.dreamtobe.kpswitch;

import android.view.View;

/**
 * Created by Jacksgong on 3/30/16.
 */
public interface IPanelConflictLayout {
    boolean isKeyboardShowing();

    /**
     * @return The real status of Visible or not
     */
    boolean isVisible();

    /**
     * Keyboard->Panel
     *
     * @see cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil#showPanel(View)
     */
    void handleShow();

    /**
     * Panel->Keyboard
     *
     * @see cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil#showKeyboard(View, View)
     */
    void handleHide();
}
