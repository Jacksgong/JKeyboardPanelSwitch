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
