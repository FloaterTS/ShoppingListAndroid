package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.shoppinglist.Adapters.ItemAdapter;
import com.example.shoppinglist.Models.ItemModel;
import com.example.shoppinglist.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener
{
    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private FloatingActionButton fab;

    private List<ItemModel> itemList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        db = new DatabaseHandler(this);
        db.openDatabase();

        itemList = new ArrayList<>();

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(db, this);
        itemsRecyclerView.setAdapter(itemAdapter);

        fab = findViewById(R.id.fab_plus);

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
}

//github commit test