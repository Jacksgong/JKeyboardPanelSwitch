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
package cn.dreamtobe.kpswitch;

import android.app.Activity;
import android.view.Window;

/**
 * Created by Jacksgong on 3/31/16.
 * <p>
 * The interface used for the panel's container layout and it used in the case of full-screen theme
 * window.
 */
public interface IFSPanelConflictLayout {

    /**
     * Record the current keyboard status on {@link Activity#onPause()} and will be restore
     * the keyboard status automatically {@link Activity#onResume()}
     * <p/>
     * Recommend invoke this method on the {@link Activity#onPause()}, to record the keyboard
     * status for the right keyboard status and non-layout-conflict when the activity on resume.
     * <p/>
     * For fix issue#12 Bug1&Bug2.
     *
     * @param window The current window of the current visual activity.
     */
    void recordKeyboardStatus(Window window);
}
