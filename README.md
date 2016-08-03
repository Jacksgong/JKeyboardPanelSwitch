# Android键盘面板冲突 布局闪动处理方案

[![Download][bintray_svg]][bintray_link]
[![Build Status][build_status_svg]][build_status_link]

---

> 起源，之前在微信工作的时候，为了给用户带来更好的基础体验，做了很多尝试，踩了很多输入法的坑，特别是动态调整键盘高度，二级页面是透明背景，魅族早期的Smart bar等, 后来逐一完善了，考虑到拥抱开源，看业界还是有很多应用存在类似问题。就有了这个repo

---

> 之前有写过一篇核心思想: [Switching between the panel and the keyboard in Wechat](http://blog.dreamtobe.cn/2015/02/07/Switching-between-the-panel-and-the-keyboard/)。

![][non-fullscreen_resolved_gif]![][fullscreen_resolved_gif]
![][adjust_resolved_gif]![][adjust_unresolved_gif]


---

## 欢迎提交 Pull requests

- 尽量多的英文注解。
- 每个提交尽量的细而精准。
- Commit message 遵循: [AngularJS's commit message convention](https://github.com/angular/angular.js/blob/master/CONTRIBUTING.md#-git-commit-guidelines)。
- 尽可能的遵循IDE的代码检查建议(如 Android Studio 的 'Inspect Code')。

---

## 如何使用

在`build.gradle`中引入:

```
compile 'cn.dreamtobe.kpswitch:library:1.5.0'
```

## 使用引导

> 可以考虑`clone`下来跑下项目中的`demo`，已经用尽量简洁的代码尽量覆盖所有Case了。

![](https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/demo_snapshot.jpg)

- [非全屏主题 或者 透明状态栏主题并且在`fitsSystemWindows=true` 情况下使用引导](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/NON-FULLSCREEN_TUTORIAL.md)
- [全屏主题 或者 透明状态栏主题并且在 `fitsSystemWindows=false` 情况下使用引导](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/FULLSCREEN_TUTORIAL.md)

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
[KeyboardUtil_calculateKeyboardHeight_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L214
[KeyboardUtil_calculateKeyboardShowing_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/util/KeyboardUtil.java#L266
[KPSwitchRootLayoutHandler_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/handler/KPSwitchRootLayoutHandler.java
[KPSwitchPanelLayoutHandler_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/handler/KPSwitchPanelLayoutHandler.java
[KPSwitchFSPanelLayoutHandler_link]: https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/library/src/main/java/cn/dreamtobe/kpswitch/handler/KPSwitchFSPanelLayoutHandler.java
