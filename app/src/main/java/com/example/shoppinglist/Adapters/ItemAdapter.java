package com.example.shoppinglist.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.AddNewItem;
import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.Models.ItemModel;
import com.example.shoppinglist.R;
import com.example.shoppinglist.Utils.DatabaseHandler;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
{
    private List<ItemModel> itemList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ItemAdapter(DatabaseHandler db, MainActivity activity)
    {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public void onBindViewHolder(ViewHolder holder, int position)
    {
        db.openDatabase();
        ItemModel item = itemList.get(position);
        holder.item.setText(item.getItem());
        holder.item.setChecked(intToBoolean(item.getStatus()));
        holder.item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    db.updateStatus(item.getId(), 1);
                }
                else
                {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    public int getItemCount()
    {
        return itemList.size();
    }

    private boolean intToBoolean(int n)
    {
        return n != 0;
    }

    public void setItems(List<ItemModel> itemList)
    {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    public Context getContext()
    {
        return activity;
    }

    public void deleteItem(int position)
    {
        ItemModel item = itemList.get(position);
        db.deleteItem(item.getId());
        itemList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position)
    {
        ItemModel item = itemList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("item", item.getItem());
        AddNewItem fragment = new AddNewItem();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewItem.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        CheckBox item;

        ViewHolder(View view)
        {
            super(view);
            item = view.findViewById(R.id.itemCheckBox);
        }
    }
}
