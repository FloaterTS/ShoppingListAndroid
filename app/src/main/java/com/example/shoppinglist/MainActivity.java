package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shoppinglist.Adapters.ItemAdapter;
import com.example.shoppinglist.Models.ItemModel;
import com.example.shoppinglist.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener
{
    private ItemAdapter itemAdapter;
    private List<ItemModel> itemList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hiding the top bar
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        //Notification in 10 Min Button
        createNotificationChannel();
        Button remindButton = findViewById(R.id.remind_button);
        remindButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.notifcation_set_toast), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                long timeAtButtonClick = System.currentTimeMillis();

                long tenMinutesInMillis = 1000 * 60 * 10;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtButtonClick + tenMinutesInMillis, pendingIntent);
            }
        });

        db = new DatabaseHandler(this);
        db.openDatabase();

        RecyclerView itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(db, this);
        itemsRecyclerView.setAdapter(itemAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_plus);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(itemAdapter));
        itemTouchHelper.attachToRecyclerView(itemsRecyclerView);

        itemList = db.getAllItems();
        Collections.reverse(itemList);
        itemAdapter.setItems(itemList);

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddNewItem.newInstance().show(getSupportFragmentManager(), AddNewItem.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog)
    {
        itemList = db.getAllItems();
        Collections.reverse(itemList);
        itemAdapter.setItems(itemList);
        itemAdapter.notifyDataSetChanged();
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "ReminderChannel";
            String description = "Channel for Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyRemind", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
