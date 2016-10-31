# The integration tutorial of The fullscreen theme Or The status bar is translucent with `fitsSystemWindows=false`

> This tutorial cover the following two cases:

> 1. The fullscreen theme (`(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0`)
> 2. The status bar is translucent(`(activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0`) and the root layout configuration with `fitsSystemWindows=false`.

![][fullscreen_resolved_gif]

## I. `AndroidManifest`

> You can directly refer to [AndroidManifest.xml][AndroidManifest_xml_link]

> The relate Activity，in the **configuration of `AndroidManifest`** `android:windowSoftInputMode=adjustUnspecified`，or without any special configuration, since the default value of `windowSoftInputMode` is `adjustUnspecified`.

## II. The layout xml

> In the layout xml, you need a **panel layout** ([KPSwitchFSPanelFrameLayout][KPSwitchFSPanelFrameLayout_link]/[KPSwitchFSPanelLinearLayout][KPSwitchFSPanelLinearLayout_link]/[KPSwitchFSPanelRelativeLayout][KPSwitchFSPanelRelativeLayout_link])

```xml
<?xml version="1.0" encoding="utf-8"?>
...
    ...

    <!-- Alternative: KPSwitchFSPanelFrameLayout、KPSwitchFSPanelLinearLayout、KPSwitchFSPanelRelativeLayout -->
    <cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelFrameLayout
        android:id="@+id/panel_root"
        style="@style/Panel"
        android:visibility="gone">

        ...
    </cn.dreamtobe.kpswitch.widget.KPSwitchFSPanelFrameLayout>

...
```


## III. Activity:

> You can directly refer to: [ChattingResolvedHandleByPlaceholderActivity.java][ChattingResolvedHandleByPlaceholderActivity_link]

1. Handle some UI reactions([KPSwitchConflictUtil][KPSwitchConflictUtil_link])
2. Listener the status of the keyboard(Height or Whether is showing)([KeyboardUtil#attach()][KeyboardUtil_attach_link])
3. Record the status of keyboard in the `Activity#onPause` method，which used for restoring the status when the Activity from the background back to the foreground([IFSPanelConflictLayout#recordKeyboardStatus()][IFSPanelConflictLayout_recordKeyboardStatus_link])

The sample demonstrate:

```java
...

// The panel layout
private KPSwitchFSPanelLinearLayout mPanelLayout;
// The cursor focus view, used for inputing content
private EditText mSendEdt;
// The view used for triggering showing between the keyboard and the panel.
private ImageView mPlusIv;

@Override
public void onCreate(Bundle saveInstanceState){
    ...


    mPanelLayout = (KPSwitchFSPanelLinearLayout)findViewById(R.id.panel_root);
    mSendEdt = (EditText) findViewById(R.id.send_edt);
    mPlusIv = (ImageView) findViewById(R.id.plus_iv);

    /**
     * This Util mainly to watch the keyboard status: showing or not And the keyboard height.
     * There is also a method private a listener for upper to listener the keyboard status, the
     * detail refer to {@Link KeyboardUtil#attach(Activity, IPanelHeightTarget, OnKeyboardShowingListener)}
     */
    KeyboardUtil.attach(this, mPanelLayout);

    /**
     * This Util mainly to assist handling the conflict between the keyboard and the panel layout.
     * This method mainly to register some event, such as switch between the keyboard and the panel
     * layout etc. The source code is very simple, you can check it out by yourself.
     * There are also some toolset method: show-keyboard、show-panel-layout、switch-panel-keyboard、
     * hide-panel-keyboard、etc.
     *
     * If you have more than one panel, please refer to :
     * KPSwitchConflictUtil.attach(panelLayout, focusView, switchClickListener, subPanelAndTriggers...)
     *
     * @param panelRoot The panel layout.
     * @param switchPanelKeyboardBtn The view used for switching between the keyboard and the panel layout.
     * @param focusView The cursor focus view, this view is usually a EditText which used to receive inputing content.
     */
    KPSwitchConflictUtil.attach(mPanelLayout, mPlusIv, mSendEdt);

}

@Override
protected void onPause() {
  super.onPause();
  // Record the current status of the keyboard. When the Activity back to the foreground from the background,
  // the keyboard status will be restore to the recorded status automatically.
  mPanelLayout.recordKeyboardStatus(getWindow());
}

...

// If you want the panel can be hidden when use press the back-button
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

> More detail about the principle, please move to [README](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/README.md)；You also can move to: [The integration tutorial of The non-fullscreen theme Or The status bar is translucent with `fitsSystemWindows=true`](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/NON-FULLSCREEN_TUTORIAL.md)。


[fullscreen_resolved_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/fullscreen_resolved.gif
[AndroidManifest_xml_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/AndroidManifest.xml#L25
[KPSwitchFSPanelFrameLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelFrameLayout.java
[KPSwitchFSPanelLinearLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelLinearLayout.java
[KPSwitchFSPanelRelativeLayout_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/widget/KPSwitchFSPanelRelativeLayout.java
[ChattingResolvedHandleByPlaceholderActivity_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/app/src/main/java/cn/dreamtobe/kpswitch/demo/activity/ChattingResolvedHandleByPlaceholderActivity.java
[KPSwitchConflictUtil_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KPSwitchConflictUtil.java
[KeyboardUtil_attach_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L134
[IFSPanelConflictLayout_recordKeyboardStatus_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/IFSPanelConflictLayout.java#L37
