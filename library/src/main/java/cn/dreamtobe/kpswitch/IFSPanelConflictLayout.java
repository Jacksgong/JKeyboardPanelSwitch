package cn.dreamtobe.kpswitch;

import android.app.Activity;
import android.view.Window;

/**
 * Created by Jacksgong on 3/31/16.
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
    void recordKeyboardStatus(final Window window);
}
