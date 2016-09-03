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

import java.util.Calendar;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_new_calc, container, false);

        final Intent intent = new Intent(getActivity(), Result.class);

        root.findViewById(R.id.calc_button);

        mDatePicker = (DatePicker) root.findViewById(R.id.datePicker);
        result_name = (EditText) root.findViewById(R.id.input_name);
        result_surname = (EditText) root.findViewById(R.id.input_surname);
        result_occupation = (EditText) root.findViewById(R.id.input_occupation);

        setCurrentDateOnView();


        final Button button = (Button) root.findViewById(R.id.calc_button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("result_name", result_name.getText().toString());
                intent.putExtra("result_name", result_surname.getText().toString());
                intent.putExtra("result_name", result_occupation.getText().toString());
                intent.putExtra("result_day", mDatePicker.getDayOfMonth()  );
                intent.putExtra("result_month", mDatePicker.getMonth() + 1);
                intent.putExtra("result_year", mDatePicker.getYear() );
                startActivity(intent);
            }
        });

        return root;
    }
}