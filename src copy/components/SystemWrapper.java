package components;

public class SystemWrapper {

    public static String getEnv(String key) {
        return System.getenv(key);
    }

    public static String getOSName() {
        return System.getProperty("os.name");
    }

    public static String getUserHome() {
        return System.getProperty("user.home");
    }

    public static String getUserDir() {
        return System.getProperty("user.dir");
    }
}
