package com.sherif.picker;
/* Fork of Oreo DatePickerSpinnerDelegate
 *
 * Original class is Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;


import com.sherif.picker.databinding.DatePickerContainerBinding;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;


/**
 * A delegate implementing the basic DatePicker
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class MyDatePicker extends FrameLayout {


    private static final String DATE_FORMAT = "MM/dd/yyyy";

    private static final boolean DEFAULT_ENABLED_STATE = true;

    private EditText mDaySpinnerInput;

    private EditText mMonthSpinnerInput;

    private EditText mYearSpinnerInput;

    private Context mContext;

    private OnDateChangedListener mOnDateChangedListener;

    private String[] mShortMonths;


    private int mNumberOfMonths;

    private Calendar mTempDate;

    private Calendar mMinDate;

    private Calendar mMaxDate;

    private Calendar mCurrentDate;

    private boolean mIsEnabled = DEFAULT_ENABLED_STATE;

    private boolean mIsDayShown = true;

    private String tag = "DatePickerTag";
    private boolean isIslamic = false ;

    private DatePickerContainerBinding binding ;

    public MyDatePicker(@NonNull Context context) {
        super(context);
        Log.v(tag , "cons 1");
        initConstructor(context , 0);
    }

    public MyDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs ,R.styleable.MyDatePicker);
        isIslamic = typedArray.getBoolean(R.styleable.MyDatePicker_isIslamic ,false);
        typedArray.recycle();
        Log.v(tag , "cons 2 isIslamic :"+isIslamic);
        initConstructor(context , 0);

    }

    public MyDatePicker(@NonNull Context context,
                        @Nullable AttributeSet attrs,
                        int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.v(tag , "cons 3");
        initConstructor(context , 0);
    }

    public MyDatePicker(@NonNull Context context,
                        @Nullable AttributeSet attrs,
                        int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        Log.v(tag , "cons 4");
        initConstructor(context , 0);
    }
    MyDatePicker(Context context, int numberPickerStyle) {
        super(context);
        Log.v(tag , "cons 5");
        initConstructor(context, numberPickerStyle);
    }
    private void initBasicData(){
        mIsDayShown = true;
        if (isIslamic) {
            Calendar minDate = Utils.getCalendar(1300, 0, 1 ,isIslamic);

            Calendar maxDate = Utils.getCalendar(1500, 0, 1,isIslamic);

            setMinDate(minDate.getTimeInMillis());
            setMaxDate(maxDate.getTimeInMillis());
            setDate(1443, 0, 1);
            updateSpinners();
        }else {

            Calendar minDate = Utils.getCalendar(1900, 0, 1,isIslamic);

            Calendar maxDate = Utils.getCalendar(2100, 0, 1,isIslamic);

            setMinDate(minDate.getTimeInMillis());
            setMaxDate(maxDate.getTimeInMillis());
            setDate(2022, 0, 1);

            updateSpinners();
        }
    }
    private void initConstructor(Context context, int numberPickerStyle){
        mContext = context;
        // initialization based on locale
//        LayoutInflater inflater2 = LayoutInflater.from(context);
        setCurrentLocale(Locale.getDefault());
        LayoutInflater inflater = (LayoutInflater) new ContextThemeWrapper(
                mContext,
                numberPickerStyle).getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        binding = DatePickerContainerBinding.inflate(inflater , this , true);

//        inflater.inflate(R.layout.date_picker_container, this, true);

//        mPickerContainer = findViewById(R.id.parent);

        OnValueChangeListener onChangeListener = (picker, oldVal, newVal) -> {
            updateInputState();
            mTempDate.setTimeInMillis(mCurrentDate.getTimeInMillis());
            // take care of wrapping of days and months to update greater fields
            if (picker == binding.mDay) {
                int maxDayOfMonth = mTempDate.getActualMaximum(Calendar.DAY_OF_MONTH);
                if (oldVal == maxDayOfMonth && newVal == 1) {
                    mTempDate.add(Calendar.DAY_OF_MONTH, 1);
                } else if (oldVal == 1 && newVal == maxDayOfMonth) {
                    mTempDate.add(Calendar.DAY_OF_MONTH, -1);
                } else {
                    mTempDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal);
                }
            }
            else if (picker == binding.mMonth) {
                if (oldVal == 11 && newVal == 0) {
                    mTempDate.add(Calendar.MONTH, 1);
                } else if (oldVal == 0 && newVal == 11) {
                    mTempDate.add(Calendar.MONTH, -1);
                } else {
                    mTempDate.add(Calendar.MONTH, newVal - oldVal);
                }
            }
            else if (picker == binding.mYear) {
                mTempDate.set(Calendar.YEAR, newVal);
            }
            else {
                throw new IllegalArgumentException();
            }
            // now set the date to the adjusted one
            setDate(mTempDate.get(Calendar.YEAR), mTempDate.get(Calendar.MONTH),
                    mTempDate.get(Calendar.DAY_OF_MONTH));
            updateSpinners();
            notifyDateChanged();
        };

        binding.mDay.setFormatter(new TwoDigitFormatter());
        binding.mDay.setOnLongPressUpdateInterval(100);
        binding.mDay.setOnValueChangedListener(onChangeListener);
        mDaySpinnerInput = NumberPickers.findEditText(binding.mDay);


        binding.mMonth.setMinValue(0);
        binding.mMonth.setMaxValue(mNumberOfMonths - 1);
        binding.mMonth.setDisplayedValues(mShortMonths);
        binding.mMonth.setOnLongPressUpdateInterval(200);
        binding.mMonth.setOnValueChangedListener(onChangeListener);
        mMonthSpinnerInput = NumberPickers.findEditText(binding.mMonth);

        binding.mYear.setOnLongPressUpdateInterval(100);
        binding.mYear.setOnValueChangedListener(onChangeListener);
        mYearSpinnerInput = NumberPickers.findEditText(binding.mYear);

        // initialize to current date
        mCurrentDate.setTimeInMillis(System.currentTimeMillis());

        // If not explicitly specified this view is important for accessibility.
        if (getImportantForAccessibility() == View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

//        root.addView(this);
        initBasicData();
    }

    public void init( OnDateChangedListener onDateChangedListener) {
        mOnDateChangedListener = onDateChangedListener;
        notifyDateChanged();
    }

    void updateDate(int year, int month, int dayOfMonth) {
        if (!isNewDate(year, month, dayOfMonth)) {
            return;
        }
        setDate(year, month, dayOfMonth);
        updateSpinners();
        notifyDateChanged();
    }

    int getYear() {
        return mCurrentDate.get(Calendar.YEAR);
    }

    int getMonth() {
        return mCurrentDate.get(Calendar.MONTH);
    }

    int getDayOfMonth() {
        return mCurrentDate.get(Calendar.DAY_OF_MONTH);
    }

    public void setMinDate(long minDate) {
        mTempDate.setTimeInMillis(minDate);
        if (mTempDate.get(Calendar.YEAR) == mMinDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) == mMinDate.get(Calendar.DAY_OF_YEAR)) {
            // Same day, no-op.
            return;
        }
        mMinDate.setTimeInMillis(minDate);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        }
        updateSpinners();
    }

    public void setMaxDate(long maxDate) {
        mTempDate.setTimeInMillis(maxDate);
        if (mTempDate.get(Calendar.YEAR) == mMaxDate.get(Calendar.YEAR)
                && mTempDate.get(Calendar.DAY_OF_YEAR) == mMaxDate.get(Calendar.DAY_OF_YEAR)) {
            // Same day, no-op.
            return;
        }
        mMaxDate.setTimeInMillis(maxDate);
        if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }
        updateSpinners();
    }

    @Override
    public void setEnabled(boolean enabled) {
        binding.mDay.setEnabled(enabled);
        binding.mMonth.setEnabled(enabled);
        binding.mYear.setEnabled(enabled);
        mIsEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return mIsEnabled;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setCurrentLocale(newConfig.locale);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    /**
     * Sets the current locale.
     *
     * @param locale The current locale.
     */
    protected void setCurrentLocale(Locale locale) {
        mTempDate = getCalendarForLocale(mTempDate, locale);
        mMinDate = getCalendarForLocale(mMinDate, locale);
        mMaxDate = getCalendarForLocale(mMaxDate, locale);
        mCurrentDate = getCalendarForLocale(mCurrentDate, locale);

        mNumberOfMonths = mTempDate.getActualMaximum(Calendar.MONTH) + 1;
        mShortMonths = /*new DateFormatSymbols().getShortMonths();*/
        Utils.getSimpleDateFormat(isIslamic).getDateFormatSymbols().getShortMonths();

        if (usingNumericMonths()) {
            // We're in a locale where a date should either be all-numeric, or all-text.
            // All-text would require custom NumberPicker formatters for day and year.
            mShortMonths = new String[mNumberOfMonths];
            for (int i = 0; i < mNumberOfMonths; ++i) {
                mShortMonths[i] = String.format("%d", i + 1);
            }
        }
    }

    /**
     * Tests whether the current locale is one where there are no real month names,
     * such as Chinese, Japanese, or Korean locales.
     */
    private boolean usingNumericMonths() {
        return false /*Character.isDigit(mShortMonths[IslamicCalendar.MUHARRAM].charAt(0))*/;
    }

    /**
     * Gets a calendar for locale bootstrapped with the value of a given calendar.
     *
     * @param oldCalendar The old calendar.
     * @param locale      The locale.
     */
    private Calendar getCalendarForLocale(Calendar oldCalendar, Locale locale) {
        if (oldCalendar == null) {
            return Utils.getCalendar(isIslamic);
        } else {
            final long currentTimeMillis = oldCalendar.getTimeInMillis();
            Calendar newCalendar = Utils.getCalendar(isIslamic);
            newCalendar.setTimeInMillis(currentTimeMillis);
            return newCalendar;
        }
    }

    //see http://androidxref.com/4.1.1/xref/packages/apps/Contacts/src/com/android/contacts/datepicker/DatePicker.java
    private String getOrderJellyBeanMr2() {
        java.text.DateFormat format;
        String order;
        if (mShortMonths[0].startsWith("1")) {
            format = DateFormat.getDateFormat(getContext());
        } else {
            format = DateFormat.getMediumDateFormat(getContext());
        }

        if (format instanceof SimpleDateFormat) {
            order = ((SimpleDateFormat) format).toPattern();
        } else {
            // Shouldn't happen, but just in case.
            order = new String(DateFormat.getDateFormatOrder(getContext()));
        }
        return order;
    }

    private boolean isNewDate(int year, int month, int dayOfMonth) {
        return (mCurrentDate.get(Calendar.YEAR) != year
                || mCurrentDate.get(Calendar.MONTH) != month
                || mCurrentDate.get(Calendar.DAY_OF_MONTH) != dayOfMonth);
    }

    private void setDate(int year, int month, int dayOfMonth) {
        mCurrentDate.set(year, month, dayOfMonth);
        if (mCurrentDate.before(mMinDate)) {
            mCurrentDate.setTimeInMillis(mMinDate.getTimeInMillis());
        } else if (mCurrentDate.after(mMaxDate)) {
            mCurrentDate.setTimeInMillis(mMaxDate.getTimeInMillis());
        }
    }

    private void updateSpinners() {
        // set the spinner ranges respecting the min and max dates
        binding.mDay.setVisibility(mIsDayShown ? View.VISIBLE : View.GONE);
        if (mCurrentDate.equals(mMinDate)) {
            binding.mDay.setMinValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
            binding.mDay.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            binding.mDay.setWrapSelectorWheel(false);
            binding.mMonth.setDisplayedValues(null);
            binding.mMonth.setMinValue(mCurrentDate.get(Calendar.MONTH));
            binding.mMonth.setMaxValue(mCurrentDate.getActualMaximum(Calendar.MONTH));
            binding.mMonth.setWrapSelectorWheel(false);
        } else if (mCurrentDate.equals(mMaxDate)) {
            binding.mDay.setMinValue(mCurrentDate.getActualMinimum(Calendar.DAY_OF_MONTH));
            binding.mDay.setMaxValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));
            binding.mDay.setWrapSelectorWheel(false);
            binding.mMonth.setDisplayedValues(null);
            binding.mMonth.setMinValue(mCurrentDate.getActualMinimum(Calendar.MONTH));
            binding.mMonth.setMaxValue(mCurrentDate.get(Calendar.MONTH));
            binding.mMonth.setWrapSelectorWheel(false);
        } else {
            binding.mDay.setMinValue(1);
            binding.mDay.setMaxValue(mCurrentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            binding.mDay.setWrapSelectorWheel(true);
            binding.mMonth.setDisplayedValues(null);
            binding.mMonth.setMinValue(0);
            binding.mMonth.setMaxValue(11);
            binding.mMonth.setWrapSelectorWheel(true);
        }

        // make sure the month names are a zero based array
        // with the months in the month spinner
        String[] displayedValues = Arrays.copyOfRange(mShortMonths,
                binding.mMonth.getMinValue(),
                binding.mMonth.getMaxValue() + 1);
        for (String displayedValue : displayedValues) {
            Log.v(tag , "displayedValues :"+displayedValue);
        }

        binding.mMonth.setDisplayedValues(displayedValues);

        // year spinner range does not change based on the current date
        binding.mYear.setMinValue(mMinDate.get(Calendar.YEAR));
        binding.mYear.setMaxValue(mMaxDate.get(Calendar.YEAR));
        binding.mYear.setWrapSelectorWheel(false);

        // set the spinner values
        binding.mYear.setValue(mCurrentDate.get(Calendar.YEAR));
        binding.mMonth.setValue(mCurrentDate.get(Calendar.MONTH));
        binding.mDay.setValue(mCurrentDate.get(Calendar.DAY_OF_MONTH));

        if (usingNumericMonths()) {
            mMonthSpinnerInput.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }


    /**
     * Notifies the listener, if such, for a change in the selected date.
     */
    private void notifyDateChanged() {
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED);
        if (mOnDateChangedListener != null) {
            int year =  getYear();
            int monthOfYear = getMonth() ;
            int dayOfMonth = getDayOfMonth() ;
            String georgian ;
            String islamic ;
            if (isIslamic) {
                Calendar islamicCal = Utils.getIslamicOnly(year, monthOfYear, dayOfMonth);
                Calendar greCal = Calendar.getInstance();
                greCal.setTimeInMillis(islamicCal.getTimeInMillis());

                georgian = Utils.getSdfGeorgianOnly().format(greCal);
                islamic = Utils.getSdfIslamicOnly().format(islamicCal);
            }else {

                Calendar greCal = Utils.getGeorgianOnly(year, monthOfYear, dayOfMonth);
                Calendar islamicCal = Utils.getIslamicOnly(year, monthOfYear, dayOfMonth);
                islamicCal.setTimeInMillis(greCal.getTimeInMillis());

                georgian = Utils.getSdfGeorgianOnly().format(greCal);
                islamic = Utils.getSdfIslamicOnly().format(islamicCal);
            }
            mOnDateChangedListener.onDateChanged(this,
                   year,
                    monthOfYear,
                     dayOfMonth,
                    isIslamic ,
                    georgian,
                    islamic);
        }
    }

    /**
     * Sets the IME options for a spinner based on its ordering.
     *
     * @param spinner      The spinner.
     * @param spinnerCount The total spinner count.
     * @param spinnerIndex The index of the given spinner.
     */
    private void setImeOptions(NumberPicker spinner, int spinnerCount, int spinnerIndex) {
        final int imeOptions;
        if (spinnerIndex < spinnerCount - 1) {
            imeOptions = EditorInfo.IME_ACTION_NEXT;
        } else {
            imeOptions = EditorInfo.IME_ACTION_DONE;
        }
        TextView input = NumberPickers.findEditText(spinner);
        input.setImeOptions(imeOptions);
    }

    private void updateInputState() {
        // Make sure that if the user changes the value and the IME is active
        // for one of the inputs if this widget, the IME is closed. If the user
        // changed the value via the IME and there is a next input the IME will
        // be shown, otherwise the user chose another means of changing the
        // value and having the IME up makes no sense.
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            if (inputMethodManager.isActive(mYearSpinnerInput)) {
                mYearSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mMonthSpinnerInput)) {
                mMonthSpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            } else if (inputMethodManager.isActive(mDaySpinnerInput)) {
                mDaySpinnerInput.clearFocus();
                inputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        return new SavedState(superState, mCurrentDate, mMinDate, mMaxDate, mIsDayShown);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        mCurrentDate = Utils.getCalendar(isIslamic);
        mCurrentDate.setTimeInMillis(ss.currentDate);
        mMinDate = Utils.getCalendar(isIslamic);
        mMinDate.setTimeInMillis(ss.minDate);
        mMaxDate = Utils.getCalendar(isIslamic);
        mMaxDate.setTimeInMillis(ss.maxDate);
        updateSpinners();
    }

    private static class SavedState extends BaseSavedState {

        @SuppressWarnings("unused") public static final Parcelable.Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        final long currentDate;
        final long minDate;
        final long maxDate;
        final boolean isDaySpinnerShown;

        /**
         * Constructor called from {@link MyDatePicker#onSaveInstanceState()}
         */
        SavedState(Parcelable superState,
                   Calendar currentDate,
                   Calendar minDate,
                   Calendar maxDate,
                   boolean isDaySpinnerShown) {
            super(superState);
            this.currentDate = currentDate.getTimeInMillis();
            this.minDate = minDate.getTimeInMillis();
            this.maxDate = maxDate.getTimeInMillis();
            this.isDaySpinnerShown = isDaySpinnerShown;
        }

        /**
         * Constructor called from {@link #CREATOR}
         */
        private SavedState(Parcel in) {
            super(in);
            this.currentDate = in.readLong();
            this.minDate = in.readLong();
            this.maxDate = in.readLong();
            this.isDaySpinnerShown = in.readByte() != 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(currentDate);
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
            dest.writeByte(isDaySpinnerShown ? (byte) 1 : (byte) 0);
        }
    }
}