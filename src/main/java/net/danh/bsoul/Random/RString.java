package net.danh.bsoul.Random;

import java.util.List;
import java.util.Random;

/**
 * @version 1.2
 */
public class RString {

    /**
     * @param list RString
     * @return random
     */
    public static String getRandomString(List<String> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
