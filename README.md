# Pigeon

Pigeon is an Open Source test automation framework powered by Sopra Steria Sweden. 
It provides the functionality for writing and running automated tests on multiple platforms (Android & iOS-devices, Web and Windows applications) and on all major browsers. 
It can also easily be used for testing the API-layer of the application.
It is a java-based framework and built upon the existing open-source automation-frameworks Selenium and Appium.
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


#### Setting up the project
1 . Clone repo from terminal/cmd:
```
git clone https://github.com/soprasteriasweden/Pigeon.git
```
2 . Navigate to project directory in terminal or cmd. Run command: 
```
mvn clean install
```
3 . Start new maven project in perferred IDE.

4 . Add dependency for pigeon jar in pom.xml:
```
<dependencies>
    <dependency>
        <groupId>se.soprasteria.automatedtesting.webdriver</groupId>
        <artifactId>pigeon</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```
5 . If maven project is not automatically imported, do so manually.
#### Add configuration file

1 . Make sure a <i>resources</i> folder exists under <i>src > test</i>. Otherwise create it. 

2 . Create a new xml-file under resources, name it config.xml.

3 . Add following configuration to the config.xml file:
```
<config>
    <drivers>
        <driver id="chromedriver" type="ChromeDriver version="73.0.3"/>
    </drivers>
</config>
```

4 . Create a class <i>BaseTest.java</i> under <i>src > main</i>

5 . Make <i>BaseTest.java</i> a subclass to <i>BaseTestCase.java</i> by extending it.</i> 

6 . In <i>BaseTest</i> class override method getDefaultPropertyFile() and let it return path to the <i>config.xml</i>: 
```
 @Override
    protected String getDefaultPropertyFile() {
        return "config.xml";
    }
```

7 . Create a new package <i>pages</i> under <i>src > main</i>.

8 . Add new java class <i>MainPage.java</i> under <i>pages</i> folder.

9 . Let class <i>MainPage</i> extend <i>BasePageObject</i>.

10 . Implement method isPageLoaded() and add constructor matching superclass:
```
 public MainPage(AutomationDriver driver) {
         super(driver);
     }
 
     public boolean isPageLoaded() {
         return false;
     }
```