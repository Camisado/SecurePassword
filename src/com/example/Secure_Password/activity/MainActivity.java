package com.example.Secure_Password.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Secure_Password.DataBaseHelper;
import com.example.Secure_Password.R;

public class MainActivity extends DataBasedActivity implements View.OnClickListener {

    private TextView logoText;
    private Button listButton;
    private Button addButton;
    private Button syncButton;

    final int REQUEST_CODE_ADD = 1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        logoText = (TextView) findViewById(R.id.logoText);
        listButton = (Button) findViewById(R.id.listButton);
        addButton = (Button) findViewById(R.id.addButton);
        syncButton = (Button) findViewById(R.id.syncButton);

        listButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        syncButton.setOnClickListener(this);

        dataBaseHelper = new DataBaseHelper(this);
        database = dataBaseHelper.getWritableDatabase();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listButton : {
                Intent intent = new Intent(this, ListActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.addButton : {
                Intent intent = new Intent(this, AddActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
                break;
            }
            case R.id.syncButton : {
                Intent intent = new Intent(this, DropboxActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            long id = data.getLongExtra("ID", -1);
            Toast.makeText(this, "Добавлена новая запись с ID = " + id, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Отмена добавления!", Toast.LENGTH_SHORT).show();
        }
    }
}
