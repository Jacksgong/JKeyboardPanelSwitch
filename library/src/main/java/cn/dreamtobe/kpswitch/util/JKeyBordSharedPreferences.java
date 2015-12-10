package cn.dreamtobe.kpswitch.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Jacksgong on 9/1/15.
 */
public class JKeyBordSharedPreferences {

    private final static String FILE_NAME = "jkeybord.common";

    private final static String KEY_KEYBORD_HEIGHT = "sp.key.keybord.height";

    volatile static SharedPreferences SP;

    public static boolean save(final Context context, final int keybordHeight) {
        return with(context).edit()
                .putInt(KEY_KEYBORD_HEIGHT, keybordHeight)
                .commit();
    }

    public static SharedPreferences with(final Context context) {
        if (SP == null) {
            synchronized (JKeyBordSharedPreferences.class) {
                if (SP == null) {
                    SP = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                }
            }
        }

        return SP;
    }

    public static int get(final Context context, final int defaultHeight) {
        return with(context).getInt(KEY_KEYBORD_HEIGHT, defaultHeight);
    }

}
