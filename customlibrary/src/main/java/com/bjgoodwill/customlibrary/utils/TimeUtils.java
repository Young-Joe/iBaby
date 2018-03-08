package com.bjgoodwill.customlibrary.utils;

import com.bjgoodwill.customlibrary.common.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * 时间管理类
 *    提供各种时间/日期转换以及一些日期计算
 * Created by QiaoJF on 17/11/23.
 */

public class TimeUtils {

    public static String getCurrentDate() {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyyMMdd");
        String name = sdfDateFormat.format(System.currentTimeMillis());
        return name;
    }

    /**
     * "yyyy-MM-dd"
     *
     * @return
     */
    public static String getCurrentDate1() {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String name = sdfDateFormat.format(System.currentTimeMillis());
        return name;
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String name = sdfDateFormat.format(System.currentTimeMillis());
        return name;
    }

    public static String getCurrentTime1() {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("HH:mm");
        String name = sdfDateFormat.format(System.currentTimeMillis());
        return name;
    }

    public static int getCurrentYears() {
        Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        return year;
    }

    public static String getCurrentTimeToLocal() {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        String name = sdfDateFormat.format(System.currentTimeMillis());
        return name;
    }

    /**
     * 得到对应的日期
     *
     * @param currentTime
     *            yyyy-MM-dd HH:mm
     * @return
     */
    public static String getDate1(String currentTime) {
        if (currentTime != null && !"".equals(currentTime)) {
            return currentTime.trim().split(" ")[0];
        }
        return "";
    }

    /**
     * format time yyyy-MM-dd HH:mm:ss.SSS-->MM-dd HH:mm
     *
     * @param input
     *            time with pattern "yyyy-MM-dd HH:mm:ss.SSS"
     *
     * @return
     */
    public static String formatTime(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input.trim(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm"));

        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * format time yyyy-MM-dd HH:mm:ss.SSS-->MM-dd HH:mm
     *
     * @param input
     *            time with pattern "yyyy-MM-dd HH:mm:ss.SSS"
     *
     * @return
     */
    public static String formatTime1(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input.trim(),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm"));

        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * format time yyyy-MM-dd HH:mm:ss.SSS-->MM-dd HH:mm
     *
     * @param input
     *            time with pattern "yyyy-MM-dd HH:mm:ss.SSS"
     *
     * @return
     */
    public static String formateTime1(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("MM-dd HH:mm"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }


    /**
     * format time yyyy/MM/dd HH:mm:ss-->MM-dd HH:mm
     *
     * @param input
     *
     * @return
     */
    public static String formateTime2(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy/MM/dd HH:mm:ss"), new SimpleDateFormat("MM-dd HH:mm"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"-->"HH:mm"
     *
     * @param input
     * @return
     */
    public static String formatTime3(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("HH:mm"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * 得到yy-MM-dd HH:mm
     *
     * @param input
     *            yyyy-MM-dd HH:mm.ss.SSS 或者 yyyy-MM-dd HH:mm
     * @return
     */
    public static String formatTime4(String input) {
        String res = "";
        try {
            String[] strs = input.trim().split(" ");
            if (strs.length > 1) {
                res = strs[0].substring(2) + " " + strs[1].substring(0, 5);
            }
        } catch (Exception e) {
        }
        return res;
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"-->"yyyy-MM-dd HH:mm"
     *
     * @param input
     * @return
     */
    public static String formatTime5(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    public static String formatDate(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateFormat("MM-dd"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"-->"yyyy-MM-dd"
     *
     * @param input
     * @return
     */
    public static String formatDate2(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * "yyyy-MM-dd HH:mm:ss"-->"yyyyMMdd"
     *
     * @param input
     * @return
     */
    public static String formatDate3(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyyMMdd"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    public static String formatDate6(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.SSS"), new SimpleDateFormat(
                    "MM-dd HH:mm:ss"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    public static String formatDate7(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat(
                    "MM-dd HH:mm:ss"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    public static String formatDate8(String input) {
        String res = null;
        try {
            res = convertStrToSpecifiedDateTime(input, new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("MM.dd"));
        } catch (Exception e) {
            res = "";
        }
        return res;
    }

    /**
     * 将字符串的日期时间转化成指定的日期时间字符串
     *
     * @param s
     * @param oldSdf
     *            原字符串的格式，必须和字符串的长度和格式一致
     * @param sdf
     * @return
     * @throws ParseException
     * @author 吴志敏 研发中心 2012-6-18下午2:42:48
     */
    public static String convertStrToSpecifiedDateTime(String s,
                                                       SimpleDateFormat oldSdf, SimpleDateFormat sdf)
            throws ParseException {
        if (CommonUtils.isTextEmpty(s))
            return "";
        Date date = oldSdf.parse(s);
        return sdf.format(date);
    }

    /**
     * 判断查询日期是否为当天
     *
     * @param verifyDate
     *            格式： yyyy-MM-dd
     * @return
     */
    public static boolean isCurrentDay(String verifyDate) {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Calendar.getInstance().getTime();
        String frmDate = sdfDateFormat.format(date);
        if (frmDate.equals(verifyDate)) {
            return true;
        }
        return false;
    }


    /**
     * 比较录入两个日期的大小 true:后者大
     * @param firstDate
     * @param secondDate
     * @return
     */
    public static boolean compareTwoDate(String firstDate, String secondDate){
        boolean isSecondLate = false;
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date fDate = sdfDateFormat.parse(firstDate);
            Date sDate = sdfDateFormat.parse(secondDate);
            if(fDate.getTime() < sDate.getTime()){
                isSecondLate = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isSecondLate;
    }

    /**
     * 根据所给的日期 YYYY-MM-DD 得到当前的起始日期字符串 精确到毫秒
     *
     * @param dateStr
     * @return
     */
    public static String[] getBeginAndOverDate(String dateStr) {
        String[] str = new String[2];
        String[] cals = dateStr.split("-");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(cals[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(cals[1]) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(cals[2]));
        // BEGIN
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 000);
        Date date = cal.getTime();
        str[0] = sdf.format(date);
        // OVER
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date date1 = cal.getTime();
        str[1] = sdf.format(date1);
        return str;
    }

    public static String getTomorrowDate() {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sf.format(date);

        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sf.parse(nowDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(Calendar.DAY_OF_YEAR, +1);
        String nextDate_1 = sf.format(cal.getTime());
        return nextDate_1;
    }

    /**
     * @author qiaojianfeng
     *
     *将服务端提供的日期yyyy/MM/dd HH:mm:ss格式化成yyyy-MM-dd HH:mm:ss
     * 然后以DateTime存入数据库
     *
     */
    public static String ChangeDateStyle(String needChangeDate){
        if(CommonUtils.isTextEmpty(needChangeDate)){
            //医嘱列表数据的STOP_DATE_TIME()可能为空
            return needChangeDate;
        }else if(needChangeDate.contains("-")){
            //添加评估单的EXEC_TIME()已经是yyyy-M-d HH:mm:ss格式  需要转成标准格式
            SimpleDateFormat sdf = null;
            if(needChangeDate.contains(":")){
                if(needChangeDate.contains("T")){ //yyyy-MM-ddTHH:mm ->yyyy-MM-dd HH:mm
                    String[] splitTDate = needChangeDate.split("T");
                    needChangeDate = splitTDate[0] + " " + splitTDate[1];
                    String[] splitDate = needChangeDate.split(":");
                    if(splitDate.length == 2){  //yyyy-M-d HH:mm
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    }else{                      //yyyy-M-d HH:mm:ss
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                }else{
                    String[] splitDate = needChangeDate.split(":");
                    if(splitDate.length == 2){  //yyyy-M-d HH:mm
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    }else{                      //yyyy-M-d HH:mm:ss
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                }
            }else {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            SimpleDateFormat trueDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(needChangeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String format = trueDate.format(date);
            return format;
        }else if(needChangeDate.contains("/")){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            SimpleDateFormat trueDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = sdf.parse(needChangeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String format = trueDate.format(date);
            return format;
        }else {
            return needChangeDate; //开始皮试医嘱的时候存储的为long型
        }
    }

    /**
     * 比较两个日期相差天数
     */
    public static long getDateDif(String patientDate) {
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentParse;
        long dates = 0L;
        try {
            currentParse = sdfDateFormat.parse(getCurrentDate1());
            Date patientParse = sdfDateFormat.parse(patientDate);
            long patientTime = patientParse.getTime();
            long currentTime = currentParse.getTime();
            dates = (currentTime-patientTime)/(24*60*60*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dates ;
    }

    /**
     *用于格式化服务返回的包含T的时间
     *
     */
    public static String ChangeDateContainT(String needChangeDate){
        String trueDate = needChangeDate;
        if(needChangeDate.contains("T")){
            String[] splits = needChangeDate.split("T");
            trueDate = splits[0] + " " + splits[1];
        }
        return trueDate;
    }

    /**
     * 获取日期中天数
     * @param needDate
     * @return
     */
    public static String dateToDay(String needDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat trueSdf = new SimpleDateFormat("dd");
        Date parse = null;
        try {
            parse = sdf.parse(needDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return trueSdf.format(parse);
    }

    /**
     *
     * @param year
     * @param month
     * @return 获取输入日期中最后一周的周六
     */
    public static String getEndDate(int year,int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        //		 c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        c.set(Calendar.WEEK_OF_MONTH, c.getActualMaximum(Calendar.WEEK_OF_MONTH));   //最后一周
        c.set(Calendar.DAY_OF_WEEK, 7);         //周六
        String firstDate=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return firstDate;
    }

    /**
     *
     * @param year
     * @param month
     * @return 获取输入日期中第一周的周日
     */
    public static String getFirstDate(int year,int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month-1);
        c.set(Calendar.WEEK_OF_MONTH, 1);   //第一周
        c.set(Calendar.DAY_OF_WEEK, 1);          //周日
        String endDate=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
        return endDate;
    }


    /**
     * 判断输入日期是星期几
     *
     * @param pTime 修要判断的时间
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */
    public static int dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int dayForWeek = 0;
        if(c.get(Calendar.DAY_OF_WEEK) == 1){
            dayForWeek = 7;
        }else{
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

}
