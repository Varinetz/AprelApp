package ru.apl_aprel.aprel;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewCalc extends Fragment {


    public NewCalc() {
        // Required empty public constructor
    }

    private DatePicker mDatePicker;

    public EditText result_name;
    public EditText result_surname;
    public EditText result_occupation;


    // устанавливаем текущую дату
    public void setCurrentDateOnView() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR) - 18;
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Устанавливаем текущую дату для DatePicker
        mDatePicker.init(year, month, day, null);
        mDatePicker.setMaxDate(new Date().getTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_new_calc, container, false);

        final Intent resultIntent = new Intent(getActivity(), Result.class);

        root.findViewById(R.id.calc_button);

        mDatePicker = (DatePicker) root.findViewById(R.id.datePicker);
        result_name = (EditText) root.findViewById(R.id.input_name);
        result_surname = (EditText) root.findViewById(R.id.input_surname);
        result_occupation = (EditText) root.findViewById(R.id.input_occupation);

        setCurrentDateOnView();


        final Button button = (Button) root.findViewById(R.id.calc_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                resultIntent.putExtra("result_day", mDatePicker.getDayOfMonth()  );
                resultIntent.putExtra("result_month", mDatePicker.getMonth() + 1);
                resultIntent.putExtra("result_year", mDatePicker.getYear() );

                if (result_name != null && result_name.getText().toString().length()>0 || result_surname != null && result_surname.getText().toString().length()>0) {
                    resultIntent.putExtra("result_name", result_name.getText().toString());
                    resultIntent.putExtra("result_surname", result_surname.getText().toString());
                    resultIntent.putExtra("result_occupation", result_occupation.getText().toString());
                    resultIntent.putExtra("correct_form", true);
                } else {
                    resultIntent.putExtra("correct_form", false);
                    Context ctx = getActivity().getApplicationContext();
                    Toast.makeText(ctx, "Чтобы сохранить, вернитесь назад и заполните имя", Toast.LENGTH_LONG).show();
                }
                startActivity(resultIntent);
            }
        });

        return root;
    }
}