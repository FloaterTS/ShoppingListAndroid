package com.example.shoppinglist.ui.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoppinglist.Adapters.ItemAdapter;
import com.example.shoppinglist.AddNewItem;
import com.example.shoppinglist.BottomNavActivity;
import com.example.shoppinglist.DialogCloseListener;
import com.example.shoppinglist.Models.ItemModel;
import com.example.shoppinglist.R;
import com.example.shoppinglist.RecyclerItemTouchHelper;
import com.example.shoppinglist.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;

public class ShoppingListFragment extends Fragment implements DialogCloseListener {

    private RecyclerView itemsRecyclerView;
    private ItemAdapter itemAdapter;
    private FloatingActionButton fab;

    private List<ItemModel> itemList;
    private DatabaseHandler db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shopping_list_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new DatabaseHandler(requireContext());
        db.openDatabase();

        itemsRecyclerView = view.findViewById(R.id.itemsRecyclerView);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        itemAdapter = new ItemAdapter(db, ((BottomNavActivity)requireContext()));
        itemsRecyclerView.setAdapter(itemAdapter);

        fab = view.findViewById(R.id.fab_plus);

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
                AddNewItem.newInstance().show(((BottomNavActivity) requireContext()).getSupportFragmentManager(), AddNewItem.TAG);
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