package com.pateo.telematic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期时间工具类
 * 
 * @author Administrator
 *
 */
public class DateUtils {

	public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final String TIME_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat DATEKEY_FORMAT = new SimpleDateFormat(
			"yyyyMMdd");

	/**
	 * 判断一个时间是否在另一个时间之前
	 * 
	 * @param time1
	 *            第一个时间
	 * @param time2
	 *            第二个时间
	 * @return 判断结果
	 */
	public static boolean before(String time1, String time2) {
		try {
			Date dateTime1 = TIME_FORMAT.parse(time1);
			Date dateTime2 = TIME_FORMAT.parse(time2);

			if (dateTime1.before(dateTime2)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断一个时间是否在另一个时间之后
	 * 
	 * @param time1
	 *            第一个时间
	 * @param time2
	 *            第二个时间
	 * @return 判断结果
	 */
	public static boolean after(String time1, String time2) {
		try {
			Date dateTime1 = TIME_FORMAT.parse(time1);
			Date dateTime2 = TIME_FORMAT.parse(time2);

			if (dateTime1.after(dateTime2)) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 计算时间差值（单位为秒）
	 * 
	 * @param time1
	 *            时间1
	 * @param time2
	 *            时间2
	 * @return 差值
	 */
	public static int minus(String time1, String time2) {
		try {
			Date datetime1 = TIME_FORMAT.parse(time1);
			Date datetime2 = TIME_FORMAT.parse(time2);

			long millisecond = datetime1.getTime() - datetime2.getTime();

			return Integer.valueOf(String.valueOf(millisecond / 1000));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 获取年月日和小时
	 * 
	 * @param datetime
	 *            时间（yyyy-MM-dd HH:mm:ss）
	 * @return 结果（yyyy-MM-dd_HH）
	 */
	public static String getDateHour(String datetime) {
		String date = datetime.split(" ")[0];
		String hourMinuteSecond = datetime.split(" ")[1];
		String hour = hourMinuteSecond.split(":")[0];
		return date + "_" + hour;
	}

	/**
	 * 获取当天日期（yyyy-MM-dd）
	 * 
	 * @return 当天日期
	 */
	public static String getTodayDate() {
		return DATE_FORMAT.format(new Date());
	}

	/**
	 * 获取昨天的日期（yyyy-MM-dd）
	 * 
	 * @return 昨天的日期
	 */
	public static String getYesterdayDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);

		Date date = cal.getTime();

		return DATE_FORMAT.format(date);
	}

	/**
	 * 格式化日期（yyyy-MM-dd）
	 * 
	 * @param date
	 *            Date对象
	 * @return 格式化后的日期
	 */
	public static String formatDate(Date date) {
		return DATE_FORMAT.format(date);
	}

	public static String formatDate(String date) {
		return DATE_FORMAT.format(date);
	}

	/**
	 * 格式化时间（yyyy-MM-dd HH:mm:ss）
	 * 
	 * @param date
	 *            Date对象
	 * @return 格式化后的时间
	 */
	public static String formatTime(Date date) {
		return TIME_FORMAT.format(date);
	}

	/**
	 * 解析时间字符串
	 * 
	 * @param time
	 *            时间字符串
	 * @return Date
	 */
	public static Date parseTime(String time) {
		try {
			return TIME_FORMAT.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化日期key
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateKey(Date date) {
		return DATEKEY_FORMAT.format(date);
	}

	/**
	 * 格式化日期key
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDateKey(String datekey) {
		try {
			return DATEKEY_FORMAT.parse(datekey);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 格式化时间，保留到分钟级别 yyyyMMddHHmm
	 * 
	 * @param date
	 * @return
	 */
	public static String formatTimeMinute(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(date);
	}

	/**
	 * 时间戳转换成日期格式字符串
	 * 
	 * @param seconds
	 *            精确到秒的字符串
	 * @param formatStr
	 * @return
	 */
	public static String timeStamp2Date(String seconds, String format) {
		if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
			return "";
		}
		if (format == null || format.isEmpty())
			format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds + "000")));
	}

	/**
	 * 日期格式字符串转换成时间戳
	 * 
	 * @param date
	 *            字符串日期
	 * @param format
	 *            如：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String date2TimeStamp(String date_str, String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return String.valueOf(sdf.parse(date_str).getTime() / 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 取得当前时间戳（精确到秒）
	 * 
	 * @return
	 */
	public static String timeStamp() {
		long time = System.currentTimeMillis();
		String t = String.valueOf(time / 1000);
		return t;
	}

	public static void main(String[] args) {

		System.out.println(" ------------- " + System.currentTimeMillis());

		System.out.println("-------"+ parseDateKey(System.currentTimeMillis() + ""));
		String timeStamp = timeStamp();
		System.out.println("timeStamp=" + timeStamp);

		String date = timeStamp2Date(timeStamp, "yyyy-MM-dd HH:mm:ss");
		System.out.println("date=" + date);

		String timeStamp2 = date2TimeStamp(date, "yyyy-MM-dd HH:mm:ss");
		System.out.println(timeStamp2);
		System.out.println("-------");
		// 1475047665863
		// 1472970987000

	}
}
