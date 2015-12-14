package cn.dreamtobe.kpswitch.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Copyright (C) 2015 Jacks Blog
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by Jacksgong on 9/1/15.
 */
public class KeyBoardSharedPreferences {

    private final static String FILE_NAME = "keyboard.common";

    private final static String KEY_KEYBORD_HEIGHT = "sp.key.keyboard.height";

    volatile static SharedPreferences SP;

    public static boolean save(final Context context, final int keyboardHeight) {
        return with(context).edit()
                .putInt(KEY_KEYBORD_HEIGHT, keyboardHeight)
                .commit();
    }

    public static SharedPreferences with(final Context context) {
        if (SP == null) {
            synchronized (KeyBoardSharedPreferences.class) {
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
