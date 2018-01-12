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
import android.view.View;
import android.widget.RelativeLayout;

import cn.dreamtobe.kpswitch.IPanelConflictLayout;
import cn.dreamtobe.kpswitch.IPanelHeightTarget;
import cn.dreamtobe.kpswitch.handler.KPSwitchPanelLayoutHandler;

/**
 * Created by Jacksgong on 3/30/16.
 * <p/>
 * The panel container relative layout.
 * Resolve the layout-conflict from switching the keyboard and the Panel.
 * <p/>
 * For full-screen theme window, please use {@link KPSwitchFSPanelRelativeLayout} instead.
 *
 * @see KPSwitchPanelFrameLayout
 * @see KPSwitchPanelRelativeLayout
 * @see KPSwitchPanelLayoutHandler
 */
public class KPSwitchPanelRelativeLayout extends RelativeLayout implements IPanelHeightTarget,
        IPanelConflictLayout {

    private KPSwitchPanelLayoutHandler panelLayoutHandler;

    public KPSwitchPanelRelativeLayout(Context context) {
        super(context);
        init(null);
    }

    public KPSwitchPanelRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public KPSwitchPanelRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public KPSwitchPanelRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr,
                                       int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(final AttributeSet attrs) {
        panelLayoutHandler = new KPSwitchPanelLayoutHandler(this, attrs);
    }

    @Override
    public void setVisibility(int visibility) {
        if (panelLayoutHandler.filterSetVisibility(visibility)) {
            return;
        }
        super.setVisibility(visibility);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int[] processedMeasureWHSpec = panelLayoutHandler.processOnMeasure(widthMeasureSpec,
                heightMeasureSpec);

        super.onMeasure(processedMeasureWHSpec[0], processedMeasureWHSpec[1]);
    }

    @Override
    public boolean isKeyboardShowing() {
        return panelLayoutHandler.isKeyboardShowing();
    }

    @Override
    public boolean isVisible() {
        return panelLayoutHandler.isVisible();
    }

    @Override
    public void handleShow() {
        super.setVisibility(View.VISIBLE);
    }

    @Override
    public void handleHide() {
        panelLayoutHandler.handleHide();
    }

    @Override
    public void setIgnoreRecommendHeight(boolean isIgnoreRecommendHeight) {
        panelLayoutHandler.setIgnoreRecommendHeight(isIgnoreRecommendHeight);
    }

    @Override
    public void refreshHeight(int panelHeight) {
        panelLayoutHandler.resetToRecommendPanelHeight(panelHeight);
    }

    @Override
    public void onKeyboardShowing(boolean showing) {
        panelLayoutHandler.setIsKeyboardShowing(showing);
    }
}
