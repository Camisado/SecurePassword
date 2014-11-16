package com.example.Secure_Password;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Camisado on 26.05.2014.
 */
public class ReminderAdapter extends ArrayAdapter<Reminder> {

    private Context context;
    private List<Reminder> reminders;

    public ReminderAdapter(Context context, List<Reminder> reminders) {
        super(context, R.layout.list_item, reminders);
        this.context = context;
        this.reminders = reminders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.list_item, parent, false);

        TextView nameView = (TextView) rowView.findViewById(R.id.name);
        TextView loginView = (TextView) rowView.findViewById(R.id.login);
        TextView passwordView = (TextView) rowView.findViewById(R.id.password);

        nameView.setText(reminders.get(position).getName());
        loginView.setText(reminders.get(position).getLogin());
        passwordView.setText(reminders.get(position).getPassword());

        return rowView;
    }
}