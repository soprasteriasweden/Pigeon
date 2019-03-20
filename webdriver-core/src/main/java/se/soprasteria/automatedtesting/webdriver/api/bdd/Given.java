package se.soprasteria.automatedtesting.webdriver.api.bdd;

import java.lang.annotation.*;


@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //can use in method only.
public @interface Given {

    String regex() default "";

}