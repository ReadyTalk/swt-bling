#swt-bling [![Build Status](https://travis-ci.org/ReadyTalk/swt-bling.png?branch=master)](https://travis-ci.org/ReadyTalk/swt-bling)

Behold, swt-bling!  A project that will help you enhance your mundane [swt](http://www.eclipse.org/swt/) where others may fail.  Perhaps you have stumbled upon this project because you're bad at spelling and it's so largely popular that it was the first in your search for "sweet bling".  If, on the other hand, you've been saddled with SWT or have chosen such a life, but are disappointed in the rather... er... pedestrian look and feel, interactions, and general "sweetness" of the standard complement of widgets (and yes, we know, not all UI elements in SWT inherit from Widget) then you've come to the right place.  In an attempt to make your life better (studies show that 90% of the users of swt-bling will live longer, but don't ask us for a reference), we offer you the "sweetness" that is swt-bling.  Feel free to reduce, reuse, and recycle (and maybe even contribute) as needed.

Don't fret, fellow SWT-user for swt-bling is surely the answer to ~~all of~~ *some of* your problems.

##Widgets
###[Bubble](http://oss.readytalk.com/swt-bling/javadoc/com/readytalk/swt/widgets/notifications/Bubble.html)
Bubble is a ToolTip that is far more customizable than the [ToolTip](http://help.eclipse.org/indigo/index.jsp?topic=%2Forg.eclipse.platform.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fswt%2Fwidgets%2FToolTip.html) provided by SWT.
Bubble can be applied to any SWT widget that inherits from ```Control```.
<p align="center">
  <img src="https://raw.github.com/ReadyTalk/swt-bling/gh-pages/images/widgets/bubble.png" alt="Bubble Widget" />
</p>

You can also apply Bubbles to any element that implements the ```CustomElementDataProvider``` interface (useful for apply ToolTips to elements drawn with ```GC```).

<p align="center">
  <img src="https://raw.github.com/ReadyTalk/swt-bling/gh-pages/images/widgets/customBubble.png" alt="Custom Bubble Widget" />
</p>

Additionally, you can apply tags to Bubble and issue commands to show/hide all Bubbles with a particular tag. This is especially useful when exposing a user to a new feature.
###[SquareButton](http://oss.readytalk.com/swt-bling/javadoc/com/readytalk/swt/widgets/buttons/SquareButton.html)
SquareButton is a simple widget that can be used in place of the standard SWT [Button](http://help.eclipse.org/helios/nftopic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/swt/widgets/Button.html). Unlike the traditional Button, it looks the same across different platforms.
<p align="center">
  <img src="https://raw.github.com/ReadyTalk/swt-bling/gh-pages/images/widgets/squareButton.png" alt="SquareButton Widget" />
</p>
The SquareButton is far more customizable than the SWT Button.
###[TextPainter](http://oss.readytalk.com/swt-bling/javadoc/com/readytalk/swt/text/painter/TextPainter.html)
TextPainter is a utility class used to render text onto a ```Composite``` within a given boundary with a call to its handlePaint method.
<p align="center">
  <img src="https://raw.github.com/ReadyTalk/swt-bling/gh-pages/images/widgets/textPainter.png" alt="Text Painter" />
</p>
TextPainter can understand a subset of WikiText formatting, which makes it easy to use Wiki formatting within SWT.

##Examples
Want to see a widget in action? We provide simple example apps for each of our widgets. To launch them, simply call a `runExample<WidgetName>`. For instance:
```
./gradlew runExampleSquareButton
```

##Artifacts
###Releases
Releases are created using the ReadyTalk internal Jenkins and uploaded to Maven Central by way of Sonatype. You can declare a Maven Dependency as follows (using any tagged [release](https://github.com/ReadyTalk/swt-bling/releases)):
```
<dependency>
    <groupId>com.readytalk</groupId>
    <artifactId>swt-bling</artifactId>
    <version>0.1.0</version>
</dependency>
```
###Snapshots
Snapshots are created and uploaded to [Sonatype](http://oss.sonatype.org/content/repositories/snapshots/com/readytalk/swt-bling/) on each push to master. Travis-CI handles the publish automagically.

##Building
Building swt-bling is simple. Just open a terminal and run
```
./gradlew build
```
from the root of the repository. You'll have a fresh build of the widgets in build/libs/swt-bling.jar.

##Documentation
Javadoc is available for swt-bling [here](http://oss.readytalk.com/swt-bling/javadoc/).

##Wiki
Additional information is available on [our wiki](https://github.com/ReadyTalk/swt-bling/wiki)
