package com.meronmee.core.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *日期工具类
 */
public class DateUtils {
	/**yyyyMMdd*/
	public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyyMMdd");
	/**yyyy-MM-dd HH:mm:ss*/
	public static final SimpleDateFormat FORMAT_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**yyyyMMddHHmmss*/
	public static final SimpleDateFormat FORMAT_DATETIME_14 = new SimpleDateFormat("yyyyMMddHHmmss");


	/** 获取当前的日历 */
	public static Calendar getCalendar() {
		return Calendar.getInstance();
	}
	/** 获取 date 对应的日历 */
	public static Calendar getCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar;	    
	}	
	/** 获取指定日历对应的年份 */
	public static int getYear(Calendar calendar) {
		return calendar.get(Calendar.YEAR);
	}
	/** 获取指定日历对应的月份 */
	public static int getMonth(Calendar calendar) {
		return calendar.get(Calendar.MONDAY) + 1;
	}
	/** 获取指定日历对应的一年中的周次 */
	public static int getWeekNum(Calendar calendar) {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	/** 获取指定日历对应的月份中的周次 */
	public static int getWeekOfMonth(Calendar calendar) {
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}
	/** 获取指定日历对应的日 */
	public static int getDay(Calendar calendar) {
		return calendar.get(Calendar.DATE);
	}
	/** 获取指定日历对应的时 */
	public static int getHour(Calendar calendar) {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	/** 获取指定日历对应的分 */
	public static int getMinute(Calendar calendar) {
		return calendar.get(Calendar.MINUTE);
	}
	/** 获取指定日历对应的秒 */
	public static int getSecond(Calendar calendar) {
		return calendar.get(Calendar.SECOND);
	}

	/** 获取指定日期对应的年份 */
	public static int getYear(Date date) {
		return getCalendar(date).get(Calendar.YEAR);
	}
	/** 获取指定日期对应的月份 */
	public static int getMonth(Date date) {
		return getCalendar(date).get(Calendar.MONDAY) + 1;
	}
	/** 获取指定日期对应的一年中的周次 */
	public static int getWeekNum(Date date) {
		return getCalendar(date).get(Calendar.WEEK_OF_YEAR);
	}
	/** 获取指定日期对应的月份中的周次 */
	public static int getWeekOfMonth(Date date) {
		return getCalendar(date).get(Calendar.WEEK_OF_MONTH);
	}
	/** 获取指定日期对应的日 */
	public static int getDay(Date date) {
		return getCalendar(date).get(Calendar.DATE);
	}
	/** 获取指定日期对应的时 */
	public static int getHour(Date date) {
		return getCalendar(date).get(Calendar.HOUR_OF_DAY);
	}
	/** 获取指定日期对应的分 */
	public static int getMinute(Date date) {
		return getCalendar(date).get(Calendar.MINUTE);
	}
	/** 获取指定日期对应的秒 */
	public static int getSecond(Date date) {
		return getCalendar(date).get(Calendar.SECOND);
	}
	
	/**
	   * 增加日期的年份
	   * @param date 日期
	   * @param yearAmount 增加数量。可为负数
	   * @return 增加年份后的日期
	   */
	public static Date addYear(Date date, int yearAmount) {
	    return addInteger(date, Calendar.YEAR, yearAmount);
	}
	/**
	   * 增加日期的月份
	   * @param date 日期
	   * @param yearAmount 增加数量。可为负数
	   * @return 增加月份后的日期
	   */
	public static Date addMonth(Date date, int monthAmount) {
	    return addInteger(date, Calendar.MONTH, monthAmount);
	}	
	/**
	   * 增加日期的周数
	   * @param date 日期
	   * @param weekAmount 增加数量。可为负数
	   * @return 增加天数后的日期
	   */
	public static Date addWeek(Date date, int weekAmount) {
	    return addInteger(date, Calendar.WEEK_OF_YEAR, weekAmount);
	}	  
	/**
	   * 增加日期的天数
	   * @param date 日期
	   * @param dayAmount 增加数量。可为负数
	   * @return 增加天数后的日期
	   */
	public static Date addDay(Date date, int dayAmount) {
	    return addInteger(date, Calendar.DATE, dayAmount);
	}	  
	/**
	   * 增加日期的小时
	   * @param date 日期
	   * @param hourAmount 增加数量。可为负数
	   * @return 增加小时后的日期
	   */
	public static Date addHour(Date date, int hourAmount) {
	    return addInteger(date, Calendar.HOUR_OF_DAY, hourAmount);
	}	
	/**
	   * 增加日期的分钟
	   * @param date 日期
	   * @param minuteAmount 增加数量。可为负数
	   * @return 增加分钟后的日期
	   */
	public static Date addMinute(Date date, int minuteAmount) {
	    return addInteger(date, Calendar.MINUTE, minuteAmount);
	}	
	/**
	   * 增加日期的秒钟
	   * @param date 日期
	   * @param secondAmount 增加数量。可为负数
	   * @return 增加秒钟后的日期
	   */
	public static Date addSecond(Date date, int secondAmount) {
	    return addInteger(date, Calendar.SECOND, secondAmount);
	}
	  	

	/**
	 * 判断是否是同一天
	 * @param date1
	 * @param date2
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		if(date1 == null || date2 == null){
			return false;
		}
		Calendar calendar1 = getCalendar(date1);
		Calendar calendar2 = getCalendar(date2);
	    
		return getYear(calendar1) == getYear(calendar2)
				&& calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR);
	}
	/**
	 * 判断是否是同一周
	 * @param date1
	 * @param date2
	 */
	public static boolean isSameWeek(Date date1, Date date2) {
		if(date1 == null || date2 == null){
			return false;
		}
		Calendar calendar1 = getCalendar(date1);
		Calendar calendar2 = getCalendar(date2);
	    
		return getYear(calendar1) == getYear(calendar2)
				&& getWeekNum(calendar1) == getWeekNum(calendar2);
	}
	/**
	 * 判断是否是同一月
	 * @param date1
	 * @param date2
	 */
	public static boolean isSameMonth(Date date1, Date date2) {
		if(date1 == null || date2 == null){
			return false;
		}
		Calendar calendar1 = getCalendar(date1);
		Calendar calendar2 = getCalendar(date2);
	    
		return getYear(calendar1) == getYear(calendar2)
				&& getMonth(calendar1) == getMonth(calendar2);
	}
	/**
	 * 判断是否是同一年
	 * @param date1
	 * @param date2
	 */
	public static boolean isSameYear(Date date1, Date date2) {
		if(date1 == null || date2 == null){
			return false;
		}
		Calendar calendar1 = getCalendar(date1);
		Calendar calendar2 = getCalendar(date2);
	    
		return getYear(calendar1) == getYear(calendar2);
	}

	/**
	 * 计算年差
	 * @param begin
	 * @param end
	 */
	public static int getYearsBetweenDate(Date begin, Date end) {
		return getYear(end) - getYear(begin);
	}
	/**
	 * 计算月差
	 * @param begin
	 * @param end
	 */
	public static int getMonthsBetweenDate(Date begin, Date end) {
		Calendar bCalendar = getCalendar(begin);
		Calendar eCalendar = getCalendar(end);
		int bYear = getYear(bCalendar);
		int eYear = getYear(eCalendar);
		int bMonth = getMonth(bCalendar);
		int eMonth = getMonth(eCalendar);
		
		if(bYear == eYear){//同一年
			return  eMonth - bMonth;
		} else {
			return ((eYear-1)*52+eMonth) - ((bYear-1)*52+bMonth);
		}		
	}

	public static int getDaysBetweenDate(Date begin, Date end) {
		return (int) ((end.getTime()-begin.getTime())/(1000 * 60 * 60 * 24));
	}


	/**判断是否超过24小时
	   *   
	   * @param beginTime
	   * @param endTime
	   * @return boolean
	   * @throws Exception
	*/
    public static boolean isExcess24(Date beginTime, Date endTime) throws Exception { 
        long cha = endTime.getTime() - beginTime.getTime(); 
        if(cha<0){
          return false; 
        }
        double result = cha * 1.0 / (1000 * 60 * 60);
        if(result<=24){ 
             return true; 
        }else{ 
             return false; 
        } 
    }
    	
	/**
	 * 获取两个时间的差值秒
	 * @param d1 靠前的时间
	 * @param d2 靠后的时间
	 * @return
	 */
	public static Integer getSecondBetweenDate(Date d1,Date d2){
		Long second=(d2.getTime()-d1.getTime())/1000;
		return second.intValue();
	}

	/**
	 * 将秒转换为年月日时分秒
	 * 如：75--1分15秒
	 * @param seconds
	 * @return
	 */
	public static String getSecondsShow(int seconds){
		if(seconds <= 60){
			return BaseUtils.leftPad(seconds, 2, '0') + "秒";
		}
		int secondsPerMinute = 60;
		int secondsPerHour = 60 * secondsPerMinute;
		int secondsPerDay = 24 * secondsPerHour;
		
		int days = seconds / secondsPerDay;
		int hours = (seconds % secondsPerDay) / secondsPerHour;
		int minutes = (seconds % secondsPerHour) / secondsPerMinute;
		int sec = (seconds % secondsPerMinute);
		
		boolean hasHead = false;
		StringBuilder sb = new StringBuilder();
		if(days > 0){
			sb.append(days).append("天");
			hasHead = true; 
		}
		if(hasHead){
			sb.append(BaseUtils.leftPad(hours, 2, '0')).append("小时");
			hasHead = true; 
		} else if(hours > 0){
			sb.append(BaseUtils.leftPad(hours, 2, '0')).append("小时");
			hasHead = true; 
		}
		
		if(hasHead){
			sb.append(BaseUtils.leftPad(minutes, 2, '0')).append("分");
			hasHead = true; 
		} else if(minutes > 0){
			sb.append(BaseUtils.leftPad(minutes, 2, '0')).append("分");
			hasHead = true; 
		}
		
		sb.append(BaseUtils.leftPad(sec, 2, '0')).append("秒");
		
		
		return sb.toString();
	}
	
		
	  /**
	   * 获取日期中的某数值。如获取月份
	   * @param date 日期
	   * @param dateField 日期字段，如：Calendar.HOUR_OF_DAY
	   * @return 数值
	   */
	  public static int getInteger(Date date, int dateField) {
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(dateField);
	  }
	  	  
	  /**
	   * 增加日期中某类型的某数值。如增加日期
	   * @param date 日期
	   * @param dateField  日期字段，如：Calendar.HOUR_OF_DAY
	   * @param amount 数值
	   * @return 计算后日期
	   */
	  public static Date addInteger(Date date, int dateField, int amount) {
	    Date myDate = null;
	    if (date != null) {
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(date);
	      calendar.add(dateField, amount);
	      myDate = calendar.getTime();
	    }
	    return myDate;
	  }

	  /**
	   * 获取精确的日期
	   * @param timestamps 时间long集合
	   * @return 日期
	   */
	  private static Date getAccurateDate(List<Long> timestamps) {
	    Date date = null;
	    long timestamp = 0;
	    Map<Long, long[]> map = new HashMap<Long, long[]>();
	    List<Long> absoluteValues = new ArrayList<Long>();

	    if (timestamps != null && timestamps.size() > 0) {
	      if (timestamps.size() > 1) {
	        for (int i = 0; i < timestamps.size(); i++) {
	          for (int j = i + 1; j < timestamps.size(); j++) {
	            long absoluteValue = Math.abs(timestamps.get(i) - timestamps.get(j));
	            absoluteValues.add(absoluteValue);
	            long[] timestampTmp = { timestamps.get(i), timestamps.get(j) };
	            map.put(absoluteValue, timestampTmp);
	          }
	        }

	        // 有可能有相等的情况。如2012-11和2012-11-01。时间戳是相等的
	        long minAbsoluteValue = -1;
	        if (!absoluteValues.isEmpty()) {
	          // 如果timestamps的size为2，这是差值只有一个，因此要给默认值
	          minAbsoluteValue = absoluteValues.get(0);
	        }
	        for (int i = 0; i < absoluteValues.size(); i++) {
	          for (int j = i + 1; j < absoluteValues.size(); j++) {
	            if (absoluteValues.get(i) > absoluteValues.get(j)) {
	              minAbsoluteValue = absoluteValues.get(j);
	            } else {
	              minAbsoluteValue = absoluteValues.get(i);
	            }
	          }
	        }

	        if (minAbsoluteValue != -1) {
	          long[] timestampsLastTmp = map.get(minAbsoluteValue);
	          if (absoluteValues.size() > 1) {
	            timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
	          } else if (absoluteValues.size() == 1) {
	            // 当timestamps的size为2，需要与当前时间作为参照
	            long dateOne = timestampsLastTmp[0];
	            long dateTwo = timestampsLastTmp[1];
	            if ((Math.abs(dateOne - dateTwo)) < 100000000000L) {
	              timestamp = Math.max(timestampsLastTmp[0], timestampsLastTmp[1]);
	            } else {
	              long now = new Date().getTime();
	              if (Math.abs(dateOne - now) <= Math.abs(dateTwo - now)) {
	                timestamp = dateOne;
	              } else {
	                timestamp = dateTwo;
	              }
	            }
	          }
	        }
	      } else {
	        timestamp = timestamps.get(0);
	      }
	    }

	    if (timestamp != 0) {
	      date = new Date(timestamp);
	    }
	    return date;
	  }

	  /**
	   * 判断字符串是否为日期字符串
	   * @param date 日期字符串
	   * @return true or false
	   */
	  public static boolean isDate(String date) {
	    boolean isDate = false;
	    if (date != null) {
	      if (stringToDate(date) != null) {
	        isDate = true;
	      }
	    }
	    return isDate;
	  }


	  /**
	   * 将日期字符串转化为日期。失败返回null。
	   * @param date 日期字符串
	   * @return 日期
	   */
	  public static Date stringToDate(String date) {
	    DateStyle dateStyle = null;
	    return stringToDate(date, dateStyle);
	  }

	  /**
	   * 将日期字符串转化为日期。失败返回null。
	   * @param date 日期字符串
	   * @param parttern 日期格式
	   * @return 日期
	   */
	  public static Date stringToDate(String date, String parttern) {
	    Date myDate = null;
	    if (date != null) {
	      if(date.matches("^14\\d+$")){//毫秒值
	              try {
	            	  long timeLong = Long.parseLong(date);
	                  return new Date(timeLong);
	              } catch (Exception e) {}
	      }
	      try {
	        myDate = new SimpleDateFormat(parttern).parse(date);
	      } catch (Exception e) {}
	    }
	    return myDate;
	  }

	  /**
	   * 将日期字符串转化为日期。失败返回null。
	   * @param date 日期字符串
	   * @param dateStyle 日期风格
	   * @return 日期
	   */
	  public static Date stringToDate(String date, DateStyle dateStyle) {
	    Date myDate = null;
	    if(date.matches("^14\\d+$")){//毫秒值
	            try {
	              long timeLong = Long.parseLong(date);
	                return new Date(timeLong);
	            } catch (Exception e) {}
	        }
	    if (dateStyle == null) {
	      List<Long> timestamps = new ArrayList<Long>();
	      for (DateStyle style : DateStyle.values()) {
	        Date dateTmp = stringToDate(date, style.getValue());
	        if (dateTmp != null) {
	          timestamps.add(dateTmp.getTime());
	        }
	      }
	      myDate = getAccurateDate(timestamps);
	    } else {
	      myDate = stringToDate(date, dateStyle.getValue());
	    }
	    return myDate;
	  }
	  /**
	   * 将日期转化为日期字符串。失败返回null。
	   * @param date 日期
	   * @param parttern 日期格式
	   * @return 日期字符串
	   */
	  public static String dateToString(long date, String parttern) {
		  return dateToString(new Date(date), parttern);
	  }
	  /**
	   * 将日期转化为日期字符串。失败返回null。
	   * @param date 日期
	   * @param parttern 日期格式
	   * @return 日期字符串
	   */
	  public static String dateToString(Date date, String parttern) {
	    String dateString = "";
	    if (date != null) {
	      try {
	        dateString = new SimpleDateFormat(parttern).format(date);
	      } catch (Exception e) {
	      }
	    }
	    return dateString;
	  }
	  /**
	   * 将日期转化为日期字符串。失败返回null。
	   * @param date 日期
	   * @param format 日期格式
	   * @return 日期字符串
	   */
	  public static String dateToString(Date date, SimpleDateFormat format) {
	    String dateString = "";
	    if (date != null) {
	      try {
	        dateString = format.format(date);
	      } catch (Exception e) {
	      }
	    }
	    return dateString;
	  }

	  /**
	   * 将日期转化为日期字符串。失败返回null。
	   * @param date 日期
	   * @param dateStyle 日期风格
	   * @return 日期字符串
	   */
	  public static String dateToString(Date date, DateStyle dateStyle) {
	    String dateString = "";
	    if (dateStyle != null) {
	      dateString = dateToString(date, dateStyle.getValue());
	    }
	    return dateString;
	  }

	  /**
	   * 将日期字符串转化为另一日期字符串。失败返回null。
	   * @param date 旧日期字符串
	   * @param parttern 新日期格式
	   * @return 新日期字符串
	   */
	  public static String stringToString(String date, String parttern) {
	    return stringToString(date, null, parttern);
	  }

	  /**
	   * 将日期字符串转化为另一日期字符串。失败返回null。
	   * @param date 旧日期字符串
	   * @param dateStyle 新日期风格
	   * @return 新日期字符串
	   */
	  public static String stringToString(String date, DateStyle dateStyle) {
	    return stringToString(date, null, dateStyle);
	  }
	  /**
	   * 获取日期字符串的日期风格。失敗返回null。
	   * @param date 日期字符串
	   * @return 日期风格
	   */
	  public static DateStyle getDateStyle(String date) {
	    DateStyle dateStyle = null;
	    Map<Long, DateStyle> map = new HashMap<Long, DateStyle>();
	    List<Long> timestamps = new ArrayList<Long>();
	    for (DateStyle style : DateStyle.values()) {
	      Date dateTmp = stringToDate(date, style.getValue());
	      if (dateTmp != null) {
	        timestamps.add(dateTmp.getTime());
	        map.put(dateTmp.getTime(), style);
	      }
	    }
	    dateStyle = map.get(getAccurateDate(timestamps).getTime());
	    return dateStyle;
	  }
	  /**
	   * 将日期字符串转化为另一日期字符串。失败返回null。
	   * @param date 旧日期字符串
	   * @param olddParttern 旧日期格式
	   * @param newParttern 新日期格式
	   * @return 新日期字符串
	   */
	  public static String stringToString(String date, String olddParttern, String newParttern) {
	    String dateString = null;
	    if (olddParttern == null) {
	      DateStyle style = getDateStyle(date);
	      if (style != null) {
	        Date myDate = stringToDate(date, style.getValue());
	        dateString = dateToString(myDate, newParttern);
	      }
	    } else {
	      Date myDate = stringToDate(date, olddParttern);
	      dateString = dateToString(myDate, newParttern);
	    }
	    return dateString;
	  }

	  /**
	   * 将日期字符串转化为另一日期字符串。失败返回null。
	   * @param date 旧日期字符串
	   * @param olddDteStyle 旧日期风格
	   * @param newDateStyle 新日期风格
	   * @return 新日期字符串
	   */
	  public static String stringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
	    String dateString = null;
	    if (olddDteStyle == null) {
	      DateStyle style = getDateStyle(date);
	      dateString = stringToString(date, style.getValue(), newDateStyle.getValue());
	    } else {
	      dateString = stringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
	    }
	    return dateString;
	  }

	  /**
	   * 判断date2是否在日期上(不算时分秒)晚于date1
	   * @param date1
	   * @param date2
	   * @return
	   */
	  public static boolean afterDay(Date date1, Date date2){
	    Date day1 = setDate(date1, 0, 0, 0);
	    Date day2 = setDate(date2, 0, 0, 0);
	    return day2.after(day1);    
	  }
	  /**
	   * 判断date2是否在日期上(不算时分秒)早于date1
	   * @param date1
	   * @param date2
	   * @return
	   */
	  public static boolean beforeDay(Date date1, Date date2){
	    Date day1 = setDate(date1, 0, 0, 0);
	    Date day2 = setDate(date2, 0, 0, 0);
	    return day2.before(day1);   
	  }
	  
	  /**
	   * 设置日期中的小时值
	   * @param date
	   * @param hour
	   * @return 返回设置后的新日期，原日期不变
	   */
	  public static Date setHour(Date date, int hour){
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, hour);
	    return calendar.getTime();
	  }
	  /**
	   * 设置日期中的分钟值
	   * @param date
	   * @param minute
	   * @return 返回设置后的新日期，原日期不变
	   */
	  public static Date setMinute(Date date, int minute){
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.set(Calendar.MINUTE, minute);
	    return calendar.getTime();
	  }
	  
	  /**
	   * 设置日期
	   * @param date
	   * @param hour - 0~23, 为-1不修改
	   * @param minute 0~59, 为-1不修改
	   * @param second 0~59, 为-1不修改
	   * @return 返回设置后的新日期，原日期不变
	   */
	  public static Date setDate(Date date, int hour, int minute, int second){
	    return setDate(date, hour, minute, second, 0);
	  }
	  /**
	   * 设置日期
	   * @param date
	   * @param hour - 0~23, 为-1不修改
	   * @param minute 0~59, 为-1不修改
	   * @param second 0~59, 为-1不修改
	   * @param millisecond 0~999, 为-1不修改
	   * @return 返回设置后的新日期，原日期不变
	   */
	  public static Date setDate(Date date, int hour, int minute, int second, int millisecond){
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    if(hour>=0 && hour<=23){
	      calendar.set(Calendar.HOUR_OF_DAY, hour);
	    }
	    if(minute>=0 && minute<=59){
	      calendar.set(Calendar.MINUTE, minute);
	    }
	    if(second>=0 && second<=59){
	      calendar.set(Calendar.SECOND, second);
	    }
	    if(millisecond>=0 && millisecond<=999){
	      calendar.set(Calendar.MILLISECOND, millisecond);
	    }
	    
	    return calendar.getTime();
	  }
	  

	  /**
	   * 获取日期 。默认yyyy-MM-dd格式。失败返回null。
	   * @param date 日期字符串
	   * @return 日期
	   */
	  public static String getDate(String date) {
	    return stringToString(date, DateStyle.YYYY_MM_DD);
	  }

	  /**
	   * 获取日期。默认yyyy-MM-dd格式。失败返回null。
	   * @param date 日期
	   * @return 日期
	   */
	  public static String getDate(Date date) {
	    return dateToString(date, DateStyle.YYYY_MM_DD);
	  }

	  /**
	   * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
	   * @param date 日期字符串
	   * @return 时间
	   */
	  public static String getTime(String date) {
	    return stringToString(date, DateStyle.HH_MM_SS);
	  }

	  /**
	   * 获取日期的时间。默认HH:mm:ss格式。失败返回null。
	   * @param date 日期
	   * @return 时间
	   */
	  public static String getTime(Date date) {
	    return dateToString(date, DateStyle.HH_MM_SS);
	  }

	  /**
	   * 获取日期的星期。失败返回null。
	   * @param date 日期字符串
	   * @return 星期
	   */
	  public static Week getWeek(String date) {
	    Week week = null;
	    DateStyle dateStyle = getDateStyle(date);
	    if (dateStyle != null) {
	      Date myDate = stringToDate(date, dateStyle);
	      week = getWeek(myDate);
	    }
	    return week;
	  }
	  /**
	   * 获取日期的星期。失败返回null。
	   * @param date 日期字符串
	   * @return 星期
	   */
	  public static Week getWeek(String date, String pattern) {
	    Week week = null;
	    Date myDate = stringToDate(date, pattern);
	    week = getWeek(myDate);
	    return week;
	  }
	  /**
	   * 获取当前的星期。失败返回null。
	   * @param date 日期
	   * @return 星期
	   */
	  public static Week getWeek() {
	    return getWeek(new Date());
	  }
	  /**
	   * 获取日期的星期。失败返回null。
	   * @param date 日期
	   * @return 星期
	   */
	  public static Week getWeek(Date date) {
	    Week week = null;
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	    switch (weekNumber) {
	    case 0:
	      week = Week.SUNDAY;
	      break;
	    case 1:
	      week = Week.MONDAY;
	      break;
	    case 2:
	      week = Week.TUESDAY;
	      break;
	    case 3:
	      week = Week.WEDNESDAY;
	      break;
	    case 4:
	      week = Week.THURSDAY;
	      break;
	    case 5:
	      week = Week.FRIDAY;
	      break;
	    case 6:
	      week = Week.SATURDAY;
	      break;
	    }
	    return week;
	  }
	  
	  /**
	   * 判断日期是否是周末
	   * @param date
	   * @return
	   */
	  public static boolean isWeekend(Date date){
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    int weekNumber = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	    if(weekNumber == 0 || weekNumber == 6){
	      return true;
	    }
	    return false;
	  }
	  
	  /**
	   * 获取两个日期相差的天数
	   * @param date 日期字符串
	   * @param otherDate 另一个日期字符串
	   * @return 相差天数
	   */
	  public static int getIntervalDays(String date, String otherDate) {
	    return getIntervalDays(stringToDate(date), stringToDate(otherDate));
	  }
	  /**
	   * 获取两个日期相差的天数
	   * @param date 日期字符串
	   * @param otherDate 另一个日期字符串
	   * @return 相差天数
	   */
	  public static int getIntervalDays(String date, String otherDate, DateStyle dateStyle) {
	    return getIntervalDays(stringToDate(date, dateStyle), stringToDate(otherDate, dateStyle));
	  }
	  /**
	   * 获取两个日期相差的天数
	   * @param date 日期字符串
	   * @param otherDate 另一个日期字符串
	   * @return 相差天数
	   */
	  public static int getIntervalDays(String date, String pattern, String otherDate, String otherPattern) {
	    return getIntervalDays(stringToDate(date, pattern), stringToDate(otherDate, otherPattern));
	  }
	  public static int getIntervalDays(String date, String otherDate, String pattern) {
	    return getIntervalDays(stringToDate(date, pattern), stringToDate(otherDate, pattern));
	  }
	  
	  /**
	   * @param date 日期
	   * @param otherDate 另一个日期
	   * @return 相差天数
	   */
	  public static int getIntervalDays(Date date, Date otherDate) {
	    long time = Math.abs(date.getTime() - otherDate.getTime());
	    return (int)(time/(24 * 60 * 60 * 1000));
	  }
	  /**
	   * @param date 日期
	   * @param otherDate 另一个日期
	   * @return 相差秒
	   */
	  public static int getIntervalSeconds(Date date, Date otherDate) {
	    long time = Math.abs(date.getTime() - otherDate.getTime());
	    return (int)(time/1000);
	  }
	  /**
	   * @param date 日期
	   * @param otherDate 另一个日期
	   * @return 相差分钟
	   */
	  public static int getIntervalMinutes(Date date, Date otherDate) {
	    long time = Math.abs(date.getTime() - otherDate.getTime());
	    return (int)(time/(60 * 1000));
	  }
	  /**
	   * @param date 日期
	   * @param otherDate 另一个日期
	   * @return 相差小时
	   */
	  public static int getIntervalHours(Date date, Date otherDate) {
	    long time = Math.abs(date.getTime() - otherDate.getTime());
	    return (int)(time/(60 * 60 * 1000));
	  }
	  /**
	   * 返回一天的零点零分
	   * @param date
	   * @return
	   */
	  public static Date getDayBegin(Date date){
		if(date == null){
			date = new Date();
		}
	    Calendar calendar = Calendar.getInstance();   
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);

	    return calendar.getTime();
	  }
	  
	  /**
	   * 返回一天的23:59:59
	   * @param date
	   * @return
	   */
	  public static Date getDayEnd(Date date){
	    if(date == null){
	      date = new Date();
	    }
	    Calendar calendar = Calendar.getInstance();   
	    calendar.setTime(date);
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 0);

	    return calendar.getTime();
	  }
	  /**
	   * 获取月的最后一天
	   * @param date
	   * @return
	   */
	  public static Date getLastDayOfMonth(Date date) {
	    return lastDayOfMonth(date);
	  }
	  public static Date getMonthLastDay(Date date) {
	        return lastDayOfMonth(date);
	    }
	  /**
	   * 获取月的最后一天
	   * @param date
	   * @return
	   */
	  public static Date lastDayOfMonth(Date date) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.set(Calendar.DAY_OF_MONTH, 1);
	        cal.roll(Calendar.DAY_OF_MONTH, -1);
	        return cal.getTime();
	    }
	  /**
	   * 获取date所在月的最后一天数值
	   * @param date 目标月中的任何一天
	   * @return 如，28或30或31
	   */
	  public static int lastDayNumOfMonth(Date date) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(date);
	        cal.set(Calendar.DAY_OF_MONTH, 1);
	        cal.roll(Calendar.DAY_OF_MONTH, -1);
	        
	        return cal.get(Calendar.DAY_OF_MONTH);
	  }
	  
	  /**
	   * 获取月的最后一天
	   * @return
	   */
	  public static Date lastDayOfMonth() {
	        return lastDayOfMonth(new Date());
	    }
	  
	  /**
	   * 获取月的首日
	   * @param date
	   * @return
	   */
	  public static Date getFirstDayOfMonth(Date date) {
	    return firstDayOfMonth(date);
	  }
	  public static Date getMonthFirstDay(Date date) {
	    return firstDayOfMonth(date);
	  }
	  /**
	   * 获取月的首日
	   * @param date
	   * @return
	   */
	  public static Date firstDayOfMonth(Date date) {
	    Calendar c = Calendar.getInstance();    
	    c.setTime(date);
	        c.add(Calendar.MONTH, 0);
	        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	        return c.getTime();
	  }
	  /**
	   * 获取月的首日
	   * @return
	   */
	  public static Date firstDayOfMonth() {
	    return firstDayOfMonth(new Date());
	  }
	  /**
	   * 获取月的最后一天
	   * @param year
	   * @param month
	   * @return
	   */
	  public static Date lastDayOfMonth(int year, int month) {
	    Calendar cal = Calendar.getInstance();     
	        cal.set(Calendar.YEAR, year);     
	        cal.set(Calendar.MONTH, month-1);     
	        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
	        return cal.getTime();
	  }
	  /**
	   * 获取月的首日
	   * @param year
	   * @param month
	   * @return
	   */
	  public static Date firstDayOfMonth(int year, int month) {
	    Calendar cal = Calendar.getInstance();     
	        cal.set(Calendar.YEAR, year);     
	        cal.set(Calendar.MONTH, month-1);  
	        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
	        return cal.getTime();
	  }
	  
	  public static String getLastDayOfMonth(int year, int month) {     
	        Calendar cal = Calendar.getInstance();     
	        cal.set(Calendar.YEAR, year);     
	        cal.set(Calendar.MONTH, month-1);     
	        cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DATE));  
	       return  new   SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());  
	    }   
	    public static String getFirstDayOfMonth(int year, int month) {     
	        Calendar cal = Calendar.getInstance();     
	        cal.set(Calendar.YEAR, year);     
	        cal.set(Calendar.MONTH, month-1);  
	        cal.set(Calendar.DAY_OF_MONTH,cal.getMinimum(Calendar.DATE));  
	       return   new   SimpleDateFormat( "yyyy-MM-dd ").format(cal.getTime());  
	    }  
	    
	    public static int getDaysOfMonth(Date date){      
	      String dateStr = dateToString(getLastDayOfMonth(date), DateStyle.YYYY_MM_DD);
	    String lastDate = dateStr.split("-")[2];
	    int days = Integer.parseInt(lastDate);
	    return days;
	    }
	    public static int getDaysOfMonth(int year, int month){      
	      String dateStr = dateToString(lastDayOfMonth(year, month), DateStyle.YYYY_MM_DD);
	    String lastDate = dateStr.split("-")[2];
	    int days = Integer.parseInt(lastDate);
	    return days;
	    }
	    /**
	     * 获取本月截止指定日期的周末数目
	     * @param date  yyyy-MM-dd
	     * @return
	     */
	    public static int getWeekendDaysOfDate(String date){  
	      int days=0;
	      //1:周一,...,7:周日
	      int dayOfWeek = DateUtils.getWeek(date, "yyyy-MM-dd").number;
	      int day = Integer.valueOf(date.split("-")[2]);
	      int offset = dayOfWeek - day%7;
	      int temp=0;
	      for(int i=1; i<=day; i++){
	        temp = (offset + i)%7;
	        if(temp<0){
	          temp = temp + 7;
	        }
	        //System.out.println(i+"==>"+temp);
	        if(temp==0||temp==6) days+=1;
	      }
	      return days;
	    }
	    /**
	     * 获得指定日期是一年中的第几周
	     * @param date
	     * @return
	     */
	    public static int getWeekNumOfYear(Date date){  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        int iWeekNum = calendar.get(Calendar.WEEK_OF_YEAR);  
	        return iWeekNum;  
	    }
	    /**
	     * 获得当前日期是一年中的第几周
	     * @return
	     */
	    public static int getWeekNumOfYear(){  
	      return getWeekNumOfYear(new Date());
	    } 
	    /**
	     * 计算某年某周的开始日期（周一为一周开始）
	     * @param year
	     * @param weekNum
	     * @return
	     */
	    public static Date getYearWeekFirstDay(int year,int weekNum){  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(Calendar.YEAR, year);  
	        cal.set(Calendar.WEEK_OF_YEAR, weekNum);  
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
	        
	        return cal.getTime();
	    }
	    /**
	     * 计算指定日期所在周的开始日期（周一为一周开始）
	     * @param weekNum
	     * @return
	     */
	    public static Date getWeekFirstDay(Date date){  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
	        return calendar.getTime();
	    }
	    /**
	     * 计算今年某周的开始日期（周一为一周开始）
	     * @param weekNum
	     * @return
	     */
	    public static Date getWeekFirstDay(int weekNum){  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(Calendar.WEEK_OF_YEAR, weekNum);  
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
	        
	        return cal.getTime();
	    }
	    /**
	     * 计算某年某周的开始日期（周一为一周开始）
	     * @param year
	     * @param weekNum
	     * @return
	     */
	    public static Date getWeekFirstDay(int year,int weekNum){  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(Calendar.YEAR, year);  
	        cal.set(Calendar.WEEK_OF_YEAR, weekNum);  
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
	        
	        return cal.getTime();
	    }
	    /**
	     * 计算某年某周的结束日期（周日为一周结束）
	     * @param year
	     * @param weekNum
	     * @return
	     */
	    public static Date getWeekLastDay(int year,int weekNum){  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(Calendar.YEAR, year);  
	        cal.set(Calendar.WEEK_OF_YEAR, weekNum+1);  
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); 
	        
	        return cal.getTime();
	    }
	    /**
	     * 计算今年某周的结束日期（周日为一周结束）
	     * @param weekNum
	     * @return
	     */
	    public static Date getWeekLastDay(int weekNum){  
	        Calendar cal = Calendar.getInstance();  
	        cal.set(Calendar.WEEK_OF_YEAR, weekNum+1);  
	        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
	        
	        return cal.getTime();
	    }
	    /**
	     * 计算今年某周的结束日期（周日为一周结束）
	     * @param weekNum
	     * @return
	     */
	    public static Date getWeekLastDay(Date date){  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(date);  
	        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR)+1);  
	        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);  
	        return calendar.getTime();
	    }
	    /**
	     * 判断是否为闰年
	     * @param year
	     * @return
	     */
	    public static boolean isLeapYear(int year){  
	        boolean isLeep = false;  
	        if((year % 4 == 0) && (year % 100 != 0)){  
	          isLeep = true;  
	        } else if(year % 400 ==0){  
	          isLeep = true;  
	        } else {  
	          isLeep = false;  
	        }  
	        return isLeep;  
	    }  

	    public static boolean before(Date d1, Date d2){
	      return d1.compareTo(d2) < 0;
	    }
	    public static boolean after(Date d1, Date d2){
	      return d1.compareTo(d2) > 0;
	    }
	    
	    /**
	     * 判断d是否在d1和d2直接
	     * 
	     * @param d 目标日期
	     * @param d1 日期1
	     * @param d2 日期2
	     * @return
	     */
	    public static boolean between(Date d, Date d1, Date d2){
	    	if(d == null || d1 == null || d2 == null){
	    		return false;
	    	}
	    	long time = d.getTime();
	    	long time1 = d1.getTime();
	    	long time2 = d2.getTime();
	    	long t1 = Math.min(time1, time2);
	    	long t2 = Math.max(time1, time2);
	    	
	    	return time>=t1 && time<=t2;
	    }
	    
	    /**
	     * 分解一个日期区间
	     * @param beginTime
	     * @param endTime
	     * @return
	     */
	    public static List<Date> getDays(Date beginTime, Date endTime){     
	      List<Date> list = new ArrayList<Date>();
	      if(beginTime == null || endTime == null){
	        return list;
	      }
	      if(endTime.before(beginTime)){
	        Date temp = beginTime;
	        beginTime = endTime;
	        endTime = temp;
	      }
	      
	      while(!DateUtils.getDayBegin(beginTime).equals(DateUtils.getDayBegin(endTime))){
	        list.add(DateUtils.getDayBegin(beginTime));
	        //更新beginTime，设为次日
	        beginTime = DateUtils.addDay(beginTime, 1);
	      }
	      //最后一天
	      list.add(DateUtils.getDayBegin(beginTime));       
	      
	      return list;
	    }
	    
	    /**
	   * @param arg
	   * @param format
	   */
	  public static boolean widthDateFormat(String arg, String format) {
	    if(arg == null || format == null){
	      return false;
	    }
	    if(arg.length() != format.length()){
	      return false;
	    }
	    String str = DateUtils.dateToString(DateUtils.stringToDate(arg, format), format);
	    if(!arg.equals(str)){
	      return false;
	    }   
	    return true;
	  }
	  
	  /**
	   * 分解一个日期区间
	   * @param beginTime
	   * @param endTime
	   * @return
	   */
	  public static List<String> getDayStrs(Date beginTime, Date endTime, String format){     
	    List<Date> list = getDays(beginTime, endTime);
	    List<String> dayStrs = new ArrayList<String>();
	    for(Date day : list){
	      String dayStr = DateUtils.dateToString(day, format);
	      dayStrs.add(dayStr);
	    }
	    return dayStrs;
	  }
	  /**
	   * 将日期转换为CRON表达式
	   * @param date
	   * @return cron 表达式
	   */
	  public static String date2Cron(Date date){
		  if(date == null){
			  return null;
		  }
		  String parttern = "ss mm HH dd MM ? yyyy";  
	      return dateToString(date, parttern);
	  }
	  
	  /**
	   * 秒格式化为xxx小时xxx分xxx秒
	   * @param second
	   * @return
	   */
	  public static String formatSecond(long second){
		  StringBuffer formatTime = new StringBuffer();
		  if(second == 0){
			  return "";
		  }
		  long sec = (second % 60);
		  if(sec != 0){
			  formatTime.append(sec + "秒");
		  }
		  long mins = (second / 60);
		  if(mins != 0){
			  long minsFormat = (mins % 60);
			  if(minsFormat != 0){
				  formatTime.insert(0, minsFormat + "分");
			  }
			  long hour = (mins / 60);
			  if(hour != 0){
				  formatTime.insert(0, hour + "小时");
			  }
		  }
		  return formatTime.toString();
	  }

	  /**
	   * 获取到秒的时间戳
	   * @param date
	   * @return
	   */
	  public static long getUnixTimestamp(Date date){
		  if(date == null){
			  return 0L;
		  }
		  return date.getTime() / 1000;
	  }
	  /**
	   * 获取到秒的时间戳
	   * @param date
	   * @return
	   */
	  public static long getTimestampSeconds(Date date){
		  return getUnixTimestamp(date);
	  }
	  
	  //---------------------------------------
	  
	  public enum DateStyle {	
			MM_DD("MM-dd"),
			YYYY_MM("yyyy-MM"),
			YYYY_MM_DD("yyyy-MM-dd"),
			YYYYMMDD("yyyyMMdd"),
			MM_DD_HH_MM("MM-dd HH:mm"),
			MM_DD_HH_MM_SS("MM-dd HH:mm:ss"),
			YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm"),
			YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"),
			
			MM_DD_EN("MM/dd"),
			YYYY_MM_EN("yyyy/MM"),
			YYYY_MM_DD_EN("yyyy/MM/dd"),
			MM_DD_HH_MM_EN("MM/dd HH:mm"),
			MM_DD_HH_MM_SS_EN("MM/dd HH:mm:ss"),
			YYYY_MM_DD_HH_MM_EN("yyyy/MM/dd HH:mm"),
			YYYY_MM_DD_HH_MM_SS_EN("yyyy/MM/dd HH:mm:ss"),
			
			MM_DD_CN("MM月dd日"),
			YYYY_MM_CN("yyyy年MM月"),
			YYYY_MM_DD_CN("yyyy年MM月dd日"),
			MM_DD_HH_MM_CN("MM月dd日 HH:mm"),
			MM_DD_HH_MM_SS_CN("MM月dd日 HH:mm:ss"),
			YYYY_MM_DD_HH_MM_CN("yyyy年MM月dd日 HH:mm"),
			YYYY_MM_DD_HH_MM_SS_CN("yyyy年MM月dd日 HH:mm:ss"),
			
			HH_MM("HH:mm"),
			HH_MM_SS("HH:mm:ss");
			
			
			private String value;
			
			DateStyle(String value) {
				this.value = value;
			}
			
			public String getValue() {
				return value;
			}
	  }
	  
	  public enum Week {	
			MONDAY("星期一", "Monday", "Mon.", 1),
			TUESDAY("星期二", "Tuesday", "Tues.", 2),
			WEDNESDAY("星期三", "Wednesday", "Wed.", 3),
			THURSDAY("星期四", "Thursday", "Thur.", 4),
			FRIDAY("星期五", "Friday", "Fri.", 5),
			SATURDAY("星期六", "Saturday", "Sat.", 6),
			SUNDAY("星期日", "Sunday", "Sun.", 7);
			
			String name_cn;
			String name_en;
			String name_enShort;
			int number;
			
			Week(String name_cn, String name_en, String name_enShort, int number) {
				this.name_cn = name_cn;
				this.name_en = name_en;
				this.name_enShort = name_enShort;
				this.number = number;
			}
			
			public String getChineseName() {
				return name_cn;
			}
		
			public String getName() {
				return name_en;
			}
		
			public String getShortName() {
				return name_enShort;
			}
		
			public int getNumber() {
				return number;
			}
	  }
}
