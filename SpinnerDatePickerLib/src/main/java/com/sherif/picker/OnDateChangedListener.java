package com.sherif.picker;

/**
 * Created by David on 25/11/2017.
 */
public interface OnDateChangedListener {
    /**
     * Called upon a date change.
     *
     * @param view        The view associated with this listener.
     * @param year        The year that was set.
     * @param monthOfYear The month that was set (0-11) for compatibility
     *                    with {@link java.util.Calendar}.
     * @param dayOfMonth  The day of the month that was set.
     */
    void onDateChanged(MyDatePicker view,
                       int year,
                       int monthOfYear,
                       int dayOfMonth ,boolean isIslamic,String georgian , String islamic);
}
