#swt-bling

##Build Status
[![Build Status](https://drone.io/github.com/ReadyTalk/swt-custom-widgets/status.png)](https://drone.io/github.com/ReadyTalk/swt-custom-widgets/latest)

##Building
Building swt-bling is simple. Just open a terminal and run
```
gradlew build
```
from the root of the repository. You'll have a fresh build of the widgets in build/libs/swt-bling.jar.

##Examples
Want to see a widget in action? We provide simple example apps for each of our widgets. To launch them, simply call a 'runExample<WidgetName>'. For instance:
```
gradlew runExampleSquareButton
```

##IDE Setup
### IntelliJ
Most active contributors use [IntelliJ](http://www.jetbrains.com/idea/) as their primary IDE. Simply import the project
by pointing IntelliJ at the build.gradle file at the root of the project.