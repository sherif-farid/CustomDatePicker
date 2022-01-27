package com.tsongkha.spinnerdatepickerexample;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.TextView;

import com.tsongkha.spinnerdatepicker.MyDatePicker;
import com.tsongkha.spinnerdatepicker.OnDateChangedListener;
import com.tsongkha.spinnerdatepicker.Utils;

/**
 * Created by rawsond on 25/08/17.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements
       /* DatePickerDialog.OnDateSetListener,
        DatePickerDialog.OnDateCancelListener, */
        OnDateChangedListener {
    private String tag = "MainActivityTag";
//    TextView dateTextView;
    TextView dateTv ;
    Button dateButton;
    SimpleDateFormat simpleDateFormat;
    private int current = 2000 ;
    private int max = 2100 ;
    private int min = 1900 ;
//    private int current = 1441 ;
//    private int max = 1443 ;
//    private int min = 1300 ;

    private MyDatePicker myDatePickerFrame;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.isIslamic = true ;
        dateButton = (Button) findViewById(R.id.set_date_button);
//        dateTextView = (TextView) findViewById(R.id.date_textview);
        myDatePickerFrame = findViewById(R.id.datePickerFrame);
        dateTv = findViewById(R.id.dateTv);

        Calendar minDate = Utils.getCalendar(1300 , 0 , 1);

        Calendar maxDate = Utils.getCalendar(1500 , 0 , 1);

        myDatePickerFrame.setMinDate(minDate.getTimeInMillis());
        myDatePickerFrame.setMaxDate(maxDate.getTimeInMillis());
        myDatePickerFrame.init(1443,
               0,
               1,
                true,
                this);

        ////
//        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy",Utils.getULocale());
//        dateButton.setOnClickListener(view ->
//                showDate(current,
//                        0,
//                        1,
//                        R.style.DatePickerSpinner)
//        );

    }

    /*@Override
    public void onDateSet(MyDatePicker view, int year, int monthOfYear, int dayOfMonth) {
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
    public void onCancelled(MyDatePicker view) {
        dateTextView.setText(R.string.cancelled);
    }
*/

    /*@VisibleForTesting
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
    }*/


    @Override
    public void onDateChanged(MyDatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String raw = ""+year+"-"+monthOfYear+"-"+dayOfMonth;
        if (Utils.isIslamic) {
            Calendar islamicCal = Utils.getIslamicOnly(year, monthOfYear, dayOfMonth);

            Calendar greCal = Calendar.getInstance();
            greCal.setTimeInMillis(islamicCal.getTimeInMillis());

            String georgian = Utils.getSdfGeorgianOnly().format(greCal);
            String islamic = Utils.getSdfIslamicOnly().format(islamicCal);
            String allText = raw + "\n" + georgian + "\n" + islamic;
            dateTv.setText(allText);
        }else {

            Calendar greCal = Utils.getGeorgianOnly(year, monthOfYear, dayOfMonth);
            Calendar islamicCal = Utils.getIslamicOnly(year, monthOfYear, dayOfMonth);
            islamicCal.setTimeInMillis(greCal.getTimeInMillis());

            String georgian = Utils.getSdfGeorgianOnly().format(greCal);
            String islamic = Utils.getSdfIslamicOnly().format(islamicCal);
            String allText = raw + "\n" + georgian + "\n" + islamic;
            dateTv.setText(allText);
        }
    }
}