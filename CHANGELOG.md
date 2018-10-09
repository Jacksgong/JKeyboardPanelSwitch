# Change Log
---

> [中文迭代文档](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/CHANGELOG_zh.md)

### Version 1.6.2 (2018-10-09)

- Feat: carry back the clicked view to the SwitchClickListener closes #87

### Version 1.6.1 (2018-01-12)

- Fix(crash): Cover the case of user-root-view not ready on global-changed callback, refs: http://crashes.to/s/00beea75992

### Version 1.6.0 (2016-08-11)

- Add `KPSwitchConflictUtil#attach(panelLayout,focusView,switchClickListener,subPanelAndTriggers...)`/`KPSwitchConflictUtil#attach(panelLayout,focusView,subPanelAndTriggers...)` Used for `attach` the case of one `PanelLayout` contains more than one `SubPanel`. Closes #39.

### Version 1.5.0 (2016-08-03)

- Fix: In the `Translucent Status` theme，some devices(Such as:Samsung S7 edge) the arithmetic used for calculating keyboard height is wrong. Closes #35 。
- Fix: In some devices(Such as: HuaWei Mate 7)The showing or hiding from the NavigationBar is mistaken for the keyboard showing or hiding. Merged #33 , Closes #34 , By @sollian 。
- Add `KeyboardUtil#detach` convenient to remove the `ViewTreeObserver.OnGlobalLayoutListener` inside the architecture. Merged #33 , By @sollian 。

### Version 1.4.6 (2016-04-26)

- Support `Translucent Status` theme，the more detail please move to demo project or tutorial docs. Closes #27 。
- Add the option configuration of `whether the height of the panel need to ensure equal to the height of the keyboard `: `setIgnoreRecommendHeight(boolean)` and the reference configuration params in the xml: `cn.dreamtobe.kpswitch.R.styleable#KPSwitchPanelLayout_ignore_recommend_height`。Closes #25.

### Version 1.4.5 (2016-04-21)

- Fix: The wrong arithmetic about calculating the keyboard height when the Page is extends from `Activity` or `FragmentActivity`, which result in the the layout conflict. Closes #24.

### Version 1.4.4 (2016-04-19)

- Fix: In some special cases(such as some inherited from `FragmentActivity`),it possible raise a bug in the wrong logic of determining whether the layout changed is triggered by the fullscreen theme page is being opened. Closes #21.

### Version 1.4.3 (2016-04-13)

- No longer clear focus automatically when switching to the panel when you use the `KPSwitchConflictUtil#attach`, since sometimes you want to show some emoji panel, which need the focus in the EditText view.
- Add the `SwitchClickListener` param in the `KPSwitchConflictUtil#attach` method for listening the click event of showing the keyboard and the panel.

### Version 1.4.2 (2016-04-06)

- Fix raising warning because carry some deprecated classes.

### Version 1.4.1 (2016-03-31)

- Handle the conflict between the keyboard and the panel layout in the fullscreen theme.
- Add some root layouts for panel: `FrameLayout`、`LinearLayout`、`RelativeLayout` and support to extend other layouts.
- Adjust some interface to make they more decoupled: Wrap the logic of showing keyboard and calculating the keyboard height to `KeyboardUtil`; Wrap the logic of handling the conflict of switching the keyboard and panel to the `Handler` of each layout.

### Version 1.3.0 (2016-01-19)

- Fix: when the chat-Activity is translucent and the current interface is not fullscreen theme with the behind interface isn't fullscreen theme either, we mistake for the height change in `measure` is the keyboard did.

### Version 1.2.1 (2015-12-14)

- minSdkVersion 8->3

### Version 1.2.0 (2015-12-13)

- Fix: the `Activity` theme is translucent.

### Version 1.1 (2015-11-10)
---

initial release.
