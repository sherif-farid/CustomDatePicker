package com.sherif.pickerexample;

import android.icu.util.IslamicCalendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.sherif.picker.MyDatePicker;
import com.sherif.picker.OnDateChangedListener;
import com.sherif.pickerexample.databinding.ActivityMainBinding;

/**
 * Created by rawsond on 25/08/17.
 */

public class MainActivity extends AppCompatActivity implements
        OnDateChangedListener{
    private String tag = "MainActivityTag";

    private ActivityMainBinding binding ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            binding.datePickerFrame.init(this);
//            binding.datePickerFrame2.init(this);
//        }
        binding.datePickerFrame.initBeforeApi24(this, "ar");
        binding.datePickerFrame2.initBeforeApi24(this, "ar");
    }

    @Override
    public void onDateChanged(MyDatePicker view, int year,
                              int monthOfYear, int dayOfMonth ,
                              boolean isIslamic ,String georgian , String islamic) {
        String raw = ""+year+"-"+monthOfYear+"-"+dayOfMonth;
        String allText = raw + "\n" + georgian + "\n" + islamic;
        if (view == binding.datePickerFrame) {
            binding.dateTv1.setText(allText);
        }else if(view == binding.datePickerFrame2){
            binding.dateTv2.setText(allText);
        }
    }
}