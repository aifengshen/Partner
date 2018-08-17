package com.cebbank.partner.utils;


import android.util.Log;

/**
 * @ClassName: AtHomeAPPCSC
 * @Description:打印日志工具类
 * @Author Pjw
 * @date 2018/3/2 11:10
 */
public class LogUtils {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static int level = ERROR;

    public static void v(String tag, String msg) {
        if (level <= VERBOSE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (level <= DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (level <= INFO) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (level <= WARN) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (level <= ERROR) {
            if (msg.length() > 3000) {
                for (int i = 0; i < msg.length(); i += 3000) {
                    if (i + 3000 < msg.length())
                        Log.e(tag, msg.substring(i, i + 3000));
                    else
                        Log.e(tag, msg.substring(i, msg.length()));
                }
            } else {
                Log.e(tag, msg);
            }
        }
    }

    public static void e(String msg) {
        if (level <= ERROR) {
            Log.e("Log打印：", msg);
        }
    }
}
