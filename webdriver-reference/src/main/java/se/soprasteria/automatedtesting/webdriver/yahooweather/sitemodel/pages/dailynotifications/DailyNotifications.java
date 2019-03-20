package se.soprasteria.automatedtesting.webdriver.yahooweather.sitemodel.pages.dailynotifications;


public interface DailyNotifications {
    public abstract void enableNotifications();
    public abstract void disableNotifications();
    public abstract boolean isPageLoaded();
    public abstract void acceptAndroidPermissionPopups();
}
