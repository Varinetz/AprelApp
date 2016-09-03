package ru.apl_aprel.aprel;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Руслан on 04.09.2016.
 */
public class TodoCursorAdapter extends CursorAdapter {
    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.saved_list_item, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView tvName = (TextView) view.findViewById(R.id.name);
        TextView tvSurname = (TextView) view.findViewById(R.id.surname);
        TextView tvOccupation = (TextView) view.findViewById(R.id.occupation);
        TextView tvDate = (TextView) view.findViewById(R.id.date);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String surname = cursor.getString(cursor.getColumnIndexOrThrow("surname"));
        String occupation = cursor.getString(cursor.getColumnIndexOrThrow("occupation"));
        int day = cursor.getInt(cursor.getColumnIndexOrThrow("day"));
        int month = cursor.getInt(cursor.getColumnIndexOrThrow("month"));
        int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));
        // Populate fields with extracted properties
        tvName.setText(name);
        tvSurname.setText(surname);
        tvOccupation.setText(occupation);
        tvDate.setText(Integer.toString(day) + "." + Integer.toString(month) + "." + Integer.toString(year));
    }
}
