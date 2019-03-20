package se.soprasteria.automatedtesting.webdriver.helpers.utility.data;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PatternMatcher {

    public static boolean matches(String str, String regex) {
        return contains(str, "^" + regex + "$");
    }

    public static boolean contains(String str, String regex) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).find();
    }

    public static List<String> getGroups(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        if (!m.find()) {
            return null;
        }
        List<String> groups = new ArrayList<>();
        for (int i=0; i<=m.groupCount(); i++) {
            groups.add(i, m.group(i));
        }
        return groups;
    }

    public static boolean containsTime(String str) {
        return contains(str, "(\\d{1,2} (hour|hours|tunti|tuntia|timer|timme|timmar))* \\d{1,2} (minuter|minutes|minuuttia|minutter)");
    }

    public static String remove(String str, String regex) {
        return str.replaceAll(regex, "");
    }
}
