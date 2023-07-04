package com.vaskka.diary.core.utils;

import org.slf4j.Logger;

public class LogUtil {

    /**
     * info format
     * @param logger Logger
     * @param format format
     * @param params param
     */
    public static void infof(Logger logger, String format, Object... params) {
        if (logger.isInfoEnabled()) {
            Throwable throwable = parseThrowable(params);
            if (throwable != null) {
                Object[] finalParams = new Object[params.length - 1];
                System.arraycopy(params, 0, finalParams, 0, params.length - 1);
                logger.info(format, finalParams, throwable);
            } else {
                logger.info(format, params);
            }
        }
    }

    /**
     * error format
     * @param logger Logger
     * @param format format
     * @param params param
     */
    public static void errorf(Logger logger, String format, Object... params) {
        if (logger.isErrorEnabled()) {
            Throwable throwable = parseThrowable(params);
            if (throwable != null) {
                Object[] finalParams = new Object[params.length - 1];
                System.arraycopy(params, 0, finalParams, 0, params.length - 1);
                logger.error(format, finalParams, throwable);
            } else {
                logger.error(format, params);
            }
        }
    }

    /**
     * parse throwable at last
     * @param params params
     * @return Throwable
     */
    private static Throwable parseThrowable(Object... params) {
        if (params == null || params.length == 0) {
            return null;
        }

        Object last = params[params.length - 1];
        if (last instanceof Throwable) {
            return (Throwable) last;
        }

        return null;
    }
}
