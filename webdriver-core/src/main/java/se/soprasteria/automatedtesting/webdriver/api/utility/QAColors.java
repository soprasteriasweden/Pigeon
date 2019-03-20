package se.soprasteria.automatedtesting.webdriver.api.utility;

import java.awt.*;

/**
 * Colors used in the framework, mostly when debugging.
 */
public class QAColors {

    /**
     * The states a test can be in.
     */
    public enum State {
        FAIL,
        PASS,
        NEUTRAL,
        SKIP
    }

    /**
     * Get a color based on a specific state.
     *
     * @param state
     * @return - AWT color for the specific state
     */
    public static Color getColor(State state) {
        switch (state) {
            case FAIL:
                return Color.RED;
            case PASS:
                return Color.GREEN;
            case NEUTRAL:
                return Color.ORANGE;
            case SKIP:
                return Color.BLUE;
            default:
                return Color.black;
        }
    }

}
