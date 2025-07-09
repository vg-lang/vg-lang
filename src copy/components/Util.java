package components;

public class Util {
    public static String getType(Object variable) {
        if (variable == null) {
            return "null";
        }
        return variable.getClass().getSimpleName();
    }
    
    public static Integer toInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
    
    public static Double toDouble(Object value) {
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }
    
    public static String toString(Object value) {
        if (value == null) {
            return "null";
        }
        return value.toString();
    }
    
    public static Boolean toBoolean(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue() != 0;
        }
        if (value instanceof String) {
            String str = ((String) value).toLowerCase();
            return str.equals("true") || str.equals("yes") || str.equals("1");
        }
        return false;
    }
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (ErrorHandler.VGException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static int indexOfString(String s, String sub) {
        return s.indexOf(sub);
    }
    public static String substringString(String s, int start, int end) {
        return s.substring(start, end);
    }
    public static int stringLength(String s) {
        return s.length();
    }
    public static String upperCase(String s) {
        return s.toUpperCase();
    }
    
    public static String charToString(int charCode) {
        return String.valueOf((char) charCode);
    }
    
    public static int charToInt(String character) {
        if (character == null || character.isEmpty()) {
            return 0;
        }
        return (int) character.charAt(0);
    }
}
