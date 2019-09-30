package org.alvin.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat
            ("yyyy年MM月dd");

    private static SimpleDateFormat timeFormat = new SimpleDateFormat
            ("yyyy-MM-dd HH:mm:ss");


    /**
     * 时间段
     */


    /**
     * 按小时计算,分隔
     *
     * @param begin
     * @param end
     * @return
     */
    public static List<DateRange> splitDateRangeByHour(Date begin, Date end) {
        System.out.println("按小时切割");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begin);
        List<DateRange> list = new ArrayList<>();
        DateRange range = new DateRange();
        range.setBegin(begin);
        while (true) {
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            if (calendar.getTime().after(end)) {
                range.setEnd(end);
                list.add(range);
                break;
            }
            if (calendar.get(Calendar.HOUR_OF_DAY) == 23) {
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                calendar.set(Calendar.MILLISECOND, 0);
                range.setEnd(calendar.getTime());
                list.add(range);
                //第二天时间计算,并创建新的时间段
                calendar.add(Calendar.SECOND, 1);
                range = new DateRange();
                range.setBegin(calendar.getTime());
                continue;
            }
        }
        return list;
    }

    /**
     * 按天计算分隔
     * @param begin
     * @param end
     * @return
     */
    public static List<DateRange> splitDateRangeByDay(Date begin, Date end) {
        System.out.println("按天切割");
        long time = end.getTime() - begin.getTime();
        if (time == 0) {
            return new ArrayList<>();
        }
        long hours = time / (60 * 60 * 1000);
        if (time % (60 * 60 * 1000) > 0) {
            hours += 1;
        }
        if (hours <= 24) {
            return splitDateRangeByHour(begin, end);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(begin);
        List<DateRange> list = new ArrayList<>();
        DateRange range = new DateRange();
        range.setBegin(begin);
        while (true) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            if (calendar.getTime().after(end)) {
                range.setEnd(end);
                list.add(range);
                break;
            }
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            //今天的开始
            Date tmpBeginTime = calendar.getTime();
            //计算出上一天的最后一秒
            calendar.add(Calendar.SECOND, -1);
            range.setEnd(calendar.getTime());
            list.add(range);
            //创建新的时间段
            range = new DateRange();
            range.setBegin(tmpBeginTime);
            //回到今天
            calendar.add(Calendar.SECOND, +1);
        }
        return list;
    }

    /**
     * @功能说明 两个日间隔的天数
     */
    public static int subDate(Date d1, Date d2) {
        Calendar start = Calendar.getInstance();
        start.setTime(d1);
        start.set(Calendar.MILLISECOND, 999);
        start.set(Calendar.HOUR_OF_DAY, 23);
        start.set(Calendar.MINUTE, 59);
        start.set(Calendar.SECOND, 59);

        Calendar end = Calendar.getInstance();
        end.setTime(d2);
        end.set(Calendar.MILLISECOND, 0);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);

        return new BigDecimal(((start.getTimeInMillis() / 100000.0 - end.getTimeInMillis() / 100000.0) / (24 * 6 * 6)))
                .setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
    }

    public static Date setStart(Date date) {
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();
    }

    /**
     * @功能说明 取日期的长整型数据
     */
    public static Long getDayLong(Date date) {
        return setStart(date).getTime();
    }

    public static Date setEnd(Date date) {
        Calendar start = Calendar.getInstance();
        start.setTime(date);
        start.set(Calendar.HOUR_OF_DAY, 23);
        start.set(Calendar.MINUTE, 59);
        start.set(Calendar.SECOND, 59);
        return start.getTime();
    }

    public static Date calcEndDate(Date startDate, int field, int timeLength) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.add(field, timeLength);

        start.set(Calendar.HOUR_OF_DAY, 23);
        start.set(Calendar.MINUTE, 59);
        start.set(Calendar.SECOND, 59);
        return start.getTime();
    }

    /**
     * 计算续期开始时间(规则为上次结束日期的第二天0点)
     *
     * @param startDate 上期结束日期
     */
    public static Date calcStartDate(Date startDate) {
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.add(Calendar.DATE, 1);

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTime();
    }

//	public static void main(String[] args) {
//		Date date = new Date();
//		logger.info(setStart(date));
//		logger.info(setEnd(date));
//		logger.info(subDate(setEnd(date), setStart(date)));
//
//	}


    /**
     * @类说明:加天
     **/
    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    /**
     * @类说明:加月
     **/
    public static Date addMonths(Date date, int month_count) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, month_count);
        return cal.getTime();
    }

    public static String dateFormat(Date date) {
        return dateFormat.format(date);
    }

    public static String timeFormat(Date date) {
        return timeFormat.format(date);
    }


    public static Date getStartMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 获得当天零时零分零秒
     */
    public static Date today() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
