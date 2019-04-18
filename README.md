# Pigeon

Pigeon is an Open Source test automation framework powered by Sopra Steria Sweden. 
It provides the functionality for writing and running automated tests on multiple platforms (Android & iOS-devices, Web and Windows applications) and on all major browsers. 
It can also easily be used for testing the API-layer of the application.
It is completely written in java.
## Getting started

#### Requirements
* [JDK 1.8+](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/)
* [Intellij](https://www.jetbrains.com/idea/), [Eclipse](https://www.eclipse.org/ide/) or other preferred IDE

For running tests on mobile devices
* [Appium](http://appium.io/) (Install via [npm](https://nodejs.org/en/) or [Appium Desktop](https://github.com/appium/appium-desktop/releases/))
* iOS-specific: MacOSX. XCode w/ Command Line Tools
* Android-specific: Mac OSX or Windows or Linux.
                    Android SDK ≥ 16


#### Setting up project
1. Clone project 
2. Navigate to project directory in terminal or cmd. Run command: 

```mvn clean install```
3. Start new maven project in perferred IDE.
4. Add dependency for pigeon jar:
```
<dependency>
<groupId>se.soprasteria.automatedtesting.webdriver</groupId>
<artifactId>pigeon</artifactId>
<version>1.0.0</version>
</dependency>
```

