package com.pcp.funeralsvc.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT_FOR_IM = "yyyyMMddHHmmssZ";
    public static final String DATE_YYYYMMDD_FORMAT = "yyyyMMdd";
    public static final String DATE_YY_MM_DD_HH_dd_ss = "yy.MM.dd HH:mm:ss";

    public static Date getDateNow(){
        return new Date(Calendar.getInstance().getTimeInMillis());
    }

    public static String toFormatString(long time, String format) {
        DateFormat df  = new SimpleDateFormat(format);
        Date       now = new Date(time);

        return df.format(now);
    }

    public static String toFormatString(String format) {
        return toFormatString(System.currentTimeMillis(), format);
    }
    public static String toFormatString(long time) {
        return toFormatString(time, DATE_FORMAT);
    }
    public static String toFormatString() {
        return toFormatString(System.currentTimeMillis(), DATE_FORMAT);
    }

    public static String toImFormatString() {
        String defaultFormatString = toFormatString(DATE_FORMAT_FOR_IM);

        StringBuffer sb = new StringBuffer();
        sb.append(defaultFormatString.substring(0 , 4 )); sb.append("-");
        sb.append(defaultFormatString.substring(4 , 6 )); sb.append("-");
        sb.append(defaultFormatString.substring(6 , 8 )); sb.append("T");
        sb.append(defaultFormatString.substring(8 , 10)); sb.append(":");
        sb.append(defaultFormatString.substring(10, 12)); sb.append(":");
        sb.append(defaultFormatString.substring(12, 14));
        sb.append(defaultFormatString.substring(14));

        return sb.toString();
    }

    public static long toTimeMillis(String timeStr) throws ParseException {
        DateFormat df   = new SimpleDateFormat(DATE_YYYYMMDD_FORMAT);
        Date       date = df.parse(timeStr);

        return date.getTime();
    }
    public static long toFormatTimeMillis(String timeStr, String format) throws ParseException {
        DateFormat df   = new SimpleDateFormat(format);
        Date       date = df.parse(timeStr);

        return date.getTime();
    }

    public static void main(String[] args) throws ParseException {
        long time = toFormatTimeMillis("20150625224755", "yyyyMMddHHmmss");

        System.out.println("time   : " + time + ", time: " + toFormatString(time, "yyyy-MM-dd HH:mm:ss"));
        //System.out.println("ImTime1: " + toImFormatString());

        time = 1435240012562L;
        System.out.println("time   : " + time + ", time: " + toFormatString(time, "yyyy-MM-dd HH:mm:ss"));
        //System.out.println("ImTime1: " + toImFormatString());

    }

    /**
     * 두 시간의 차이(일)를 구한다.
     *
     * @param date1 시간1
     * @param date2 시간2
     * @return 시간(일) 차이
     */
    public static float diffDays(Date date1, Date date2) {
        long diffTime = Math.abs(date1.getTime() - date2.getTime());
        return Float.valueOf(String.format("%.2f", diffTime / (float) (24 * 60 * 60 * 1000)));
    }


    /**
     * 현재 시간을 기준으로 몇 시간 전/후의 시간을 구할 때 사용한다.<p/>
     * 예: 6월 18일 = 6월 17일 + (-1)
     *
     * @param amountDate 계산할 일 차
     * @return
     */
    public static Date getAmountDateToDate(int amountDate) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, amountDate);
        return cal.getTime();
    }

    /**
     * 두 날짜의 차이를 구할때 사용한다.<p/>
     * 예: 6월 18일 = 6월 17일 + (-1)
     *
     * @param beforeDt 날짜
     * @param afterDt 날짜
     * @return
     */

    public static long  getdiffDateToDate(String beforeDt, String afterDt)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        try {


            Date FirstDate = format.parse(beforeDt);
            Date SecondDate = format.parse(afterDt);
            // Date로 변환된 두 날짜를 계산한 뒤 그 리턴값으로 long type 변수를 초기화 하고 있다.
            // 연산결과 -950400000. long type 으로 return 된다.
            long calDate = FirstDate.getTime() - SecondDate.getTime();

            // Date.getTime() 은 해당날짜를 기준으로1970년 00:00:00 부터 몇 초가 흘렀는지를 반환해준다.
            // 이제 24*60*60*1000(각 시간값에 따른 차이점) 을 나눠주면 일수가 나온다.
            long calDateDays = calDate / ( 24*60*60*1000);

            calDateDays = Math.abs(calDateDays);

            return calDateDays;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }

    }

    public static String addDate(int addDay) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, addDay);
        // 특정 형태의 날짜로 값을 뽑기
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(cal.getTime());

    }

    public static boolean  getValidDateToDate(long beforeDt, long afterDt, int day)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyymmdd");
        Calendar cal = Calendar.getInstance();

        try {

            /* 저장 일자 비교 */
            long diffDay = DateUtil.getdiffDateToDate(String.valueOf(beforeDt), String.valueOf( afterDt));
            if(diffDay < 0 || diffDay > day)
                return false;

            if(Long.valueOf(beforeDt) < Long.valueOf(addDate(-(day))))
                return false;

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * Date 객체를 원하는 포맷의 문자열로 변환한다.
     *
     * @param date   변환할 Date 객체
     * @param format 날짜 포맷
     * @return yyyy-MM-dd 와 같은 포맷의 문자열
     */
    public static String convertDateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

}
