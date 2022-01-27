package com.tsongkha.spinnerdatepickerexample;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.icu.util.ULocale;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tsongkha.spinnerdatepicker.DatePicker;
import com.tsongkha.spinnerdatepicker.DatePickerDialog;
import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;
import com.tsongkha.spinnerdatepicker.Utils;

import android.icu.util.IslamicCalendar;
import java.util.Locale;

/**
 * Created by rawsond on 25/08/17.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , DatePickerDialog.OnDateCancelListener{
    private String tag = "MainActivityTag";
    TextView dateTextView;
    Button dateButton;
    SimpleDateFormat simpleDateFormat;
    private int current = 2000 ;
    private int max = 2100 ;
    private int min = 1900 ;
//    private int current = 1441 ;
//    private int max = 1443 ;
//    private int min = 1300 ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.isIslamic = false ;
        dateButton = (Button) findViewById(R.id.set_date_button);
        dateTextView = (TextView) findViewById(R.id.date_textview);
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy",Utils.getULocale());
        dateButton.setOnClickListener(view ->
                showDate(current,
                        0,
                        1,
                        R.style.DatePickerSpinner)
        );


//        Log.v(tag ,"locale "+ulocale.getISO3Language());
//
//        IslamicCalendar islamicCalendar = new IslamicCalendar(ulocale);
//        islamicCalendar.set(1980 ,0 , 1);
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat =
//                new SimpleDateFormat ("yyyy MMM dd",ulocale);
//        Log.v(tag ,"gorgian calendar : "+dateFormat.format(calendar.getTime()));
//
//
//// full date
//        DateFormat df = DateFormat.getDateInstance(DateFormat.FULL,
//                ulocale);
//        Log.v(tag ,"1"+df.format(islamicCalendar.getTime()));
//
//// date in "yyyy MMM dd" format
//        SimpleDateFormat df1 =
//                new SimpleDateFormat ("yyyy MMM dd",ulocale);
//        Log.v(tag ,"2"+df1.format(islamicCalendar.getTime()));
//
//// name of month
//        SimpleDateFormat df2 = new SimpleDateFormat (SimpleDateFormat.MONTH, ulocale);
//        Log.v(tag ,"3"+df2.format(islamicCalendar.getTime()));
//
//// name of weekday
//        SimpleDateFormat df3 = new SimpleDateFormat (SimpleDateFormat.WEEKDAY, ulocale);
//        Log.v(tag ,"4"+df3.format(islamicCalendar.getTime()));

//        IslamicCalendar islamic = new IslamicCalendar(1443 , 0 ,1);
//        islamic.setCalculationType(IslamicCalendar.CalculationType.ISLAMIC_UMALQURA);
//        for (int i =1 ;i<365;i++){
//            islamic.add(IslamicCalendar.DAY_OF_MONTH , 1);
//            Log.v(tag , "islamicCalendar MONTH :"+islamic.get(IslamicCalendar.MONTH));
//            Log.v(tag , "islamicCalendar DAY_OF_MONTH :"+islamic.get(IslamicCalendar.DAY_OF_MONTH));
//        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String raw = ""+year+"-"+monthOfYear+"-"+dayOfMonth;
        if (Utils.isIslamic) {
            Calendar islamicCal = Utils.getIslamicOnly(year, monthOfYear, dayOfMonth);

            Calendar greCal = Calendar.getInstance();
            greCal.setTimeInMillis(islamicCal.getTimeInMillis());

            String georgian = Utils.getSdfGeorgianOnly().format(greCal);
            String islamic = Utils.getSdfIslamicOnly().format(islamicCal);
            String allText = raw + "\n" + georgian + "\n" + islamic;
            dateTextView.setText(allText);
        }else {

            Calendar greCal = Utils.getGeorgianOnly(year, monthOfYear, dayOfMonth);
            Calendar islamicCal = Utils.getIslamicOnly(year, monthOfYear, dayOfMonth);
            islamicCal.setTimeInMillis(greCal.getTimeInMillis());

            String georgian = Utils.getSdfGeorgianOnly().format(greCal);
            String islamic = Utils.getSdfIslamicOnly().format(islamicCal);
            String allText = raw + "\n" + georgian + "\n" + islamic;
            dateTextView.setText(allText);
        }
    }

    @Override
    public void onCancelled(DatePicker view) {
        dateTextView.setText(R.string.cancelled);
    }


    @VisibleForTesting
    void showDate(int year, int monthOfYear, int dayOfMonth, int spinnerTheme) {
        SpinnerDatePickerDialogBuilder builder =   new SpinnerDatePickerDialogBuilder()
                .context(MainActivity.this)
                .callback(MainActivity.this)
                .onCancel(MainActivity.this)
                .spinnerTheme(spinnerTheme)
                .defaultDate(year, monthOfYear, dayOfMonth)
                .maxDate(max ,11 ,31)
                .minDate(min , 0 ,1);
        builder.build().show();
    }


}