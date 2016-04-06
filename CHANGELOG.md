# Change Log
---

### Version 1.4.2 (2016-04-06)

- 修复Jar包中带上了已经弃用的Class，导致Proguard报Warn的问题。

### Version 1.4.1 (2016-03-31)

- 基本处理了全屏主题下的键盘切换问题。
- 为面板布局与根布局适配了`FrameLayout`、`LinearLayout`、`RelativeLayout`并支持拓展其他类型布局。
- 调整接口，更加解耦: 键盘高度与是否显示逻辑封装于`KeyboardUtil`，布局闪动逻辑封装于不同布局的`Handler`中。

### Version 1.3.0 (2016-01-19)

- 修复如果聊天所在界面为透明背景，当前所在界面不是全屏，而背后的界面也不是全屏，导致刚进入时的measure高度变化误认为是键盘升起的bug.

### Version 1.2.1 (2015-12-14)

- minSdkVersion 8->3

### Version 1.2.0 (2015-12-13)

- 修复打开页面是透明主题Activity对组件的影响

### Version 1.1 (2015-11-10)
---

initial release.
