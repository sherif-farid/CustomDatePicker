package com.tsongkha.spinnerdatepicker;

/*
 * Created by Sherif farid
 * Date: 1/26/2022.
 * email: sherffareed39@gmail.com.
 * phone: 00201007538470
 */

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.IslamicCalendar;
import android.icu.util.ULocale;
import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Utils {
    public static boolean  isIslamic = true ;
    public static ULocale getULocale(){
        if (isIslamic)
        return new ULocale("ar", "eg", "@calendar=islamic-umalqura");
        else
        return new ULocale("ar");
    }
    public static SimpleDateFormat getSimpleDateFormat(){
        return new SimpleDateFormat("dd MMM yyyy",getULocale());
    }
    public static Calendar getCalendar(int year, int monthOfYear , int dayOfMonth){
        if (isIslamic) {
            IslamicCalendar calendar = new IslamicCalendar(year, monthOfYear, dayOfMonth);
            calendar.setCalculationType(IslamicCalendar.CalculationType.ISLAMIC_UMALQURA);
            return calendar;
        }else {
            Calendar calendar = Calendar.getInstance() ;
            calendar.set(year ,monthOfYear , dayOfMonth);
            return calendar ;
        }
    }
    public static Calendar getCalendar(){
        if (isIslamic) {
            IslamicCalendar calendar = new IslamicCalendar();
            calendar.setCalculationType(IslamicCalendar.CalculationType.ISLAMIC_UMALQURA);
            return calendar;
        }else {
            return Calendar.getInstance();
        }
    }
    public static Calendar getIslamicOnly(int year, int monthOfYear , int dayOfMonth){
        IslamicCalendar calendar = new IslamicCalendar(year, monthOfYear, dayOfMonth);
        calendar.setCalculationType(IslamicCalendar.CalculationType.ISLAMIC_UMALQURA);
        return calendar;
    }
    public static Calendar getGeorgianOnly(int year, int monthOfYear , int dayOfMonth){
        Calendar calendar = Calendar.getInstance() ;
        calendar.set(year ,monthOfYear , dayOfMonth);
        return calendar ;
    }
    public static SimpleDateFormat getSdfIslamicOnly(){
        return new SimpleDateFormat("dd MMM yyyy",
                new ULocale("ar", "eg", "@calendar=islamic-umalqura"));
    }
    public static SimpleDateFormat getSdfGeorgianOnly(){
        return new SimpleDateFormat("dd MMM yyyy",
                new ULocale("ar"));
    }

}
