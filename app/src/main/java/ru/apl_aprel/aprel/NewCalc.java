package ru.apl_aprel.aprel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
/**
 * A simple {@link Fragment} subclass.
 */
public class NewCalc extends Fragment {


    public NewCalc() {
        // Required empty public constructor
    }

    private DatePicker mDatePicker;
    private TextView mInfoTextView;

    public int result_day;
    public int result_month;
    public int result_year;
    public EditText result_name;


    // устанавливаем текущую дату
    public void setCurrentDateOnView() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 18;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // set current date into textview
        mInfoTextView.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(day).append(".").append(month + 1).append(".")
                .append(year));

        // Устанавливаем текущую дату для DatePicker
        mDatePicker.init(year, month, day, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_new_calc, container, false);

        final Intent intent = new Intent(getActivity(), Result.class);

        root.findViewById(R.id.calc_button);

        mInfoTextView = (TextView) root.findViewById(R.id.debugTextView);
        mDatePicker = (DatePicker) root.findViewById(R.id.datePicker);
        result_name = (EditText) root.findViewById(R.id.input_name);
        /*Calendar today = Calendar.getInstance();

        mDatePicker.init(
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                        result_day = dayOfMonth;
                        result_month = (monthOfYear + 1);
                        result_year = year;
                        Toast.makeText(getActivity().getApplicationContext(),
                                "onDateChanged", Toast.LENGTH_SHORT).show();

                        mInfoTextView.setText("Год: " + year + "\n" + "Месяц: "
                                + (monthOfYear + 1) + "\n" + "День: " + dayOfMonth);
                    }
                });*/

        setCurrentDateOnView();


        final Button button = (Button) root.findViewById(R.id.calc_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("result_name", result_name.getText().toString());
                intent.putExtra("result_day", mDatePicker.getDayOfMonth()  );
                intent.putExtra("result_month", mDatePicker.getMonth() + 1);
                intent.putExtra("result_year", mDatePicker.getYear() );


                /*Toast.makeText(getActivity().getApplicationContext(),
                        result_name.getText().toString(), Toast.LENGTH_SHORT).show();*/
                //mInfoTextView.setText(result_name.getText().toString());
                startActivity(intent);
            }
        });

        return root;
    }
}