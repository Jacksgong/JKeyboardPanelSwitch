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
import android.widget.FrameLayout;

import cn.dreamtobe.kpswitch.handler.KPSwitchRootLayoutHandler;

/**
 * Created by Jacksgong on 3/30/16.
 * <p/>
 * To keep watch on the keyboard status before occur layout-conflict.
 * <p/>
 * This layout must be the root layout in your Activity. In other words, must be the
 * child of content view.
 * <p/>
 * Resolve the layout-conflict from switching the keyboard and the Panel.
 *
 * @see KPSwitchRootRelativeLayout
 * @see KPSwitchRootLinearLayout
 * @see KPSwitchPanelLinearLayout
 */
public class KPSwitchRootFrameLayout extends FrameLayout {

    private KPSwitchRootLayoutHandler conflictHandler;

    public KPSwitchRootFrameLayout(Context context) {
        super(context);
        init();
    }

    public KPSwitchRootFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KPSwitchRootFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KPSwitchRootFrameLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                   int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        conflictHandler = new KPSwitchRootLayoutHandler(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        conflictHandler.handleBeforeMeasure(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
