package com.example.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.shoppinglist.Adapters.ItemAdapter;
import com.example.shoppinglist.Models.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;

    private List<ItemModel> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getSupportActionBar() != null)
            getSupportActionBar().hide();

        itemList = new ArrayList<>();

        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemAdapter = new ItemAdapter(this);
        itemsRecyclerView.setAdapter(itemAdapter);

        ItemModel item =  new ItemModel();
        item.setItem("Test test");
        item.setStatus(0);
        item.setId(1);

        itemList.add(item);
        itemList.add(item);
        itemList.add(item);

        itemAdapter.setItems(itemList);
    }
}

//github commit test