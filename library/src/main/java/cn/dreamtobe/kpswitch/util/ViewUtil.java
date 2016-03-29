package cn.dreamtobe.kpswitch.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jacksgong on 3/28/16.
 * <p/>
 * For wrap some utils for view.
 *
 * @see cn.dreamtobe.kpswitch.widget.PanelLayout
 * @see cn.dreamtobe.kpswitch.widget.FullScreenPanelLayout
 */
public class ViewUtil {

    private final static String TAG = "ViewUtil";

    public static boolean refreshHeight(final View view, final int aimHeight) {
        if (view.isInEditMode()) {
            return false;
        }
        Log.d(TAG,
                String.format("refresh Height %d %d", view.getHeight(), aimHeight));

        if (view.getHeight() == aimHeight) {
            return false;
        }

        if (Math.abs(view.getHeight() - aimHeight) ==
                StatusBarHeightUtil.getStatusBarHeight(view.getContext())) {
            return false;
        }

        final int validPanelHeight = KeyboardUtil.getValidPanelHeight(view.getContext());
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    validPanelHeight);
            view.setLayoutParams(layoutParams);
        } else {
            layoutParams.height = validPanelHeight;
            view.requestLayout();
        }

        return true;

    }
}
