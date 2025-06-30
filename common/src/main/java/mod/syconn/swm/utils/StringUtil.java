package mod.syconn.swm.utils;

import org.apache.commons.lang3.text.WordUtils;

public class StringUtil {

    public static String makeLightsaberName(String id) {
        return WordUtils.capitalize(id.replace("_", " "));
    }
}
