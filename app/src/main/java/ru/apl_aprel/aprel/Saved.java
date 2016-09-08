package ru.apl_aprel.aprel;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Saved extends Fragment {


    public Saved() {
        // Required empty public constructor
    }

    private boolean deleteItem(SQLiteDatabase db, long id) {
        db.delete(DbHelper.TABLE_NAME, "_id = " + id, null);
        return true;
    }

    private Map<String, String> getEntryData(SQLiteDatabase db, long id) {
        db.delete(DbHelper.TABLE_NAME, "_id = " + id, null);

        Cursor cursor = null;
        Map<String, String> entry = new HashMap<>();

        try{
            cursor = db.rawQuery("SELECT * FROM " + DbHelper.TABLE_NAME + " WHERE _id=?", new String[] {id + ""});

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();

                entry.put("name", cursor.getString(cursor.getColumnIndex("name")));
                entry.put("surname", cursor.getString(cursor.getColumnIndex("surname")));
                entry.put("occupation", cursor.getString(cursor.getColumnIndex("occupation")));
            }

            return entry;
        }finally {
            cursor.close();
        }
    }

    private AlertDialog AskOption(final SQLiteDatabase db, final long id, final ListView lvItems)
    {

        AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getActivity())
                //set message, title, and icon
                .setTitle("Удаление")
                .setMessage("Удалить запись?")
                .setIcon(R.drawable.ic_delete_forever_white_24dp)

                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteItem(db, id);
                        refreshSavedList(db, lvItems);
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }

    private boolean refreshSavedList(SQLiteDatabase db, ListView lvItems) {
        String searchQuery = "SELECT * FROM " + DbHelper.TABLE_NAME;
        Cursor savedListCursor = db.rawQuery(searchQuery, null);

        SavedListCursorAdapter savedListAdapter = new SavedListCursorAdapter(getActivity(), savedListCursor);
        // Attach cursor adapter to the ListView
        lvItems.setAdapter(savedListAdapter);
        lvItems.invalidateViews();
       return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_saved, container, false);

        // TodoDatabaseHandler is a SQLiteOpenHelper class connecting to SQLite
        DbHelper handler = new DbHelper(getActivity());
        // Get access to the underlying writeable database
        SQLiteDatabase db = handler.getWritableDatabase();
        // Query for items from the database and get a cursor back

        // Find ListView to populate
        final ListView lvItems = (ListView) root.findViewById(R.id.result_list);

        refreshSavedList(db, lvItems);

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                DbHelper handler = new DbHelper(getActivity());
                SQLiteDatabase db = handler.getWritableDatabase();

                AlertDialog diaBox = AskOption(db, id, lvItems);
                diaBox.show();
                refreshSavedList(db, lvItems);
                return true;
            }
        });

        return root;
    }
}
