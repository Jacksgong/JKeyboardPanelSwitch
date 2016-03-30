# Android键盘面板冲突 布局闪动处理方案

[![Download][bintray_svg]][bintray_link]
[![Build Status][build_status_svg]][build_status_link]

---

> 起源，之前在微信工作的时候，为了给用户带来更好的基础体验，做了很多尝试，踩了很多输入法的坑，特别是动态调整键盘高度，二级页面是透明背景，魅族早期的Smart bar等, 后来逐一完善了，考虑到拥抱开源，看业界还是有很多应用存在类似问题。就有了这个repo

---

> 之前有写过一篇核心思想: [Switching between the panel and the keyboard in Wechat](http://blog.dreamtobe.cn/2015/02/07/Switching-between-the-panel-and-the-keyboard/)。


我们可以看到微信中的 从键盘与微信的切换是无缝的，而且是无闪动的，这种基础体验是符合预期的。

但是实际中，简单的 键盘与面板切换 是会有闪动，问题的。今天我们就实践分析与解决这个问题。

<!--more-->
## 最终效果对比:

![][non-fullscreen_resolved_gif]![][fullscreen_resolved_gif]
![][adjust_resolved_gif]![][adjust_unresolved_gif]


## 如何使用

在`build.gradle`中:

```
compile 'cn.dreamtobe.kpswitch:library:1.4.1'
```

### I. 非全屏主题情况

> `(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0`

#### 1. `AndroidManifest`

> 可直接参照: [AndroidManifest.xml][AndroidManifest_xml_link]

> 对应的Activity，在**`AndroidManifest`中配置**`android:windowSoftInputMode=adjustResize`

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

#### 2. 需要处理页面的layout xml

> 可直接参照: [activity_chatting_resolved.xml][activity_chatting_resolved_xml_link]

1. 需要用到 **最上层布局** ([KPSwitchRootFrameLayout][KPSwitchRootFrameLayout_link]/[KPSwitchRootLinearLayout][KPSwitchRootLinearLayout_link]/[KPSwitchRootRelativeLayout][KPSwitchRootRelativeLayout_link])
2. 需要用到 **面板布局**([KPSwitchPanelFrameLayout][KPSwitchPanelFrameLayout_link]/[KPSwitchPanelLinearLayout][KPSwitchPanelLinearLayout_link]/[KPSwitchPanelRelativeLayout][KPSwitchPanelRelativeLayout_link])。

简单案例:

```
<?xml version="1.0" encoding="utf-8"?>
<!-- 可选用 KPSwitchRootLinearLayout、KPSwitchRootRelativeLayout、KPSwitchRootFrameLayout -->
<cn.dreamtobe.kpswitch.widget.KPSwitchRootLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <!-- 布局内容 -->
    ...

    <!-- 可选用 KPSwitchPanelLinearLayout、KPSwitchPanelRelativeLayout、KPSwitchPanelFrameLayout -->
    <cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout
        android:id="@+id/panel_root"
        android:layout_width="fill_parent"
        android:layout_height="@dimen/panel_height"
        android:visibility="gone">
        <!-- 面板内容 -->
        ...
    </cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout>

</cn.dreamtobe.kpswitch.widget.KPSwitchRootLinearLayout>
```

### 3. 需要处理页面的Activity:

> 可直接参照: [ChattingResolvedActivity.java][ChattingResolvedActivity_link]

1. 处理一些事件([KPSwitchConflictUtil][KPSwitchConflictUtil_link])
2. 键盘状态(高度与显示与否)监听([KeyboardUtil#attach()][KeyboardUtil_attach_link])

简单案例:

```
...

// 面板View
private KPSwitchPanelLinearLayout mPanelLayout;
// 键盘焦点View，用于输入内容
private EditText mSendEdt;
// 用于切换键盘与面板的按钮View
private ImageView mPlusIv;

@Override
public void onCreate(Bundle saveInstanceState){
    ...

    mPanelLayout = (KPSwitchPanelLinearLayout)findViewById(R.id.panel_root);
    mSendEdt = (EditText) findViewById(R.id.send_edt);
    mPlusIv = (ImageView) findViewById(R.id.plus_iv);

    /**
     * 这个Util主要是监控键盘的状态: 显示与否 以及 键盘的高度
     * 这里也有提供给外界监听 键盘显示/隐藏 的监听器，具体参看
     * 这个接口 {@Link KeyboardUtil#attach(Activity, IPanelHeightTarget, OnKeyboardShowingListener)}
     */
    KeyboardUtil.attach(this, mPanelLayout);

    /**
     * 这个Util主要是协助处理一些面板与键盘相关的事件。
     * 这个方法主要是对一些相关事件进行注册，如切换面板与键盘等，具体参看源码，比较简单。
     * 里面还提供了一些已经处理了冲突的工具方法: 显示面板；显示键盘；键盘面板切换；隐藏键盘与面板；
     *
     * @param panelRoot 面板的布局。
     * @param switchPanelKeyboardBtn 用于触发切换面板与键盘的按钮。
     * @param focusView 键盘弹起时会给这个View focus，收回时这个View会失去focus，通常是发送的EditText。
     */
    KPSwitchConflictUtil.attach(mPanelLayout, mPlusIv, mSendEdt);

}

...

...

// 如果需要处理返回收起面板的话
@Override
public boolean dispatchKeyEvent(KeyEvent event){
    if (event.getAction() == KeyEvent.ACTION_UP &&
            event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
        if (mPanelLayout.getVisibility() == View.VISIBLE) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelLayout);
            return true;
        }
    }
    return super.dispatchKeyEvent(event);
}
```

### II. 全屏主题情况

> `(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0`

#### 1. `AndroidManifest`

> 可直接参照: [AndroidManifest.xml][AndroidManifest_xml_link]

> 对应的Activity，在 **`AndroidManifest`中配置** `android:windowSoftInputMode=adjustUnspecified`，或者不配置，默认就是这个模式。

#### 2. 需要处理页面的layout xml

> 可直接参照: [activity_chatting_fullscreen_resolved.xml][activity_chatting_fullscreen_resolved_xml_link]

> 这边只需要用到一个 **面板布局** ([KPSwitchFSPanelFrameLayout][KPSwitchFSPanelFrameLayout_link]/[KPSwitchFSPanelLinearLayout][KPSwitchFSPanelLinearLayout_link]/[KPSwitchFSPanelRelativeLayout][KPSwitchFSPanelRelativeLayout_link])

```
<?xml version="1.0" encoding="utf-8"?>
...
    ...

    <!-- 可选用 KPSwitchFSPanelFrameLayout、KPSwitchFSPanelLinearLayout、KPSwitchFSPanelRelativeLayout -->
    <cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelFrameLayout
        android:id="@+id/panel_root"
        style="@style/Panel"
        android:visibility="gone">

        ...
    </cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelFrameLayout>

...
```


### 3. 需要处理页面的Activity:

> 可直接参照: [ChattingResolvedFullScreenActivity.java][ChattingResolvedFullScreenActivity_link]

1. 主要是处理一些事件([KPSwitchConflictUtil][KPSwitchConflictUtil_link])
2. 键盘状态(高度与显示与否)监听([KeyboardUtil#attach()][KeyboardUtil_attach_link])
3. 在`onPause`时，记录键盘状态用于从后台回到当前布局，恢复键盘状态不至于冲突([IFSPanelConflictLayout#recordKeyboardStatus()][IFSPanelConflictLayout_recordKeyboardStatus_link])

如下使用案例:

```
...

// 面板View
private KPSwitchFSPanelLinearLayout mPanelLayout;
// 键盘焦点View，用于输入内容
private EditText mSendEdt;
// 用于切换键盘与面板的按钮View
private ImageView mPlusIv;

@Override
public void onCreate(Bundle saveInstanceState){
    ...


    mPanelLayout = (KPSwitchFSPanelLinearLayout)findViewById(R.id.panel_root);
    mSendEdt = (EditText) findViewById(R.id.send_edt);
    mPlusIv = (ImageView) findViewById(R.id.plus_iv);

    /**
     * 这个Util主要是监控键盘的状态: 显示与否 以及 键盘的高度
     * 这里也有提供给外界监听 键盘显示/隐藏 的监听器，具体参看
     * 这个接口 {@Link KeyboardUtil#attach(Activity, IPanelHeightTarget, OnKeyboardShowingListener)}
     */
    KeyboardUtil.attach(this, mPanelLayout);

    /**
     * 这个Util主要是协助处理一些面板与键盘相关的事件。
     * 这个方法主要是对一些相关事件进行注册，如切换面板与键盘等，具体参看源码，比较简单。
     * 里面还提供了一些已经处理了冲突的工具方法: 显示面板；显示键盘；键盘面板切换；隐藏键盘与面板；
     *
     * @param panelRoot 面板的布局。
     * @param switchPanelKeyboardBtn 用于触发切换面板与键盘的按钮。
     * @param focusView 键盘弹起时会给这个View focus，收回时这个View会失去focus，通常是发送的EditText。
     */
    KPSwitchConflictUtil.attach(mPanelLayout, mPlusIv, mSendEdt);

}

@Override
protected void onPause() {
  super.onPause();
  // 用于记录当前的键盘状态，在从后台回到当前页面的时候，键盘状态能够正确的恢复并且不会导致布局冲突。
  mPanelLayout.recordKeyboardStatus(getWindow());
}

...

// 如果需要处理返回收起面板的话
@Override
public boolean dispatchKeyEvent(KeyEvent event){
    if (event.getAction() == KeyEvent.ACTION_UP &&
            event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
        if (mPanelLayout.getVisibility() == View.VISIBLE) {
            KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelLayout);
            return true;
        }
    }
    return super.dispatchKeyEvent(event);
}
```

---

> 如果发现你的键盘切换底色为黑色那是因为你的主题使用的是黑色背景，将对应的主题背景颜色改为白色即可。

---

## 基本原理

- 键盘高度计算，以及键盘是否显示的计算，参看: [KeyboardUtil.KeyboardStatusListener#calculateKeyboardHeight][KeyboardUtil_calculateKeyboardHeight_link]、[KeyboardUtil.KeyboardStatusListener#calculateKeyboardShowing][KeyboardUtil_calculateKeyboardShowing_link]。
- 处理闪动问题，参看: [KPSwitchRootLayoutHandler][KPSwitchRootLayoutHandler_link]，以及如果是非全屏主题用到的面板布局: [KPSwitchPanelLayoutHandler][KPSwitchPanelLayoutHandler_link]；如果是全屏主题用到的面板布局: [KPSwitchFSPanelLayoutHandler][KPSwitchFSPanelLayoutHandler_link]。


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
[fullscreen_resolved_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/fullscreen_resolved.gif
[non-fullscreen_resolved_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/non-fullscreen_resolved.gif
[adjust_resolved_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/adjust_resolved.gif
[adjust_unresolved_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/adjust_unresolved.gif
[build_status_svg]: https://travis-ci.org/Jacksgong/JKeyboardPanelSwitch.svg?branch=master
[build_status_link]: https://travis-ci.org/Jacksgong/JKeyboardPanelSwitch
[KPSwitchRootFrameLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchRootFrameLayout.java
[KPSwitchRootLinearLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchRootLinearLayout.java
[KPSwitchRootRelativeLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchRootRelativeLayout.java
[KPSwitchPanelFrameLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchPanelFrameLayout.java
[KPSwitchPanelLinearLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchPanelLinearLayout.java
[KPSwitchPanelRelativeLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchPanelRelativeLayout.java
[KPSwitchFSPanelFrameLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelFrameLayout.java
[KPSwitchFSPanelLinearLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelLinearLayout.java
[KPSwitchFSPanelRelativeLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelRelativeLayout.java
[ChattingResolvedActivity_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/java/cn/dreamtobe/kpswitch/demo/activity/ChattingResolvedActivity.java
[ChattingResolvedFullScreenActivity_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/java/cn/dreamtobe/kpswitch/demo/activity/ChattingResolvedFullScreenActivity.java
[IFSPanelConflictLayout_recordKeyboardStatus_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/IFSPanelConflictLayout.java#L22
[KPSwitchConflictUtil_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KPSwitchConflictUtil.java
[KeyboardUtil_attach_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L146
[KeyboardUtil_calculateKeyboardHeight_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L197
[KeyboardUtil_calculateKeyboardShowing_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L248
[KPSwitchRootLayoutHandler_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/handler/KPSwitchRootLayoutHandler.java
[KPSwitchPanelLayoutHandler_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/handler/KPSwitchPanelLayoutHandler.java
[KPSwitchFSPanelLayoutHandler_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/handler/KPSwitchFSPanelLayoutHandler.java
[activity_chatting_resolved_xml_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/res/layout/activity_chatting_resolved.xml
[activity_chatting_fullscreen_resolved_xml_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/res/layout/activity_chatting_fullscreen_resolved.xml
[AndroidManifest_xml_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/AndroidManifest.xml
