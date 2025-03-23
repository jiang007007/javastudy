package org.jj.until;

public class StringUtil {

    private static final String PACKAGE_SEPARATOR_CHAR = ".";

    public static String simpleClassName(Class<?> clazz) {
        String clazzName = clazz.getName();
        int lastDotIdx = clazzName.lastIndexOf(PACKAGE_SEPARATOR_CHAR);
        //存在点结尾的
        if (lastDotIdx > -1) {
            return clazzName.substring(lastDotIdx + 1);
        }
        return clazzName;
    }
}
