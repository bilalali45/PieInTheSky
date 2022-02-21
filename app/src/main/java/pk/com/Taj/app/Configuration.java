package pk.com.Taj.app;

import pk.com.Taj.app.beans.AddressDetail;
import pk.com.Taj.app.beans.FilterDetail;
import pk.com.Taj.app.beans.LocationDetail;
import pk.com.Taj.app.beans.OrderMaster;
import pk.com.Taj.app.beans.ShortcutFilterDetail;
import pk.com.Taj.app.beans.User;

import java.util.List;

public class Configuration {

    private static boolean appLoaded = false;
    private static AppLoginWith appLoginWith;
    private static boolean isLogin;
    private static boolean isOfflineLogin;
    private static User user;
    private static LocationDetail currentLocation;
    private static boolean locationSet;
    private static List<FilterDetail> filterDetails;
    private static List<ShortcutFilterDetail> shortcutFilterDetails;
    private static OrderMaster orderMaster = null;
    private static List<AddressDetail> addressList = null;

    public static String getBuildVersion() {
        return BuildConfig.VERSION_NAME;
    }

    public static boolean isAppLoaded() {
        return appLoaded;
    }

    public static void setAppLoaded(boolean appLoaded) {
        Configuration.appLoaded = appLoaded;
    }
    public static boolean isLogin() {
        return isLogin;
    }

    public static void setLogin(boolean login) {
        Configuration.isLogin = login;
    }

    public static boolean IsOfflineLogin() {
        return isOfflineLogin;
    }

    public static void setOfflineLogin(boolean offlineLogin) {
        Configuration.isOfflineLogin = offlineLogin;
    }

    public static AppLoginWith getAppLoginWith() {
        return appLoginWith;
    }

    public static void setAppLoginWith(AppLoginWith appLoginWith) {
        Configuration.appLoginWith = appLoginWith;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        Configuration.user = user;
    }

    public static String getUserId()
    {
        return user.getUserId();
    }

    public static boolean isLocationSet() {
        return locationSet;
    }

    public static void setIsLocationSet(boolean locationSet) {
        Configuration.locationSet = locationSet;
    }

    public static LocationDetail getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(LocationDetail currentLocation) {
        Configuration.currentLocation = currentLocation;
    }

    public static List<FilterDetail> getFilterDetails() {
        return filterDetails;
    }

    public static void setFilterDetails(List<FilterDetail> filterDetails) {
        Configuration.filterDetails = filterDetails;
    }

    public static List<ShortcutFilterDetail> getShortcutFilterDetails() {
        return shortcutFilterDetails;
    }

    public static void setShortcutFilterDetails(List<ShortcutFilterDetail> shortcutFilterDetails) {
        Configuration.shortcutFilterDetails = shortcutFilterDetails;
    }

    public enum AppLoginWith {
        User("User"), Facebook("Facebook"), GoogleSignIn("GoogleSignIn");
        public String value;

        private AppLoginWith(String value) {
            this.value = value;
        }
    }


    public static OrderMaster getOrderMaster() {
        return orderMaster;
    }

    public static void setOrderMaster(OrderMaster orderMaster) {
        Configuration.orderMaster = orderMaster;
    }

    public static List<AddressDetail> getAddressList() {
        return addressList;
    }

    public static void setAddressList(List<AddressDetail> addressList) {
        Configuration.addressList = addressList;
    }
}
