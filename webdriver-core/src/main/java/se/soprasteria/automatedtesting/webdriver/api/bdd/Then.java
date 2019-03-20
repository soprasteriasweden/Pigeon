package se.soprasteria.automatedtesting.webdriver.api.bdd;

import java.lang.annotation.*;



@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Then {

    String regex() default "";
    boolean value() default true;

}