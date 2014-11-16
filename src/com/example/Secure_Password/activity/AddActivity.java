package com.example.Secure_Password.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.Secure_Password.DataBaseHelper;
import com.example.Secure_Password.R;

/**
 * Created by Camisado on 25.05.2014.
 */
public class AddActivity extends DataBasedActivity implements View.OnClickListener {

    private EditText nameEdit;
    private EditText resourceLinkEdit;
    private EditText loginEdit;
    private EditText passwordEdit;
    private EditText descriptionEdit;
    private Button saveButton;

    private Integer id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        id = getIntent().getIntExtra("id", -1);

        nameEdit = (EditText) findViewById(R.id.nameEdit);
        resourceLinkEdit = (EditText) findViewById(R.id.resourceLinkEdit);
        loginEdit = (EditText) findViewById(R.id.loginEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        descriptionEdit = (EditText) findViewById(R.id.descriptionEdit);
        saveButton = (Button) findViewById(R.id.saveButton);

        saveButton.setOnClickListener(this);

        if(id != -1) {
            Cursor cursor = database.query(DataBaseHelper.TABLE_NAME, new String[] {
                            DataBaseHelper.NAME,
                            DataBaseHelper.RESOURCE_LINK,
                            DataBaseHelper.LOGIN,
                            DataBaseHelper.PASSWORD,
                            DataBaseHelper.DESCRIPTION
                    },
                    DataBaseHelper.UID + " = ?", // The columns for the WHERE clause
                    new String[]{id.toString()}, // The values for the WHERE clause
                    null, // don't group the rows
                    null, // don't filter by row groups
                    null // The sort order
            );
            if (cursor.moveToFirst()) {
                nameEdit.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.NAME)));
                resourceLinkEdit.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.RESOURCE_LINK)));
                loginEdit.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.LOGIN)));
                passwordEdit.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PASSWORD)));
                descriptionEdit.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.DESCRIPTION)));
            }
            cursor.close();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.NAME, nameEdit.getText().toString());
        cv.put(DataBaseHelper.RESOURCE_LINK, resourceLinkEdit.getText().toString());
        cv.put(DataBaseHelper.LOGIN, loginEdit.getText().toString());
        cv.put(DataBaseHelper.PASSWORD, passwordEdit.getText().toString());
        cv.put(DataBaseHelper.DESCRIPTION, descriptionEdit.getText().toString());
        long rowID;
        if(id == -1) {
            rowID = database.insert(DataBaseHelper.TABLE_NAME, null, cv);
        } else {
            database.update(DataBaseHelper.TABLE_NAME, cv, DataBaseHelper.UID + " = ?", new String[]{id.toString()});
            rowID = id;
        }
        intent.putExtra("ID", rowID);
        setResult(RESULT_OK, intent);
        finish();
    }
}