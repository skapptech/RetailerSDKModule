package app.retailer.krina.shop.com.mp_shopkrina_retailer.picker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private String mTitle = "Set Date";
    private OnTimeSetListener mListener;


    public interface OnTimeSetListener {
        void onTimeSet(int hour, int min, String am_pm);
    }

    public void setListener(OnTimeSetListener listener) {
        mListener = listener;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int hour, min;

        if (Build.VERSION.SDK_INT >= 24) {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR);
            min = c.get(Calendar.MINUTE);
        } else {
            final java.util.Calendar c = java.util.Calendar.getInstance();
            hour = c.get(java.util.Calendar.HOUR);
            min = c.get(java.util.Calendar.MINUTE);
        }

        // Create a new instance of TimePickerDialog and return it
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), this, hour, min, false) {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                super.onTimeChanged(view, hourOfDay, minute);
                setTitle(mTitle);
            }
        };

        dialog.setTitle(mTitle);
        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        if (mListener != null) {
            if (timePicker.getCurrentHour() > 12) {
                mListener.onTimeSet(hour, min, "PM");
            } else {
                mListener.onTimeSet(hour, min, "AM");
            }
        }
    }
}