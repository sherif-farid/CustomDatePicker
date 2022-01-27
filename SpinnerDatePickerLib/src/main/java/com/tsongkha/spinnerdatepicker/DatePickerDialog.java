package com.tsongkha.spinnerdatepicker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

/**
 * A fork of the Android Open Source Project DatePickerDialog class
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class DatePickerDialog extends AlertDialog implements OnClickListener,
        OnDateChangedListener {

    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String TITLE_SHOWN = "title_enabled";
    private static final String CUSTOM_TITLE = "custom_title";

    private final MyDatePicker mMyDatePicker;
    private final OnDateSetListener mCallBack;
    private final OnDateCancelListener mOnCancel;
    private final SimpleDateFormat mTitleDateFormat;

    private boolean mIsDayShown = true;
    private boolean mIsTitleShown = true;
    private String mCustomTitle = "";

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {
        /**
         * @param view        The view associated with this listener.
         * @param year        The year that was set
         * @param monthOfYear The month that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onDateSet(MyDatePicker view, int year, int monthOfYear, int dayOfMonth);
    }

    /**
     * Callback for when things are cancelled
     */
    public interface OnDateCancelListener {
        /**
         * Called when cancel happens.
         *
         * @param view The view associated with this listener.
         */
        void onCancelled(MyDatePicker view);
    }

    DatePickerDialog(Context context,
                     int theme,
                     int spinnerTheme,
                     OnDateSetListener callBack,
                     OnDateCancelListener onCancel,
                     Calendar defaultDate,
                     Calendar minDate,
                     Calendar maxDate,
                     boolean isDayShown,
                     boolean isTitleShown,
                     String customTitle) {
        super(context, theme);

        mCallBack = callBack;
        mOnCancel = onCancel;
        mTitleDateFormat = Utils.getSimpleDateFormat();
        mIsDayShown = isDayShown;
        mIsTitleShown = isTitleShown;
        mCustomTitle = customTitle;

        updateTitle(defaultDate);

        setButton(BUTTON_POSITIVE, context.getText(android.R.string.ok),
                this);
        setButton(BUTTON_NEGATIVE, context.getText(android.R.string.cancel),
                this);

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_picker_dialog_container, null);
        setView(view);
        mMyDatePicker = new MyDatePicker(view.getContext(), spinnerTheme);
        mMyDatePicker.setMinDate(minDate.getTimeInMillis());
        mMyDatePicker.setMaxDate(maxDate.getTimeInMillis());
        mMyDatePicker.init(defaultDate.get(Calendar.YEAR),
                defaultDate.get(Calendar.MONTH),
                defaultDate.get(Calendar.DAY_OF_MONTH),
                isDayShown, this);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE: {
                if (mCallBack != null) {
                    mMyDatePicker.clearFocus();
                    mCallBack.onDateSet(mMyDatePicker, mMyDatePicker.getYear(),
                            mMyDatePicker.getMonth(), mMyDatePicker.getDayOfMonth());
                }
                break;
            }
            case BUTTON_NEGATIVE: {
                if (mOnCancel != null) {
                    mMyDatePicker.clearFocus();
                    mOnCancel.onCancelled(mMyDatePicker);
                }
                break;
            }
        }
    }

    @Override
    public void onDateChanged(MyDatePicker view, int year, int monthOfYear, int dayOfMonth) {
        updateTitle(Utils.getCalendar(year , monthOfYear , dayOfMonth));
    }

    private void updateTitle(Calendar updatedDate) {
        if (mIsTitleShown && mCustomTitle != null && !mCustomTitle.isEmpty()) {
            setTitle(mCustomTitle);
        } else if (mIsTitleShown) {
            setTitle(mTitleDateFormat.format(updatedDate.getTime()));
        } else {
            setTitle(" ");
        }
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(YEAR, mMyDatePicker.getYear());
        state.putInt(MONTH, mMyDatePicker.getMonth());
        state.putInt(DAY, mMyDatePicker.getDayOfMonth());
        state.putBoolean(TITLE_SHOWN, mIsTitleShown);
        state.putString(CUSTOM_TITLE, mCustomTitle);
        return state;
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int year = savedInstanceState.getInt(YEAR);
        int month = savedInstanceState.getInt(MONTH);
        int day = savedInstanceState.getInt(DAY);
        mIsTitleShown = savedInstanceState.getBoolean(TITLE_SHOWN);
        mCustomTitle = savedInstanceState.getString(CUSTOM_TITLE);
        updateTitle(Utils.getCalendar(year , month , day));
        mMyDatePicker.init(year, month, day, mIsDayShown, this);
    }
}