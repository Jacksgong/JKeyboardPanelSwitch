#Android键盘面板冲突 布局闪动处理方案

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


## I. 准备

> 以下建立在`android:windowSoftInputMode`带有`adjustResize`的基础上。

> 如图，为了方便分析，我们分出3个View:

![](https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/img/WeChat_1435588322.png)

- `CustomRootView`: 除去statusBar与ActionBar(ToolBar...balabala)
- `FootRootView`: 整个底部（包括输入框与底部面板在内的整个View）
- `PanelView`: 面板View

> 整个处理过程，其实需要分为两块处理:

1. 从`PanelView`切换到`Keybord`

**现象:** 由于显示`Keybord`时直接`PanelView#setVisibility(View.GONE)`，导致会出现整个`FooterRootView`到底部然后又被键盘顶起。

**符合预期的应该:** 直接被键盘顶起，不需要到底部再顶起。

2. 从`Keybord`切换到`PanelView`

**现象:** 由于隐藏`Keybord`时，直接`PanelView#setVisibility(View.VISIBLE)`，导致会出现整个`FootRootView`先被顶到键盘上面，然后再随着键盘的动画，下到底部。

**符合预期的应该:** 随着键盘收下直接切换到底部，而配有被键盘顶起的闪动。

## II. 处理

#### 原理

在真正由`Keybord`导致布局**真正**将要变化的时候，才对`PanelView`做出适配。（**注意**，所有的判断处理要在`Super.onMeasure`之前完成判断）

#### 方法:

> 通过`CustomRootView`高度的变化，来保证在`Super.onMeasure`之前获得**真正**的由于键盘导致布局将要变化，然后告知`PanelView`，让其在`Super.onMeasure`之前给到有效高度。

#### 需要注意:

> 1) 在`adjustResize`模式下，键盘弹起会导致`CustomRootView`的高度变小，键盘收回会导致`CustomRootView`的高度变大，反之变小。因此可以通过这个机制获知真正的`PanelView`将要变化的时机。


> 2) 由于到了`onLayout`，clipRect的大小已经确定了，又要避免不多次调用`onMeasure`因此要在`Super.onMeasure`之前

> 3) 由于键盘收回的时候，会触发多次`measure`，如果 不判断真正的由于键盘收回导致布局将要变化，就直接给`View#VISIBLE`，依然会有闪动的情况。

> 4) 从`Keybord`切换到`PanelView`导致的布局冲突，只有在`Keybord`正在显示的时候。

> 5) 从`PanelView`切换到`Keybord`导致的布局冲突，已经在`PanelView`与`CustomRootView`中内部处理。

## III. License

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
