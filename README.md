#swt-bling
Behold, swt-bling!  A project that will help you enhance your mundane [swt](http://www.eclipse.org/swt/) where others may fail.  Perhaps you have stumbled upon this project because you're bad at spelling and it's so largely popular that it was the first in your search for "sweet bling".  In that event, may we direct you to some [truly sweet bling](http://bit.ly/1ho6tNM).  If, on the other hand, you've been saddled with SWT or have chosen such a life, but are disappointed in the rather... er... pedestrian look and feel, interactions, and general "sweetness" of the standard complement of widgets (and yes, we know, not all UI elements in SWT inherit from Widget) then you've come to the right place.  In an attempt to make your life better (studies show that 90% of the users of swt-bling will live longer, but don't ask us for a reference), we offer you the "sweetness" that is swt-bling.  Feel free to reduce, reuse, and recycle (and maybe even contribute) as needed.

Don't fret, fellow SWT-user for swt-bling is surely the answer to ~~all of~~ *some of* your problems.

##Build Status
[![Build Status](https://drone.io/github.com/ReadyTalk/swt-bling/status.png)](https://drone.io/github.com/ReadyTalk/swt-bling/latest)

##Building
Building swt-bling is simple. Just open a terminal and run
```
./gradlew build
```
from the root of the repository. You'll have a fresh build of the widgets in build/libs/swt-bling.jar.

##GUI Tests
In addition to standard unit tests, swt-bling has gui-based tests located in `src/integTest/java`. To run them:
```
./gradlew integTest
```

##Examples
Want to see a widget in action? We provide simple example apps for each of our widgets. To launch them, simply call a `runExample<WidgetName>`. For instance:
```
./gradlew runExampleSquareButton
```

##IDE Setup
### IntelliJ
Most active contributors use [IntelliJ](http://www.jetbrains.com/idea/) as their primary IDE. Simply import the project
by pointing IntelliJ at the build.gradle file at the root of the project.

You'll need to inform Gradle about our examples and integTest directory. Do so by right clicking the swt-bling project and choosing "Open Module Settings (F4)".
* Mark src/examples/java as "Sources"
* Mark src/integTest/java as "Test Sources"

Note: On OS X, if you receive a ```Invalid Thread Access``` message, you'll need to open the Run Configuration and add ```-XstartOnFirstThread``` to the VM Options text input box.
