package app.retailer.krina.shop.com.mp_shopkrina_retailer.picker;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Locale;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private String minMax;
    private OnDateSetListener mListener;
    private OnCancelListener cancelListener;

    public DatePickerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = this.getArguments();
            minMax = bundle.getString("minMax");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        int year, month, day;
        if (Build.VERSION.SDK_INT >= 24) {
            final Calendar c = Calendar.getInstance(Locale.ENGLISH);
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        } else {
            final java.util.Calendar c = java.util.Calendar.getInstance(Locale.ENGLISH);
            year = c.get(java.util.Calendar.YEAR);
            month = c.get(java.util.Calendar.MONTH);
            day = c.get(java.util.Calendar.DAY_OF_MONTH);
        }
        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerFragment.this, year, month, day) {
            @Override
            public void onDateChanged(@NonNull DatePicker view, int year1, int month1, int dayOfMonth) {
                super.onDateChanged(view, year1, month1, dayOfMonth);
            }
        };
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog1, which) -> {
            if (cancelListener != null) {
                cancelListener.onCancel();
            }
        });
        /*dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (cancelListener != null) {
                    cancelListener.onCancel();
                }
            }
        });*/

        if (minMax.equals("0")) {
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000);
        } else if (minMax.equals("1")) {
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 1000);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (mListener != null) {
            mListener.onDateSet(year, month, day);
        }
    }

    public interface OnDateSetListener {
        void onDateSet(int year, int month, int day);
    }

    public interface OnCancelListener {
        void onCancel();
    }

    public void setCancelListener(OnCancelListener listener) {
        cancelListener = listener;
    }

    public void setListener(OnDateSetListener listener) {
        mListener = listener;
    }
}
