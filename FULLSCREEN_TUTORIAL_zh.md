# 全屏主题 或者 透明状态栏主题并且在`fitsSystemWindows=false` 情况下使用引导

> 这个引导覆盖以下两个Case:

> 1. 全屏主题 (`(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0`)
> 2. 透明状态栏主题(`(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0`) 并且 根布局的 `fitsSystemWindows=false`。

![][fullscreen_resolved_gif]

## I. `AndroidManifest`

> 可直接参照: [AndroidManifest.xml][AndroidManifest_xml_link]

> 对应的Activity，在 **`AndroidManifest`中配置** `android:windowSoftInputMode=adjustUnspecified`，或者不配置，默认就是这个模式。

## II. 需要处理页面的layout xml

> 这边只需要用到一个 **面板布局** ([KPSwitchFSPanelFrameLayout][KPSwitchFSPanelFrameLayout_link]/[KPSwitchFSPanelLinearLayout][KPSwitchFSPanelLinearLayout_link]/[KPSwitchFSPanelRelativeLayout][KPSwitchFSPanelRelativeLayout_link])

```xml
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


## III. 需要处理页面的Activity:

> 可直接参照: [ChattingResolvedHandleByPlaceholderActivity.java][ChattingResolvedHandleByPlaceholderActivity_link]

1. 主要是处理一些事件([KPSwitchConflictUtil][KPSwitchConflictUtil_link])
2. 键盘状态(高度与显示与否)监听([KeyboardUtil#attach()][KeyboardUtil_attach_link])
3. 在`onPause`时，记录键盘状态用于从后台回到当前布局，恢复键盘状态不至于冲突([IFSPanelConflictLayout#recordKeyboardStatus()][IFSPanelConflictLayout_recordKeyboardStatus_link])

如下使用案例:

```java
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
     * 如果有多个面板的需求，可以参看: KPSwitchConflictUtil.attach(panelLayout, focusView, switchClickListener, subPanelAndTriggers...)
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

> 更多原理相关移步[README](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/README_zh.md)；也可移步参看: [非全屏主题情况下使用引导](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/NON-FULLSCREEN_TUTORIAL_zh.md)。


[fullscreen_resolved_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/fullscreen_resolved.gif
[AndroidManifest_xml_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/AndroidManifest.xml#L25
[KPSwitchFSPanelFrameLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelFrameLayout.java
[KPSwitchFSPanelLinearLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelLinearLayout.java
[KPSwitchFSPanelRelativeLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelRelativeLayout.java
[ChattingResolvedHandleByPlaceholderActivity_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/java/cn/dreamtobe/kpswitch/demo/activity/ChattingResolvedHandleByPlaceholderActivity.java
[KPSwitchConflictUtil_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KPSwitchConflictUtil.java
[KeyboardUtil_attach_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L134
[IFSPanelConflictLayout_recordKeyboardStatus_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/IFSPanelConflictLayout.java#L37
