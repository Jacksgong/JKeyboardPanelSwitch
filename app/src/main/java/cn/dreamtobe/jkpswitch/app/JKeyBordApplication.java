package cn.dreamtobe.jkpswitch.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jacksgong on 9/1/15.
 */
public class JKeyBordApplication extends Application {

    private static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;

    }

    public static Context getContext() {
        return CONTEXT;
    }
}
