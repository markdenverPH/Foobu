package com.example.babar.foobu;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    final Calendar c = Calendar.getInstance();
    int year, day, month;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker

        year = c.get(Calendar.YEAR) - 18;
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
        Calendar userAge = new GregorianCalendar(year,month,day);
        Calendar minAdultAge = new GregorianCalendar();
        minAdultAge.add(Calendar.YEAR, -18);
        RegistrationActivity reg = new RegistrationActivity();
        if (minAdultAge.before(userAge)) {
            Toast.makeText(getActivity(), "You should be in a legal age",Toast.LENGTH_LONG).show();
            reg.reg_birthdate.setText("");
        } else {
            reg.reg_birthdate.setText(new DateFormatSymbols().getMonths()[month] + " " + day + ", " + year); //in words; proper
            reg.hold_date = month + "-"+day+"-"+year; //set the original int
        }
    }
}
