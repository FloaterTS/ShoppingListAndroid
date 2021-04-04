package com.example.shoppinglist.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.Models.ItemModel;
import com.example.shoppinglist.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>
{
    private List<ItemModel> itemList;
    private MainActivity activity;

    public ItemAdapter(MainActivity activity)
    {
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
        ItemModel itemModel = itemList.get(position);
        holder.item.setText(itemModel.getItem());
        holder.item.setChecked(intToBoolean(itemModel.getStatus()));
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
