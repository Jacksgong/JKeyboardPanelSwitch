package cn.dreamtobe.jkpswitch.util;

import android.content.Context;
import android.content.SharedPreferences;

import cn.dreamtobe.jkpswitch.app.JKeyBordApplication;

/**
 * Created by Jacksgong on 9/1/15.
 */
public class JKeyBordSharedPreferences {

    private final static String FILE_NAME = "jkeybord.common";

    private final static String KEY_KEYBORD_HEIGHT = "sp.key.keybord.height";

    private SharedPreferences sp;

    public final static class HolderClass {
        private final static JKeyBordSharedPreferences INSTANCE = new JKeyBordSharedPreferences();
    }

    public static JKeyBordSharedPreferences getImpl() {
        return HolderClass.INSTANCE;
    }

    private JKeyBordSharedPreferences() {
        sp = JKeyBordApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public boolean save(final int keybordHeight) {
        return sp.edit()
                .putInt(KEY_KEYBORD_HEIGHT, keybordHeight)
                .commit();
    }

    public int get(final int defaultHeight) {
        return sp.getInt(KEY_KEYBORD_HEIGHT, defaultHeight);
    }

}
