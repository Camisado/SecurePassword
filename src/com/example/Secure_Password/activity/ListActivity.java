package com.example.Secure_Password.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.example.Secure_Password.DataBaseHelper;
import com.example.Secure_Password.R;
import com.example.Secure_Password.Reminder;
import com.example.Secure_Password.ReminderAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camisado on 24.05.2014.
 */
public class ListActivity extends DataBasedActivity {

    private ListView passwordList;
    private AlertDialog.Builder ad;

    final int REQUEST_CODE_EDIT = 2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        passwordList = (ListView) findViewById(R.id.passwordList);
        ad = new AlertDialog.Builder(this);
        ad.setTitle("Удаление");


    }

    public void onStart() {
        super.onStart();
        final ReminderAdapter adapter = new ReminderAdapter(this, makeList());
        passwordList.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                Reminder reminder = (Reminder) parent.getItemAtPosition(position);
                intent.putExtra("id", reminder.getId());
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        };

        AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Reminder reminder = (Reminder) parent.getItemAtPosition(position);
                ad.setMessage("Вы действительно хотите удалить " + reminder.getName() + "?");
                ad.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.delete(DataBaseHelper.TABLE_NAME, DataBaseHelper.UID + " = " + reminder.getId(), null);
                        adapter.remove(reminder);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Удалено!", Toast.LENGTH_LONG).show();
                    }
                });
                ad.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Удаление отменено!", Toast.LENGTH_LONG).show();
                    }
                });

                ad.show();

                return true;
            }
        };

        passwordList.setOnItemClickListener(itemClickListener);
        passwordList.setOnItemLongClickListener(itemLongClickListener);
    }

    public ArrayList<Reminder> makeList() {
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        Cursor cursor = database.query(DataBaseHelper.TABLE_NAME, new String[] {
                        DataBaseHelper.UID,
                        DataBaseHelper.NAME,
                        DataBaseHelper.RESOURCE_LINK,
                        DataBaseHelper.LOGIN,
                        DataBaseHelper.PASSWORD,
                        DataBaseHelper.DESCRIPTION
                },
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null // The sort order
        );
        while (cursor.moveToNext()) {
            // GET COLUMN INDICES + VALUES OF THOSE COLUMNS
            Reminder reminder = new Reminder();
            reminder.setId(cursor.getInt(cursor.getColumnIndex(DataBaseHelper.UID)));
            reminder.setName(cursor.getString(cursor.getColumnIndex(DataBaseHelper.NAME)));
            reminder.setResourceLink(cursor.getString(cursor.getColumnIndex(DataBaseHelper.RESOURCE_LINK)));
            reminder.setLogin(cursor.getString(cursor.getColumnIndex(DataBaseHelper.LOGIN)));
            reminder.setPassword(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PASSWORD)));
            reminder.setDescription(cursor.getString(cursor.getColumnIndex(DataBaseHelper.DESCRIPTION)));;
            reminders.add(reminder);
        }
        cursor.close();
        return reminders;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra("ID", -1);
            Toast.makeText(this, "Редактирование прошло успешно с ID = " + id, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Отмена редактирования!", Toast.LENGTH_SHORT).show();
        }
    }
}