# The handler for the keyboard and panel layout conflict in Android

[![Download][bintray_svg]][bintray_link]
[![Build Status][build_status_svg]][build_status_link]
[![](https://img.shields.io/badge/SnapShot-1.6.3-white.svg)](https://oss.sonatype.org/content/repositories/snapshots/cn/dreamtobe/kpswitch/library/)

> [中文文档](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/README_zh.md)

---

> This solution was built When I was working in WeChat, what is used for resolving the layout conflict when you switch between the keyboard and the emoji-panel/function-panel.

---

> There is a post to declaration the core rules of this solution: [Switching between the panel and the keyboard in Wechat](http://blog.dreamtobe.cn/2015/02/07/Switching-between-the-panel-and-the-keyboard/)。

![][non-fullscreen_resolved_gif]![][fullscreen_resolved_gif]
![][adjust_resolved_gif]![][adjust_unresolved_gif]

---

## Welcome PR

- Comments as much as possible.
- Commit message format follow: [AngularJS's commit message convention](https://github.com/angular/angular.js/blob/master/CONTRIBUTING.md#-git-commit-guidelines) .
- The change of each commit as small as possible.

---

## INSTALLATION

JKeyboardPanelSwitch is installed by adding the following dependency to your `build.gradle` file:

```groovy
compile 'cn.dreamtobe.kpswitch:library:1.6.1'
```

If you want to import snapshot version, We have already publish the snapshot version to [the sonatype](https://oss.sonatype.org/content/repositories/snapshots/cn/dreamtobe/kpswitch/) so you can import snapshot version after declare the following repository: 
```groovy
allprojects {
  repositories {
      maven { url "https://oss.sonatype.org/content/repositories/snapshots/" }
  }
}
```

## USAGE

> Recommend `clone` the `demo` project and run it, I has already cover cases as much as possible in the demo project.

![](https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/art/demo_snapshot.png)

- [The integration tutorial of The non-fullscreen theme Or The status bar is translucent with `fitsSystemWindows=true`](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/NON-FULLSCREEN_TUTORIAL.md)
- [The integration tutorial of The fullscreen theme Or The status bar is translucent with `fitsSystemWindows=false`](https://github.com/Jacksgong/JKeyboardPanelSwitch/blob/master/FULLSCREEN_TUTORIAL.md)

## PRINCIPLE

- The calculation about the height of keyboard and whether the keyboard is showing，Ref: [KeyboardUtil.KeyboardStatusListener#calculateKeyboardHeight][KeyboardUtil_calculateKeyboardHeight_link]、[KeyboardUtil.KeyboardStatusListener#calculateKeyboardShowing][KeyboardUtil_calculateKeyboardShowing_link]。
- Handle the problem about the layout conflict，Ref: [KPSwitchRootLayoutHandler][KPSwitchRootLayoutHandler_link]，Besides the panel layout used in the case of the non-fullscreen theme: [KPSwitchPanelLayoutHandler][KPSwitchPanelLayoutHandler_link]；The panel layout used in case of the fullscreen theme: [KPSwitchFSPanelLayoutHandler][KPSwitchFSPanelLayoutHandler_link]。


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
