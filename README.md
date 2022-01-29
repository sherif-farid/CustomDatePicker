# CustomDatePicker

base code forked from https://github.com/drawers/SpinnerDatePicker

- changes :
- use picker as a normal frame layout instead of alert dialog
- add support for hijri calendar

usage :
[![](https://jitpack.io/v/sherif-farid/CustomDatePicker.svg)](https://jitpack.io/#sherif-farid/CustomDatePicker)


Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	```

Add the dependency
```
dependencies {
	        implementation 'com.github.sherif-farid:CustomDatePicker:Tag'
	}
```

in xml (fragment - activity - dialog)

```
 <com.sherif.picker.MyDatePicker
        android:id="@+id/datePickerFrame"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:datePickerMode="spinner"
        android:calendarViewShown="false"
        style="@style/DialogPickerStyle"
        app:isIslamic="false"/>	// or true to show islamic or gregorian calendar
```
in onCreate
```
binding.datePickerFrame.initBeforeApi24(this, "ar");
```
also override this call back to listen to changes
```
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
```
