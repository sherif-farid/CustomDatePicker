package com.tsongkha.spinnerdatepicker;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;


@RequiresApi(api = Build.VERSION_CODES.N)
public class SpinnerDatePickerDialogBuilder {
    private String tag = "SpinnerDateTag";
    private Context context;
    private DatePickerDialog.OnDateSetListener callBack;
    private DatePickerDialog.OnDateCancelListener onCancel;
    private boolean isDayShown = true;
    private boolean isTitleShown = true;
    private String customTitle = "";
    private int theme = 0;                 //default theme
    private int spinnerTheme = 0;          //default theme
    private Calendar defaultDate /* new GregorianCalendar(1980, 0, 1)*/;
    private Calendar minDate /* new GregorianCalendar(1900, 0, 1)*/;
    private Calendar maxDate  /*new GregorianCalendar(2100, 0, 1)*/;


    public SpinnerDatePickerDialogBuilder context(Context context) {
        this.context = context;
        return this;
    }

    public SpinnerDatePickerDialogBuilder callback(DatePickerDialog.OnDateSetListener callBack) {
        this.callBack = callBack;
        return this;
    }

    public SpinnerDatePickerDialogBuilder onCancel(DatePickerDialog.OnDateCancelListener onCancel) {
        this.onCancel = onCancel;
        return this;
    }

    public SpinnerDatePickerDialogBuilder dialogTheme(int theme) {
        this.theme = theme;
        return this;
    }

    public SpinnerDatePickerDialogBuilder spinnerTheme(int spinnerTheme) {
        this.spinnerTheme = spinnerTheme;
        return this;
    }

    public SpinnerDatePickerDialogBuilder defaultDate(int year, int monthIndexedFromZero, int day) {
        this.defaultDate = Utils.getCalendar(year, monthIndexedFromZero, day);
        return this;
    }

    public SpinnerDatePickerDialogBuilder minDate(int year, int monthIndexedFromZero, int day) {
        this.minDate =Utils.getCalendar(year, monthIndexedFromZero, day);
        return this;
    }

    public SpinnerDatePickerDialogBuilder maxDate(int year, int monthIndexedFromZero, int day) {
        this.maxDate = Utils.getCalendar(year, monthIndexedFromZero, day);
        return this;
    }

    public SpinnerDatePickerDialogBuilder showDaySpinner(boolean showDaySpinner) {
        this.isDayShown = showDaySpinner;
        return this;
    }

    public SpinnerDatePickerDialogBuilder showTitle(boolean showTitle) {
        this.isTitleShown = showTitle;
        return this;
    }

    public SpinnerDatePickerDialogBuilder customTitle(String title) {
        this.customTitle = title;
        return this;
    }

    public DatePickerDialog build() {
        if (context == null) throw new IllegalArgumentException("Context must not be null");
        if (maxDate.getTime().getTime() <= minDate.getTime().getTime()) {
            Log.v(tag , "maxDate : "+maxDate.get(Calendar.YEAR) +" MONTH : "
                    +maxDate.get(Calendar.MONTH)+" DAY_OF_MONTH : "+maxDate.get(Calendar.DAY_OF_MONTH)
                    +" \n / minDate : "+minDate.get(Calendar.YEAR)+" MONTH : "
                    +minDate.get(Calendar.MONTH)+" DAY_OF_MONTH : "+minDate.get(Calendar.DAY_OF_MONTH) );
            throw new IllegalArgumentException("Max date is not after Min date");
        }

        return new DatePickerDialog(context,
                theme,
                spinnerTheme,
                callBack,
                onCancel,
                defaultDate,
                minDate,
                maxDate,
                isDayShown,
                isTitleShown,
                customTitle);
    }
}