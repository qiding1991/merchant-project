package com.kankan.merchant.utils;

import com.google.gson.Gson;
import org.slf4j.Logger;

public class LogUtil {

    private static Gson gson = new Gson();

    public static void printLog(Logger log,String desc, Object param) {
        log.info(desc + " method input param ========>>{}",gson.toJson(param));
    }
}
