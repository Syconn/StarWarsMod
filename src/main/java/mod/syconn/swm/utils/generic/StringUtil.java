package mod.syconn.swm.utils.generic;

import org.apache.commons.lang3.text.WordUtils;

public class StringUtil {

    public static String makeLightsaberName(String id) {
        return WordUtils.capitalize(id.replace("_", " "));
    }

    public static String trimPrefix(String str, String prefix) {
        if (str.startsWith(prefix)) return str.substring(prefix.length());
        return str;
    }

    public static String trimSuffix(String str, String suffix) {
        if (str.endsWith(suffix)) return str.substring(0, str.length() - suffix.length());
        return str;
    }
}
