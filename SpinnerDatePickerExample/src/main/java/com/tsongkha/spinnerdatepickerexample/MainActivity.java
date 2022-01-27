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
import com.tsongkha.spinnerdatepickerexample.databinding.ActivityMainBinding;

/**
 * Created by rawsond on 25/08/17.
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity implements OnDateChangedListener {
    private String tag = "MainActivityTag";

    private ActivityMainBinding binding ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.datePickerFrame.init(this);
        binding.datePickerFrame2.init(this);
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