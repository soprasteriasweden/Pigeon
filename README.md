# Pigeon

Pigeon is an Open Source test automation framework powered by Sopra Steria Sweden. 
It provides the functionality for writing and running automated tests on multiple platforms (Android & iOS-devices, Web and Windows applications) and on all major browsers. 
It can also easily be used for testing the API-layer of the application.
It is a java-based framework and built upon the existing open-source automation-frameworks Selenium and Appium.

## Getting started
Congratulations! You are only a few steps from have written and run your first automated browser test with Pigeon!

This guide will take you through the following steps: 
* Setting up 


of setting up the framework on your computer



#### Requirements
* [JDK 1.8+](https://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Maven](https://maven.apache.org/)
* [Intellij](https://www.jetbrains.com/idea/), [Eclipse](https://www.eclipse.org/ide/) or other preferred IDE

For running tests on mobile devices
* [Appium](http://appium.io/) (Install via [npm](https://nodejs.org/en/) or [Appium Desktop](https://github.com/appium/appium-desktop/releases/))
* iOS-specific: MacOSX. XCode w/ Command Line Tools
* Android-specific: Mac OSX or Windows or Linux.
                    Android SDK ≥ 16


#### Building the jar
1. Clone repo from terminal/cmd:

`git clone https://github.com/soprasteriasweden/Pigeon.git`

2. Open terminal and navigate to <i>webdriver-core</i> in project directory. 

3. Run command:
`mvn clean install`

#### Setting up the project

1. Start new maven project in preferred IDE.

2. Add dependency for the pigeon library in pom.xml:

```
<dependencies>
   <dependency>
      <groupId>se.soprasteria</groupId>
      <artifactId>pigeon</artifactId>
      <version>@release.version@</version>
   </dependency>
</dependencies>
```

If maven project is not automatically imported, do it manually.