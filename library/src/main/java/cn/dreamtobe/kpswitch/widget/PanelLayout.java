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
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.lang.ref.WeakReference;

import cn.dreamtobe.kpswitch.util.KeyboardUtil;


/**
 * @see CustomRootLayout
 */
public class PanelLayout extends LinearLayout {

    /**
     * The real status of Visible or not
     *
     * @see #setIsHide(boolean)
     * @see #setVisibility(int)
     * <p/>
     * if true, the status is non-Visible or will
     * non-Visible(may delay and handle in {@link #onMeasure(int, int)})
     * <p/>
     * The value of {@link #getVisibility()} maybe be assigned dully for cover the keyboard->panel.
     * In this case, the {@code mIsHide} will mark the right status.
     * Handle by {@link #setVisibility(int)} & {@link #onMeasure(int, int)}
     */
    private boolean mIsHide = false;
    private RefreshHeightRunnable mRefreshHeightRunnable;

    public PanelLayout(Context context) {
        super(context);
        init();
    }

    public PanelLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public PanelLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mRefreshHeightRunnable = new RefreshHeightRunnable(new WeakReference<>(this));
        refreshHeight();
    }

    /**
     * for handle the panel's height, will be equal to the keyboard height which had saved last time.
     */
    public void refreshHeight() {
        if (isInEditMode()) {
            return;
        }

        if (getHeight() == KeyboardUtil.getValidPanelHeight(getContext())) {
            return;
        }

        post(mRefreshHeightRunnable);
    }

    private static class RefreshHeightRunnable implements Runnable {
        private WeakReference<PanelLayout> panelLayoutWeakReference;

        private RefreshHeightRunnable(final WeakReference<PanelLayout> panelLayoutWeakReference) {
            this.panelLayoutWeakReference = panelLayoutWeakReference;
        }

        @Override
        public void run() {
            if (this.panelLayoutWeakReference == null ||
                    this.panelLayoutWeakReference.get() == null) {
                return;
            }

            final PanelLayout panelLayout = this.panelLayoutWeakReference.get();
            ViewGroup.LayoutParams layoutParams = panelLayout.getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        KeyboardUtil.getValidPanelHeight(panelLayout.getContext()));
            } else {
                layoutParams.height = KeyboardUtil.getValidPanelHeight(panelLayout.getContext());
            }

            panelLayout.setLayoutParams(layoutParams);
        }
    }


    /**
     * @param visibility {@link View#VISIBLE}: 这里有两种情况，1. 键盘没有弹起(需要适配)、2. 键盘没有弹起（不用适配）
     */
    @Override
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE) {
            setIsHide(false);
        }

        if (visibility == getVisibility()) {
            return;
        }


        /**
         * Will be handled on {@link CustomRootLayout#onMeasure(int, int)} -> {@link #handleShow()}
         * Delay show, until the {@link CustomRootLayout} discover the size is changed by keyboard-show.
         * And will show, on the next frame of the above change discovery.
         */
        if (mIsKeyboardShowing && visibility == VISIBLE) {
            return;
        }

        super.setVisibility(visibility);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsHide) {
            setVisibility(View.GONE);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 这里只是一个状态，是在{@link #onMeasure}之前{@link CustomRootLayout#onLayout(boolean, int, int, int, int)}中获知
     */
    private boolean mIsKeyboardShowing = false;

    public void setIsKeyboardShowing(final boolean isKeyboardShowing) {
        this.mIsKeyboardShowing = isKeyboardShowing;
    }


    private void setIsHide(final boolean isHide) {
        this.mIsHide = isHide;
    }

    /**
     * @return The real status of Visible or not
     */
    public boolean isVisible() {
        return !mIsHide;
    }

    /**
     * Case: keyboard/non -> panel
     */
    void handleShow() {
        super.setVisibility(View.VISIBLE);
    }

    /**
     * panel -> keyboard/non
     */
    void handleHide() {
        setIsHide(true);
    }

}
