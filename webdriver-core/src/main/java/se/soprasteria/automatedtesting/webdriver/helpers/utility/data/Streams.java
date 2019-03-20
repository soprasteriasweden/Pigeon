package se.soprasteria.automatedtesting.webdriver.helpers.utility.data;

import java.util.List;
import java.util.stream.Collectors;

public class Streams {
    public static <T> List<T> exclude(List<T> from, List<T> what) {
        return from.stream().filter(e -> !what.contains(e)).collect(Collectors.toList());
    }
}
