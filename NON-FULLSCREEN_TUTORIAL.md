# 非全屏主题 或者 透明状态栏主题并且在`fitsSystemWindows=true` 情况下使用引导

> 这个引导覆盖以下两个Case:

> 1. 非全屏主题 (`(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0`) 。
> 2. 透明状态栏主题(`(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0`) 并且 根布局的 `fitsSystemWindows=true`。

![][non-fullscreen_resolved_gif]

## I. `AndroidManifest`

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

## II. 需要处理页面的layout xml

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

## III. 需要处理页面的Activity:

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

---

> 更多原理相关移步[README](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/README.md)；也可移步参看: [全屏主题情况下使用引导](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/FULLSCREEN_TUTORIAL.md)。

[non-fullscreen_resolved_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/non-fullscreen_resolved.gif
[AndroidManifest_xml_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/AndroidManifest.xml
[activity_chatting_resolved_xml_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/res/layout/activity_chatting_resolved.xml
[KPSwitchRootFrameLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchRootFrameLayout.java
[KPSwitchPanelLinearLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchPanelLinearLayout.java
[KPSwitchPanelRelativeLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchPanelRelativeLayout.java
[KPSwitchPanelFrameLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchPanelFrameLayout.java
[KPSwitchRootRelativeLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchRootRelativeLayout.java
[KPSwitchRootLinearLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchRootLinearLayout.java
[ChattingResolvedActivity_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/java/cn/dreamtobe/kpswitch/demo/activity/ChattingResolvedActivity.java
[KPSwitchConflictUtil_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KPSwitchConflictUtil.java
[KeyboardUtil_attach_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L134
