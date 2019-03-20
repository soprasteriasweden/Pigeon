package se.soprasteria.automatedtesting.webdriver.api.bdd;

import java.lang.annotation.*;



@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
public @interface When {

    String regex() default "";

    // TODO make matching of args in here
    //String [] argsRegex();

    // TODO create a function for extracting args from line

}