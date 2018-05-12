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
package cn.dreamtobe.kpswitch.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Jacksgong on 9/1/15.
 * <p/>
 * For save the keyboard height.
 */
class KeyBoardSharedPreferences {

    private static final String FILE_NAME = "keyboard.common";

    private static final String KEY_KEYBOARD_HEIGHT = "sp.key.keyboard.height";

    private static volatile SharedPreferences sp;

    public static boolean save(final Context context, final int keyboardHeight) {
        return with(context).edit()
                .putInt(KEY_KEYBOARD_HEIGHT, keyboardHeight)
                .commit();
    }

    private static SharedPreferences with(final Context context) {
        if (sp == null) {
            synchronized (KeyBoardSharedPreferences.class) {
                if (sp == null) {
                    sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                }
            }
        }

        return sp;
    }

    public static int get(final Context context, final int defaultHeight) {
        return with(context).getInt(KEY_KEYBOARD_HEIGHT, defaultHeight);
    }

}
