package com.example.imgtest;

import android.content.Context;
import android.os.FileUtils;
import android.util.Log;

import com.cvt.library.clog.CLog;
import com.cvt.library.clog.Log4aFileLogEngine;
import com.cvt.library.clog.LogOptions;

import java.io.File;

public class LogUtils {


    //配置cLog
    public static void initConfig(Context context) {
        LogOptions options = new LogOptions();
        options.setCustomWrapper(true);
        //设置日志引擎为log4a
        options.setFileLogEngine(new Log4aFileLogEngine(context.getApplicationContext()));
        //设置日志文件前缀
        options.setLogFileNamePrefix("dd55_log");
        options.setFileLogLevel(Log.INFO);
        options.setLogDir(context.getExternalCacheDir().getAbsolutePath() +File.separator + "clog/");

        CLog.init(true, options);

    }

    public static void i(Object o) {
        CLog.i(o);
    }

    public static void d(Object o) {
        CLog.d(o);
    }

    public static void e(Object o) {
        CLog.e(o.toString());
    }

    public static void v(Object o) {
        CLog.v(o);
    }

    public static void w(Object o) {
        CLog.w(o);
    }

    public static void f(Object o) {
        CLog.file(o);
    }

//    private static String getFunctionName() {
//        return getFunctionName(false);
//    }

//    /**
//     * 获取当前线程、类、方法、行
//     */
//    @SuppressWarnings("SameParameterValue")
//    private static String getFunctionName(boolean isThreadBreak) {
//        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
//        if (sts == null) return "";
//        for (StackTraceElement st : sts) {
//            if (st.isNativeMethod()) {
//                continue;
//            }
//            if (st.getClassName().equals(Thread.class.getName())) {
//                continue;
//            }
//            if (st.getClassName().equals(LogUtils.class.getName()) || st.getClassName().equals(LogUtils.class.getName() + "$Companion")) {
//                continue;
//            }
//            if (isThreadBreak) {
//                return "[ " + Thread.currentThread().getName() + ": \n("
//                        + st.getFileName() + ":" + st.getLineNumber() + ") "
//                        + st.getMethodName() + " ]";
//            } else {
//                return "[ " + Thread.currentThread().getName() + ": ("
//                        + st.getFileName() + ":" + st.getLineNumber() + ") "
//                        + st.getMethodName() + " ]";
//            }
//        }
//        return "";
//    }
}