# Android键盘面板冲突 布局闪动处理方案

[![Download][bintray_svg]][bintray_link]
![][license_2_svg]

---

> 起源，之前在微信工作的时候，为了给用户带来更好的基础体验，做了很多尝试，踩了很多输入法的坑，特别是动态调整键盘高度，二级页面是透明背景，魅族早期的smartbar等, 后来逐一完善了，考虑到拥抱开源，看业界还是有很多应用存在类似问题。就有了这个repo

---

> 之前有写过一篇核心思想: [Switching between the panel and the keyboard in Wechat](http://blog.dreamtobe.cn/2015/02/07/Switching-between-the-panel-and-the-keyboard/)


> 这里主要是根据核心思想的实践，实践原理是通过`CustomRootLayout`布局变化，来获知是否是键盘引起的真正的布局变化，进而处理到接下来`PanelLayout`的`onMersure`中。

我们可以看到微信中的 从键盘与微信的切换是无缝的，而且是无闪动的，这种基础体验是符合预期的。

但是实际中，简单的 键盘与面板切换 是会有闪动，问题的。今天我们就实践分析与解决这个问题。

<!--more-->
## 最终效果对比:

![][resolve_mv_gif]![][unresolve_mv_gif]
![][resolve_dynamic_mv_gif]![][unresolve_dynamic_mv_gif]


## 如何使用

在`build.gradle`中:

```
compile 'cn.dreamtobe.kpswitch:library:1.3.0'
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

> 如果发现你的键盘切换底色为黑色那是因为你的主题使用的是黑色背景，将对应的主题背景颜色改为白色即可，还有任何问题欢迎提[issue](https://github.com/Jacksgong/JKeyboardPanelSwitch/issues/new)

> 还有不明白或者要测试的，建议参考项目demo中的`JChattingActivity`

---

## 基本原理答疑

#### 为什么`onMeasure`、`onLayout`、`onGlobalLayout` 分别在这三个里面做不同的处理?

##### 1. 为什么需要在`CustomRootLayout`的`onMeasure`中判断，而不是其他地方判断是否是真正键盘引起变化的?

 必须要在`PanelLayout`的`onMeasure`之前获知键盘变化，因此比较恰当的地方就是`PanelLayout`的父布局的`onMeasure`，ps: 其实真正感知键盘变化我们可以非常确定(肯定会受到键盘变化影响的)的也只有对我们可见的最外层的布局。综合之，并且就就封装而言就只能是`CustomRootLayout`的`onMeasure`。

##### 2. 为什么需要在`PanelLayout`的`onMeasure`中做防止面板闪现而不是其他地方?

如果在onLayout或者其他点中处理，都没有`onMeasure`中处理简单精准，`PanelLayout`的`onMeasure`结果的影响不仅仅是`PanelLayout`自己将要进行的大小与layout，还有其他View的布局layout与大小。

##### 3. 为什么要在`CustomRootLayout`的`onLayout`来判断是否键盘弹起，而不是其他地方?

`CustomRootLayout`的`onLayout`中判断，是准确 与 获知变化时间比较早 的权衡结果。

##### 4. 为什么要在`onGlobalLayout`中处理真正的键盘变化并且进行键盘高度变化存储?

由于`onMeasure`与`onLayout`可能被多次调用，而`onGlobalLayout`是布局变化后只会被一次调用，并且我们需要处理所有键盘高度的变化(如搜狗输入法的动态调整键盘高度)因此在`onGlobalLayout`中计算键盘高度变化以及有效高度进行存储更为恰当。

#### 能不能只在一个View里面做所有的处理?

我们需要较早的布局变化中获知是否是键盘变化，至少要在面板大小变化进而导致布局变化之前获知，并且需要十分确定该布局受到键盘影响肯定会随之变化(对我们而言只能是可见的顶层布局) ，并且需要处理掉面板闪动的那一帧，结合前面问题的解答。这里无法避免就引入了两个布局, 对于我们可见的顶级布局(`CustomRootLayout`)与面板布局(`PanelLayout`)。

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
[license_2_svg]: https://img.shields.io/hexpm/l/plug.svg
[bintray_link]: https://bintray.com/jacksgong/maven/JKeyboardPanelSwitch/_latestVersion
[bintray_svg]: https://api.bintray.com/packages/jacksgong/maven/JKeyboardPanelSwitch/images/download.svg
[resolve_mv_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/img/resolve_mv.gif
[unresolve_mv_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/img/unresolve_mv.gif
[resolve_dynamic_mv_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/img/resolve_dynamic_mv.gif
[unresolve_dynamic_mv_gif]: https://raw.githubusercontent.com/Jacksgong/JKeybordPanelSwitch/master/img/unresolve_dynamic_mv.gif
