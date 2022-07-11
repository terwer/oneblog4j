package com.terwergreen.util;

import com.terwergreen.constant.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理类
 *
 * @name: DateUtil
 * @author: terwer
 * @date: 2022-07-11 11:27
 **/
public class DateUtil {
    public static String parseDatestr(Object datastrObj) {
        String datastr = null;
        try {
            if (null == datastrObj) {
                return null;
            }

            if (datastrObj instanceof Date) {
                Date dt = (Date) datastrObj;
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_CN_24);
                return sdf.format(dt);
            }

            if (datastrObj instanceof String) {
                // 空值处理
                datastr = String.valueOf(datastrObj);
                // 如果是格式化好的直接返回
                if (datastr.contains("-") || datastr.contains("/")) {
                    return datastr;
                }
                // 重新处理
                DateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
                Date newdt = df.parse(datastr);
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_CN_24);
                return sdf.format(newdt);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
