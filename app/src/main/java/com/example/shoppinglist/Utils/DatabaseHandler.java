package com.example.shoppinglist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.shoppinglist.Models.ItemModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String NAME = "itemsToBuyDatabase";
    private static final String ITEM_TABLE = "itemstobuy";
    private static final String ID = "id";
    private static final String ITEM = "item";
    private static final String STATUS = "status";
    private static final String CREATE_ITEM_TABLE = "CREATE TABLE " + ITEM_TABLE + "(" + ID +
        " INTEGER PRIMARY KEY AUTOINCREMENT, " + ITEM + " TEXT, " + STATUS + " INTEGER)";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context)
    {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop older table
        db.execSQL("DROP TABLE IF EXISTS " + ITEM_TABLE);
        //Recreate table
        onCreate(db);
    }

    public void openDatabase()
    {
        db = this.getWritableDatabase();
    }

    public void insertItem(ItemModel item)
    {
        ContentValues cv = new ContentValues();
        cv.put(ITEM, item.getItem());
        cv.put(STATUS, 0); //every new item is inserted as not checked
        db.insert(ITEM_TABLE, null, cv);
    }

    public List<ItemModel> getAllItems()
    {
        List<ItemModel> itemList = new ArrayList<>();
        db.beginTransaction();
        try (Cursor cursor = db.query(ITEM_TABLE, null, null, null, null, null, null, null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        ItemModel item = new ItemModel();
                        item.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                        item.setItem(cursor.getString((cursor.getColumnIndex(ITEM))));
                        item.setStatus(cursor.getInt((cursor.getColumnIndex(STATUS))));
                        itemList.add(item);
                    } while (cursor.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
        }
        return itemList;
    }

    public void updateStatus(int id, int status)
    {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(ITEM_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void updateItem(int id, String item)
    {
        ContentValues cv = new ContentValues();
        cv.put(ITEM, item);
        db.update(ITEM_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteItem(int id)
    {
        db.delete(ITEM_TABLE, ID + "=?", new String[] {String.valueOf(id)});
    }

}
