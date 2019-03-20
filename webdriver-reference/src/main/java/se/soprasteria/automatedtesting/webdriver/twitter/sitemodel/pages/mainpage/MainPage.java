package se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.mainpage;

public interface MainPage {

    public abstract boolean isPageLoaded ();
    public void performTweet(String message);
    public boolean checkIfTweetHasBeenMade(String message);

}


