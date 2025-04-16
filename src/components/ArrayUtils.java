package components;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtils {

    public static int length(Object obj) {
        if (obj instanceof List) {
            return ((List<?>) obj).size();
        } else if (obj != null && obj.getClass().isArray()) {
            return Array.getLength(obj);
        } else {
            return 0;
        }
    }

    public static void push(List<Object> list, Object element) {
        list.add(element);
    }


    public static Object pop(List<Object> list) {
        if (list.size() == 0) {
            return null;
        }
        return list.remove(list.size() - 1);
    }

    public static void unshift(List<Object> list, Object element) {
        list.add(0, element);
    }

    public static Object shift(List<Object> list) {
        if (list.size() == 0) {
            return null;
        }
        return list.remove(0);
    }

    public static List<String> split(String source, String delimiter) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }

        if (delimiter == null || delimiter.isEmpty()) {
            return Collections.singletonList(source);
        }

        List<String> result = new ArrayList<>();
        int startIndex = 0;
        int endIndex;

        while ((endIndex = source.indexOf(delimiter, startIndex)) != -1) {
            result.add(source.substring(startIndex, endIndex));
            startIndex = endIndex + delimiter.length();
        }

        result.add(source.substring(startIndex));

        return result;
    }


    public static String joinArray(List<?> list, String delimiter) {
        if (list == null || delimiter == null) return "";
        return list.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }
}
