# Android键盘面板冲突 布局闪动处理方案

[![Download][bintray_svg]][bintray_link]

> 之前有写过一篇核心思想: [Switching between the panel and the keyboard in Wechat](http://blog.dreamtobe.cn/2015/02/07/Switching-between-the-panel-and-the-keyboard/)

> 这里主要是根据核心思想的实践，实践原理是通过`CustomRootLayout`布局变化，来获知是否是键盘引起的真正的布局变化，进而处理到接下来`PanelLayout`的`onMersure`中。

---

- 新算法不再动态修改`View#LayoutParams`，而是更加简单明了的方式
- 最新代码在原基础上**自动动态适配面板高度与键盘高度等高**，很大程度上优化了体验。

---

我们可以看到微信中的 从键盘与微信的切换是无缝的，而且是无闪动的，这种基础体验是符合预期的。

但是实际中，简单的 键盘与面板切换 是会有闪动，问题的。今天我们就实践分析与解决这个问题。

<!--more-->
## 最终效果对比:

![](https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/img/resolve_mv.gif)![](https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/img/unresolve_mv.gif)


## 如何使用

在`build.gradle`中:

```
compile 'cn.dreamtobe.kpswitch:library:1.2.1'
```

对应的Activity，在Manifest中配置`android:windowSoftInputMode=adjustResize`:

```
<manifest
  ...>
  <application
    ...>

    <activity
      android:name=".activity.ChattingActivity"
      android:windowSoftInputMode=adjustResize"/>
      ...
  </application>
  ...
</manifest>
```

在面板页面的layout中:

```
<?xml version="1.0" encoding="utf-8"?>
<cn.dreamtobe.kpswitch.widget.CustomRootLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <!-- 布局内容 -->
    ...

    <cn.dreamtobe.kpswitch.widget.PanelLayout
        android:id="@+id/panel_root"
        android:layout_width="fill_parent"
        android:layout_height="@dimen/panel_height"
        android:visibility="gone">
        <!-- 面板内容 -->
        ...
    </cn.dreamtobe.kpswitch.widget.PanelLayout>

</cn.dreamtobe.kpswitch.widget.CustomRootLayout>
```

在Activity中:

```

...

private PanelLayout mPanelLayout;
// 任何的可以用于收键盘输入的View，可有可无，用于显示keyboard的时候传入
private EditText mSendEdt;

@Override
public void onCreate(Bundle saveInstanceState){
    ...

    mPanelLayout = (PanelLayout)findViewById(R.id.panel_root)
}

...

// Keyboard与面板相互切换
public void switchPanel(){
    if (mPanelLayout.getVisibility() == View.VISIBLE){
        KeyboardUtil.showKeyboard(mSendEdt);
    } else {
        KeyboardUtil.hideKeyboard(mSendEdt);
        showPanel()
    }
}

public void hidePanel(){
    mPanelLayout.setVisibility(View.GONE);
}

public void showPanel(){
    mPanelLayout.setVisibility(View.VISIBLE);
}

...

// 如果需要处理返回收起面板的话
@Override
public boolean dispatchKeyEvent(KeyEvent event){
    if (event.getAction() == KeyEvent.ACTION_UP &&
            event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
        if (mPanelLayout.getVisibility() == View.VISIBLE) {
            hidePanel();
            return true;
        }
    }
    return super.dispatchKeyEvent(event);
}
```

> 还有不明白或者要测试的，建议参考项目demo中的`JChattingActivity`

## License

```
Copyright 2015 Jacks gong.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

[bintray_link]: https://bintray.com/jacksgong/maven/JKeyboardPanelSwitch/_latestVersion
[bintray_svg]: https://api.bintray.com/packages/jacksgong/maven/JKeyboardPanelSwitch/images/download.svg
