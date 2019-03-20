package se.soprasteria.automatedtesting.webdriver.twitter.sitemodel.pages.loginpage;


import se.soprasteria.automatedtesting.webdriver.helpers.base.baseconfig.config.User;

public interface LoginPage {

    public abstract boolean isPageLoaded ();
    public void performLogin(User user);

}
